package org.eluder.coveralls.maven.plugin;

/*
 * #[license]
 * coveralls-maven-plugin
 * %%
 * Copyright (C) 2013 - 2014 Tapio Rautonen
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

import java.io.IOException;

import org.eluder.coveralls.maven.plugin.domain.Source;

/**
 * Source callback handler that allows chaining multiple callback handlers. Chained callback
 * handler is executed after this callback.
 */
public abstract class ChainingSourceCallback implements SourceCallback {

    private final SourceCallback chained;

    public ChainingSourceCallback(final SourceCallback chained) {
        if (chained == null) {
            throw new IllegalArgumentException("chained must be defined");
        }
        this.chained = chained;
    }
    
    @Override
    public final void onSource(final Source source) throws ProcessingException, IOException {
        onSourceInternal(source);
        chained.onSource(source);
    }
    
    /**
     * @see #onSource(Source)
     */
    protected abstract void onSourceInternal(final Source source) throws ProcessingException, IOException;
}
