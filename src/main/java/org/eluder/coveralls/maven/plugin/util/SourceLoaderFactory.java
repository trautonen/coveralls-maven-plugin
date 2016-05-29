package org.eluder.coveralls.maven.plugin.util;

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

import org.apache.maven.project.MavenProject;
import org.eluder.coveralls.maven.plugin.source.DirectorySourceLoader;
import org.eluder.coveralls.maven.plugin.source.MultiSourceLoader;
import org.eluder.coveralls.maven.plugin.source.ScanSourceLoader;
import org.eluder.coveralls.maven.plugin.source.SourceLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SourceLoaderFactory {

    private final File baseDir;
    private final MavenProject project;
    private final String sourceEncoding;
    private List<File> sourceDirectories;
    private boolean scanForSources;

    public SourceLoaderFactory(final File baseDir, final MavenProject project, final String sourceEncoding) {
        this.baseDir = baseDir;
        this.project = project;
        this.sourceEncoding = sourceEncoding;
    }

    public SourceLoaderFactory withSourceDirectories(final List<File> sourceDirectories) {
        this.sourceDirectories = sourceDirectories;
        return this;
    }

    public SourceLoaderFactory withScanForSources(final boolean scanForSources) {
        this.scanForSources = scanForSources;
        return this;
    }

    public SourceLoader createSourceLoader() {
        MultiSourceLoader multiSourceLoader = new MultiSourceLoader();
        List<File> directories = new ArrayList<>();
        List<MavenProject> modules = new MavenProjectCollector(project).collect();
        for (MavenProject module : modules) {
            for (String sourceRoot : module.getCompileSourceRoots()) {
                File sourceDirectory = new File(sourceRoot);
                directories.add(sourceDirectory);
            }
        }
        if (sourceDirectories != null) {
            directories.addAll(sourceDirectories);
        }
        for (File directory: directories) {
            if (directory.exists() && directory.isDirectory()) {
                DirectorySourceLoader moduleSourceLoader = new DirectorySourceLoader(baseDir, directory, sourceEncoding);
                multiSourceLoader.add(moduleSourceLoader);
            }
        }

        if (scanForSources) {
            for (File directory: directories) {
                if (directory.exists() && directory.isDirectory()) {
                    ScanSourceLoader scanSourceLoader = new ScanSourceLoader(baseDir, directory, sourceEncoding);
                    multiSourceLoader.add(scanSourceLoader);
                }
            }
        }
        return multiSourceLoader;
    }
}
