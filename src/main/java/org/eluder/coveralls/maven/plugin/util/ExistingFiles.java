package org.eluder.coveralls.maven.plugin.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class ExistingFiles implements Iterable<File> {
    
    private final ArrayList<File> delegate = new ArrayList<File>();

    public ExistingFiles addAll(final Iterable<File> files) {
        if (files == null) {
            throw new NullPointerException("Files must be defined");
        }
        for (File file : files) {
            add(file);
        }
        return this;
    }
    
    public ExistingFiles add(final File file) {
        if (file == null) {
            throw new NullPointerException("File must be defined");
        }
        if (file.exists() && file.isFile() && !delegate.contains(file)) {
            delegate.add(file);
        }
        return this;
    }
    
    @Override
    public Iterator<File> iterator() {
        return delegate.iterator();
    }
    
    public static ExistingFiles create(final Iterable<File> files) {
        ExistingFiles existingFiles = new ExistingFiles();
        if (files != null) {
            existingFiles.addAll(files);
        }
        return existingFiles;
    }
}
