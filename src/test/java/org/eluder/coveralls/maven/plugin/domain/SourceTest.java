package org.eluder.coveralls.maven.plugin.domain;

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

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class SourceTest {

    @Test
    public void testAddCoverage() {
        Source source = new Source("src/main/java/Hello.java", "public class Hello {\n  \n}\n", "E8BD88CF0BDB77A6408234FD91FD22C3");
        source.addCoverage(1, 3);
        source.addCoverage(3, 3);
        assertArrayEquals(new Integer[] { 3, null, 3, null }, source.getCoverage());
    }

    @Test
    public void testAddBranchCoverage() {
        Source source = new Source("src/main/java/Hello.java", "public class Hello {\n  if(true) {\n  }\n}\n", "609BD24390ADB11D11536CA2ADD18BD0");
        source.addBranchCoverage(2, 0, 0, 2);
        source.addBranchCoverage(2, 0, 1, 3);
        assertArrayEquals(new Integer[] { 2, 0, 0, 2, 2, 0, 1, 3 }, source.getBranches());
    }

    @Test
    public void testAddSameBranchReplaceExistingOne() {
        Source source = new Source("src/main/java/Hello.java", "public class Hello {\n  if(true) {\n  }\n}\n", "609BD24390ADB11D11536CA2ADD18BD0");
        source.addBranchCoverage(2, 0, 0, 2);
        source.addBranchCoverage(2, 0, 0, 3);
        assertArrayEquals(new Integer[] { 2, 0, 0, 3 }, source.getBranches());
    }

    @Test
    public void testAddSameBranchDoNotKeepOrdering() {
        Source source = new Source("src/main/java/Hello.java", "public class Hello {\n  if(true) {\n  }\n}\n", "609BD24390ADB11D11536CA2ADD18BD0");
        source.addBranchCoverage(2, 0, 0, 0);
        source.addBranchCoverage(2, 0, 1, 0);
        source.addBranchCoverage(2, 0, 0, 1);
        assertArrayEquals(new Integer[] { 2, 0, 1, 0, 2, 0, 0, 1 }, source.getBranches());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddCoverageForSourceOutOfBounds() {
        Source source = new Source("src/main/java/Hello.java", "public class Hello {\n  \n}\n", "E8BD88CF0BDB77A6408234FD91FD22C3");
        source.addCoverage(5, 1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddBranchCoverageForSourceOutOfBounds() {
        Source source = new Source("src/main/java/Hello.java", "public class Hello {\n  if(true) {\n  }\n}\n", "609BD24390ADB11D11536CA2ADD18BD0");
        source.addBranchCoverage(6, 0, 0, 2);
    }

    @Test
    @Ignore("#45: https://github.com/trautonen/coveralls-maven-plugin/issues/45")
    public void testGetNameWithClassifier() throws Exception {
        Source source = new Source("src/main/java/Hello.java", "public class Hello {\n  \n}\n", "E8BD88CF0BDB77A6408234FD91FD22C3");
        source.setClassifier("Inner");
        assertEquals("src/main/java/Hello.java", source.getName());
        assertEquals("src/main/java/Hello.java#Inner", source.getFullName());
    }

    @Test
    public void testMerge() {
        Source source1 = new Source("src/main/java/Hello.java", "public class Hello {\n  if(true) {\n  }\n}\n", "609BD24390ADB11D11536CA2ADD18BD0");
        source1.addCoverage(1, 2);
        source1.addCoverage(3, 4);
        source1.addBranchCoverage(2, 0, 0, 1);
        Source source2 = new Source("src/main/java/Hello.java", "public class Hello {\n  if(true) {\n  }\n}\n", "609BD24390ADB11D11536CA2ADD18BD0");
        source2.addCoverage(2, 1);
        source2.addCoverage(3, 3);
        source2.addBranchCoverage(2, 0, 0, 1);
        source2.addBranchCoverage(2, 0, 1, 3);

        Source merged = source1.merge(source2);
        assertFalse(source1 == merged);
        assertFalse(source2 == merged);
        assertEquals(source1.getName(), merged.getName());
        assertEquals(source1.getDigest(), merged.getDigest());
        assertEquals(source1.getClassifier(), merged.getClassifier());
        assertEquals(new Integer(2), merged.getCoverage()[0]);
        assertEquals(new Integer(1), merged.getCoverage()[1]);
        assertEquals(new Integer(7), merged.getCoverage()[2]);
        assertNull(merged.getCoverage()[3]);
        assertEquals(new Integer(2), merged.getBranches()[0]);
        assertEquals(new Integer(0), merged.getBranches()[1]);
        assertEquals(new Integer(0), merged.getBranches()[2]);
        assertEquals(new Integer(2), merged.getBranches()[3]);
        assertEquals(new Integer(2), merged.getBranches()[4]);
        assertEquals(new Integer(0), merged.getBranches()[5]);
        assertEquals(new Integer(1), merged.getBranches()[6]);
        assertEquals(new Integer(3), merged.getBranches()[7]);
    }

    @Test
    public void testMergeDifferent() {
        Source source1 = new Source("src/main/java/Hello.java", "public class Hello {\n  \n}\n", "E8BD88CF0BDB77A6408234FD91FD22C3");
        source1.addCoverage(1, 3);
        Source source2 = new Source("src/main/java/Hello.java", "public class Hello {\n  void();\n}\n", "CBA7831606B51D1499349451B70758E3");
        source2.addCoverage(2, 4);
        Source merged = source1.merge(source2);
        assertFalse(source1 == merged);
        assertFalse(source2 == merged);
        assertArrayEquals(source1.getCoverage(), merged.getCoverage());
    }

    @Test
    public void testEqualsForNull() {
        Source source = new Source("src/main/java/Hello.java", "public class Hello {\n  \n}\n", "E8BD88CF0BDB77A6408234FD91FD22C3");
        assertFalse(source.equals(null));
    }

    @Test
    public void testEqualsForDifferentSources() throws Exception {
        Source source1 = new Source("src/main/java/Hello.java", "public class Hello {\n  \n}\n", "E8BD88CF0BDB77A6408234FD91FD22C3");
        Source source2 = new Source("src/main/java/Hello.java", "public class Hello {\n  void();\n}\n", "CBA7831606B51D1499349451B70758E3");
        assertFalse(source1.equals(source2));
    }

    @Test
    public void testHashCode() {
        Source source1 = new Source("src/main/java/Hello.java", "public class Hello {\n  \n}\n", "E8BD88CF0BDB77A6408234FD91FD22C3");
        Source source2 = new Source("src/main/java/Hello.java", "public class Hello {\n  \n}\n", "E8BD88CF0BDB77A6408234FD91FD22C3");
        Source source3 = new Source("src/main/java/Hello.java", "public class Hello {\n  void();\n}\n", "CBA7831606B51D1499349451B70758E3");
        assertTrue(source1.hashCode() == source2.hashCode());
        assertFalse(source1.hashCode() == source3.hashCode());
        assertFalse(source2.hashCode() == source3.hashCode());
    }
}
