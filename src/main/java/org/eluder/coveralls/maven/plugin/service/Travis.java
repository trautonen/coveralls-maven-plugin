package org.eluder.coveralls.maven.plugin.service;

import java.util.Map;
import java.util.Properties;

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

/**
 * Service implementation for Travis CI.
 * <p>
 * https://travis-ci.org/
 */
public class Travis extends AbstractServiceSetup {

    public static final String TRAVIS_NAME = "travis-ci";
    public static final String TRAVIS = "TRAVIS";
    public static final String TRAVIS_JOB_ID = "TRAVIS_JOB_ID";
    public static final String TRAVIS_BRANCH = "TRAVIS_BRANCH";
    public static final String TRAVIS_PULL_REQUEST = "TRAVIS_PULL_REQUEST";
    
    public Travis(final Map<String, String> env) {
        super(env);
    }

    @Override
    public boolean isSelected() {
        return ("true".equalsIgnoreCase(getProperty(TRAVIS)));
    }

    @Override
    public String getName() {
        return TRAVIS_NAME;
    }
    
    @Override
    public String getJobId() {
        return getProperty(TRAVIS_JOB_ID);
    }

    @Override
    public String getBranch() {
        return getProperty(TRAVIS_BRANCH);
    }
    
    @Override
    public String getPullRequest() {
        return getProperty(TRAVIS_PULL_REQUEST);
    }
    
    @Override
    public Properties getEnvironment() {
        Properties environment = new Properties();
        addProperty(environment, "travis_job_id", getProperty(TRAVIS_JOB_ID));
        addProperty(environment, "travis_pull_request", getProperty(TRAVIS_PULL_REQUEST));
        return environment;
    }
}
