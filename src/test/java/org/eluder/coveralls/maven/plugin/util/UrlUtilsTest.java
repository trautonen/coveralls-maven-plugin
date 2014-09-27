package org.eluder.coveralls.maven.plugin.util;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URL;

import org.junit.Test;

public class UrlUtilsTest {

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidUrlToUri() throws Exception {
        UrlUtils.toUri(new URL("http://google.com?q=s|r"));
    }
    
    @Test
    public void testValidUrlToUri() throws Exception {
        URI uri = UrlUtils.toUri(new URL("http://google.com"));
        assertEquals(new URI("http://google.com"), uri);
    }
}