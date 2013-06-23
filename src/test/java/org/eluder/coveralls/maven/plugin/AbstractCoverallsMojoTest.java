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

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.eluder.coveralls.maven.plugin.domain.CoverallsResponse;
import org.eluder.coveralls.maven.plugin.domain.Git;
import org.eluder.coveralls.maven.plugin.domain.Git.Head;
import org.eluder.coveralls.maven.plugin.domain.Job;
import org.eluder.coveralls.maven.plugin.domain.Source;
import org.eluder.coveralls.maven.plugin.domain.SourceLoader;
import org.eluder.coveralls.maven.plugin.httpclient.CoverallsClient;
import org.eluder.coveralls.maven.plugin.json.JsonWriter;
import org.eluder.coveralls.maven.plugin.util.TestIoUtil;
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

    private static final int MAX_NUMBER_OF_LINES = 30;
    
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
    
    @Before
    public void init() throws Exception {
        final AbstractCoverallsMojo delegate = createMojo();
        coverallsFile = folder.newFile();
        
        when(sourceLoaderMock.load(anyString())).then(new Answer<Source>() {
            @Override
            public Source answer(final InvocationOnMock invocation) throws Throwable {
                StringBuilder content = new StringBuilder();
                for (int i = 0; i < MAX_NUMBER_OF_LINES; i++) {
                    content.append("\n");
                }
                return new Source(invocation.getArguments()[0].toString(), content.toString());
            }
        });
        
        mojo = new AbstractCoverallsMojo() {
            @Override
            protected CoverageParser createCoverageParser(final SourceLoader sourceLoader) {
                return delegate.createCoverageParser(sourceLoader);
            }
            @Override
            protected SourceLoader createSourceLoader() {
                return sourceLoaderMock;
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
    }
    
    @Test
    public void testSuccesfullSubmission() throws Exception {
        when(coverallsClientMock.submit(any(File.class))).thenReturn(new CoverallsResponse("success", false, null));
        mojo.execute();
        String json = TestIoUtil.readFileContent(coverallsFile);
        
        assertNotNull(json);
        for (String[] coverageFile : CoverageFixture.COVERAGE_FILES) {
            assertThat(json, containsString(coverageFile[0]));
        }
    }
    
    @Test(expected = MojoFailureException.class)
    public void testFailedSubmission() throws Exception {
        when(coverallsClientMock.submit(any(File.class))).thenReturn(new CoverallsResponse("failure", true, null));
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
    public void testDescribeJob() throws Exception {
        Git git = new Git(new Head("ab679cf2d81ac", null, null, null, null, null), "master", null);
        when(jobMock.getServiceName()).thenReturn("service");
        when(jobMock.getServiceJobId()).thenReturn("666");
        when(jobMock.getRepoToken()).thenReturn("123456789");
        when(jobMock.getGit()).thenReturn(git);
        when(coverallsClientMock.submit(any(File.class))).thenReturn(new CoverallsResponse("success", false, null));
        
        mojo.execute();
        
        verify(logMock).info("Starting Coveralls job for service (666)");
        verify(logMock).info("Using repository token <secret>");
        verify(logMock).info("Git commit ab679cf in master");
    }
    
    protected abstract AbstractCoverallsMojo createMojo();
}
