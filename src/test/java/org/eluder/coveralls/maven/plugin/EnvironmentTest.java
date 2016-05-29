package org.eluder.coveralls.maven.plugin;

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

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.maven.plugin.logging.Log;
import org.eluder.coveralls.maven.plugin.service.ServiceSetup;
import org.eluder.coveralls.maven.plugin.source.SourceLoader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EnvironmentTest {
    
    private CoverallsReportMojo mojo;
    
    @Mock
    private CoverageParser coverageParserMock;
    
    @Mock
    private Log logMock;
    
    @Mock
    private ServiceSetup serviceMock;
    
    @Before
    public void init() throws Exception {
        mojo = new CoverallsReportMojo() {
            @Override
            protected List<CoverageParser> createCoverageParsers(SourceLoader sourceLoader) {
                return Arrays.asList(coverageParserMock);
            }

            @Override
            public Log getLog() {
                return logMock;
            }
        };
        mojo.serviceName = "service";
        mojo.sourceEncoding = "UTF-8";
        when(logMock.isDebugEnabled()).thenReturn(true);
        when(logMock.isInfoEnabled()).thenReturn(true);
        when(serviceMock.isSelected()).thenReturn(true);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMissingMojo() {
        new Environment(null, Arrays.asList(serviceMock));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMissingServices() {
        new Environment(mojo, null);
    }
    
    @Test
    public void testSetupWithoutServices() {
        create(Collections.<ServiceSetup>emptyList()).setup();
        assertEquals("service", mojo.serviceName);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetupWithoutSourceEncoding() {
        mojo.sourceEncoding = null;
        create(Arrays.asList(serviceMock)).setup();
    }
    
    @Test
    public void testSetupWithIncompleteJob() {
        when(serviceMock.getJobId()).thenReturn("");
        when(serviceMock.getBuildUrl()).thenReturn("  ");
        
        create(Arrays.asList(serviceMock)).setup();
        assertEquals("service", mojo.serviceName);
        assertNull(mojo.serviceJobId);
        assertNull(mojo.serviceBuildNumber);
        assertNull(mojo.serviceBuildUrl);
        assertNull(mojo.branch);
        assertNull(mojo.pullRequest);
        assertNull(mojo.serviceEnvironment);
    }
    
    @Test
    public void testSetupWithCompleteJob() {
        mojo.serviceName = null;
        Properties environment = new Properties();
        environment.setProperty("env", "true");
        when(serviceMock.getName()).thenReturn("defined service");
        when(serviceMock.getJobId()).thenReturn("123");
        when(serviceMock.getBuildNumber()).thenReturn("456");
        when(serviceMock.getBuildUrl()).thenReturn("http://ci.com/project");
        when(serviceMock.getBranch()).thenReturn("master");
        when(serviceMock.getPullRequest()).thenReturn("111");
        when(serviceMock.getEnvironment()).thenReturn(environment);
        
        create(Arrays.asList(mock(ServiceSetup.class), serviceMock)).setup();
        assertEquals("defined service", mojo.serviceName);
        assertEquals("123", mojo.serviceJobId);
        assertEquals("456", mojo.serviceBuildNumber);
        assertEquals("http://ci.com/project", mojo.serviceBuildUrl);
        assertEquals("master", mojo.branch);
        assertEquals("111", mojo.pullRequest);
        assertEquals("true", mojo.serviceEnvironment.get("env"));
    }
    
    @Test
    public void testSetupWithoutJobOverride() {
        Properties environment = new Properties();
        environment.setProperty("env", "true");
        Properties serviceEnvironment = new Properties();
        serviceEnvironment.setProperty("env", "setProperty");
        when(serviceMock.getName()).thenReturn("defined service");
        when(serviceMock.getJobId()).thenReturn("123");
        when(serviceMock.getBuildNumber()).thenReturn("456");
        when(serviceMock.getBuildUrl()).thenReturn("http://ci.com/project");
        when(serviceMock.getBranch()).thenReturn("master");
        when(serviceMock.getPullRequest()).thenReturn("111");
        when(serviceMock.getEnvironment()).thenReturn(environment);
        mojo.serviceJobId = "setJobId";
        mojo.serviceBuildNumber = "setBuildNumber";
        mojo.serviceBuildUrl = "setBuildUrl";
        mojo.serviceEnvironment = serviceEnvironment;
        mojo.branch = "setBranch";
        mojo.pullRequest = "setPullRequest";
        
        create(Arrays.asList(serviceMock)).setup();
        
        assertEquals("service", mojo.serviceName);
        assertEquals("setJobId", mojo.serviceJobId);
        assertEquals("setBuildNumber", mojo.serviceBuildNumber);
        assertEquals("setBuildUrl", mojo.serviceBuildUrl);
        assertEquals("setBranch", mojo.branch);
        assertEquals("setPullRequest", mojo.pullRequest);
        assertEquals("setProperty", mojo.serviceEnvironment.get("env"));        
    }
    
    private Environment create(final Iterable<ServiceSetup> services) {
        return new Environment(mojo, services);
    }
}
