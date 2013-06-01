package org.eluder.coveralls.maven.plugin.domain;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import org.codehaus.plexus.util.IOUtil;

public class SourceLoader {

    private final File sourceDirectory;
    private final Charset sourceEncoding;
    
    public SourceLoader(final File sourceDirectory, final String sourceEncoding) {
        if (!sourceDirectory.exists()) {
            throw new IllegalArgumentException("Source directory " + sourceDirectory.getAbsolutePath() + " does not exist");
        }
        if (!sourceDirectory.isDirectory()) {
            throw new IllegalArgumentException(sourceDirectory.getAbsolutePath() + " is not directory");
        }
        this.sourceDirectory = sourceDirectory;
        this.sourceEncoding = Charset.forName(sourceEncoding);
    }
    
    public Source load(final String sourceFile) throws IOException {
        File file = new File(sourceDirectory, sourceFile);
        if (!file.exists()) {
            throw new IllegalArgumentException("Source file " + file.getAbsolutePath() + " does not exist");
        }
        if (!file.isFile()) {
            throw new IllegalArgumentException(file.getAbsolutePath() + " is not file");
        }
        Reader reader = new InputStreamReader(new BufferedInputStream(new FileInputStream(file)), sourceEncoding);
        try {
            String source = IOUtil.toString(reader);
            return new Source(sourceFile, source);
        } finally {
            IOUtil.close(reader);
        }
    }
    
}
