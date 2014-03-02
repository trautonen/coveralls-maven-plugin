package org.eluder.coveralls.maven.plugin;

/*
 * #[license]
 * coveralls-maven-plugin
 * %%
 * Copyright (C) 2013 - 2014 Tapio Rautonen
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

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eluder.coveralls.maven.plugin.domain.Source;
import org.eluder.coveralls.maven.plugin.domain.SourceLoader;
import org.eluder.coveralls.maven.plugin.util.TestIoUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

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
                return new Source(name, content);
            }
        };
    }

    @Test
    public void testParseCoverage() throws Exception {
        CoverageParser parser = createCoverageParser(TestIoUtil.getFile(getCoverageResource()), sourceLoaderMock);
        parser.parse(sourceCallbackMock);
        
        String[][] fixture = getCoverageFixture();
        
        ArgumentCaptor<Source> captor = ArgumentCaptor.forClass(Source.class);
        verify(sourceCallbackMock, atLeast(CoverageFixture.getTotalFiles(fixture))).onSource(captor.capture());
        
        Collection<Source> combined = combineCoverage(captor.getAllValues());
        for (String[] coverageFile : fixture) {
            assertCoverage(combined, coverageFile[0], Integer.parseInt(coverageFile[1]), toSet(coverageFile[2]), toSet(coverageFile[3]));
        }
    }
    
    protected abstract CoverageParser createCoverageParser(File coverageFile, SourceLoader sourceLoader);
    
    protected abstract String getCoverageResource();

    protected abstract String[][] getCoverageFixture();
    
    private Collection<Source> combineCoverage(final List<Source> sources) {
        Map<String, Source> combined = new HashMap<String, Source>();
        for (Source source : sources) {
            Source existing = combined.get(source.getName());
            if (existing == null) {
                combined.put(source.getName(), source);
            } else {
                for (int i = 0; i < source.getCoverage().length; i++) {
                    if (source.getCoverage()[i] != null) {
                        existing.addCoverage(i + 1, source.getCoverage()[i]);
                    }
                }
            }
        }
        return combined.values();
    }
    
    private Set<Integer> toSet(final String commaSeparated) {
        if (commaSeparated.isEmpty()) {
            return new HashSet<Integer>(0);
        }

        String[] split = commaSeparated.split(",");
        Set<Integer> values = new HashSet<Integer>(split.length);
        for (String value : split) {
            values.add(Integer.valueOf(value));
        }
        return values;
    }
    
    private static void assertCoverage(final Collection<Source> sources, final String name, final int lines, final Set<Integer> coveredLines, final Set<Integer> missedLines) {
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
            if (coveredLines.contains(lineNumber)) {
                assertTrue(tested.getCoverage()[i] > 0);
            } else if (missedLines.contains(lineNumber)) {
                assertTrue(tested.getCoverage()[i] == 0);
            } else {
                assertNull(tested.getCoverage()[i]);
            }
        }

    }
}
