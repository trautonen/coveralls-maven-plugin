package org.eluder.coveralls.maven.plugin.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.maven.project.MavenProject;
import org.eluder.coveralls.maven.plugin.CoverageParser;
import org.eluder.coveralls.maven.plugin.parser.CoberturaParser;
import org.eluder.coveralls.maven.plugin.domain.SourceLoader;
import org.eluder.coveralls.maven.plugin.parser.JaCoCoParser;
import org.eluder.coveralls.maven.plugin.parser.SagaParser;

public class CoverageParsersFactory {
    
    private final MavenProject project;
    private final SourceLoader sourceLoader;
    private List<File> jacocoReports;
    private List<File> coberturaReports;
    private List<File> sagaReports;

    public CoverageParsersFactory(final MavenProject project, final SourceLoader sourceLoader) {
        this.project = project;
        this.sourceLoader = sourceLoader;
    }
    
    public CoverageParsersFactory withJacocoReports(final List<File> jacocoReports) {
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
    
    public List<CoverageParser> createParsers() {
        List<CoverageParser> parsers = new ArrayList<CoverageParser>();
        List<MavenProject> projects = new MavenProjectCollector(project).collect();

        ExistingFiles jacocoFiles = ExistingFiles.create(jacocoReports);
        ExistingFiles coberturaFiles = ExistingFiles.create(coberturaReports);
        ExistingFiles sagaFiles = ExistingFiles.create(sagaReports);
        for (MavenProject p : projects) {
            File reportingDirectory = new File(p.getModel().getReporting().getOutputDirectory());
            File buildDirectory = new File(p.getBuild().getOutputDirectory());
            
            jacocoFiles.add(new File(reportingDirectory, "/jacoco/jacoco.xml"));
            coberturaFiles.add(new File(reportingDirectory, "/cobertura/coverage.xml"));
            sagaFiles.add(new File(buildDirectory, "/saga-coverage/total-coverage.xml"));
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
        
        return Collections.unmodifiableList(parsers);
    }
}
