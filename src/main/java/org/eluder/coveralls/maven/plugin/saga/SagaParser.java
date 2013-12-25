package org.eluder.coveralls.maven.plugin.saga;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.IOException;

import org.eluder.coveralls.maven.plugin.ProcessingException;
import org.eluder.coveralls.maven.plugin.SourceCallback;
import org.eluder.coveralls.maven.plugin.cobertura.CoberturaParser;
import org.eluder.coveralls.maven.plugin.domain.SourceLoader;

/**
 * @author Jakub Bednář (25/12/2013 10:07)
 */
public class SagaParser extends CoberturaParser {

    private final String deployedDirectoryName;

    public SagaParser(final File coverageFile, final String deployedDirectoryName, final SourceLoader sourceLoader) {
        super(coverageFile, sourceLoader);

        this.deployedDirectoryName = deployedDirectoryName;
    }

    @Override
    protected void onEvent(final XMLStreamReader xml, final SourceCallback callback) throws XMLStreamException, ProcessingException, IOException {

        if (isStartElement(xml, "class")) {

            String filename = xml.getAttributeValue(null, "filename");
            if (deployedDirectoryName != null) {
                filename = filename.replace(deployedDirectoryName + "/", "/");
            }

            source = loadSource(filename);

        } else {
            super.onEvent(xml, callback);
        }
    }
}
