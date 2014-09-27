package org.eluder.coveralls.maven.plugin.util;

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