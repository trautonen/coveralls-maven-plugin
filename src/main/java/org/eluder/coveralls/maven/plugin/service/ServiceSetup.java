package org.eluder.coveralls.maven.plugin.service;

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
 * Service specific mojo properties.
 */
public interface ServiceSetup {

    /**
     * @return <code>true</code> if this service is selected, otherwise <code>false</code>
     */
    boolean isSelected();
    
    /**
     * @return service name
     */
    String getName();
    
    /**
     * @return service job id, or <code>null</code> if not defined
     */
    String getJobId();
    
    /**
     * @return service build number, or <code>null</code> if not defined
     */
    String getBuildNumber();
    
    /**
     * @return service build url, or <code>null</code> if not defined
     */
    String getBuildUrl();
    
    /**
     * @return git branch name, or <code>null</code> if not defined
     */
    String getBranch();
    
    /**
     * @return pull request identifier, or <code>null</code> if not defined
     */
    String getPullRequest();
    
    /**
     * @return environment related to service, or <code>null</code> if not defined
     */
    Properties getEnvironment();
}
