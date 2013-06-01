package org.eluder.coveralls.maven.plugin.cobertura;

import java.io.File;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.eluder.coveralls.maven.plugin.AbstractCoverallsMojo;
import org.eluder.coveralls.maven.plugin.CoverageParser;
import org.eluder.coveralls.maven.plugin.domain.SourceLoader;

@Mojo(name = "cobertura", threadSafe = false)
public class CoberturaMojo extends AbstractCoverallsMojo {

    @Parameter(property = "coverageFile", defaultValue = "${project.reporting.outputDirectory}/cobertura/coverage.xml")
    private File coverageFile;
    
    @Override
    protected CoverageParser createCoverageParser(final SourceLoader sourceLoader) {
        return new CoberturaParser(coverageFile, sourceLoader);
    }

}
