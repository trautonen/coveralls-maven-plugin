package org.eluder.coveralls.maven.plugin.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public final class UrlUtils {
    
    public static URI toUri(final URL url) {
        try {
            return url.toURI();
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
    
    private UrlUtils() {
        // hide constructor
    }
}
