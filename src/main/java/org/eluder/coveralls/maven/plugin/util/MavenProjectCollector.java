package org.eluder.coveralls.maven.plugin.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.maven.project.MavenProject;

public class MavenProjectCollector {
    
    private final MavenProject root;

    public MavenProjectCollector(final MavenProject root) {
        this.root = root;
    }
    
    public List<MavenProject> collect() {
        List<MavenProject> projects = new ArrayList<MavenProject>();
        collect(root, projects);
        return Collections.unmodifiableList(projects);
    }
    
    private void collect(final MavenProject project, final List<MavenProject> projects) {
        projects.add(project);
        for (MavenProject child : project.getCollectedProjects()) {
            collect(child, projects);
        }
    }
}
