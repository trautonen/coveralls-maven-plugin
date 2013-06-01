package org.eluder.coveralls.maven.plugin.jacoco;

import java.io.File;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.eluder.coveralls.maven.plugin.AbstractCoverallsMojo;
import org.eluder.coveralls.maven.plugin.CoverageParser;
import org.eluder.coveralls.maven.plugin.domain.SourceLoader;

@Mojo(name = "jacoco", threadSafe = false)
public class JaCoCoMojo extends AbstractCoverallsMojo {

    @Parameter(property = "coverageFile", defaultValue = "${project.reporting.outputDirectory}/jacoco/jacoco.xml")
    private File coverageFile;

    @Override
    protected CoverageParser createCoverageParser(final SourceLoader sourceLoader) {
        return new JaCoCoParser(coverageFile, sourceLoader);
    }
}
