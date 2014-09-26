package org.eluder.coveralls.maven.plugin.parser;

import java.io.File;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.eluder.coveralls.maven.plugin.ProcessingException;
import org.eluder.coveralls.maven.plugin.source.SourceCallback;
import org.eluder.coveralls.maven.plugin.source.SourceLoader;

public class CloverParser extends AbstractXmlEventParser {

    public CloverParser(final File coverageFile, final SourceLoader sourceLoader) {
        super(coverageFile, sourceLoader);
    }

    @Override
    protected void onEvent(final XMLStreamReader xml, final SourceCallback callback) throws XMLStreamException, ProcessingException, IOException {
        
    }
}
