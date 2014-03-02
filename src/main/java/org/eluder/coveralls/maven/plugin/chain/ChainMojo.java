package org.eluder.coveralls.maven.plugin.chain;

/*
 * #[license]
 * coveralls-maven-plugin
 * %%
 * Copyright (C) 2013 - 2014 Tapio Rautonen
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * %[license]
 */

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
    @Parameter(property = "coberturaFile", defaultValue = "${project.reporting.outputDirectory}/cobertura/coverage.xml")
    protected File coberturaFile;

    /**
     * File path to JaCoCo coverage file.
     */
    @Parameter(property = "jacocoFile", defaultValue = "${project.reporting.outputDirectory}/jacoco/jacoco.xml")
    protected File jacocoFile;

    /**
     * File path to Saga coverage file.
     */
    @Parameter(property = "sagaFile", defaultValue = "${project.build.directory}/saga-coverage/total-coverage.xml")
    protected File sagaFile;

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
            int sources = 0;

            if (coberturaFile != null && coberturaFile.exists()) {
                sources++;
                getLog().info("  .. from Cobertura report: " + coberturaFile.getAbsolutePath());
                new CoberturaParser(coberturaFile, sourceLoader).parse(sourceCallback);
            }

            if (jacocoFile != null && jacocoFile.exists()) {
                sources++;
                getLog().info("  .. from JaCoCo report: " + jacocoFile.getAbsolutePath());
                new JaCoCoParser(jacocoFile, sourceLoader).parse(sourceCallback);
            }

            if (sagaFile != null && sagaFile.exists()) {
                sources++;
                getLog().info("  .. from Saga report: " + sagaFile.getAbsolutePath());
                new SagaParser(sagaFile, sourceLoader).parse(sourceCallback);
            }

            writer.writeEnd();
            long duration = System.currentTimeMillis() - now;
            getLog().info("Successfully wrote Coveralls data in " + duration + "ms from " + sources + " coverage sources");
        } finally {
            writer.close();
        }
    }
}
