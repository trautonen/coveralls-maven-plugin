package org.eluder.coveralls.maven.plugin.source;

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

import org.eluder.coveralls.maven.plugin.domain.Source;
import org.eluder.coveralls.maven.plugin.util.TestIoUtil;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class UrlSourceLoaderTest {

    @Mock
    private File dirMock;

    @Mock
    private File fileMock;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    @Test
    public void testMissingSourceFileFromUrl() throws Exception {
        UrlSourceLoader sourceLoader = new UrlSourceLoader(folder.getRoot().toURI().toURL(), new URL("http://domainthatreallydoesnotexistsdfsmshjsfsj.com"), "UTF-8");
        assertNull(sourceLoader.load("Foo.java"));
    }

    @Test
    public void testLoadSourceFromUrl() throws Exception {
        String fileName = "scripts/file.coffee";
        URL sourceUrl  = folder.getRoot().toURI().toURL();

        folder.newFolder("scripts");
        File file = folder.newFile(fileName);
        TestIoUtil.writeFileContent("math =\n  root:   Math.sqrt\n  square: square", file);

        UrlSourceLoader sourceLoader = new UrlSourceLoader(folder.getRoot().toURI().toURL(), sourceUrl, "UTF-8");
        Source source = sourceLoader.load(fileName);

        assertEquals(fileName, source.getName());
        assertEquals("9897A4BB0467180D3C6ACD95475DD77D", source.getDigest());
        assertEquals(3, source.getCoverage().length);
    }

}
