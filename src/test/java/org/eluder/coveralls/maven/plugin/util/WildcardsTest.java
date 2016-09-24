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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WildcardsTest {

    @Test
    public void testMatchesAgainstNull() throws Exception {
        assertFalse(Wildcards.matches(null, "*"));
    }

    @Test
    public void testMatchesAgainstJoker() throws Exception {
        assertTrue(Wildcards.matches("a", "?"));
    }

    @Test
    public void testMatchesAgainstStar() throws Exception {
        assertTrue(Wildcards.matches("abc", "*"));
    }

    @Test
    public void testMatchesAgainstWildcards() throws Exception {
        assertTrue(Wildcards.matches("abcdefg", "a*d??g"));
        assertFalse(Wildcards.matches("abcdefg", "a*d?g?"));
    }

    @Test
    public void testMatchesAgainstText() throws Exception {
        assertTrue(Wildcards.matches("abc", "abc"));
        assertFalse(Wildcards.matches("abc", "cba"));
    }

}
