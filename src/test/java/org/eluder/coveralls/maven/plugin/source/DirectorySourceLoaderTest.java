package org.eluder.coveralls.maven.plugin.source;

import static org.junit.Assert.*;

import java.io.File;

import org.eluder.coveralls.maven.plugin.domain.Source;
import org.eluder.coveralls.maven.plugin.util.TestIoUtil;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DirectorySourceLoaderTest {

    @Mock
    private File dirMock;

    @Mock
    private File fileMock;
    
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    @Test
    public void testMissingSourceFileFromDirectory() throws Exception {
        DirectorySourceLoader sourceLoader = new DirectorySourceLoader(folder.getRoot(), folder.getRoot(), "UTF-8");
        assertNull(sourceLoader.load("Foo.java"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSourceFile() throws Exception {
        File subFolder = folder.newFolder();
        DirectorySourceLoader sourceLoader = new DirectorySourceLoader(folder.getRoot(), folder.getRoot(), "UTF-8");
        sourceLoader.load(subFolder.getName());
    }

    @Test
    public void testLoadSource() throws Exception {
        File file = folder.newFile();
        TestIoUtil.writeFileContent("public class Foo {\r\n    \n}\r", file);
        DirectorySourceLoader sourceLoader = new DirectorySourceLoader(folder.getRoot(), folder.getRoot(), "UTF-8");
        Source source = sourceLoader.load(file.getName());
        assertEquals(file.getName(), source.getName());
        assertEquals("public class Foo {\n    \n}\n", source.getSource());
        assertEquals(4, source.getCoverage().length);
    }
}
