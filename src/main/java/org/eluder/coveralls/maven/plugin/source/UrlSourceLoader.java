package org.eluder.coveralls.maven.plugin.source;

import java.io.File;
import java.io.FileOutputStream;

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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.eluder.coveralls.maven.plugin.util.UrlUtils;

public class UrlSourceLoader extends AbstractSourceLoader {

    private final URL sourceUrl;
    private static final int BUFF_LEN = 4 * 1024;

    public UrlSourceLoader(final URL base, final URL sourceUrl, final String sourceEncoding) {
        super(UrlUtils.toUri(base), UrlUtils.toUri(sourceUrl), sourceEncoding);
        this.sourceUrl = sourceUrl;
    }
    
    @Override
    protected File locate(final String sourceFile) throws IOException {
        File file = File.createTempFile(sourceFile, "tmpDownload");
        file.deleteOnExit();
        URL url = new URL(sourceUrl, sourceFile);
        // Checkstyle OFF: EmptyBlock
        try (InputStream in = url.openStream();
                OutputStream out = new FileOutputStream(file)) {
            byte[] buffer = new byte[BUFF_LEN];
            int read = 0;
            while (-1 < (read = in.read(buffer))) {
                if (read > 0) {
                    out.write(buffer, 0, read);
                }
            }
            out.flush();
            return file;
        } catch (IOException ex) {
            // not found from url
        }
        // Checkstyle ON: EmptyBlock
        return null;
    }
}
