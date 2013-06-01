package org.eluder.coveralls.maven.plugin;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.eluder.coveralls.maven.plugin.domain.CoverallsResponse;
import org.eluder.coveralls.maven.plugin.domain.Git;
import org.eluder.coveralls.maven.plugin.domain.GitRepository;
import org.eluder.coveralls.maven.plugin.domain.Job;
import org.eluder.coveralls.maven.plugin.domain.SourceLoader;
import org.eluder.coveralls.maven.plugin.httpclient.CoverallsClient;
import org.eluder.coveralls.maven.plugin.json.JsonWriter;

public abstract class AbstractCoverallsMojo extends AbstractMojo {

    @Parameter(property = "coverallsFile", defaultValue = "${project.build.directory}/coveralls.json")
    private File coverallsFile;
    
    @Parameter(property = "coverallsUrl", defaultValue = "https://coveralls.io/api/v1/jobs")
    private String coverallsUrl;
    
    @Parameter(property = "sourceDirectory", defaultValue = "${project.build.sourceDirectory}")
    private File sourceDirectory;
    
    @Parameter(property = "sourceEncoding", defaultValue = "${project.build.sourceEncoding}")
    private String sourceEncoding;
    
    @Parameter(property = "serviceName")
    private String serviceName;
    
    @Parameter(property = "serviceJobId")
    private String serviceJobId;
    
    @Parameter(property = "repoToken")
    private String repoToken;
    
    @Override
    public final void execute() throws MojoExecutionException, MojoFailureException {
        try {
            CoverageParser parser = createCoverageParser(createSourceLoader());
            Job job = createJob();
            JsonWriter writer = createJsonWriter(job);
            CoverallsClient client = createCoverallsClient();
            writeCoveralls(writer, parser);
            CoverallsResponse response = client.submit(writer.getCoverallsFile());
            handleResponse(response);
        } catch (MojoExecutionException ex) {
            throw ex;
        } catch (MojoFailureException ex) {
            throw ex;
        } catch (ProcessingException ex) {
            throw new MojoExecutionException("Processing of input or output data failed", ex);
        } catch (IOException ex) {
            throw new MojoExecutionException("IO operation failed during build", ex);
        } catch (Exception ex) {
            throw new MojoExecutionException("Build error", ex);
        }
    }

    protected abstract CoverageParser createCoverageParser(SourceLoader sourceLoader);
    
    protected SourceLoader createSourceLoader() {
        return new SourceLoader(sourceDirectory, sourceEncoding);
    }
    
    protected Job createJob() throws IOException {
        Git git = new GitRepository(sourceDirectory).load();
        return new Job(repoToken, serviceName, serviceJobId, git);
    }
    
    protected JsonWriter createJsonWriter(final Job job) throws IOException {
        return new JsonWriter(job, coverallsFile);
    }
    
    protected CoverallsClient createCoverallsClient() {
        return new CoverallsClient(coverallsUrl);
    }
    
    protected void handleResponse(final CoverallsResponse response) throws MojoExecutionException, MojoFailureException {
        if (response.isError()) {
            throw new MojoFailureException("Failed to submit coveralls report: " + response.getMessage());
        }
    }
    
    private void writeCoveralls(final JsonWriter writer, final CoverageParser parser) throws ProcessingException, IOException {
        try {
            writer.writeStart();
            parser.parse(writer);
            writer.writeEnd();
        } finally {
            writer.close();
        }
    }
}
