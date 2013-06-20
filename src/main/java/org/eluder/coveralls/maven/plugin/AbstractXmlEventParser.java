package org.eluder.coveralls.maven.plugin;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.xml.XmlStreamReader;
import org.eluder.coveralls.maven.plugin.domain.Source;
import org.eluder.coveralls.maven.plugin.domain.SourceLoader;

public abstract class AbstractXmlEventParser implements CoverageParser {

    private final File coverageFile;
    private final SourceLoader sourceLoader;
    
    public AbstractXmlEventParser(final File coverageFile, final SourceLoader sourceLoader) {
        this.coverageFile = coverageFile;
        this.sourceLoader = sourceLoader;
    }

    @Override
    public final void parse(final SourceCallback callback) throws ProcessingException, IOException {
        XmlStreamReader reader = ReaderFactory.newXmlReader(coverageFile);
        XMLStreamReader xml = createEventReader(reader);
        try {
            while (xml.hasNext()) {
                xml.next();
                onEvent(xml, callback);
            }
        } catch (XMLStreamException ex) {
            throw new ProcessingException(ex);
        } finally {
            close(xml);
            IOUtil.close(reader);
        }
    }
    
    @Override
    public final File getCoverageFile() {
        return coverageFile;
    }
    
    protected XMLStreamReader createEventReader(final Reader reader) throws ProcessingException {
        try {
            return XMLInputFactory.newInstance().createXMLStreamReader(reader);
        } catch (FactoryConfigurationError ex) {
            throw new IllegalArgumentException(ex);
        } catch (XMLStreamException ex) {
            throw new ProcessingException(ex);
        }
    }
    
    private void close(final XMLStreamReader xml) throws ProcessingException {
        if (xml != null) {
            try {
                xml.close();
            } catch (XMLStreamException ex) {
                throw new ProcessingException(ex);
            }
        }
    }
    
    protected abstract void onEvent(final XMLStreamReader xml, SourceCallback callback) throws XMLStreamException, ProcessingException, IOException;
    
    protected final Source loadSource(final String sourceFile) throws IOException {
        return sourceLoader.load(sourceFile);
    }
    
    protected final boolean isStartElement(final XMLStreamReader xml, final String name) {
        return (XMLStreamConstants.START_ELEMENT == xml.getEventType() && xml.getLocalName().equals(name));
    }
    
    protected final boolean isEndElement(final XMLStreamReader xml, final String name) {
        return (XMLStreamConstants.END_ELEMENT == xml.getEventType() && xml.getLocalName().equals(name));
    }
}
