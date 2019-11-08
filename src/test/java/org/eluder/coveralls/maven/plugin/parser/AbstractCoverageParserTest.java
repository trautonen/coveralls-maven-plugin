package org.eluder.coveralls.maven.plugin.parser;

/*
 * #[license]
 * coveralls-maven-plugin
 * %%
 * Copyright (C) 2013 - 2016 Tapio Rautonen
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * %[license]
 */

import org.eluder.coveralls.maven.plugin.CoverageFixture;
import org.eluder.coveralls.maven.plugin.CoverageParser;
import org.eluder.coveralls.maven.plugin.ProcessingException;
import org.eluder.coveralls.maven.plugin.domain.Branch;
import org.eluder.coveralls.maven.plugin.domain.Source;
import org.eluder.coveralls.maven.plugin.source.ChainingSourceCallback;
import org.eluder.coveralls.maven.plugin.source.SourceCallback;
import org.eluder.coveralls.maven.plugin.source.SourceLoader;
import org.eluder.coveralls.maven.plugin.source.UniqueSourceCallback;
import org.eluder.coveralls.maven.plugin.util.TestIoUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractCoverageParserTest {
    
    @Mock
    protected SourceLoader sourceLoaderMock;
    
    @Mock
    protected SourceCallback sourceCallbackMock;
    
    @Before
    public void init() throws IOException {
        for (String[] coverageFile : getCoverageFixture()) {
            final String name = sourceName(coverageFile[0]);
            final String content = TestIoUtil.readFileContent(TestIoUtil.getFile(name));
            when(sourceLoaderMock.load(name)).then(sourceAnswer(name, content));
        }
    }
    
    protected String sourceName(final String coverageFile) {
        return coverageFile;
    }
    
    protected Answer<Source> sourceAnswer(final String name, final String content) {
        return new Answer<Source>() {
            @Override
            public Source answer(final InvocationOnMock invocation) throws Throwable {
                return new Source(name, content, TestIoUtil.getMd5DigestHex(content));
            }
        };
    }

    @Test
    public void testParseCoverage() throws Exception {
        for (String coverageResource : getCoverageResources()) {
            CoverageParser parser = createCoverageParser(TestIoUtil.getFile(coverageResource), sourceLoaderMock);
            parser.parse(sourceCallbackMock);
        }
        
        String[][] fixture = getCoverageFixture();
        
        ArgumentCaptor<Source> captor = ArgumentCaptor.forClass(Source.class);
        verify(sourceCallbackMock, atLeast(CoverageFixture.getTotalFiles(fixture))).onSource(captor.capture());

        SourceCollector sourceCollector = new SourceCollector();
        UniqueSourceCallback uniqueSourceCallback = new UniqueSourceCallback(sourceCollector);
        ClassifierRemover classifierRemover = new ClassifierRemover(uniqueSourceCallback);
        classifierRemover.onBegin();
        for (Source source: captor.getAllValues()) {
            classifierRemover.onSource(source);
        }
        classifierRemover.onComplete();

        for (String[] coverageFile : fixture) {
            assertCoverage(sourceCollector.sources, coverageFile[0], Integer.parseInt(coverageFile[1]),
                    toIntegerSet(coverageFile[2]), toIntegerSet(coverageFile[3]),
                    toIntegerSet(coverageFile[4]), toIntegerSet(coverageFile[5]));
        }
    }
    
    protected abstract CoverageParser createCoverageParser(File coverageFile, SourceLoader sourceLoader);
    
    protected abstract List<String> getCoverageResources();

    protected abstract String[][] getCoverageFixture();
    
    private Set<Integer> toIntegerSet(final String commaSeparated) {
        if (commaSeparated.isEmpty()) {
            return Collections.emptySet();
        }
        String[] split = commaSeparated.split(",");
        Set<Integer> values = new HashSet<>();
        for (String value : split) {
            values.add(Integer.valueOf(value));
        }
        return values;
    }

    private static class SourceCollector implements SourceCallback {

        private List<Source> sources = new ArrayList<>();

        @Override
        public void onBegin() throws ProcessingException, IOException {

        }

        @Override
        public void onSource(Source source) throws ProcessingException, IOException {
            sources.add(source);
        }

        @Override
        public void onComplete() throws ProcessingException, IOException {

        }
    }

    private static class ClassifierRemover extends ChainingSourceCallback {

        public ClassifierRemover(SourceCallback chained) {
            super(chained);
        }

        @Override
        protected void onSourceInternal(Source source) throws ProcessingException, IOException {
            source.setClassifier(null);
        }
    }

    private static void assertCoverage(final Collection<Source> sources, final String name, final int lines,
            final Set<Integer> coveredLines, final Set<Integer> missedLines,
            final Set<Integer> coveredBranches, final Set<Integer> missedBranches) {

        Source tested = null;
        for (Source source : sources) {
            if (source.getName().endsWith(name)) {
                tested = source;
                break;
            }
        }
        if (tested == null) {
            fail("Expected source " + name + " not found from coverage report");
        }
        if (tested.getCoverage().length != lines) {
            fail("Expected " + lines + " lines for " + name + " was " + tested.getCoverage().length);
        }
        for (int i = 0; i < tested.getCoverage().length; i++) {
            Integer lineNumber = i + 1;
            String message = name + " line " + lineNumber + " coverage";
            if (coveredLines.contains(lineNumber)) {
                assertTrue(message, tested.getCoverage()[i] > 0);
            } else if (missedLines.contains(lineNumber)) {
                assertTrue(message, tested.getCoverage()[i] == 0);
            } else {
                assertNull(message, tested.getCoverage()[i]);
            }
        }
        for (final Branch b : tested.getBranchesList()) {
            final String message = name + " branch " + b.getBranchNumber() + " coverage in line " + b.getLineNumber();
            if (b.getHits() > 0) {
                assertTrue(message, coveredBranches.contains(b.getLineNumber()));
            } else {
                assertTrue(message, missedBranches.contains(b.getLineNumber()));
            }
        }
    }
}
