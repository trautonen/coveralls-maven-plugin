package org.eluder.coveralls.maven.plugin.logging;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.apache.maven.plugin.logging.Log;
import org.eluder.coveralls.maven.plugin.SourceCallback;
import org.eluder.coveralls.maven.plugin.domain.Source;
import org.eluder.coveralls.maven.plugin.logging.Logger.Position;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CoverageTracingLoggerTest {

    @Mock
    private Log logMock;
    
    @Mock
    private SourceCallback sourceCallbackMock;
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNull() {
        new CoverageTracingLogger(null);
    }
    
    @Test
    public void testGetPosition() {
        assertEquals(Position.AFTER, new CoverageTracingLogger(sourceCallbackMock).getPosition());
    }
    
    @Test
    public void testLogForSources() throws Exception {
        Source source1 = new Source("Source1.java", "public class Source1 {\n    \n}\n");
        source1.addCoverage(1, 0);
        source1.addCoverage(2, 0);
        source1.addCoverage(3, 0);
        Source source2 = new Source("Source2.java", "public class Source2 {\n    new Interface() { public void run() { } };\n}\n");
        source2.addCoverage(1, 1);
        source2.addCoverage(2, 1);
        source2.addCoverage(3, 1);
        Source source2inner = new Source("Source2.java", "public class Source2 {\n    new Interface() { public void run() { } };\n}\n");
        source2inner.setClassifier("$1");
        source2inner.addCoverage(2, 1);
        
        CoverageTracingLogger coverageTracingLogger = new CoverageTracingLogger(sourceCallbackMock);
        coverageTracingLogger.onSource(source1);
        coverageTracingLogger.onSource(source2);
        coverageTracingLogger.onSource(source2inner);
        coverageTracingLogger.log(logMock);
        
        assertEquals(8, coverageTracingLogger.getLines());
        assertEquals(7, coverageTracingLogger.getRelevant());
        assertEquals(4, coverageTracingLogger.getCovered());
        assertEquals(3, coverageTracingLogger.getMissed());
        verify(sourceCallbackMock, times(3)).onSource(any(Source.class));
        verify(logMock).info("Gathered code coverage metrics for 2 source files with 8 lines of code:");
        verify(logMock).info("- 7 relevant lines");
        verify(logMock).info("- 4 covered lines");
        verify(logMock).info("- 3 missed lines");
    }
    
}
