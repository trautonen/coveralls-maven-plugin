package org.eluder.coveralls.maven.plugin.util;

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
import org.apache.maven.project.MavenProject;
import org.eluder.coveralls.maven.plugin.CoverageParser;
import org.eluder.coveralls.maven.plugin.parser.CoberturaParser;
import org.eluder.coveralls.maven.plugin.parser.JaCoCoParser;
import org.eluder.coveralls.maven.plugin.parser.SagaParser;
import org.eluder.coveralls.maven.plugin.source.SourceLoader;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CoverageParsersFactoryTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    @Mock
    private MavenProject projectMock;
    
    @Mock
    private SourceLoader sourceLoaderMock;
    
    @Mock
    private Model modelMock;
    
    @Mock
    private Reporting reportingMock;
    
    @Mock
    private Build buildMock;
    
    private File reportingDir;
    
    private File targetDir;
    
    @Before
    public void init() throws Exception{
        reportingDir = folder.newFolder();
        targetDir = folder.newFolder();
        when(projectMock.getCollectedProjects()).thenReturn(Collections.<MavenProject>emptyList());
        when(projectMock.getModel()).thenReturn(modelMock);
        when(projectMock.getBuild()).thenReturn(buildMock);
        when(modelMock.getReporting()).thenReturn(reportingMock);
        when(reportingMock.getOutputDirectory()).thenReturn(reportingDir.getAbsolutePath());
        when(buildMock.getDirectory()).thenReturn(targetDir.getAbsolutePath());
    }
    
    @Test(expected = IOException.class)
    public void testCreateEmptyParsers() throws Exception {
        createCoverageParsersFactory().createParsers();
    }
    
    @Test
    public void testCreateJaCoCoParser() throws Exception {
        File jacocoDir = new File(reportingDir, "jacoco");
        jacocoDir.mkdir(); new File(jacocoDir, "jacoco.xml").createNewFile();
        List<CoverageParser> parsers = createCoverageParsersFactory().createParsers();
        assertEquals(1, parsers.size());
        assertTrue(JaCoCoParser.class.equals(parsers.get(0).getClass()));
    }
    
    @Test
    public void testCreateCoberturaParser() throws Exception {
        File coberturaDir = new File(reportingDir, "cobertura");
        coberturaDir.mkdir(); new File(coberturaDir, "coverage.xml").createNewFile();
        List<CoverageParser> parsers = createCoverageParsersFactory().createParsers();
        assertEquals(1, parsers.size());
        assertTrue(CoberturaParser.class.equals(parsers.get(0).getClass()));
    }
    
    @Test
    public void testCreateSagaParser() throws Exception {
        File sagaDir = new File(targetDir, "saga-coverage");
        sagaDir.mkdir(); new File(sagaDir, "total-coverage.xml").createNewFile();
        List<CoverageParser> parsers = createCoverageParsersFactory().createParsers();
        assertEquals(1, parsers.size());
        assertTrue(SagaParser.class.equals(parsers.get(0).getClass()));
    }
    
    @Test
    public void testWithJaCoCoReport() throws Exception {
        File jacocoFile = new File(reportingDir, "jacoco-report.xml");
        jacocoFile.createNewFile();
        CoverageParsersFactory factory = createCoverageParsersFactory().withJaCoCoReports(Arrays.asList(jacocoFile));
        List<CoverageParser> parsers = factory.createParsers();
        assertEquals(1, parsers.size());
        assertTrue(JaCoCoParser.class.equals(parsers.get(0).getClass()));
    }
    
    @Test
    public void testWithCoberturaReport() throws Exception {
        File coberturaFile = new File(reportingDir, "cobertura-report.xml");
        coberturaFile.createNewFile();
        CoverageParsersFactory factory = createCoverageParsersFactory().withCoberturaReports(Arrays.asList(coberturaFile));
        List<CoverageParser> parsers = factory.createParsers();
        assertEquals(1, parsers.size());
        assertTrue(CoberturaParser.class.equals(parsers.get(0).getClass()));
    }
    
    @Test
    public void testWithSagaReport() throws Exception {
        File sagaFile = new File(reportingDir, "saga-report.xml");
        sagaFile.createNewFile();
        CoverageParsersFactory factory = createCoverageParsersFactory().withSagaReports(Arrays.asList(sagaFile));
        List<CoverageParser> parsers = factory.createParsers();
        assertEquals(1, parsers.size());
        assertTrue(SagaParser.class.equals(parsers.get(0).getClass()));
    }

    @Test
    public void testWithRelativeReportDirectory() throws Exception {
        File coberturaDir = new File(reportingDir, "customdir");
        coberturaDir.mkdir(); new File(coberturaDir, "coverage.xml").createNewFile();
        CoverageParsersFactory factory = createCoverageParsersFactory().withRelativeReportDirs(Arrays.asList("customdir"));
        List<CoverageParser> parsers = factory.createParsers();
        assertEquals(1, parsers.size());
        assertTrue(CoberturaParser.class.equals(parsers.get(0).getClass()));
    }

    @Test
    public void testWithRootRelativeReportDirectory() throws Exception {
        new File(reportingDir, "coverage.xml").createNewFile();
        CoverageParsersFactory factory = createCoverageParsersFactory().withRelativeReportDirs(Arrays.asList(File.separator));
        List<CoverageParser> parsers = factory.createParsers();
        assertEquals(1, parsers.size());
        assertTrue(CoberturaParser.class.equals(parsers.get(0).getClass()));
    }

    private CoverageParsersFactory createCoverageParsersFactory() {
        return new CoverageParsersFactory(projectMock, sourceLoaderMock);
    }
}
