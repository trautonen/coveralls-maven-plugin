package org.eluder.coveralls.maven.plugin.source;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.eluder.coveralls.maven.plugin.domain.Source;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UniqueSourceCallbackTest {

    @Mock
    private SourceCallback sourceCallbackMock;
    
    @Test
    public void testOnSourceWithUniqueSources() throws Exception {
        Source s1 = createSource("Foo.java", "{\n  void();\n}\n", 2);
        Source s2 = createSource("Bar.java", "{\n  bar();\n}\n", 2);
        
        UniqueSourceCallback cb = createUniqueSourceCallback();
        cb.onSource(s1);
        cb.onSource(s2);
        verify(sourceCallbackMock, times(2)).onSource(Mockito.any(Source.class));
    }

    @Test
    public void testOnSourceWithDuplicateSources() throws Exception {
        Source s1 = createSource("Foo.java", "{\n  void();\n}\n", 2);

        UniqueSourceCallback cb = createUniqueSourceCallback();
        cb.onSource(s1);
        cb.onSource(s1);
        verify(sourceCallbackMock, times(1)).onSource(Mockito.any(Source.class));
    }

    @Test
    public void testOnSourceWithUniqueRelevantLines() throws Exception {
        Source s1 = createSource("Foo.java", "{\n  void();\n}\n", 2);
        Source s2 = createSource("Foo.java", "{\n  void();\n}\n", 1, 3);

        UniqueSourceCallback cb = createUniqueSourceCallback();
        cb.onSource(s1);
        cb.onSource(s2);
        verify(sourceCallbackMock, times(2)).onSource(Mockito.any(Source.class));
    }

    private UniqueSourceCallback createUniqueSourceCallback() {
        return new UniqueSourceCallback(sourceCallbackMock);
    }
    
    private Source createSource(final String name, final String source, final int... relevant) {
        Source s = new Source(name, source);
        for (int i : relevant) {
            s.addCoverage(i, 1);
        }
        return s;
    }
}