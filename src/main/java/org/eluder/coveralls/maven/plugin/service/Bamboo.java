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

/**
 * Service implementation for Atlassian Bamboo.
 * <p>
 * https://www.atlassian.com/software/bamboo/
 */
public class Bamboo extends AbstractServiceSetup {

    public static final String BAMBOO_NAME = "bamboo";
    public static final String BAMBOO_BUILD_NUMBER = "bamboo.buildNumber";
    public static final String BAMBOO_BUILD_URL = "bamboo.buildResultsUrl";
    public static final String BAMBOO_BRANCH = "bamboo.repository.git.branch";
    
    public Bamboo(final Map<String, String> env) {
        super(env);
    }

    @Override
    public boolean isSelected() {
        return (getProperty(BAMBOO_BUILD_NUMBER) != null);
    }
    
    @Override
    public String getName() {
        return BAMBOO_NAME;
    }
    
    @Override
    public String getBuildNumber() {
        return getProperty(BAMBOO_BUILD_NUMBER);
    }
    
    @Override
    public String getBuildUrl() {
        return getProperty(BAMBOO_BUILD_URL);
    }
    
    @Override
    public String getBranch() {
        return getProperty(BAMBOO_BRANCH);
    }
}
