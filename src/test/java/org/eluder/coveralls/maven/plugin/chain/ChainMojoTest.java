package org.eluder.coveralls.maven.plugin.chain;

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

import static org.eluder.coveralls.maven.plugin.AbstractCoverallsMojoTest.verifySuccessfullSubmit;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.ReflectionUtils;
import org.eluder.coveralls.maven.plugin.CoverageFixture;
import org.eluder.coveralls.maven.plugin.Environment;
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

/**
 * @author Jakub Bednář (27/12/2013 10:38)
 */
@RunWith(MockitoJUnitRunner.class)
public class ChainMojoTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    public File coverallsFile;

    private ChainMojo mojo;

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
                String content = TestIoUtil.readFileContent(TestIoUtil.getFile(sourceFile));
                return new Source(sourceFile, content);
            }
        });
        when(logMock.isInfoEnabled()).thenReturn(true);
        
        when(jobMock.validate()).thenReturn(new ValidationErrors());

        mojo = new ChainMojo() {
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
                return new JsonWriter(jobMock, ChainMojoTest.this.coverallsFile);
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

        Field field = ReflectionUtils.getFieldByNameIncludingSuperclasses("project", mojo.getClass());
        field.setAccessible(true);
        field.set(mojo, projectMock);

        List<MavenProject> projects = new ArrayList<MavenProject>();
        projects.add(collectedProjectMock);
        when(projectMock.getCollectedProjects()).thenReturn(projects);

        List<String> sourceRoots = new ArrayList<String>();
        sourceRoots.add(folder.getRoot().getAbsolutePath());
        when(collectedProjectMock.getCompileSourceRoots()).thenReturn(sourceRoots);
    }


    @Test
    public void testSuccesfullSubmissionForCoberturaAndSaga() throws Exception {
        mojo.coberturaFile = TestIoUtil.getFile("cobertura.xml");
        mojo.sagaFile = TestIoUtil.getFile("saga.xml");

        when(coverallsClientMock.submit(any(File.class))).thenReturn(new CoverallsResponse("success", false, null));
        mojo.execute();
        String json = TestIoUtil.readFileContent(coverallsFile);
        assertNotNull(json);
        
        String[][] fixture = CoverageFixture.JAVA_AND_JAVASCRIPT_FILES;
        for (String[] coverageFile : fixture) {
            assertThat(json, containsString(coverageFile[0]));
        }

        verifySuccessfullSubmit(logMock, fixture);
    }

    @Test
    public void testSuccesfullSubmissionForJaCoCo() throws Exception {
        mojo.jacocoFile = TestIoUtil.getFile("jacoco.xml");

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
}
