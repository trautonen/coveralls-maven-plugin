package org.eluder.coveralls.maven.plugin.domain;

/*
 * #[license]
 * coveralls-maven-plugin
 * %%
 * Copyright (C) 2013 - 2015 Tapio Rautonen
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

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class SourceTest {

    @Test
    public void testAddCoverage() {
        Source source = new Source("src/main/java/Hello.java", "public class Hello {\n    \n}\n", "asdfasdf1234asfasdf2345");
        source.addCoverage(1, 3);
        source.addCoverage(3, 3);
        assertArrayEquals(new Integer[] { 3, null, 3, null }, source.getCoverage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddCoverageForSourceOutOfBounds() {
        Source source = new Source("src/main/java/Hello.java", "public class Hello {\n    \n}\n", "asdfasdf1234asfasdf2345");
        source.addCoverage(5, 1);
    }
    
    @Test
    @Ignore("#45: https://github.com/trautonen/coveralls-maven-plugin/issues/45")
    public void testGetNameWithClassifier() throws Exception {
        Source source = new Source("src/main/java/Hello.java", "public class Hello {\n    \n}\n", "asdfasdf1234asfasdf2345");
        source.setClassifier("Inner");
        assertEquals("src/main/java/Hello.java", source.getName());
        assertEquals("src/main/java/Hello.java#Inner", source.getFullName());
    }
}
