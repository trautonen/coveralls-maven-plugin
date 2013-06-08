package org.eluder.coveralls.maven.plugin.jacoco;

import java.io.File;

import org.eluder.coveralls.maven.plugin.AbstractCoverageParserTest;
import org.eluder.coveralls.maven.plugin.CoverageParser;
import org.eluder.coveralls.maven.plugin.domain.SourceLoader;

public class JaCoCoParserTest extends AbstractCoverageParserTest {
    
    @Override
    protected CoverageParser createCoverageParser(final File coverageFile, final SourceLoader sourceLoader) {
        return new JaCoCoParser(coverageFile, sourceLoader);
    }
    
    @Override
    protected String getCoverageResource() {
        return "/jacoco.xml";
    }
}
