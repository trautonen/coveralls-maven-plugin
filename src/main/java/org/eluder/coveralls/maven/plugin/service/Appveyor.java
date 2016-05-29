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

/**
 * Service implementation for Appveyor.
 * <p>
 * http://appveyor.com/
 */
public class Appveyor extends AbstractServiceSetup {

    public static final String APPVEYOR_NAME = "Appveyor";
    public static final String APPVEYOR = "APPVEYOR";
    public static final String APPVEYOR_BUILD_NUMBER = "APPVEYOR_BUILD_NUMBER";
    public static final String APPVEYOR_BUILD_ID = "APPVEYOR_BUILD_ID";
    public static final String APPVEYOR_BRANCH = "APPVEYOR_REPO_BRANCH";
    public static final String APPVEYOR_COMMIT = "APPVEYOR_REPO_COMMIT";
    public static final String APPVEYOR_PULL_REQUEST = "APPVEYOR_PULL_REQUEST_NUMBER";
    public static final String APPVEYOR_REPO_NAME = "APPVEYOR_REPO_NAME";

    public Appveyor(final Map<String, String> env) {
        super(env);
    }

    @Override
    public boolean isSelected() {
        return ("true".equalsIgnoreCase(getProperty(APPVEYOR)));
    }

    @Override
    public String getName() {
        return APPVEYOR_NAME;
    }

    @Override
    public String getBuildNumber() {
        return getProperty(APPVEYOR_BUILD_NUMBER);
    }

    @Override
    public String getBuildUrl() {
        return "https://ci.appveyor.com/project/" + getProperty(APPVEYOR_REPO_NAME) + "/build/" + getProperty(APPVEYOR_BUILD_NUMBER);
    }

    @Override
    public String getBranch() {
        return getProperty(APPVEYOR_BRANCH);
    }

    @Override
    public String getPullRequest() {
        return getProperty(APPVEYOR_PULL_REQUEST);
    }


    @Override
    public String getJobId() {
        return getProperty(APPVEYOR_BUILD_ID);
    }

}
