package org.eluder.coveralls.maven.plugin.jacoco;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.eluder.coveralls.maven.plugin.AbstractCoverallsMojo;
import org.eluder.coveralls.maven.plugin.CoverageParser;
import org.eluder.coveralls.maven.plugin.ProcessingException;
import org.eluder.coveralls.maven.plugin.SourceCallback;
import org.eluder.coveralls.maven.plugin.domain.SourceLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:ketoth.xupack@gmail.com">Ketoth Xupack</a>
 */
@Mojo(name = "jacoco-aggregate", threadSafe = false, aggregator = true)
public class JaCoCoAggregateMojo extends AbstractCoverallsMojo {

    /**
     * File path to JaCoCo coverage file.
     */
    @Parameter(property = "coverageFile", defaultValue = "jacoco/jacoco.xml")
    protected String coverageFile;

    /**
     * The projects in the reactor for aggregation report.
     */
    @Parameter(property = "reactorProjects", defaultValue = "${reactorProjects}", readonly = true)
    private List<MavenProject> reactorProjects;

    private final List<File> aggregatedSourceRoots = new ArrayList<File>();
    private final List<File> coverageFiles = new ArrayList<File>();

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (isLastReactorProject()) {
            for (final MavenProject mavenProject : reactorProjects) {
                if ("pom".equals(mavenProject.getPackaging())) {
                    continue;
                }

                final File reportFile = new File(
                        mavenProject.getModel().getReporting().getOutputDirectory(),
                        coverageFile);
                if (reportFile.exists()) {
                    coverageFiles.add(reportFile);
                } else {
                    getLog().warn("Skipping report file " + reportFile + " (does not exists)");
                }

                for (final String s : mavenProject.getCompileSourceRoots()) {
                    aggregatedSourceRoots.add(new File(s));
                }
            }
            super.execute();
        }
    }

    @Override
    protected CoverageParser createCoverageParser(final SourceLoader sourceLoader) {
        return new MultipleCoverageParser(sourceLoader);
    }

    @Override
    protected SourceLoader createSourceLoader() {
        return new SourceLoader(aggregatedSourceRoots, sourceEncoding);
    }

    private boolean isLastReactorProject() {
        return !reactorProjects.isEmpty() && reactorProjects.get(reactorProjects.size() - 1).equals(project);
    }

    private final class MultipleCoverageParser implements CoverageParser {
        private final List<JaCoCoParser> parsers = new ArrayList<JaCoCoParser>();

        private MultipleCoverageParser(final SourceLoader sourceLoader) {
            for (final File coverageFile : coverageFiles) {
                parsers.add(new JaCoCoParser(coverageFile, sourceLoader));
            }
        }

        @Override
        public void parse(final SourceCallback callback) throws ProcessingException, IOException {
            for (final JaCoCoParser parser : parsers) {
                parser.parse(callback);
            }
        }

        @Override
        public File getCoverageFile() {
            return project.getBasedir();
        }
    }
}
