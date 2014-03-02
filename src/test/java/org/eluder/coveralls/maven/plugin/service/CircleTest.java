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
import java.util.Properties;

import org.junit.Test;

public class CircleTest {

    private Map<String, String> env() {
        Map<String, String> env = new HashMap<String, String>();
        env.put("CIRCLECI", "true");
        env.put("CIRCLE_BUILD_NUM", "build123");
        env.put("CIRCLE_BRANCH", "master");
        env.put("CIRCLE_SHA1", "a3562fgcd2");
        return env;
    }
    
    @Test
    public void testIsSelectedForNothing() {
        assertFalse(new Circle(new HashMap<String, String>()).isSelected());
    }
    
    @Test
    public void testIsSelectedForCircle() {
        assertTrue(new Circle(env()).isSelected());
    }
    
    @Test
    public void testGetName() {
        assertEquals("circleci", new Circle(env()).getName());
    }
    
    @Test
    public void testGetBuildNumber() {
        assertEquals("build123", new Circle(env()).getBuildNumber());
    }
    
    @Test
    public void testGetBranch() {
        assertEquals("master", new Circle(env()).getBranch());
    }
    
    @Test
    public void testGetEnvironment() {
        Properties properties = new Circle(env()).getEnvironment();
        assertEquals(3, properties.size());
        assertEquals("build123", properties.getProperty("circleci_build_num"));
        assertEquals("master", properties.getProperty("branch"));
        assertEquals("a3562fgcd2", properties.getProperty("commit_sha"));
    }
}
