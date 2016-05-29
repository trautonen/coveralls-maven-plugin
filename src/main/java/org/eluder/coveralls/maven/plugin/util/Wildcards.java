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

/**
 * Wildcard text utilities.
 */
public final class Wildcards {

    /**
     * Matches text against a wildcard pattern where ? is single letter and * is zero or more
     * letters.
     *
     * @param text the text to test against
     * @param wildcard the wildcard pattern
     * @return <code>true</code> if the given text matches the wildcard, otherwise <code>false</code>
     */
    public static boolean matches(final String text, final String wildcard) {
        String pattern = wildcard.replace("?", "\\w").replace("*", "\\w*");
        return (text != null && text.matches(pattern));
    }

    private Wildcards() {
        // hide constructor
    }
}
