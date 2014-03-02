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
 * Service implementation for CircleCI.
 * <p>
 * https://circleci.com/
 */
public class Circle extends AbstractServiceSetup {

    public static final String CIRCLE_NAME = "circleci";
    public static final String CIRCLE = "CIRCLECI";
    public static final String CIRCLE_BUILD_NUMBER  = "CIRCLE_BUILD_NUM";
    public static final String CIRCLE_BRANCH = "CIRCLE_BRANCH";
    public static final String CIRCLE_COMMIT = "CIRCLE_SHA1";
    
    public Circle(final Map<String, String> env) {
        super(env);
    }

    @Override
    public boolean isSelected() {
        return (getProperty(CIRCLE) != null);
    }
    
    @Override
    public String getName() {
        return CIRCLE_NAME;
    }
    
    @Override
    public String getBuildNumber() {
        return getProperty(CIRCLE_BUILD_NUMBER);
    }
    
    @Override
    public String getBranch() {
        return getProperty(CIRCLE_BRANCH);
    }
    
    @Override
    public Properties getEnvironment() {
        Properties environment = new Properties();
        addProperty(environment, "circleci_build_num", getProperty(CIRCLE_BUILD_NUMBER));
        addProperty(environment, "branch", getProperty(CIRCLE_BRANCH));
        addProperty(environment, "commit_sha", getProperty(CIRCLE_COMMIT));
        return environment;
    }
}
