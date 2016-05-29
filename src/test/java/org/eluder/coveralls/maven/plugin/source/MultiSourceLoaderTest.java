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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MultiSourceLoaderTest {

    @Mock
    private SourceLoader sl1;
    
    @Mock
    private SourceLoader sl2;
    
    private Source s1 = new Source("source", "{ 1 }", "9A0174D3A5B8D202C6E404601FC260D1");
    
    private Source s2 = new Source("source", "{ 2 }", "849409F24F4BCAAC904F3B142447D65D");
    
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
