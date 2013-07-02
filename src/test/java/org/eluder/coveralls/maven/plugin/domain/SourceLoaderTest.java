package org.eluder.coveralls.maven.plugin.domain;

/*
 * #[license]
 * coveralls-maven-plugin
 * %%
 * Copyright (C) 2013 Tapio Rautonen
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

import org.eluder.coveralls.maven.plugin.util.TestIoUtil;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SourceLoaderTest {

    @Mock
    private File dirMock;
    
    @Mock
    private File fileMock;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    @Test(expected = IllegalArgumentException.class)
    public void testMissingSourceDirectory() throws Exception {
        when(dirMock.exists()).thenReturn(false);
        when(dirMock.isDirectory()).thenReturn(false);
        new SourceLoader(Arrays.asList(dirMock), "UTF-8");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSourceDirectory() throws Exception {
        when(dirMock.exists()).thenReturn(true);
        when(dirMock.isDirectory()).thenReturn(false);
        new SourceLoader(Arrays.asList(dirMock), "UTF-8");
    }

    @Test
    public void testMissingSourceFile() throws Exception {
        assertNull(new SourceLoader(Arrays.asList(folder.getRoot()), "UTF-8").load("Foo.java"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSourceFile() throws Exception {
        File subFolder = folder.newFolder();
        new SourceLoader(Arrays.asList(folder.getRoot()), "UTF-8").load(subFolder.getName());
    }
    
    @Test
    public void testLoadSource() throws Exception {
        File file = folder.newFile();
        TestIoUtil.writeFileContent("public class Foo {\r\n    \n}\r", file);
        Source source = new SourceLoader(Arrays.asList(folder.getRoot()), "UTF-8").load(file.getName());
        assertEquals(file.getName(), source.getName());
        assertEquals("public class Foo {\n    \n}\n", source.getSource());
        assertEquals(4, source.getCoverage().length);
    }
}
