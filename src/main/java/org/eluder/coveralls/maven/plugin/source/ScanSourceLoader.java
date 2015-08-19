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
import java.util.HashMap;
import java.util.Map;

import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.SelectorUtils;

public class ScanSourceLoader extends AbstractSourceLoader {
    
    private final Map<String, String[]> cache = new HashMap<String, String[]>();

    private final File sourceDirectory;

    public ScanSourceLoader(final File base, final File sourceDirectory, final String sourceEncoding) {
        super(base.toURI(), sourceDirectory.toURI(), sourceEncoding);
        this.sourceDirectory = sourceDirectory;
    }

    @Override
    protected File locate(final String sourceFile) throws IOException {
        File file = new File(sourceDirectory, getFileName(sourceFile));

        if (file.exists()) {
            if (!file.isFile()) {
                throw new IllegalArgumentException(file.getAbsolutePath() + " is not file");
            }
            return file.getAbsoluteFile();
        }
        return null;
    }

    private String[] scanFor(final String extension) {
        if (!cache.containsKey(extension)) {
            DirectoryScanner scanner = new DirectoryScanner();
            scanner.setBasedir(sourceDirectory);
            scanner.addDefaultExcludes();
            scanner.setIncludes(new String[] {"**/*." + extension});
            scanner.scan();

            cache.put(extension, scanner.getIncludedFiles());
        }
        return cache.get(extension);
    }

    protected String getFileName(final String sourceFile) {
        String extension = FileUtils.extension(sourceFile);
        String[] matchingExtensionFiles = scanFor(extension);

        for (String matchingExtensionFile : matchingExtensionFiles) {
            if (SelectorUtils.matchPath("**/" + sourceFile, matchingExtensionFile, true)) {
                return matchingExtensionFile;
            }
        }

        return sourceFile;
    }

}
