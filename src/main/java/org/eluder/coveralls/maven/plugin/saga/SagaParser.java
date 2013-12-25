package org.eluder.coveralls.maven.plugin.saga;

import java.io.File;

import org.eluder.coveralls.maven.plugin.cobertura.CoberturaParser;
import org.eluder.coveralls.maven.plugin.domain.SourceLoader;

/**
 * @author Jakub Bednář (25/12/2013 10:07)
 */
public class SagaParser extends CoberturaParser {

    public SagaParser(final File coverageFile, final SourceLoader sourceLoader) {
        super(coverageFile, sourceLoader);
    }
}
