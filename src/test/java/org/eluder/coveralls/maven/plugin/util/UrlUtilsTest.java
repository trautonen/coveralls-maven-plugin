package org.eluder.coveralls.maven.plugin.util;

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

import java.net.URI;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class UrlUtilsTest {

    @Test(expected = IllegalArgumentException.class)
    public void testCreateInvalidUrl() throws Exception {
        UrlUtils.create("sdfds");
    }

    @Test
    public void testCreateValidUrl() throws Exception {
        assertEquals("http://example.org", UrlUtils.create("http://example.org").toURI().toASCIIString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidUrlToUri() throws Exception {
        UrlUtils.toUri(new URL("http://google.com?q=s|r"));
    }
    
    @Test
    public void testValidUrlToUri() throws Exception {
        URI uri = UrlUtils.toUri(new URL("http://google.com"));
        assertEquals(new URI("http://google.com"), uri);
    }
}
