package org.eluder.coveralls.maven.plugin.util;

import java.io.File;
import java.util.List;

import org.apache.maven.project.MavenProject;
import org.eluder.coveralls.maven.plugin.source.DirectorySourceLoader;
import org.eluder.coveralls.maven.plugin.source.MultiSourceLoader;
import org.eluder.coveralls.maven.plugin.source.SourceLoader;

public class SourceLoaderFactory {
    
    private final MavenProject project;
    private final String sourceEncoding;
    private List<File> sourceDirectories;

    public SourceLoaderFactory(final MavenProject project, final String sourceEncoding) {
        this.project = project;
        this.sourceEncoding = sourceEncoding;
    }

    public SourceLoaderFactory withSourceDirectories(final List<File> sourceDirectories) {
        this.sourceDirectories = sourceDirectories;
        return this;
    }
    
    public SourceLoader createSourceLoader() {
        MultiSourceLoader multiSourceLoader = new MultiSourceLoader();
        List<MavenProject> modules = new MavenProjectCollector(project).collect();
        for (MavenProject module : modules) {
            for (String sourceRoot : module.getCompileSourceRoots()) {
                File sourceDirectory = new File(sourceRoot);
                if (sourceDirectory.exists() && sourceDirectory.isDirectory()) {
                    DirectorySourceLoader moduleSourceLoader = new DirectorySourceLoader(project.getBasedir(), sourceDirectory, sourceEncoding);
                    multiSourceLoader.add(moduleSourceLoader);
                }
            }
        }
        return multiSourceLoader;
    }
}
