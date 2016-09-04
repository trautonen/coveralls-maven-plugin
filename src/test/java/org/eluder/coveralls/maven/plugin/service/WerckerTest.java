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

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WerckerTest {
    
    private Map<String, String> env() {
        Map<String, String> env = new HashMap<String, String>();
        env.put("WERCKER", "true");
        env.put("WERCKER_BUILD_URL", "https://app.wercker.com/build/123456789");
        env.put("WERCKER_BUILD_ID", "123456789");
        env.put("WERCKER_GIT_BRANCH", "master");
        return env;
    }
    
    @Test
    public void testIsSelectedForNothing() {
        assertFalse(new Wercker(new HashMap<String, String>()).isSelected());
    }
    
    @Test
    public void testIsSelectedForWercker() {
        assertTrue(new Wercker(env()).isSelected());
    }
    
    @Test
    public void testGetName() {
        assertEquals("wercker", new Wercker(env()).getName());
    }
    
    @Test
    public void testGetJobId() {
        assertEquals("123456789", new Wercker(env()).getJobId());
    }

    @Test
    public void testGetBuildUrl() {
        assertEquals("https://app.wercker.com/build/123456789", new Wercker(env()).getBuildUrl());
    }

    @Test
    public void testGetBranch() {
        assertEquals("master", new Wercker(env()).getBranch());
    }
}
