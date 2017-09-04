package org.eluder.coveralls.maven.plugin.parser;

import org.eluder.coveralls.maven.plugin.ProcessingException;
import org.eluder.coveralls.maven.plugin.domain.Source;
import org.eluder.coveralls.maven.plugin.source.SourceCallback;
import org.eluder.coveralls.maven.plugin.source.SourceLoader;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.IOException;

public class CloverParser extends AbstractXmlEventParser {

    private Source source;
    private String packageName;

    public CloverParser(final File coverageFile, final SourceLoader sourceLoader) {
        super(coverageFile, sourceLoader);
    }

    @Override
    protected void onEvent(XMLStreamReader xml, SourceCallback callback) throws XMLStreamException, ProcessingException, IOException {
        if (isStartElement(xml, "package")) {
            this.packageName = xml.getAttributeValue(null, "name");
        } else if (isStartElement(xml, "file") && packageName != null) {
            String sourceFile = getSourceFile(xml.getAttributeValue(null, "name"));
            this.source = loadSource(sourceFile);
        } else if (isStartElement(xml, "line") && this.source != null) {
            // lines can be "method", "stmt", or "cond"
            String type = xml.getAttributeValue(null, "type");
            int coverage = 0;
            if ("method".equals(type) || "stmt".equals(type)) {
                coverage = (Integer.parseInt(xml.getAttributeValue(null, "count")) == 0) ? 0 : 1;
            } else if ("cond".equals(type)) {
                int falseCount = Integer.parseInt(xml.getAttributeValue(null, "falsecount"));
                int trueCount = Integer.parseInt(xml.getAttributeValue(null, "truecount"));
                coverage = (trueCount == 0 || falseCount == 0) ? 0 : 1;
            }
            int lineNumber = Integer.parseInt(xml.getAttributeValue(null, "num"));
            this.source.addCoverage(lineNumber, coverage);
        } else if (isEndElement(xml, "file") && this.source != null) {
            callback.onSource(this.source);
            this.source = null;
        } else if (isEndElement(xml, "package")) {
            this.packageName = null;
        }
    }

    private String getSourceFile(String fileName) {
        return this.packageName.replace('.', '/') + "/" + fileName;
    }
}
