package org.eluder.coveralls.maven.plugin.chain;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.eluder.coveralls.maven.plugin.AbstractCoverallsMojo;
import org.eluder.coveralls.maven.plugin.CoverageParser;
import org.eluder.coveralls.maven.plugin.ProcessingException;
import org.eluder.coveralls.maven.plugin.SourceCallback;
import org.eluder.coveralls.maven.plugin.cobertura.CoberturaParser;
import org.eluder.coveralls.maven.plugin.domain.SourceLoader;
import org.eluder.coveralls.maven.plugin.jacoco.JaCoCoParser;
import org.eluder.coveralls.maven.plugin.json.JsonWriter;
import org.eluder.coveralls.maven.plugin.saga.SagaParser;

/**
 * @author Jakub Bednář (27/12/2013 09:57)
 */
@Mojo(name = "chain", threadSafe = false)
public class ChainMojo extends AbstractCoverallsMojo {

    /**
     * File path to Cobertura coverage file.
     */
    @Parameter(property = "coberturaFile")
    protected File coberturaFile;

    /**
     * File path to JaCoCo coverage file.
     */
    @Parameter(property = "jacocoFile")
    protected File jacocoFile;

    /**
     * File path to Saga coverage file.
     */
    @Parameter(property = "sagaFile")
    protected File sagaFile;

    /**
     * @see org.eluder.coveralls.maven.plugin.saga.SagaMojo#deployedDirectoryName
     */
    @Parameter(property = "deployedDirectoryName", required = true, defaultValue = "src/")
    protected String deployedDirectoryName;

    @Override
    protected CoverageParser createCoverageParser(final SourceLoader sourceLoader) {
        return null;
    }

    @Override
    protected void writeCoveralls(final JsonWriter writer, final SourceLoader sourceLoader, final SourceCallback sourceCallback, final CoverageParser parser) throws ProcessingException, IOException {

        try {
            getLog().info("Writing Coveralls data to " + writer.getCoverallsFile().getAbsolutePath());
            long now = System.currentTimeMillis();
            writer.writeStart();

            if (coberturaFile != null) {
                getLog().info("Writing from Cobertura report: " + coberturaFile.getAbsolutePath());
                new CoberturaParser(coberturaFile, sourceLoader).parse(sourceCallback);
            }

            if (jacocoFile != null) {
                getLog().info("Writing from JaCoCo report: " + jacocoFile.getAbsolutePath());
                new JaCoCoParser(jacocoFile, sourceLoader).parse(sourceCallback);
            }

            if (sagaFile != null) {
                getLog().info("Writing from Saga report: " + sagaFile.getAbsolutePath());
                new SagaParser(sagaFile, deployedDirectoryName, sourceLoader).parse(sourceCallback);
            }

            writer.writeEnd();
            long duration = System.currentTimeMillis() - now;
            getLog().info("Successfully wrote Coveralls data in " + duration + "ms");
        } finally {
            writer.close();
        }
    }
}
