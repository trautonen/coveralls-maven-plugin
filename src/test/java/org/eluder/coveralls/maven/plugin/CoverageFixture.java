package org.eluder.coveralls.maven.plugin;

/*
 * #[license]
 * coveralls-maven-plugin
 * %%
 * Copyright (C) 2013 Tapio Rautonen
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

public class CoverageFixture {

    public static final String[][] COVERAGE_FILES = new String[][] {
        // file                      lines  covered lines            missed lines
        { "SimpleCoverage.java",     "14",  "3,6",                   "10,11" },
        { "InnerClassCoverage.java", "30",  "3,6,9,10,12,15,18,21",  "25,26" }
    };

    public static final String[][] COVERAGE_FILES_SAGA = new String[][] {
            // file                 lines   covered lines   missed lines
            { "Localization.js",    "17",   "1,2,4,5,9,13", "6,10" },
            { "Components.js",      "5",    "1,2",          "" }
    };

    public static final String[][] COVERAGE_FILES_SAGA_COBERTURA = new String[][] {
            // file                         lines   covered lines           missed lines
            { "SimpleCoverage.java",        "14",   "3,6",                  "10,11" },
            { "InnerClassCoverage.java",    "30",   "3,6,9,10,12,15,18,21", "25,26"},
            { "Localization.js",            "17",   "1,2,4,5,9,13",         "6,10" },
            { "Components.js",              "5",    "1,2",                  "" }
    };
}
