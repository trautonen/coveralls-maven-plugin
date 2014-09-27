package org.eluder.coveralls.maven.plugin.source;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.eluder.coveralls.maven.plugin.domain.Source;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MultiSourceLoaderTest {

    @Mock
    private SourceLoader sl1;
    
    @Mock
    private SourceLoader sl2;
    
    private Source s1 = new Source("source", "{ 1 }");
    
    private Source s2 = new Source("source", "{ 2 }");
    
    @Test(expected = IOException.class)
    public void testMissingSource() throws Exception {
        creaMultiSourceLoader().load("source");
    }
    
    @Test
    public void testPrimarySource() throws Exception {
        when(sl1.load("source")).thenReturn(s1);
        when(sl2.load("source")).thenReturn(s2);
        Source source = creaMultiSourceLoader().load("source");
        assertSame(s1, source);
    }
    
    @Test
    public void testSecondarySource() throws Exception {
        when(sl2.load("source")).thenReturn(s2);
        Source source = creaMultiSourceLoader().load("source");
        assertSame(s2, source);
    }
    
    private MultiSourceLoader creaMultiSourceLoader() {
        return new MultiSourceLoader().add(sl1).add(sl2);
    }
}