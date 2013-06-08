package org.eluder.coveralls.maven.plugin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eluder.coveralls.maven.plugin.domain.Source;
import org.eluder.coveralls.maven.plugin.domain.SourceLoader;
import org.eluder.coveralls.maven.plugin.util.TestIoUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractCoverageParserTest {

    @Mock
    private SourceLoader sourceLoaderMock;
    
    @Mock
    private SourceCallback sourceCallbackMock;
    
    @Before
    public void init() throws IOException {
        String content = TestIoUtil.readFileContent(TestIoUtil.getFile("/CoverageTest.java"));
        Source source = new Source("org/eluder/coverage/test/CoverageTest.java", content);
        when(sourceLoaderMock.load("org/eluder/coverage/test/CoverageTest.java")).thenReturn(source);
    }
    
    @Test
    public void testParseCoverage() throws Exception {
        CoverageParser parser = createCoverageParser(TestIoUtil.getFile(getCoverageResource()), sourceLoaderMock);
        parser.parse(sourceCallbackMock);
        
        ArgumentCaptor<Source> captor = ArgumentCaptor.forClass(Source.class);
        verify(sourceCallbackMock).onSource(captor.capture());
        
        Set<Integer> coveredLines = new HashSet<Integer>(Arrays.asList(3, 6));
        Set<Integer> missedLines = new HashSet<Integer>(Arrays.asList(10, 11));
        Source source = captor.getValue();
        assertEquals("org/eluder/coverage/test/CoverageTest.java", source.getName());
        for (int i = 0; i < source.getCoverage().length; i++) {
            Integer lineNumber = i + 1;
            if (coveredLines.contains(lineNumber)) {
                assertTrue(source.getCoverage()[i] > 0);
            } else if (missedLines.contains(lineNumber)) {
                assertTrue(source.getCoverage()[i] == 0);
            } else {
                assertNull(source.getCoverage()[i]);
            }
        }
    }
    
    protected abstract CoverageParser createCoverageParser(File coverageFile, SourceLoader sourceLoader);
    
    protected abstract String getCoverageResource();
}
