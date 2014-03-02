package org.eluder.coveralls.maven.plugin.util;

/*
 * #[license]
 * coveralls-maven-plugin
 * %%
 * Copyright (C) 2013 - 2014 Tapio Rautonen
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
            String local = resource;
            if (local.lastIndexOf("/") > 0) {
                local = local.substring(local.lastIndexOf('/'));
            }
            if (!local.startsWith("/")) {
                local = "/" + local;
            }
            return new File(getResourceUrl(local).toURI());
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
    
    private static URL getResourceUrl(final String resource) {
        return TestIoUtil.class.getResource(resource);
    }
}
