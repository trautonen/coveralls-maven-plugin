package org.eluder.coveralls.maven.plugin.saga;

import java.io.File;

import org.eluder.coveralls.maven.plugin.AbstractCoverageParserTest;
import org.eluder.coveralls.maven.plugin.CoverageFixture;
import org.eluder.coveralls.maven.plugin.CoverageParser;
import org.eluder.coveralls.maven.plugin.domain.SourceLoader;

/**
 * @author Jakub Bednář (25/12/2013 10:17)
 */
public class SagaParserTest extends AbstractCoverageParserTest {

    @Override
    protected CoverageParser createCoverageParser(final File coverageFile, final SourceLoader sourceLoader) {
        return new SagaParser(coverageFile, sourceLoader);
    }

    @Override
    protected String getCoverageResource() {
        return "saga.xml";
    }

    @Override
    protected String[][] getCoverageFixture() {
        return CoverageFixture.JAVASCRIPT_FILES;
    }
}
