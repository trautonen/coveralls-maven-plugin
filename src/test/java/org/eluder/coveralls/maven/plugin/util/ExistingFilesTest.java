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

import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ExistingFilesTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();


    @Test(expected = NullPointerException.class)
    public void testAddAllForNull() throws Exception {
        new ExistingFiles().addAll(null);
    }

    @Test(expected = NullPointerException.class)
    public void testAddForNull() throws Exception {
        new ExistingFiles().add(null);
    }
    
    @Test
    public void testAddForExisting() throws Exception {
        File f = folder.newFile();
        Iterator<File> iter = new ExistingFiles().add(f).add(f).iterator();
        assertSize(iter, 1);
    }
    
    @Test
    public void testAddForDirectory() throws Exception {
        File d = folder.newFolder();
        Iterator<File> iter = new ExistingFiles().add(d).iterator();
        assertSize(iter, 0);
    }

    @Test
    public void testCreateForNull() throws Exception {
        Iterator<File> iter = ExistingFiles.create(null).iterator();
        assertSize(iter, 0);
    }
    
    @Test
    public void testCreateForMultipleFiles() throws Exception {
        File f1 = folder.newFile();
        File f2 = folder.newFile();
        Iterator<File> iter = ExistingFiles.create(Arrays.asList(f1, f2)).iterator();
        assertSize(iter, 2);
    }
    
    private static void assertSize(Iterator<?> iter, int size) {
        for (int i = 0; i < size; i++) {
            iter.next();
        }
        assertFalse(iter.hasNext());
    }
}
