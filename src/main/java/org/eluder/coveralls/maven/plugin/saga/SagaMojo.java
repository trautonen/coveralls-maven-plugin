package org.eluder.coveralls.maven.plugin.saga;

import java.io.File;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.eluder.coveralls.maven.plugin.AbstractCoverallsMojo;
import org.eluder.coveralls.maven.plugin.CoverageParser;
import org.eluder.coveralls.maven.plugin.domain.SourceLoader;

@Mojo(name = "saga", threadSafe = false)
public class SagaMojo extends AbstractCoverallsMojo {

    /**
     * File path to Saga coverage file.
     */
    @Parameter(property = "coverageFile", defaultValue = "${project.build.directory}/saga-coverage/total-coverage.xml")
    protected File coverageFile;

    /**
     * <a href="http://searls.github.io/jasmine-maven-plugin/bdd-mojo.html#srcDirectoryName">Deploy directory name</a>
     * on "Jasmine" server. This will be omitted from the directory path of loaded source files.
     */
    @Parameter(property = "deployDirectoryName", required = true, defaultValue = "src")
    protected String deployDirectoryName;

    @Override
    protected CoverageParser createCoverageParser(final SourceLoader sourceLoader) {
        return new SagaParser(coverageFile, deployDirectoryName, sourceLoader);
    }
}
