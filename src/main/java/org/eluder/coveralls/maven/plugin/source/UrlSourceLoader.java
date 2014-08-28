package org.eluder.coveralls.maven.plugin.source;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eluder.coveralls.maven.plugin.util.UrlUtils;

public class UrlSourceLoader extends AbstractSourceLoader {

    private final URL sourceUrl;

    public UrlSourceLoader(final URL base, final URL sourceUrl, final String sourceEncoding) {
        super(UrlUtils.toUri(base), UrlUtils.toUri(sourceUrl), sourceEncoding);
        this.sourceUrl = sourceUrl;
    }
    
    @Override
    protected InputStream locate(final String sourceFile) throws IOException {
        URL url = new URL(sourceUrl, sourceFile);
        // Checkstyle OFF: EmptyBlock
        try {
            return url.openStream();
        } catch (IOException ex) {
            // not found from url
        }
        // Checkstyle ON: EmptyBlock
        return null;
    }
}
