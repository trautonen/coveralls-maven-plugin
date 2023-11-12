package org.eluder.coveralls.maven.plugin.parser;

import org.eluder.coveralls.maven.plugin.CoverageFixture;
import org.eluder.coveralls.maven.plugin.CoverageParser;
import org.eluder.coveralls.maven.plugin.source.SourceLoader;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class CloverParserTest extends AbstractCoverageParserTest {

    @Override
    protected CoverageParser createCoverageParser(final File coverageFile, final SourceLoader sourceLoader) {
        return new CloverParser(coverageFile, sourceLoader);
    }

    @Override
    protected List<String> getCoverageResources() {
        return Arrays.asList("clover.xml");
    }

    @Override
    protected String[][] getCoverageFixture() {
        return CoverageFixture.JAVA_FILES_CLOVER;
    }
}
