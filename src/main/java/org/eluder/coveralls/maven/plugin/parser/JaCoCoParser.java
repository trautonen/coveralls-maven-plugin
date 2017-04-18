package org.eluder.coveralls.maven.plugin.parser;

/*
 * #[license]
 * coveralls-maven-plugin
 * %%
 * Copyright (C) 2013 - 2016 Tapio Rautonen
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

import org.eluder.coveralls.maven.plugin.ProcessingException;
import org.eluder.coveralls.maven.plugin.domain.Source;
import org.eluder.coveralls.maven.plugin.source.SourceCallback;
import org.eluder.coveralls.maven.plugin.source.SourceLoader;

public class JaCoCoParser extends AbstractXmlEventParser {

    private String packageName;
    private Source source;
    private int branchId;

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
            this.branchId = 0;
        } else
        
        if (isStartElement(xml, "line") && this.source != null) {
            int ci = Integer.parseInt(xml.getAttributeValue(null, "ci"));
            int cb = Integer.parseInt(xml.getAttributeValue(null, "cb"));
            int mb = Integer.parseInt(xml.getAttributeValue(null, "mb"));
            int nr = Integer.parseInt(xml.getAttributeValue(null, "nr"));

            // jacoco does not count hits. this is why hits is always 0 or 1
            this.source.addCoverage(nr, (ci == 0 ? 0 : 1));

            // add branches. unfortunately, there is NO block number and
            // branch number will NOT be unique between coverage changes.
            for (int b = 0; b < cb; b++) {
              this.source.addBranchCoverage(nr, 0, this.branchId++, 1);
            }
            for (int b = 0; b < mb; b++) {
              this.source.addBranchCoverage(nr, 0, this.branchId++, 0);
            }
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
