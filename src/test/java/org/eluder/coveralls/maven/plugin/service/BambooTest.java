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

public class BambooTest {

    private Map<String, String> env() {
        Map<String, String> env = new HashMap<String, String>();
        env.put("bamboo.buildNumber", "build123");
        env.put("bamboo.buildResultsUrl", "http://company.com/bamboo/build123");
        env.put("bamboo.repository.git.branch", "master");
        return env;
    }
    
    @Test
    public void testIsSelectedForNothing() {
        assertFalse(new Bamboo(new HashMap<String, String>()).isSelected());
    }
    
    @Test
    public void testIsSelectedForBamboo() {
        assertTrue(new Bamboo(env()).isSelected());
    }
    
    @Test
    public void testGetName() {
        assertEquals("bamboo", new Bamboo(env()).getName());
    }
    
    @Test
    public void testGetBuildNumber() {
        assertEquals("build123", new Bamboo(env()).getBuildNumber());
    }
    
    @Test
    public void testGetBuildUrl() {
        assertEquals("http://company.com/bamboo/build123", new Bamboo(env()).getBuildUrl());
    }
    
    @Test
    public void testGetBranch() {
        assertEquals("master", new Bamboo(env()).getBranch());
    }
    
}
