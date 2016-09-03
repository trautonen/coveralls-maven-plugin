package org.eluder.coveralls.maven.plugin;

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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Settings;
import org.eluder.coveralls.maven.plugin.domain.CoverallsResponse;
import org.eluder.coveralls.maven.plugin.domain.Git;
import org.eluder.coveralls.maven.plugin.domain.GitRepository;
import org.eluder.coveralls.maven.plugin.domain.Job;
import org.eluder.coveralls.maven.plugin.httpclient.CoverallsClient;
import org.eluder.coveralls.maven.plugin.httpclient.CoverallsProxyClient;
import org.eluder.coveralls.maven.plugin.json.JsonWriter;
import org.eluder.coveralls.maven.plugin.logging.CoverageTracingLogger;
import org.eluder.coveralls.maven.plugin.logging.DryRunLogger;
import org.eluder.coveralls.maven.plugin.logging.JobLogger;
import org.eluder.coveralls.maven.plugin.logging.Logger;
import org.eluder.coveralls.maven.plugin.logging.Logger.Position;
import org.eluder.coveralls.maven.plugin.service.Appveyor;
import org.eluder.coveralls.maven.plugin.service.Bamboo;
import org.eluder.coveralls.maven.plugin.service.Circle;
import org.eluder.coveralls.maven.plugin.service.General;
import org.eluder.coveralls.maven.plugin.service.Jenkins;
import org.eluder.coveralls.maven.plugin.service.ServiceSetup;
import org.eluder.coveralls.maven.plugin.service.Shippable;
import org.eluder.coveralls.maven.plugin.service.Travis;
import org.eluder.coveralls.maven.plugin.service.Wercker;
import org.eluder.coveralls.maven.plugin.source.SourceCallback;
import org.eluder.coveralls.maven.plugin.source.SourceLoader;
import org.eluder.coveralls.maven.plugin.source.UniqueSourceCallback;
import org.eluder.coveralls.maven.plugin.util.CoverageParsersFactory;
import org.eluder.coveralls.maven.plugin.util.SourceLoaderFactory;
import org.eluder.coveralls.maven.plugin.util.TimestampParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Mojo(name = "report", threadSafe = false, aggregator = true)
public class CoverallsReportMojo extends AbstractMojo {

    /**
     * File paths to additional JaCoCo coverage report files.
     */
    @Parameter(property = "jacocoReports")
    protected List<File> jacocoReports;

    /**
     * File paths to additional Cobertura coverage report files.
     */
    @Parameter(property = "coberturaReports")
    protected List<File> coberturaReports;

    /**
     * File paths to additional Saga coverage report files.
     */
    @Parameter(property = "sagaReports")
    protected List<File> sagaReports;

    /**
     * Directories for relative per module specific report files.
     */
    @Parameter(property = "relativeReportDirs")
    protected List<String> relativeReportDirs;

    /**
     * File path to write and submit Coveralls data.
     */
    @Parameter(property = "coverallsFile", defaultValue = "${project.build.directory}/coveralls.json")
    protected File coverallsFile;
    
    /**
     * Url for the Coveralls API.
     */
    @Parameter(property = "coverallsUrl", defaultValue = "https://coveralls.io/api/v1/jobs")
    protected String coverallsUrl;

    /**
     * Source directories.
     */
    @Parameter(property = "sourceDirectories")
    protected List<File> sourceDirectories;
    
    /**
     * Source file encoding.
     */
    @Parameter(property = "sourceEncoding", defaultValue = "${project.build.sourceEncoding}")
    protected String sourceEncoding;
    
    /**
     * CI service name.
     */
    @Parameter(property = "serviceName")
    protected String serviceName;
    
    /**
     * CI service job id.
     */
    @Parameter(property = "serviceJobId")
    protected String serviceJobId;
    
    /**
     * CI service build number.
     */
    @Parameter(property = "serviceBuildNumber")
    protected String serviceBuildNumber;
    
    /**
     * CI service build url.
     */
    @Parameter(property = "serviceBuildUrl")
    protected String serviceBuildUrl;
    
    /**
     * CI service specific environment properties.
     */
    @Parameter(property = "serviceEnvironment")
    protected Properties serviceEnvironment;
    
    /**
     * Coveralls repository token.
     */
    @Parameter(property = "repoToken")
    protected String repoToken;
    
    /**
     * Git branch name.
     */
    @Parameter(property = "branch")
    protected String branch;

    /**
     * GitHub pull request identifier.
     */
    @Parameter(property = "pullRequest")
    protected String pullRequest;

    /**
     * Build timestamp format. Must be in format supported by SimpleDateFormat.
     */
    @Parameter(property = "timestampFormat", defaultValue = "${maven.build.timestamp.format}")
    protected String timestampFormat;

    /**
     * Build timestamp. Must be in format defined by 'timestampFormat' if it's available or in
     * default timestamp format yyyy-MM-dd'T'HH:mm:ss'Z'.
     */
    @Parameter(property = "timestamp", defaultValue = "${maven.build.timestamp}")
    protected String timestamp;
    
    /**
     * Dry run Coveralls report without actually sending it.
     */
    @Parameter(property = "dryRun", defaultValue = "false")
    protected boolean dryRun;

    /**
     * Fail build if Coveralls service is not available or submission fails for internal errors.
     */
    @Parameter(property = "failOnServiceError", defaultValue = "true")
    protected boolean failOnServiceError;

    /**
     * Scan subdirectories for source files.
     */
    @Parameter(property = "scanForSources", defaultValue = "false")
    protected boolean scanForSources;

    /**
     * Base directory of the project.
     */
    @Parameter(property = "coveralls.basedir", defaultValue = "${project.basedir}")
    protected File basedir;

    /**
     * Skip the plugin execution.
     */
    @Parameter(property = "coveralls.skip", defaultValue = "false")
    protected boolean skip;


    /**
     * Maven settings.
     */
    @Parameter(defaultValue = "${settings}", readonly = true, required = true)
    protected Settings settings;

    /**
     * Maven project for runtime value resolution.
     */
    @Component
    protected MavenProject project;


    @Override
    public final void execute() throws MojoExecutionException, MojoFailureException {
        if (skip) {
            getLog().info("Skip property set, skipping plugin execution");
            return;
        }
        
        try {
            createEnvironment().setup();
            Job job = createJob();
            job.validate().throwOrInform(getLog());
            SourceLoader sourceLoader = createSourceLoader(job);
            List<CoverageParser> parsers = createCoverageParsers(sourceLoader);
            JsonWriter writer = createJsonWriter(job);
            CoverallsClient client = createCoverallsClient();
            List<Logger> reporters = new ArrayList<>();
            reporters.add(new JobLogger(job));
            SourceCallback sourceCallback = createSourceCallbackChain(writer, reporters);
            reporters.add(new DryRunLogger(job.isDryRun(), writer.getCoverallsFile()));
            
            report(reporters, Position.BEFORE);
            writeCoveralls(writer, sourceCallback, parsers);
            report(reporters, Position.AFTER);
            
            if (!job.isDryRun()) {
                submitData(client, writer.getCoverallsFile());
            }
        } catch (ProcessingException ex) {
            throw new MojoFailureException("Processing of input or output data failed", ex);
        } catch (IOException ex) {
            throw new MojoFailureException("I/O operation failed", ex);
        } catch (Exception ex) {
            throw new MojoExecutionException("Build error", ex);
        }
    }

    /**
     * 
     * @param sourceLoader source loader that extracts source files
     * @return coverage parsers for all maven modules and additional reports
     * @throws IOException if parsers cannot be created
     */
    protected List<CoverageParser> createCoverageParsers(final SourceLoader sourceLoader) throws IOException {
        return new CoverageParsersFactory(project, sourceLoader)
                .withJaCoCoReports(jacocoReports)
                .withCoberturaReports(coberturaReports)
                .withSagaReports(sagaReports)
                .withRelativeReportDirs(relativeReportDirs)
                .createParsers();
    }
    
    /**
     * @return source loader that extracts source files
     * 
     * @param job the job describing the coveralls report
     */
    protected SourceLoader createSourceLoader(final Job job) {
        return new SourceLoaderFactory(job.getGit().getBaseDir(), project, sourceEncoding)
                .withSourceDirectories(sourceDirectories)
                .withScanForSources(scanForSources)
                .createSourceLoader();
    }

    /**
     * @return environment to setup mojo and service specific properties
     */
    protected Environment createEnvironment() {
        return new Environment(this, getServices());
    }
    
    /**
     * @return list of available continuous integration services
     */
    protected List<ServiceSetup> getServices() {
        Map<String, String> env = System.getenv();
        List<ServiceSetup> services = new ArrayList<>();
        services.add(new Shippable(env));
        services.add(new Travis(env));
        services.add(new Circle(env));
        services.add(new Jenkins(env));
        services.add(new Bamboo(env));
        services.add(new Appveyor(env));
        services.add(new Wercker(env));
        services.add(new General(env));
        return services;
    }
    
    /**
     * @return job that describes the coveralls report
     * @throws ProcessingException if processing of timestamp fails
     * @throws IOException if an I/O error occurs
     */
    protected Job createJob() throws ProcessingException, IOException {
        Git git = new GitRepository(basedir).load();
        Date time = new TimestampParser(timestampFormat).parse(timestamp);
        return new Job()
            .withRepoToken(repoToken)
            .withServiceName(serviceName)
            .withServiceJobId(serviceJobId)
            .withServiceBuildNumber(serviceBuildNumber)
            .withServiceBuildUrl(serviceBuildUrl)
            .withServiceEnvironment(serviceEnvironment)
            .withDryRun(dryRun)
            .withBranch(branch)
            .withPullRequest(pullRequest)
            .withTimestamp(time)
            .withGit(git);
    }
    
    /**
     * @param job the job describing the coveralls report
     * @return JSON writer that writes the coveralls data
     * @throws IOException if an I/O error occurs
     */
    protected JsonWriter createJsonWriter(final Job job) throws IOException {
        return new JsonWriter(job, coverallsFile);
    }
    
    /**
     * @return http client that submits the coveralls data
     */
    protected CoverallsClient createCoverallsClient() {
        return new CoverallsProxyClient(coverallsUrl, settings.getActiveProxy());
    }
    
    /**
     * @param writer the JSON writer
     * @param reporters the logging reporters
     * @return source callback chain for different source handlers
     */
    protected SourceCallback createSourceCallbackChain(final JsonWriter writer, final List<Logger> reporters) {
        SourceCallback chain = writer;
        if (getLog().isInfoEnabled()) {
            CoverageTracingLogger coverageTracingReporter = new CoverageTracingLogger(chain);
            chain = coverageTracingReporter;
            reporters.add(coverageTracingReporter);
        }
        chain = new UniqueSourceCallback(chain);
        return chain;
    }

    /**
     * Writes coverage data to JSON file.
     *
     * @param writer JSON writer that writes the coveralls data
     * @param sourceCallback the source callback handler
     * @param parsers list of coverage parsers
     * @throws ProcessingException if process to to create JSON file fails
     * @throws IOException if an I/O error occurs
     */
    protected void writeCoveralls(final JsonWriter writer, final SourceCallback sourceCallback, final List<CoverageParser> parsers) throws ProcessingException, IOException {
        try {
            getLog().info("Writing Coveralls data to " + writer.getCoverallsFile().getAbsolutePath() + "...");
            long now = System.currentTimeMillis();
            sourceCallback.onBegin();
            for (CoverageParser parser : parsers) {
                getLog().info("Processing coverage report from " + parser.getCoverageFile().getAbsolutePath());
                parser.parse(sourceCallback);
            }
            sourceCallback.onComplete();
            long duration = System.currentTimeMillis() - now;
            getLog().info("Successfully wrote Coveralls data in " + duration + "ms");
        } finally {
            writer.close();
        }
    }
    
    private void submitData(final CoverallsClient client, final File coverallsFile) throws ProcessingException, IOException {
        getLog().info("Submitting Coveralls data to API");
        long now = System.currentTimeMillis();
        try {
            CoverallsResponse response = client.submit(coverallsFile);
            long duration = System.currentTimeMillis() - now;
            getLog().info("Successfully submitted Coveralls data in " + duration + "ms for " + response.getMessage());
            getLog().info(response.getUrl());
            getLog().info("*** It might take hours for Coveralls to update the actual coverage numbers for a job");
            getLog().info("    If you see question marks in the report, please be patient");
        } catch (ProcessingException ex) {
            long duration = System.currentTimeMillis() - now;
            String message = "Submission failed in " + duration + "ms while processing data";
            handleSubmissionError(ex, message, true);
        } catch (IOException ex) {
            long duration = System.currentTimeMillis() - now;
            String message = "Submission failed in " + duration + "ms while handling I/O operations";
            handleSubmissionError(ex, message, failOnServiceError);
        }
    }

    private <T extends Exception> void handleSubmissionError(final T ex, final String message, final boolean failOnException) throws T {
        if (failOnException) {
            getLog().error(message);
            throw ex;
        } else {
            getLog().warn(message);
        }
    }
    
    private void report(final List<Logger> reporters, final Position position) {
        for (Logger reporter : reporters) {
            if (position.equals(reporter.getPosition())) {
                reporter.log(getLog());
            }
        }
    }
}
