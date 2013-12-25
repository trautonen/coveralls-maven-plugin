package org.eluder.coveralls.maven.plugin.saga;

import java.io.File;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.eluder.coveralls.maven.plugin.AbstractCoverallsMojo;
import org.eluder.coveralls.maven.plugin.CoverageParser;
import org.eluder.coveralls.maven.plugin.domain.SourceLoader;

/**
 * <a href="http://timurstrekalov.github.io/saga/">Saga</a> code coverage
 *
 * @author Jakub Bednar (24/12/2013 14:34)
 */
@Mojo(name = "saga", threadSafe = false)
public class SagaMojo extends AbstractCoverallsMojo
{
    /**
     * File path to Saga coverage file.
     */
    @Parameter(property = "coverageFile", defaultValue = "${project.build.directory}/saga-coverage/total-coverage.xml")
    protected File coverageFile;

    @Override
    protected CoverageParser createCoverageParser(final SourceLoader sourceLoader)
    {
        return new SagaParser(coverageFile, sourceLoader);
    }
}
