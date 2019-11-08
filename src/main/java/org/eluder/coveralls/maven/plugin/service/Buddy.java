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
 * Service implementation for Buddy.
 * <p>
 * https://buddy.works
 */
public class Buddy extends AbstractServiceSetup {

    public static final String BUDDY_NAME = "buddy";
    public static final String BUDDY_URL = "BUDDY_WORKSPACE_URL";
    public static final String BUDDY_BUILD_NUMBER = "BUDDY_EXECUTION_ID";
    public static final String BUDDY_BUILD_URL = "BUDDY_EXECUTION_URL";
    public static final String BUDDY_BRANCH = "BUDDY_EXECUTION_BRANCH";
    public static final String BUDDY_COMMIT = "BUDDY_EXECUTION_REVISION";

    public Buddy(final Map<String, String> env) {
        super(env);
    }

    @Override
    public boolean isSelected() {
        return (getProperty(BUDDY_URL) != null);
    }
    
    @Override
    public String getName() {
        return BUDDY_NAME;
    }
    
    @Override
    public String getBuildNumber() {
        return getProperty(BUDDY_BUILD_NUMBER);
    }
    
    @Override
    public String getBuildUrl() {
        return getProperty(BUDDY_BUILD_URL);
    }
    
    @Override
    public String getBranch() {
        return getProperty(BUDDY_BRANCH);
    }
    
    @Override
    public Properties getEnvironment() {
        Properties environment = new Properties();
        addProperty(environment, "branch", getProperty(BUDDY_BRANCH));
        addProperty(environment, "commit_sha", getProperty(BUDDY_COMMIT));
        return environment;
    }
}
