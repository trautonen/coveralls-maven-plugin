package org.eluder.coveralls.maven.plugin.service;

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

import java.util.Map;
import java.util.Properties;

/**
 * Service implementation for Jenkins.
 * <p>
 * http://jenkins-ci.org/
 */
public class Jenkins extends AbstractServiceSetup {

    public static final String JENKINS_NAME = "jenkins";
    public static final String JENKINS_URL = "JENKINS_URL";
    public static final String JENKINS_BUILD_NUMBER = "BUILD_NUMBER";
    public static final String JENKINS_BUILD_URL = "BUILD_URL";
    public static final String JENKINS_BRANCH = "GIT_BRANCH";
    public static final String JENKINS_COMMIT = "GIT_COMMIT";
    
    public Jenkins(final Map<String, String> env) {
        super(env);
    }

    @Override
    public boolean isSelected() {
        return (getProperty(JENKINS_URL) != null);
    }
    
    @Override
    public String getName() {
        return JENKINS_NAME;
    }
    
    @Override
    public String getBuildNumber() {
        return getProperty(JENKINS_BUILD_NUMBER);
    }
    
    @Override
    public String getBuildUrl() {
        return getProperty(JENKINS_BUILD_URL);
    }
    
    @Override
    public String getBranch() {
        return getProperty(JENKINS_BRANCH);
    }
    
    @Override
    public Properties getEnvironment() {
        Properties environment = new Properties();
        addProperty(environment, "jenkins_build_num", getProperty(JENKINS_BUILD_NUMBER));
        addProperty(environment, "jenkins_build_url", getProperty(JENKINS_BUILD_URL));
        addProperty(environment, "branch", getProperty(JENKINS_BRANCH));
        addProperty(environment, "commit_sha", getProperty(JENKINS_COMMIT));
        return environment;
    }
}
