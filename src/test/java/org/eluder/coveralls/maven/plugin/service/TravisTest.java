package org.eluder.coveralls.maven.plugin.service;

/*
 * #[license]
 * coveralls-maven-plugin
 * %%
 * Copyright (C) 2013 Tapio Rautonen
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

public class TravisTest {
    
    @Test
    public void testIsSelectedForTravisCi() {
        assertTrue(new Travis("travis-ci", new HashMap<String, String>()).isSelected());
    }
    
    @Test
    public void testIsSelectedForTravisPro() {
        assertTrue(new Travis("travis-pro", new HashMap<String, String>()).isSelected());
    }
    
    @Test
    public void testIsSelectedForEnvironment() {
        Map<String, String> env = new HashMap<String, String>();
        env.put("TRAVIS", "true");
        assertTrue(new Travis(null, env).isSelected());
    }
    
    @Test
    public void testIsSelectedForNothing() {
        assertFalse(new Travis(null, new HashMap<String, String>()).isSelected());
    }
    
    @Test
    public void testGetNameForServiceName() {
        assertEquals("travis-pro", new Travis("travis-pro", new HashMap<String, String>()).getName());
    }
    
    @Test
    public void testGetNameForDefault() {
        assertEquals("travis-ci", new Travis(null, new HashMap<String, String>()).getName());
    }
    
    @Test
    public void testGetJobId() {
        Map<String, String> env = new HashMap<String, String>();
        env.put("TRAVIS_JOB_ID", "job123");
        assertEquals("job123", new Travis(null, env).getJobId());
    }

    @Test
    public void testGetBranch() {
        Map<String, String> env = new HashMap<String, String>();
        env.put("TRAVIS_BRANCH", "master");
        assertEquals("master", new Travis(null, env).getBranch());
    }
    
    @Test
    public void testGetPullRequest() {
        Map<String, String> env = new HashMap<String, String>();
        env.put("TRAVIS_PULL_REQUEST", "pull10");
        assertEquals("pull10", new Travis(null, env).getPullRequest());
    }
    
    @Test
    public void testGetCustomProperties() {
        Map<String, String> env = new HashMap<String, String>();
        env.put("TRAVIS_JOB_ID", "123");
        env.put("TRAVIS_PULL_REQUEST", "999");
        Properties properties = new Travis(null, env).getEnvironment();
        assertEquals(2, properties.size());
        assertEquals("123", properties.getProperty("travis_job_id"));
        assertEquals("999", properties.getProperty("travis_pull_request"));
    }
}
