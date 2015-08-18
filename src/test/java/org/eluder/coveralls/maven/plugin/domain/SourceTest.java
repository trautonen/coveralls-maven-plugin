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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class SourceTest {

    @Test
    public void testAddCoverage() {
        Source source = new Source("src/main/java/Hello.java", createTempFile("public class Hello {\n    \n}\n"), StandardCharsets.UTF_8);
        source.addCoverage(1, 3);
        source.addCoverage(3, 3);
        assertArrayEquals(new Integer[] { 3, null, 3, null }, source.getCoverage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddCoverageForSourceOutOfBounds() {
        Source source = new Source("src/main/java/Hello.java", createTempFile("public class Hello {\n    \n}\n"), StandardCharsets.UTF_8);
        source.addCoverage(5, 1);
    }

    @Test
    @Ignore("#45: https://github.com/trautonen/coveralls-maven-plugin/issues/45")
    public void testGetNameWithClassifier() throws Exception {
        Source source = new Source("src/main/java/Hello.java", createTempFile("public class Hello {\n    \n}\n"), StandardCharsets.UTF_8);
        source.setClassifier("Inner");
        assertEquals("src/main/java/Hello.java", source.getName());
        assertEquals("src/main/java/Hello.java#Inner", source.getFullName());
    }

    public static File createTempFile(String tmpFileContent) {
        File tmp = null;
        try {
            tmp = File.createTempFile("testfile", ".tmp");
        } catch (IOException e1) {
        }
        try (OutputStream out = new FileOutputStream(tmp)){
            out.write(tmpFileContent.getBytes(StandardCharsets.UTF_8));
            out.flush();
        } catch (IOException e) {
        }
        return tmp;
    }
}
