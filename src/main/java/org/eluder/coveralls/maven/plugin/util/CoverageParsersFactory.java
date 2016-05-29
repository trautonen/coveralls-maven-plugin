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

import org.apache.maven.project.MavenProject;
import org.eluder.coveralls.maven.plugin.CoverageParser;
import org.eluder.coveralls.maven.plugin.parser.CoberturaParser;
import org.eluder.coveralls.maven.plugin.parser.JaCoCoParser;
import org.eluder.coveralls.maven.plugin.parser.SagaParser;
import org.eluder.coveralls.maven.plugin.source.SourceLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CoverageParsersFactory {

    private static final String JACOCO_FILE = "/jacoco.xml";
    private static final String JACOCO_PREFIX = "/jacoco";
    private static final String JACOCO_IT_PREFIX = "/jacoco-it";

    private static final String COBERTURA_FILE = "/coverage.xml";
    private static final String COBERTURA_PREFIX = "/cobertura";

    private static final String SAGA_FILE = "/total-coverage.xml";
    private static final String SAGA_PREFIX = "/saga-coverage";
    
    private final MavenProject project;
    private final SourceLoader sourceLoader;
    private List<File> jacocoReports;
    private List<File> coberturaReports;
    private List<File> sagaReports;
    private List<String> relativeReportDirs;

    public CoverageParsersFactory(final MavenProject project, final SourceLoader sourceLoader) {
        this.project = project;
        this.sourceLoader = sourceLoader;
    }
    
    public CoverageParsersFactory withJaCoCoReports(final List<File> jacocoReports) {
        this.jacocoReports = jacocoReports;
        return this;
    }
    
    public CoverageParsersFactory withCoberturaReports(final List<File> coberturaReports) {
        this.coberturaReports = coberturaReports;
        return this;
    }
    
    public CoverageParsersFactory withSagaReports(final List<File> sagaReports) {
        this.sagaReports = sagaReports;
        return this;
    }

    public CoverageParsersFactory withRelativeReportDirs(final List<String> relativeReportDirs) {
        this.relativeReportDirs = relativeReportDirs;
        return this;
    }
    
    public List<CoverageParser> createParsers() throws IOException {
        List<CoverageParser> parsers = new ArrayList<>();
        List<MavenProject> projects = new MavenProjectCollector(project).collect();

        ExistingFiles jacocoFiles = ExistingFiles.create(jacocoReports);
        ExistingFiles coberturaFiles = ExistingFiles.create(coberturaReports);
        ExistingFiles sagaFiles = ExistingFiles.create(sagaReports);
        for (MavenProject p : projects) {
            File reportingDirectory = new File(p.getModel().getReporting().getOutputDirectory());
            File buildDirectory = new File(p.getBuild().getDirectory());
            
            jacocoFiles.add(new File(reportingDirectory, JACOCO_PREFIX + JACOCO_FILE));
            jacocoFiles.add(new File(reportingDirectory, JACOCO_IT_PREFIX + JACOCO_FILE));
            coberturaFiles.add(new File(reportingDirectory, COBERTURA_PREFIX + COBERTURA_FILE));
            sagaFiles.add(new File(buildDirectory, SAGA_PREFIX + SAGA_FILE));

            if (relativeReportDirs != null) {
                for (String relativeReportPath : relativeReportDirs) {
                    File relativeReportingDirectory = reportingDirectory;
                    File relativeBuildDirectory = buildDirectory;
                    if (!relativeReportPath.isEmpty() && !File.separator.equals(relativeReportPath)) {
                        relativeReportingDirectory = new File(reportingDirectory, relativeReportPath);
                        relativeBuildDirectory = new File(buildDirectory, relativeReportPath);
                    }

                    jacocoFiles.add(new File(relativeReportingDirectory, JACOCO_FILE));
                    jacocoFiles.add(new File(relativeBuildDirectory, JACOCO_FILE));
                    coberturaFiles.add(new File(relativeReportingDirectory, COBERTURA_FILE));
                    coberturaFiles.add(new File(relativeBuildDirectory, COBERTURA_FILE));
                    sagaFiles.add(new File(relativeReportingDirectory, SAGA_FILE));
                    sagaFiles.add(new File(relativeBuildDirectory, SAGA_FILE));
                }
            }
        }
        
        for (File jacocoFile : jacocoFiles) {
            parsers.add(new JaCoCoParser(jacocoFile, sourceLoader));
        }
        for (File coberturaFile : coberturaFiles) {
            parsers.add(new CoberturaParser(coberturaFile, sourceLoader));
        }
        for (File sagaFile : sagaFiles) {
            parsers.add(new SagaParser(sagaFile, sourceLoader));
        }
        
        if (parsers.isEmpty()) {
            throw new IOException("No coverage report files found");
        }
        
        return Collections.unmodifiableList(parsers);
    }
}
