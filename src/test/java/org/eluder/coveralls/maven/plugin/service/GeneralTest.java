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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class GeneralTest {

    private Map<String, String> env() {
        Map<String, String> env = new HashMap<String, String>();
        env.put("CI_NAME", "ci_service");
        env.put("CI_BUILD_NUMBER", "build123");
        env.put("CI_BUILD_URL", "http://ci.com/build123");
        env.put("CI_BRANCH", "master");
        env.put("CI_PULL_REQUEST", "pull10");
        return env;
    }
    
    @Test
    public void testIsSelectedForNothing() {
        assertFalse(new General(new HashMap<String, String>()).isSelected());
    }
    
    @Test
    public void testIsSelectedForCi() {
        assertTrue(new General(env()).isSelected());
    }
    
    @Test
    public void testGetName() {
        assertEquals("ci_service", new General(env()).getName());
    }
    
    @Test
    public void testGetBuildNumber() {
        assertEquals("build123", new General(env()).getBuildNumber());
    }
    
    @Test
    public void testGetBuildUrl() {
        assertEquals("http://ci.com/build123", new General(env()).getBuildUrl());
    }
    
    @Test
    public void testGetBranch() {
        assertEquals("master", new General(env()).getBranch());
    }
    
    @Test
    public void testGetPullRequest() {
        assertEquals("pull10", new General(env()).getPullRequest());
    }
}
