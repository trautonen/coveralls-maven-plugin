package org.eluder.coveralls.maven.plugin.source;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;

import org.eluder.coveralls.maven.plugin.domain.Source;
import org.eluder.coveralls.maven.plugin.util.TestIoUtil;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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
        UrlSourceLoader sourceLoader = new UrlSourceLoader(folder.getRoot().toURI().toURL(), new URL("http://domainthatdoesnotexist.com"), "UTF-8");
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
        assertEquals("math =\n  root:   Math.sqrt\n  square: square", source.getSource());
        assertEquals(3, source.getCoverage().length);
    }

}
