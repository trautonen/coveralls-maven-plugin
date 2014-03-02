package org.eluder.coveralls.maven.plugin.logging;

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

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.same;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class JobLoggerTest {

    @Mock
    private Job jobMock;
    
    @Mock
    private Log logMock;
    
    @Mock
    private ObjectMapper jsonMapperMock;
    
    @Test(expected = IllegalArgumentException.class)
    public void testMissingJob() {
        new JobLogger(null);
    }
    
    @Test
    public void testGetPosition() {
        assertEquals(Position.BEFORE, new JobLogger(jobMock).getPosition());
    }
    
    @Test
    public void testLogJobWithId() {
        Git git = new Git(new Head("ab679cf2d81ac", null, null, null, null, null), "master", null);
        when(jobMock.getServiceName()).thenReturn("service");
        when(jobMock.getServiceJobId()).thenReturn("666");
        when(jobMock.getRepoToken()).thenReturn("123456789");
        when(jobMock.isDryRun()).thenReturn(true);
        when(jobMock.getGit()).thenReturn(git);
        
        new JobLogger(jobMock).log(logMock);
        
        verify(logMock).info("Starting Coveralls job for service (666) in dry run mode");
        verify(logMock).info("Using repository token <secret>");
        verify(logMock).info("Git commit ab679cf in master");
        verify(logMock).isDebugEnabled();
        verifyNoMoreInteractions(logMock);
    }
    
    @Test
    public void testLogWithBuildNumberAndUrl() {
        when(jobMock.getServiceName()).thenReturn("service");
        when(jobMock.getServiceBuildNumber()).thenReturn("10");
        when(jobMock.getServiceBuildUrl()).thenReturn("http://ci.com/build/10");
        
        new JobLogger(jobMock).log(logMock);
        
        verify(logMock).info("Starting Coveralls job for service (10 / http://ci.com/build/10)");
        verify(logMock).isDebugEnabled();
        verifyNoMoreInteractions(logMock);
    }
    
    @Test
    public void testLogDryRun() {
        when(jobMock.isDryRun()).thenReturn(true);
        
        new JobLogger(jobMock).log(logMock);
        
        verify(logMock).info("Starting Coveralls job in dry run mode");
        verify(logMock).isDebugEnabled();
        verifyNoMoreInteractions(logMock);
    }
    
    @Test
    public void testLogJobWithDebug() throws Exception {
        when(logMock.isDebugEnabled()).thenReturn(true);
        when(jobMock.getServiceName()).thenReturn("service");
        when(jsonMapperMock.writeValueAsString(same(jobMock))).thenReturn("{\"serviceName\":\"service\"}");
        
        new JobLogger(jobMock, jsonMapperMock).log(logMock);
        
        verify(logMock).info("Starting Coveralls job for service");
        verify(logMock).isDebugEnabled();
        verify(logMock).debug("Complete Job description:\n{\"serviceName\":\"service\"}");
        verifyNoMoreInteractions(logMock);
    }
    
    @Test(expected = RuntimeException.class)
    @SuppressWarnings("unchecked")
    public void testLogJobWithErrorInDebug() throws Exception {
        when(logMock.isDebugEnabled()).thenReturn(true);
        when(jobMock.getServiceName()).thenReturn("service");
        when(jsonMapperMock.writeValueAsString(same(jobMock))).thenThrow(JsonProcessingException.class);
        
        new JobLogger(jobMock, jsonMapperMock).log(logMock);
    }
}
