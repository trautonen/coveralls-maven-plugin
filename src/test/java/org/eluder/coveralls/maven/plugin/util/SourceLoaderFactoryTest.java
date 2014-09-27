package org.eluder.coveralls.maven.plugin.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;

import org.apache.maven.project.MavenProject;
import org.eluder.coveralls.maven.plugin.source.SourceLoader;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SourceLoaderFactoryTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    @Mock
    private MavenProject root;
    
    @Mock
    private MavenProject m1;
    
    @Mock
    private MavenProject m2;
    
    private File rootSources;
    private File m1Sources;
    private File m2Sources;
    
    @Before
    public void init() throws Exception {
        rootSources = new File(folder.getRoot(), "src");
        m1Sources = folder.newFolder("m1", "src");
        m2Sources = folder.newFolder("m2", "src");
        when(root.getCollectedProjects()).thenReturn(Arrays.asList(m1, m2));
        when(m1.getCollectedProjects()).thenReturn(Collections.<MavenProject>emptyList());
        when(m2.getCollectedProjects()).thenReturn(Collections.<MavenProject>emptyList());
        when(root.getCompileSourceRoots()).thenReturn(Arrays.asList(rootSources.getAbsolutePath()));
        when(m1.getCompileSourceRoots()).thenReturn(Arrays.asList(m1Sources.getAbsolutePath()));
        when(m2.getCompileSourceRoots()).thenReturn(Arrays.asList(m2Sources.getAbsolutePath()));
    }
    
    @Test
    public void testCreateSourceLoader() throws Exception {
        SourceLoader sourceLoader = createSourceLoaderFactory("UTF-8").createSourceLoader();
        assertNotNull(sourceLoader);
    }
    
    @Test
    public void testCreateSourceLoaderWithAdditionalSourceDirectories() throws Exception {
        File s1 = new File(folder.getRoot(), "s1");
        File s2 = folder.newFolder("s2");
        SourceLoader sourceLoader = createSourceLoaderFactory("UTF-8")
                .withSourceDirectories(Arrays.asList(s1, s2))
                .createSourceLoader();
        assertNotNull(sourceLoader);
    }
    
    private SourceLoaderFactory createSourceLoaderFactory(String sourceEncoding) {
        return new SourceLoaderFactory(folder.getRoot(), root, sourceEncoding);
    }
}