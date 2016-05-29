package org.eluder.coveralls.maven.plugin.source;

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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DirectorySourceLoader extends AbstractSourceLoader {

    private final File sourceDirectory;

    public DirectorySourceLoader(final File base, final File sourceDirectory, final String sourceEncoding) {
        super(base.toURI(), sourceDirectory.toURI(), sourceEncoding);
        this.sourceDirectory = sourceDirectory;
    }

    @Override
    protected InputStream locate(final String sourceFile) throws IOException {
        File file = new File(sourceDirectory, sourceFile);
        if (file.exists()) {
            if (!file.isFile()) {
                throw new IllegalArgumentException(file.getAbsolutePath() + " is not file");
            }
            return new BufferedInputStream(new FileInputStream(file));
        }
        return null;
    }
}
