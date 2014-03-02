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

import org.codehaus.plexus.util.StringUtils;

/**
 * Convenient base class for service setups.
 */
public abstract class AbstractServiceSetup implements ServiceSetup {

    private final Map<String, String> env;
    
    public AbstractServiceSetup(final Map<String, String> env) {
        this.env = env;
    }

    @Override
    public String getJobId() {
        return null;
    }

    @Override
    public String getBuildNumber() {
        return null;
    }

    @Override
    public String getBuildUrl() {
        return null;
    }

    @Override
    public String getBranch() {
        return null;
    }

    @Override
    public String getPullRequest() {
        return null;
    }

    @Override
    public Properties getEnvironment() {
        return null;
    }
    
    protected final String getProperty(final String name) {
        return env.get(name);
    }
    
    protected final void addProperty(final Properties properties, final String name, final String value) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("name must be defined");
        }
        if (StringUtils.isNotBlank(value)) {
            properties.setProperty(name, value);
        }
    }
}
