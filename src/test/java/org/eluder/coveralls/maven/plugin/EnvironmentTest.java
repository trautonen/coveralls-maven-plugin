package org.eluder.coveralls.maven.plugin;

/*
 * #[license]
 * coveralls-maven-plugin
 * %%
 * Copyright (C) 2013 Tapio Rautonen
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;

import org.eluder.coveralls.maven.plugin.domain.SourceLoader;
import org.eluder.coveralls.maven.plugin.service.ServiceSetup;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EnvironmentTest {

    private AbstractCoverallsMojo mojo;
    
    @Mock
    private ServiceSetup serviceMock;
    
    @Before
    public void init() {
        mojo = new AbstractCoverallsMojo() {
            @Override
            protected CoverageParser createCoverageParser(final SourceLoader sourceLoader) {
                return mock(CoverageParser.class);
            }
        };
        mojo.serviceName = "service";
        when(serviceMock.isSelected("service")).thenReturn(true);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithoutMojo() {
        new Environment(null, Arrays.asList(serviceMock));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithoutServices() {
        new Environment(mojo, null);
    }
    
    @Test
    public void testSetupWithoutServices() {
        create(Collections.<ServiceSetup>emptyList()).setup();
        assertProperties(null, null, null);
    }
    
    @Test
    public void testSetupWithoutValues() {
        when(serviceMock.getServiceJobId()).thenReturn("");
        when(serviceMock.getRepoToken()).thenReturn(null);
        when(serviceMock.getBranch()).thenReturn(null);
        
        create(Arrays.asList(serviceMock)).setup();
        assertProperties(null, null, null);
    }
    
    @Test
    public void testSetupWithValues() {
        when(serviceMock.getServiceJobId()).thenReturn("123");
        when(serviceMock.getRepoToken()).thenReturn("abcde");
        when(serviceMock.getBranch()).thenReturn("master");
        
        create(Arrays.asList(mock(ServiceSetup.class), serviceMock)).setup();
        assertProperties("123", "abcde", "master");
    }
    
    private Environment create(final Iterable<ServiceSetup> services) {
        return new Environment(mojo, services);
    }
    
    private void assertProperties(final String serviceJobId, final String repoToken, final String branch) {
        assertEquals(serviceJobId, mojo.serviceJobId);
        assertEquals(repoToken, mojo.repoToken);
        assertEquals(branch, mojo.branch);
    }
}
