package org.eluder.coveralls.maven.plugin.logging;

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

import org.apache.maven.plugin.logging.Log;
import org.eluder.coveralls.maven.plugin.domain.Source;
import org.eluder.coveralls.maven.plugin.logging.Logger.Position;
import org.eluder.coveralls.maven.plugin.source.SourceCallback;
import org.eluder.coveralls.maven.plugin.source.UniqueSourceCallback;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
        Source source1 = new Source("Source1.java", "public class Source1 {\n  if(true) { }\n}\n", "FE0538639E8CE73733E77659C1043B5C");
        source1.addCoverage(1, 0);
        source1.addCoverage(2, 0);
        source1.addCoverage(3, 0);
        source1.addBranchCoverage(2, 0, 0, 3);
        source1.addBranchCoverage(2, 0, 1, 0);
        Source source2 = new Source("Source2.java", "public class Source2 {\n    new Interface() { public void run() { } };\n}\n", "34BD6501A6D1CE5181AECEA688C7D382");
        source2.addCoverage(1, 1);
        source2.addCoverage(3, 1);
        Source source2inner = new Source("Source2.java", "public class Source2 {\n    new Interface() { public void run() { } };\n}\n", "34BD6501A6D1CE5181AECEA688C7D382");
        source2inner.setClassifier("$1");
        source2inner.addCoverage(2, 1);
        
        CoverageTracingLogger coverageTracingLogger = new CoverageTracingLogger(sourceCallbackMock);
        UniqueSourceCallback uniqueSourceCallback = new UniqueSourceCallback(coverageTracingLogger);
        uniqueSourceCallback.onSource(source1);
        uniqueSourceCallback.onSource(source2);
        uniqueSourceCallback.onSource(source2inner);
        uniqueSourceCallback.onComplete();
        coverageTracingLogger.log(logMock);
        
        assertEquals(8, coverageTracingLogger.getLines());
        assertEquals(6, coverageTracingLogger.getRelevant());
        assertEquals(3, coverageTracingLogger.getCovered());
        assertEquals(3, coverageTracingLogger.getMissed());
        assertEquals(2, coverageTracingLogger.getBranches());
        assertEquals(1, coverageTracingLogger.getCoveredBranches());
        assertEquals(1, coverageTracingLogger.getMissedBranches());
        verify(sourceCallbackMock, times(2)).onSource(any(Source.class));
        verify(logMock).info("Gathered code coverage metrics for 2 source files with 8 lines of code:");
        verify(logMock).info("- 6 relevant lines");
        verify(logMock).info("- 3 covered lines");
        verify(logMock).info("- 3 missed lines");
        verify(logMock).info("- 2 branches");
        verify(logMock).info("- 2 branches");
        verify(logMock).info("- 1 covered branches");
        verify(logMock).info("- 1 missed branches");
    }
    
}
