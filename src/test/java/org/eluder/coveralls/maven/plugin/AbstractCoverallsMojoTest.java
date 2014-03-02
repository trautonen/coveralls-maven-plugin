package org.eluder.coveralls.maven.plugin;

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

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.eluder.coveralls.maven.plugin.domain.CoverallsResponse;
import org.eluder.coveralls.maven.plugin.domain.Job;
import org.eluder.coveralls.maven.plugin.domain.Source;
import org.eluder.coveralls.maven.plugin.domain.SourceLoader;
import org.eluder.coveralls.maven.plugin.httpclient.CoverallsClient;
import org.eluder.coveralls.maven.plugin.json.JsonWriter;
import org.eluder.coveralls.maven.plugin.service.ServiceSetup;
import org.eluder.coveralls.maven.plugin.util.TestIoUtil;
import org.eluder.coveralls.maven.plugin.validation.ValidationErrors;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractCoverallsMojoTest {
    
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    public File coverallsFile;
    
    private AbstractCoverallsMojo mojo;
    
    @Mock
    private CoverallsClient coverallsClientMock;
    
    @Mock
    private SourceLoader sourceLoaderMock;
    
    @Mock
    private Job jobMock;
    
    @Mock
    private Log logMock;

    @Mock
    private MavenProject projectMock;

    @Mock
    private MavenProject collectedProjectMock;
    
    @Before
    public void init() throws Exception {
        coverallsFile = folder.newFile();
        
        when(sourceLoaderMock.load(anyString())).then(new Answer<Source>() {
            @Override
            public Source answer(final InvocationOnMock invocation) throws Throwable {
                String sourceFile = invocation.getArguments()[0].toString();
                String content = readFileContent(sourceFile);
                return new Source(sourceFile, content);
            }
        });
        when(logMock.isInfoEnabled()).thenReturn(true);
        when(jobMock.validate()).thenReturn(new ValidationErrors());
        
        mojo = new AbstractCoverallsMojo() {
            @Override
            protected CoverageParser createCoverageParser(final SourceLoader sourceLoader) {
                return createMojo().createCoverageParser(sourceLoader);
            }
            @Override
            protected SourceLoader createSourceLoader() {
                return sourceLoaderMock;
            }
            @Override
            protected Environment createEnvironment() {
                return new Environment(this, Collections.<ServiceSetup>emptyList());
            }
            @Override
            protected Job createJob() throws IOException {
                return jobMock;
            }
            @Override
            protected JsonWriter createJsonWriter(final Job job) throws IOException {
                return new JsonWriter(jobMock, AbstractCoverallsMojoTest.this.coverallsFile);
            }
            @Override
            protected CoverallsClient createCoverallsClient() {
                return coverallsClientMock;
            }
            @Override
            public Log getLog() {
                return logMock;
            }
        };
        mojo.project = projectMock;
        
        List<MavenProject> projects = new ArrayList<MavenProject>();
        projects.add(collectedProjectMock);
        when(projectMock.getCollectedProjects()).thenReturn(projects);
        List<String> sourceRoots = new ArrayList<String>();
        sourceRoots.add(folder.getRoot().getAbsolutePath());
        when(collectedProjectMock.getCompileSourceRoots()).thenReturn(sourceRoots);
    }

    @Test
    public void testDefaultBehavior() throws Exception {
        mojo = new AbstractCoverallsMojo() {
            @Override
            protected CoverageParser createCoverageParser(final SourceLoader sourceLoader) {
                return createMojo().createCoverageParser(sourceLoaderMock);
            }
        };
        mojo.sourceDirectories = Arrays.asList(TestIoUtil.getFile("/"));
        mojo.sourceEncoding = "UTF-8";
        mojo.project = projectMock;
        mojo.repoToken = "asdfg";
        mojo.coverallsFile = folder.newFile();
        mojo.dryRun = true;
        mojo.skip = false;
        
        when(projectMock.getBasedir()).thenReturn(TestIoUtil.getFile("/"));
        
        mojo.execute();
    }
    
    @Test
    public void testSuccesfullSubmission() throws Exception {
        when(coverallsClientMock.submit(any(File.class))).thenReturn(new CoverallsResponse("success", false, null));
        mojo.execute();
        String json = TestIoUtil.readFileContent(coverallsFile);
        assertNotNull(json);
        
        String[][] fixture = getCoverageFixture();
        for (String[] coverageFile : fixture) {
            assertThat(json, containsString(coverageFile[0]));
        }

        verifySuccessfullSubmit(logMock, fixture);
    }

    @Test(expected = MojoFailureException.class)
    public void testFailedSubmission() throws Exception {
        when(coverallsClientMock.submit(any(File.class))).thenThrow(ProcessingException.class);
        mojo.execute();
    }
    
    @Test
    public void testFailWithProcessingException() throws Exception {
        when(coverallsClientMock.submit(any(File.class))).thenThrow(new ProcessingException());
        try {
            mojo.execute();
            fail("Should have failed with MojoFailureException");
        } catch (MojoFailureException ex) {
            assertEquals(ex.getCause().getClass(), ProcessingException.class);
        }
    }
    
    @Test
    public void testFailWithIOException() throws Exception {
        when(coverallsClientMock.submit(any(File.class))).thenThrow(new IOException());
        try {
            mojo.execute();
            fail("Should have failed with MojoFailureException");
        } catch (MojoFailureException ex) {
            assertEquals(ex.getCause().getClass(), IOException.class);
        }
    }
    
    @Test
    public void testFailWithNullPointerException() throws Exception {
        when(coverallsClientMock.submit(any(File.class))).thenThrow(new NullPointerException());
        try {
            mojo.execute();
            fail("Should have failed with MojoFailureException");
        } catch (MojoExecutionException ex) {
            assertEquals(ex.getCause().getClass(), NullPointerException.class);
        }
    }
    
    @Test
    public void testSkipExecution() throws Exception {
        mojo.skip = true;
        mojo.execute();
        
        verifyZeroInteractions(jobMock);
    }
    
    protected abstract AbstractCoverallsMojo createMojo();

    protected abstract String[][] getCoverageFixture();

    public static void verifySuccessfullSubmit(Log logMock, String[][] fixture) {
        verify(logMock).info("Gathered code coverage metrics for " + CoverageFixture.getTotalFiles(fixture) + " source files with " + CoverageFixture.getTotalLines(fixture) + " lines of code:");
        verify(logMock).info("*** It might take hours for Coveralls to update the actual coverage numbers for a job");
    }

    protected String readFileContent(final String sourceFile) throws IOException {
        return TestIoUtil.readFileContent(TestIoUtil.getFile(sourceFile));
    }
}
