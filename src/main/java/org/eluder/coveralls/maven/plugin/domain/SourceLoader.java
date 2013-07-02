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

import org.codehaus.plexus.util.IOUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;

public class SourceLoader {

    private List<File> sourceDirectories;
    private final Charset sourceEncoding;
    
    public SourceLoader(final List<File> sourceDirectories, final String sourceEncoding) {
        this.sourceDirectories = sourceDirectories;
        for(File sourceDirectory : sourceDirectories) {
          if(!sourceDirectory.exists()) {
            throw new IllegalArgumentException("Source directory " + sourceDirectory.getAbsolutePath() + " does not exist");
          }
          if(!sourceDirectory.isDirectory()) {
            throw new IllegalArgumentException(sourceDirectory.getAbsolutePath() + " is not directory");
          }
        }
        this.sourceEncoding = Charset.forName(sourceEncoding);
    }
    
    public Source load(final String sourceFile) throws IOException {
        File file = null;
        for(File sourceDirectory : sourceDirectories) {
          file = new File(sourceDirectory, sourceFile);
          if(!file.exists()) {
            file = null;
          } else {
            break;
          }
        }
        if(file != null) {
            if (!file.isFile()) {
                throw new IllegalArgumentException(file.getAbsolutePath() + " is not file");
            }
            Reader reader = new InputStreamReader(new BufferedInputStream(new FileInputStream(file)), sourceEncoding);
            try {
                String source = IOUtil.toString(reader);
                return new Source(sourceFile, source);
            } finally {
                IOUtil.close(reader);
            }
        } else {
            return null;
        }
    }
    
}
