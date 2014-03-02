package org.eluder.coveralls.maven.plugin;

/*
 * #[license]
 * coveralls-maven-plugin
 * %%
 * Copyright (C) 2013 - 2014 Tapio Rautonen
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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.StringUtils;
import org.eluder.coveralls.maven.plugin.service.ServiceSetup;

/**
 * Constructs and setups the project environment and continuous integration service.
 */
public final class Environment {
    
    private final AbstractCoverallsMojo mojo;
    private final Iterable<ServiceSetup> services;

    public Environment(final AbstractCoverallsMojo mojo, final Iterable<ServiceSetup> services) {
        if (mojo == null) {
            throw new IllegalArgumentException("mojo must be defined");
        }
        if (services == null) {
            throw new IllegalArgumentException("services must be defined");
        }
        this.mojo = mojo;
        this.services = services;
    }
    
    public void setup() {
        setupSourceDirectories();
        setupService();
    }
    
    private void setupSourceDirectories() {
        if (mojo.sourceDirectories == null || mojo.sourceDirectories.isEmpty()) {
            List<File> directories = new ArrayList<File>();
            collectSourceDirectories(mojo.project, directories);
            mojo.sourceDirectories = directories;
        }
        if (mojo.sourceDirectories == null || mojo.sourceDirectories.isEmpty()) {
            throw new IllegalArgumentException("No source directories set up");
        }
        logSourceDirectories();
    }
    
    private void collectSourceDirectories(final MavenProject project, final List<File> directories) {
        for (String sourceRoot : project.getCompileSourceRoots()) {
            File directory = new File(sourceRoot);
            if (directory.exists() && directory.isDirectory()) {
                directories.add(directory);
            }
        }
        for (MavenProject collectedProject : project.getCollectedProjects()) {
            collectSourceDirectories(collectedProject, directories);
        }
    }
    
    private void setupService() {
        for (ServiceSetup service : services) {
            if (service.isSelected()) {
                setupEnvironment(service);
                break;
            }
        }
    }
    
    private void setupEnvironment(final ServiceSetup service) {
        String name = service.getName();
        if (StringUtils.isBlank(mojo.serviceName) && StringUtils.isNotBlank(name)) {
            mojo.serviceName = name;
        }
        
        String jobId = service.getJobId();
        if (StringUtils.isBlank(mojo.serviceJobId) && StringUtils.isNotBlank(jobId)) {
            mojo.serviceJobId = jobId;
        }
        
        String buildNumber = service.getBuildNumber();
        if (StringUtils.isBlank(mojo.serviceBuildNumber) && StringUtils.isNotBlank(buildNumber)) {
            mojo.serviceBuildNumber = buildNumber;
        }
        
        String buildUrl = service.getBuildUrl();
        if (StringUtils.isBlank(mojo.serviceBuildUrl) && StringUtils.isNotBlank(buildUrl)) {
            mojo.serviceBuildUrl = buildUrl;
        }
        
        String branch = service.getBranch();
        if (StringUtils.isBlank(mojo.branch) && StringUtils.isNotBlank(branch)) {
            mojo.branch = branch;
        }
        
        String pullRequest = service.getPullRequest();
        if (StringUtils.isBlank(mojo.pullRequest) && StringUtils.isNotBlank(pullRequest)) {
            mojo.pullRequest = pullRequest;
        }
        
        Properties environment = service.getEnvironment();
        if ((mojo.serviceEnvironment == null || mojo.serviceEnvironment.isEmpty()) &&
                (environment != null && !environment.isEmpty())) {
            mojo.serviceEnvironment = environment;
        }
    }
    
    private void logSourceDirectories() {
        if (mojo.getLog().isDebugEnabled()) {
            mojo.getLog().debug("Using " + mojo.sourceDirectories.size() + " source directories to scan source files:");
            for (File sourceDirectory : mojo.sourceDirectories) {
                mojo.getLog().debug("- " + sourceDirectory.getAbsolutePath());
            }
        }
    }
}
