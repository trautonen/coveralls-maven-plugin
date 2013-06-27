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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TravisTest {

    private boolean travis;
    
    @Before
    public void init() {
        travis = Boolean.valueOf(System.getenv("TRAVIS")).booleanValue();
    }
    
    @Test
    public void testIsSelected() {
        Travis service = new Travis();
        assertTrue(service.isSelected("travis-ci"));
        assertTrue(service.isSelected("travis-pro"));
        assertFalse(service.isSelected("travis"));
        assertFalse(service.isSelected(""));
        assertFalse(service.isSelected(null));
    }
    
    @Test
    public void testValues() {
        Travis service = new Travis();
        if (travis) {
            assertNotNull(service.getServiceJobId());
            assertNotNull(service.getBranch());
        } else {
            assertNull(service.getServiceJobId());
            assertNull(service.getBranch());
        }
        assertNull(service.getRepoToken());
    }
}
