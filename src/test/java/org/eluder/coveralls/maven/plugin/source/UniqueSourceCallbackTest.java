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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UniqueSourceCallbackTest {

    @Mock
    private SourceCallback sourceCallbackMock;
    
    @Test
    public void testOnSourceWithUniqueFiles() throws Exception {
        Source s1 = createSource("Foo.java", "{\n  void();\n}\n", 2);
        Source s2 = createSource("Bar.java", "{\n  bar();\n}\n", 2);
        
        UniqueSourceCallback cb = createUniqueSourceCallback();
        cb.onBegin();
        cb.onSource(s1);
        cb.onSource(s2);
        cb.onComplete();
        verify(sourceCallbackMock).onBegin();
        verify(sourceCallbackMock, times(2)).onSource(Mockito.any(Source.class));
        verify(sourceCallbackMock).onComplete();
    }

    @Test
    public void testOnSourceWithDuplicateSources() throws Exception {
        Source s1 = createSource("Foo.java", "{\n  void();\n}\n", 2);
        Source s2 = createSource("Foo.java", "{\n  void();\n}\n", 2);

        UniqueSourceCallback cb = createUniqueSourceCallback();
        cb.onBegin();
        cb.onSource(s1);
        cb.onSource(s2);
        cb.onComplete();
        verify(sourceCallbackMock).onBegin();
        verify(sourceCallbackMock, times(1)).onSource(Mockito.any(Source.class));
        verify(sourceCallbackMock).onComplete();
    }

    @Test
    public void testOnSourceWithUniqueSources() throws Exception {
        Source s1 = createSource("Foo.java", "{\n  void();\n}\n", 2);
        Source s2 = createSource("Foo.java", "{\n  void();\n  func();\n}\n", 2, 3);

        UniqueSourceCallback cb = createUniqueSourceCallback();
        cb.onBegin();
        cb.onSource(s1);
        cb.onSource(s2);
        cb.onComplete();
        verify(sourceCallbackMock).onBegin();
        verify(sourceCallbackMock, times(2)).onSource(Mockito.any(Source.class));
        verify(sourceCallbackMock).onComplete();
    }

    private UniqueSourceCallback createUniqueSourceCallback() {
        return new UniqueSourceCallback(sourceCallbackMock);
    }
    
    private Source createSource(final String name, final String source, final int... relevant) throws Exception {
        Source s = new Source(name, source, TestIoUtil.getMd5DigestHex(source));
        for (int i : relevant) {
            s.addCoverage(i, 1);
        }
        return s;
    }
}
