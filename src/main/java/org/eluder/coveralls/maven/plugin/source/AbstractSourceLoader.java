package org.eluder.coveralls.maven.plugin.source;

/*
 * #[license]
 * coveralls-maven-plugin
 * %%
 * Copyright (C) 2013 - 2015 Tapio Rautonen
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
import java.net.URI;
import java.nio.charset.Charset;

import org.eluder.coveralls.maven.plugin.domain.Source;

public abstract class AbstractSourceLoader implements SourceLoader {

    private final Charset sourceEncoding;
    private final String directoryPrefix;
    
    public AbstractSourceLoader(final URI base, final URI sourceBase, final String sourceEncoding) {
        this.sourceEncoding = Charset.forName(sourceEncoding);
        this.directoryPrefix = base.relativize(sourceBase).toString();
    }
    
    @Override
    public Source load(final String sourceFile) throws IOException {
        File source = locate(sourceFile);
        if (source != null) {
            return new Source(getFileName(sourceFile), source, getSourceEncoding());
        } else {
            return null;
        }
    }
    
    protected Charset getSourceEncoding() {
        return sourceEncoding;
    }
    
    protected String getFileName(final String sourceFile) {
        return directoryPrefix + sourceFile;
    }
    
    protected abstract File locate(String sourceFile) throws IOException;
}
