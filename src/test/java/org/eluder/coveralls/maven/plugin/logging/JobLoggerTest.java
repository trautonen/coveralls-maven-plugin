package org.eluder.coveralls.maven.plugin.logging;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.apache.maven.plugin.logging.Log;
import org.eluder.coveralls.maven.plugin.domain.Git;
import org.eluder.coveralls.maven.plugin.domain.Git.Head;
import org.eluder.coveralls.maven.plugin.domain.Job;
import org.eluder.coveralls.maven.plugin.logging.Logger.Position;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JobLoggerTest {

    @Mock
    private Job jobMock;
    
    @Mock
    private Log logMock;
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNull() {
        new JobLogger(null);
    }
    
    @Test
    public void testGetPosition() {
        assertEquals(Position.BEFORE, new JobLogger(jobMock).getPosition());
    }
    
    @Test
    public void testLogFullJob() {
        Git git = new Git(new Head("ab679cf2d81ac", null, null, null, null, null), "master", null);
        when(jobMock.getServiceName()).thenReturn("service");
        when(jobMock.getServiceJobId()).thenReturn("666");
        when(jobMock.getRepoToken()).thenReturn("123456789");
        when(jobMock.getGit()).thenReturn(git);
        
        new JobLogger(jobMock).log(logMock);
        
        verify(logMock).info("Starting Coveralls job for service (666)");
        verify(logMock).info("Using repository token <secret>");
        verify(logMock).info("Git commit ab679cf in master");
        verifyNoMoreInteractions(logMock);
    }
}
