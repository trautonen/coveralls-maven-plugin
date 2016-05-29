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

import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.Reporting;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Settings;
import org.eluder.coveralls.maven.plugin.domain.CoverallsResponse;
import org.eluder.coveralls.maven.plugin.domain.Git;
import org.eluder.coveralls.maven.plugin.domain.Job;
import org.eluder.coveralls.maven.plugin.domain.Source;
import org.eluder.coveralls.maven.plugin.httpclient.CoverallsClient;
import org.eluder.coveralls.maven.plugin.json.JsonWriter;
import org.eluder.coveralls.maven.plugin.parser.CoberturaParser;
import org.eluder.coveralls.maven.plugin.service.ServiceSetup;
import org.eluder.coveralls.maven.plugin.source.SourceLoader;
import org.eluder.coveralls.maven.plugin.util.TestIoUtil;
import org.eluder.coveralls.maven.plugin.validation.ValidationErrors;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CoverallsReportMojoTest {
    
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    public File coverallsFile;
    
    private CoverallsReportMojo mojo;
    
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
    
    @Mock
    private Model modelMock;
    
    @Mock
    private Reporting reportingMock;
    
    @Mock
    private Build buildMock;

    @Mock
    private Settings settingsMock;
    
    @Before
    public void init() throws Exception {
        coverallsFile = folder.newFile();
        
        when(sourceLoaderMock.load(anyString())).then(new Answer<Source>() {
            @Override
            public Source answer(final InvocationOnMock invocation) throws Throwable {
                String sourceFile = invocation.getArguments()[0].toString();
                String content = readFileContent(sourceFile);
                return new Source(sourceFile, content, TestIoUtil.getMd5DigestHex(content));
            }
        });
        when(logMock.isInfoEnabled()).thenReturn(true);
        when(jobMock.validate()).thenReturn(new ValidationErrors());

        
        mojo = new CoverallsReportMojo() {
            @Override
            protected SourceLoader createSourceLoader(final Job job) {
                return sourceLoaderMock;
            }
            @Override
            protected List<CoverageParser> createCoverageParsers(SourceLoader sourceLoader) throws IOException {
                List<CoverageParser> parsers = new ArrayList<CoverageParser>();
                parsers.add(new CoberturaParser(TestIoUtil.getFile("cobertura.xml"), sourceLoader));
                return parsers;
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
                return new JsonWriter(jobMock, CoverallsReportMojoTest.this.coverallsFile);
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
        mojo.settings = settingsMock;
        mojo.project = projectMock;
        mojo.sourceEncoding = "UTF-8";
        mojo.failOnServiceError = true;
        
        when(modelMock.getReporting()).thenReturn(reportingMock);
        when(reportingMock.getOutputDirectory()).thenReturn(folder.getRoot().getAbsolutePath());
        when(buildMock.getDirectory()).thenReturn(folder.getRoot().getAbsolutePath());
        
        List<MavenProject> projects = new ArrayList<MavenProject>();
        projects.add(collectedProjectMock);
        when(projectMock.getCollectedProjects()).thenReturn(projects);
        when(projectMock.getBuild()).thenReturn(buildMock);
        when(projectMock.getModel()).thenReturn(modelMock);
        List<String> sourceRoots = new ArrayList<String>();
        sourceRoots.add(folder.getRoot().getAbsolutePath());
        when(collectedProjectMock.getCompileSourceRoots()).thenReturn(sourceRoots);
        when(collectedProjectMock.getBuild()).thenReturn(buildMock);
        when(collectedProjectMock.getModel()).thenReturn(modelMock);
    }

    @Test(expected = IOException.class)
    public void testCreateCoverageParsersWithoutCoverageReports() throws Exception {
        mojo = new CoverallsReportMojo();
        mojo.settings = settingsMock;
        mojo.project = projectMock;
        mojo.createCoverageParsers(sourceLoaderMock);
    }

    @Test
    public void testCreateSourceLoader() throws Exception {
        Git gitMock = Mockito.mock(Git.class);
        when(gitMock.getBaseDir()).thenReturn(folder.newFolder("git"));
        when(jobMock.getGit()).thenReturn(gitMock);
        TestIoUtil.writeFileContent("public interface Test { }", folder.newFile("source.java"));
        mojo = new CoverallsReportMojo();
        mojo.settings = settingsMock;
        mojo.project = projectMock;
        mojo.sourceEncoding = "UTF-8";
        SourceLoader sourceLoader = mojo.createSourceLoader(jobMock);
        Source source = sourceLoader.load("source.java");
        assertNotNull(source);
    }

    @Test
    public void testDefaultBehavior() throws Exception {
        mojo = new CoverallsReportMojo() {
            @Override
            protected SourceLoader createSourceLoader(final Job job) {
                return sourceLoaderMock;
            }
            @Override
            protected List<CoverageParser> createCoverageParsers(SourceLoader sourceLoader) throws IOException {
                return Collections.emptyList();
            }
        };
        mojo.sourceDirectories = Arrays.asList(TestIoUtil.getFile("/"));
        mojo.sourceEncoding = "UTF-8";
        mojo.settings = settingsMock;
        mojo.project = projectMock;
        mojo.repoToken = "asdfg";
        mojo.coverallsFile = folder.newFile();
        mojo.dryRun = true;
        mojo.skip = false;
        mojo.basedir = TestIoUtil.getFile("/");
        
        when(projectMock.getBasedir()).thenReturn(TestIoUtil.getFile("/"));
        
        mojo.execute();
    }
    
    @Test
    public void testSuccesfullSubmission() throws Exception {
        when(coverallsClientMock.submit(any(File.class))).thenReturn(new CoverallsResponse("success", false, null));
        mojo.execute();
        String json = TestIoUtil.readFileContent(coverallsFile);
        assertNotNull(json);
        
        String[][] fixture = CoverageFixture.JAVA_FILES;
        for (String[] coverageFile : fixture) {
            assertThat(json, containsString(coverageFile[0]));
        }

        verifySuccessfullSubmit(logMock, fixture);
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
    public void testProcessingExceptionWithAllowedServiceFailure() throws Exception {
        mojo.failOnServiceError = false;
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
    public void testIOExceptionWithAllowedServiceFailure() throws Exception {
        mojo.failOnServiceError = false;
        when(coverallsClientMock.submit(any(File.class))).thenThrow(new IOException());
        mojo.execute();
        verify(logMock).warn(anyString());
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
    
    public static void verifySuccessfullSubmit(Log logMock, String[][] fixture) {
        verify(logMock).info("Gathered code coverage metrics for " + CoverageFixture.getTotalFiles(fixture) + " source files with " + CoverageFixture.getTotalLines(fixture) + " lines of code:");
        verify(logMock).info("*** It might take hours for Coveralls to update the actual coverage numbers for a job");
    }

    protected String readFileContent(final String sourceFile) throws IOException {
        return TestIoUtil.readFileContent(TestIoUtil.getFile(sourceFile));
    }
}
