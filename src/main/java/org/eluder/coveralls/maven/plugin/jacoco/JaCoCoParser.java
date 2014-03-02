package org.eluder.coveralls.maven.plugin.jacoco;

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
            int ci = Integer.parseInt(xml.getAttributeValue(null, "ci"));
            this.source.addCoverage(
                    Integer.parseInt(xml.getAttributeValue(null, "nr")),
                    (ci == 0 ? 0 : 1) // jacoco does not count hits
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
