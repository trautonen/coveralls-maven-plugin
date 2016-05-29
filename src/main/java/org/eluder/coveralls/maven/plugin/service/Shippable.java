package org.eluder.coveralls.maven.plugin.service;

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

import java.util.Map;
import java.util.Properties;

/**
 * Service implementation for Shippable.
 * <p>
 * http://shippable.com/
 */
public class Shippable extends AbstractServiceSetup {

    public static final String SHIPPABLE_NAME = "shippable";
    public static final String SHIPPABLE = "SHIPPABLE";
    public static final String SHIPPABLE_BUILD_NUMBER = "SHIPPABLE_BUILD_NUMBER";
    public static final String SHIPPABLE_BUILD_ID = "SHIPPABLE_BUILD_ID";
    public static final String SHIPPABLE_BRANCH = "BRANCH";
    public static final String SHIPPABLE_COMMIT = "COMMIT";
    public static final String SHIPPABLE_PULL_REQUEST = "PULL_REQUEST";

    public Shippable(final Map<String, String> env) {
        super(env);
    }

    @Override
    public boolean isSelected() {
        return ("true".equalsIgnoreCase(getProperty(SHIPPABLE)));
    }

    @Override
    public String getName() {
        return SHIPPABLE_NAME;
    }

    @Override
    public String getBuildNumber() {
        return getProperty(SHIPPABLE_BUILD_NUMBER);
    }

    @Override
    public String getBuildUrl() {
        return "https://app.shippable.com/builds/" + getProperty(SHIPPABLE_BUILD_ID);
    }

    @Override
    public String getBranch() {
        return getProperty(SHIPPABLE_BRANCH);
    }

    @Override
    public String getPullRequest() {
        String pullRequest = getProperty(SHIPPABLE_PULL_REQUEST);
        if ("false".equals(pullRequest)) {
            return null;
        }
        return pullRequest;
    }

    @Override
    public Properties getEnvironment() {
        Properties environment = new Properties();
        addProperty(environment, "shippable_build_number", getProperty(SHIPPABLE_BUILD_NUMBER));
        addProperty(environment, "shippable_build_id", getProperty(SHIPPABLE_BUILD_ID));
        addProperty(environment, "shippable_build_url", getBuildUrl());
        addProperty(environment, "branch", getProperty(SHIPPABLE_BRANCH));
        addProperty(environment, "commit_sha", getProperty(SHIPPABLE_COMMIT));
        return environment;
    }

}
