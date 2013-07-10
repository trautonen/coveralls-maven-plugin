package org.eluder.coveralls.maven.plugin.logging;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.File;

import org.apache.maven.plugin.logging.Log;
import org.eluder.coveralls.maven.plugin.logging.Logger.Position;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DryRunLoggerTest {

    @Mock
    private Log logMock;
    
    @Mock
    private File coverallsFileMock;
    
    @Test(expected = IllegalArgumentException.class)
    public void testMissingCoverallsFile() {
        new DryRunLogger(true, null);
    }
    
    @Test
    public void testGetPosition() {
        assertEquals(Position.AFTER, new DryRunLogger(true, coverallsFileMock).getPosition());
    }
    
    @Test
    public void testLogDryRunDisabled() {
        new DryRunLogger(false, coverallsFileMock).log(logMock);
        
        verifyZeroInteractions(logMock);
    }
    
    @Test
    public void testLogDryRunEnabled() {
        when(coverallsFileMock.length()).thenReturn(1024l);
        when(coverallsFileMock.getAbsolutePath()).thenReturn("/target/coveralls.json");
        
        new DryRunLogger(true, coverallsFileMock).log(logMock);
        
        verify(logMock).info("Dry run enabled, Coveralls report will not be submitted to API");
        verify(logMock).info("1024 bytes of data was recorded in /target/coveralls.json");
    }
}
