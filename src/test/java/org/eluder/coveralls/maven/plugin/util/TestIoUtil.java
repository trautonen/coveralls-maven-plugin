package org.eluder.coveralls.maven.plugin.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

import org.codehaus.plexus.util.IOUtil;

public class TestIoUtil {

    public static void writeFileContent(final String content, final File file) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(file);
        try {
            writer.write(content);
        } finally {
            IOUtil.close(writer);
        }
    }
    
    public static String readFileContent(final File file) throws IOException {
        InputStreamReader reader = new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8"));
        try {
            return IOUtil.toString(reader);
        } finally {
            IOUtil.close(reader);
        }
    }
    
    public static File getFile(final String resource) {
        try {
            return new File(getResourceUrl(resource).toURI());
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
    
    private static URL getResourceUrl(final String resource) {
        return TestIoUtil.class.getResource(resource);
    }
}
