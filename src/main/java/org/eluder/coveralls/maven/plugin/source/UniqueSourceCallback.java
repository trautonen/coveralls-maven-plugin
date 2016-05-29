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

import org.eluder.coveralls.maven.plugin.ProcessingException;
import org.eluder.coveralls.maven.plugin.domain.Source;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Source callback that tracks passed by source files and provides only unique
 * source files to the delegate. Note that the implementation is not thread
 * safe so the {@link #onSource(org.eluder.coveralls.maven.plugin.domain.Source)}
 * can be called only from single thread concurrently.
 */
public class UniqueSourceCallback implements SourceCallback {

    private final Map<Source, Source> cache;
    private final SourceCallback delegate;

    public UniqueSourceCallback(final SourceCallback delegate) {
        this.cache = new LinkedHashMap<>();
        this.delegate = delegate;
    }

    @Override
    public void onBegin() throws ProcessingException, IOException {
        delegate.onBegin();
    }

    @Override
    public void onSource(final Source source) throws ProcessingException, IOException {
        Source merged = source.merge(cache.get(source));
        cache.put(merged, merged);
    }

    @Override
    public void onComplete() throws ProcessingException, IOException {
        for (Source source : cache.values()) {
            delegate.onSource(source);
        }
        delegate.onComplete();
    }
}
