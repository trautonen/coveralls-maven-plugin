package org.eluder.coveralls.maven.plugin;

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

    private static final String[][] COVERAGE_FILES = new String[][] {
        // file                      lines  covered lines            missed lines
        { "SimpleCoverage.java",     "14",  "3,6",                   "10,11" },
        { "InnerClassCoverage.java", "30",  "3,6,9,10,12,15,18,21",  "25,26" }
    };
    
    @Mock
    private SourceLoader sourceLoaderMock;
    
    @Mock
    private SourceCallback sourceCallbackMock;
    
    @Before
    public void init() throws IOException {
        for (String[] coverageFile : COVERAGE_FILES) {
            final String name = coverageFile[0];
            final String content = TestIoUtil.readFileContent(TestIoUtil.getFile("/" + name));
            when(sourceLoaderMock.load("org/eluder/coverage/sample/" + name)).then(new Answer<Source>() {
                @Override
                public Source answer(final InvocationOnMock invocation) throws Throwable {
                    return new Source("org/eluder/coverage/sample/" + name, content);
                }
            });
        }
    }
    
    @Test
    public void testParseCoverage() throws Exception {
        CoverageParser parser = createCoverageParser(TestIoUtil.getFile(getCoverageResource()), sourceLoaderMock);
        parser.parse(sourceCallbackMock);
        
        ArgumentCaptor<Source> captor = ArgumentCaptor.forClass(Source.class);
        verify(sourceCallbackMock, atLeast(COVERAGE_FILES.length)).onSource(captor.capture());
        
        Collection<Source> combined = combineCoverage(captor.getAllValues());
        for (String[] coverageFile : COVERAGE_FILES) {
            assertCoverage(combined, coverageFile[0], Integer.parseInt(coverageFile[1]), toSet(coverageFile[2]), toSet(coverageFile[3]));
        }
    }
    
    protected abstract CoverageParser createCoverageParser(File coverageFile, SourceLoader sourceLoader);
    
    protected abstract String getCoverageResource();
    
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
