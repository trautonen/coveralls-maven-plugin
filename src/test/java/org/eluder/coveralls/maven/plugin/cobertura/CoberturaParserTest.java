package org.eluder.coveralls.maven.plugin.cobertura;

import java.io.File;

import org.eluder.coveralls.maven.plugin.AbstractCoverageParserTest;
import org.eluder.coveralls.maven.plugin.CoverageParser;
import org.eluder.coveralls.maven.plugin.domain.SourceLoader;

public class CoberturaParserTest extends AbstractCoverageParserTest {

    @Override
    protected CoverageParser createCoverageParser(final File coverageFile, final SourceLoader sourceLoader) {
        return new CoberturaParser(coverageFile, sourceLoader);
    }
    
    @Override
    protected String getCoverageResource() {
        return "/cobertura.xml";
    }

}
