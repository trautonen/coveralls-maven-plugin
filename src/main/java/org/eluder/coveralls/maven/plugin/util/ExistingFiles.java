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

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class ExistingFiles implements Iterable<File> {
    
    private final ArrayList<File> delegate = new ArrayList<>();

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
