package org.eluder.coveralls.maven.plugin.jacoco;

import java.io.File;
import java.io.IOException;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.eluder.coveralls.maven.plugin.AbstractXmlEventParser;
import org.eluder.coveralls.maven.plugin.ProcessingException;
import org.eluder.coveralls.maven.plugin.SourceCallback;
import org.eluder.coveralls.maven.plugin.domain.Source;
import org.eluder.coveralls.maven.plugin.domain.SourceLoader;

public class JaCoCoParser extends AbstractXmlEventParser {

    private String packageName;
    private Source source;
    
    public JaCoCoParser(final File coverageFile, final SourceLoader sourceLoader) {
        super(coverageFile, sourceLoader);
    }
    
    @Override
    protected void onEvent(final XMLStreamReader xml, final SourceCallback callback) throws XMLStreamException, ProcessingException, IOException {
        if (isStartElement(xml, "package")) {
            this.packageName = xml.getAttributeValue(null, "name");
        } else
        
        if (isStartElement(xml, "sourcefile") && packageName != null) {
            String sourceFile = this.packageName + "/" + xml.getAttributeValue(null, "name");
            this.source = loadSource(sourceFile);
        } else
        
        if (isStartElement(xml, "line") && this.source != null) {
            this.source.addCoverage(
                    Integer.parseInt(xml.getAttributeValue(null, "nr")),
                    Integer.valueOf(xml.getAttributeValue(null, "ci"))
            );
        } else
        
        if (isEndElement(xml, "sourcefile") && this.source != null) {
            callback.onSource(this.source);
            this.source = null;
        } else
        
        if (isEndElement(xml, "package")) {
            this.packageName = null;
        }
    }

}
