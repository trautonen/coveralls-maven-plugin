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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.eluder.coveralls.maven.plugin.domain.SourceLoader;
import org.eluder.coveralls.maven.plugin.service.ServiceSetup;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EnvironmentTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    private AbstractCoverallsMojo mojo;
    
    @Mock
    private CoverageParser coverageParserMock;
    
    @Mock
    private Log logMock;
    
    @Mock
    private ServiceSetup serviceMock;
    
    @Mock
    private MavenProject mavenProjectMock;
    
    @Mock
    private MavenProject mavenProjectMock2;
    private File folder2;

    @Mock
    private MavenProject mavenProjectMock3;

    @Mock
    private MavenProject mavenProjectMock4;
    private File folder4;

    @Mock
    private MavenProject mavenProjectMock5;
    private File folder5;
    
    @Before
    public void init() throws Exception {
        folder2 = folder.newFolder();
        folder4 = folder.newFolder();
        folder5 = folder.newFolder();
        mojo = new AbstractCoverallsMojo() {
            @Override
            protected CoverageParser createCoverageParser(final SourceLoader sourceLoader) {
                return coverageParserMock;
            }
            @Override
            public Log getLog() {
                return logMock;
            }
        };
        mojo.serviceName = "service";
        mojo.project = mavenProjectMock;
        when(serviceMock.isSelected("service")).thenReturn(true);
        when(mavenProjectMock.getCollectedProjects()).thenReturn(Arrays.asList(mavenProjectMock2, mavenProjectMock3));
        when(mavenProjectMock3.getCollectedProjects()).thenReturn(Arrays.asList(mavenProjectMock4, mavenProjectMock5));
        when(mavenProjectMock2.getCompileSourceRoots()).thenReturn(Arrays.asList(folder2.getAbsolutePath()));
        when(mavenProjectMock4.getCompileSourceRoots()).thenReturn(Arrays.asList(folder4.getAbsolutePath()));
        when(mavenProjectMock5.getCompileSourceRoots()).thenReturn(Arrays.asList(folder5.getAbsolutePath()));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithoutMojo() {
        new Environment(null, Arrays.asList(serviceMock));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithoutServices() {
        new Environment(mojo, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetupWithoutSourceDirectories() {
        when(mavenProjectMock.getCollectedProjects()).thenReturn(new ArrayList<MavenProject>());
        create(Collections.<ServiceSetup>emptyList()).setup();
    }
    
    @Test
    public void testSetupWithoutMojoSourceDirectories() {
        create(Collections.<ServiceSetup>emptyList()).setup();
        assertArrayEquals(new File[] { folder2.getAbsoluteFile(), folder4.getAbsoluteFile(), folder5.getAbsoluteFile() },
                          mojo.sourceDirectories.toArray(new File[0]));
        verify(logMock).debug("Using 3 source directories to scan source files:");
    }
    
    @Test
    public void testSetupWithMojoSourceDirectories() {
        mojo.sourceDirectories = Arrays.asList(folder.getRoot());
        create(Collections.<ServiceSetup>emptyList()).setup();
        assertArrayEquals(new File[] { folder.getRoot() },
                          mojo.sourceDirectories.toArray(new File[0]));
        verify(logMock).debug("Using 1 source directories to scan source files:");
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
