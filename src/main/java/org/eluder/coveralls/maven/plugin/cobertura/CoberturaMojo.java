package org.eluder.coveralls.maven.plugin.cobertura;

import java.io.File;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.eluder.coveralls.maven.plugin.AbstractCoverallsMojo;
import org.eluder.coveralls.maven.plugin.CoverageParser;
import org.eluder.coveralls.maven.plugin.domain.SourceLoader;

@Mojo(name = "cobertura", threadSafe = false)
public class CoberturaMojo extends AbstractCoverallsMojo {

    /**
     * File path to Cobertura coverage file.
     */
    @Parameter(property = "coverageFile", defaultValue = "${project.reporting.outputDirectory}/cobertura/coverage.xml")
    protected File coverageFile;
    
    @Override
    protected CoverageParser createCoverageParser(final SourceLoader sourceLoader) {
        return new CoberturaParser(coverageFile, sourceLoader);
    }

}
