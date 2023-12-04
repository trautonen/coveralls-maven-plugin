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

import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.SelectorUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScanSourceLoader extends AbstractSourceLoader {
    
    private final Map<String, String[]> cache = new HashMap<>();

    private final File sourceDirectory;

    private final List<String> scanExclusions;

    public ScanSourceLoader(final File base, final File sourceDirectory, final String sourceEncoding, final List<String> scanExclusions) {
        super(base.toURI(), sourceDirectory.toURI(), sourceEncoding);
        this.sourceDirectory = sourceDirectory;
        this.scanExclusions = scanExclusions;
    }

    @Override
    protected InputStream locate(final String sourceFile) throws IOException {
        File file = new File(sourceDirectory, getFileName(sourceFile));

        if (file.exists()) {
            if (!file.isFile()) {
                throw new IllegalArgumentException(file.getAbsolutePath() + " is not file");
            }
            return new BufferedInputStream(new FileInputStream(file));
        }
        return null;
    }

    private String[] scanFor(final String extension) {
        if (!cache.containsKey(extension)) {
            DirectoryScanner scanner = new DirectoryScanner();
            scanner.setBasedir(sourceDirectory);
            if (scanExclusions != null) {
                scanner.setExcludes(scanExclusions.toArray(new String[0]));
            }
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
