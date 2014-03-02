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

import java.io.File;
import java.io.IOException;


/**
 * Handles parsing of a coverage report. The implemenation can be statefull, and the same instance
 * should be used only one time to parse a coverage report. Completed source files are passed to
 * the {@link SourceCallback} handler. To maximize performance, the parser should use streaming.
 */
public interface CoverageParser {

    /**
     * Parses a coverage report. Parsed source files are passed to the callback handler. This
     * method should be called only once per instance.
     * 
     * @param callback the source callback handler
     * @throws ProcessingException if processing of the coverage report fails
     * @throws IOException if an I/O error occurs
     */
    void parse(SourceCallback callback) throws ProcessingException, IOException;
    
    /**
     * @return the coverage report file under processing
     */
    File getCoverageFile();
}
