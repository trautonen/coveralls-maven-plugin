package org.eluder.coveralls.maven.plugin.source;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DirectorySourceLoader extends AbstractSourceLoader {

    private final File sourceDirectory;

    public DirectorySourceLoader(final File base, final File sourceDirectory, final String sourceEncoding) {
        super(base.toURI(), sourceDirectory.toURI(), sourceEncoding);
        this.sourceDirectory = sourceDirectory;
    }

    @Override
    protected InputStream locate(final String sourceFile) throws IOException {
        File file = new File(sourceDirectory, sourceFile);
        if (file.exists()) {
            if (!file.isFile()) {
                throw new IllegalArgumentException(file.getAbsolutePath() + " is not file");
            }
            return new BufferedInputStream(new FileInputStream(file));
        }
        return null;
    }
}
