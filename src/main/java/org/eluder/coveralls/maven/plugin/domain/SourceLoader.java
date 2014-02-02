package org.eluder.coveralls.maven.plugin.domain;

/*
 * #[license]
 * coveralls-maven-plugin
 * %%
 * Copyright (C) 2013 Tapio Rautonen
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
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import org.codehaus.plexus.util.IOUtil;

public class SourceLoader {

    private final List<File> sourceDirectories;
    private final Charset sourceEncoding;

    private final String baseURL;

    public SourceLoader(final List<File> sourceDirectories, final String sourceEncoding) {
        this(sourceDirectories, sourceEncoding, null);
    }

    public SourceLoader(final List<File> sourceDirectories, final String sourceEncoding, final String baseURL) {
        if (sourceDirectories == null || sourceDirectories.isEmpty()) {
            throw new IllegalArgumentException("At least one source directory must be defined");
        }
        for (File sourceDirectory : sourceDirectories) {
            if (!sourceDirectory.exists()) {
                throw new IllegalArgumentException("Source directory " + sourceDirectory.getAbsolutePath() + " does not exist");
            }
            if (!sourceDirectory.isDirectory()) {
                throw new IllegalArgumentException(sourceDirectory.getAbsolutePath() + " is not directory");
            }
        }
        this.sourceDirectories = sourceDirectories;
        this.sourceEncoding = Charset.forName(sourceEncoding);
        this.baseURL = baseURL;
    }
    
    public Source load(final String sourceFile) throws IOException {
        Reader reader = locate(sourceFile);
        try {
            String source = IOUtil.toString(reader);
            return new Source(sourceFile, source);
        } finally {
            IOUtil.close(reader);
        }
    }
    
    private InputStreamReader locate(final String sourceFile) throws IOException {
        for (File sourceDirectory : sourceDirectories) {
            File file = new File(sourceDirectory, sourceFile);
            if (file.exists()) {
                if (!file.isFile()) {
                    throw new IllegalArgumentException(file.getAbsolutePath() + " is not file");
                }
                return new InputStreamReader(new BufferedInputStream(new FileInputStream(file)), sourceEncoding);
            }
        }

        if (baseURL != null && !baseURL.isEmpty()) {
            URL pathToFile = new URL(new URL(baseURL), sourceFile);

            return new InputStreamReader(pathToFile.openStream(), sourceEncoding);
        }

        throw new IllegalArgumentException("Could not find source file " + sourceFile + " from any source directory or base URL.");
    }
}
