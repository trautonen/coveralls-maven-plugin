package org.eluder.coveralls.maven.plugin.source;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.Charset;

import org.codehaus.plexus.util.IOUtil;
import org.eluder.coveralls.maven.plugin.domain.Source;

public abstract class AbstractSourceLoader implements SourceLoader {

    private final Charset sourceEncoding;
    private final String directoryPrefix;
    
    public AbstractSourceLoader(final URI base, final URI sourceBase, final String sourceEncoding) {
        this.sourceEncoding = Charset.forName(sourceEncoding);
        this.directoryPrefix = base.relativize(sourceBase).toString();
    }
    
    @Override
    public Source load(final String sourceFile) throws IOException {
        InputStream stream = locate(sourceFile);
        if (stream != null) {
            InputStreamReader reader = new InputStreamReader(stream, getSourceEncoding());
            try {
                String source = IOUtil.toString(reader);
                return new Source(getFileName(sourceFile), source);
            } finally {
                IOUtil.close(reader);
            }
        } else {
            return null;
        }
    }
    
    protected Charset getSourceEncoding() {
        return sourceEncoding;
    }
    
    protected String getFileName(final String sourceFile) {
        return directoryPrefix + sourceFile;
    }
    
    protected abstract InputStream locate(String sourceFile) throws IOException;
}
