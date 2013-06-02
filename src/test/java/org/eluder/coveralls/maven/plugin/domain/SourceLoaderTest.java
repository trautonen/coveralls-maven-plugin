package org.eluder.coveralls.maven.plugin.domain;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.PrintWriter;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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
        new SourceLoader(dirMock, "UTF-8");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSourceDirectory() throws Exception {
        when(dirMock.exists()).thenReturn(true);
        when(dirMock.isDirectory()).thenReturn(false);
        new SourceLoader(dirMock, "UTF-8");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingSourceFile() throws Exception {
        new SourceLoader(folder.getRoot(), "UTF-8").load("Foo.java");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSourceFile() throws Exception {
        File subFolder = folder.newFolder();
        new SourceLoader(folder.getRoot(), "UTF-8").load(subFolder.getName());
    }
    
    @Test
    public void testLoadSource() throws Exception {
        File file = folder.newFile();
        writeSource("public class Foo {\r\n    \n}\r", file);
        Source source = new SourceLoader(folder.getRoot(), "UTF-8").load(file.getName());
        assertEquals(file.getName(), source.getName());
        assertEquals("public class Foo {\n    \n}\n", source.getSource());
        assertEquals(4, source.getCoverage().length);
    }
    
    private void writeSource(final String source, final File file) throws Exception {
        PrintWriter writer = new PrintWriter(file);
        try {
            writer.write(source);
        } finally {
            writer.close();
        }
    }
}
