package org.eluder.coveralls.maven.plugin.cobertura;

import java.io.File;
import java.io.IOException;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.eluder.coveralls.maven.plugin.AbstractXmlEventParser;
import org.eluder.coveralls.maven.plugin.ProcessingException;
import org.eluder.coveralls.maven.plugin.SourceCallback;
import org.eluder.coveralls.maven.plugin.domain.Source;
import org.eluder.coveralls.maven.plugin.domain.SourceLoader;

public class CoberturaParser extends AbstractXmlEventParser {

    private Source source;
    private boolean inMethods;
    
    public CoberturaParser(final File coverageFile, final SourceLoader sourceLoader) {
        super(coverageFile, sourceLoader);
    }
    
    @Override
    protected void onEvent(final XMLStreamReader xml, final SourceCallback callback) throws XMLStreamException, ProcessingException, IOException {
        if (isStartElement(xml, "class")) {
            source = loadSource(xml.getAttributeValue(null, "filename"));
            String className = xml.getAttributeValue(null, "name");
            int classifierPosition = className.indexOf('$');
            if (classifierPosition > 0) {
                source.setClassifier(className.substring(classifierPosition));
            }
        } else
        
        if (isStartElement(xml, "methods") && source != null) {
            inMethods = true;
        } else
        
        if (isEndElement(xml, "methods") && source != null) {
            inMethods = false;
        } else
        
        if (isStartElement(xml, "line") && !inMethods && source != null) {
            source.addCoverage(
                    Integer.parseInt(xml.getAttributeValue(null, "number")),
                    Integer.valueOf(xml.getAttributeValue(null, "hits"))
            );
        } else
        
        if (isEndElement(xml, "class") && source != null) {
            callback.onSource(source);
            source = null;
        }
    }

}
