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

import org.apache.commons.lang3.ArrayUtils;

public final class CoverageFixture {

    public static String[][] JAVA_FILES = new String[][] {
            // file                                                 lines  covered lines            missed lines
            { "org/eluder/coverage/sample/SimpleCoverage.java",     "14",  "3,6",                   "10,11" },
            { "org/eluder/coverage/sample/InnerClassCoverage.java", "30",  "3,6,9,10,12,15,18,21",  "25,26" }
    };

    public static String[][] JAVASCRIPT_FILES = new String[][] {
            // file                 lines   covered lines   missed lines
            { "Localization.js",    "18",   "1,2,4,5,9,13", "6,10" },
            { "Components.js",      "5",    "1,2",          ""     }
    };

    public static String[][] JAVA_AND_JAVASCRIPT_FILES = ArrayUtils.addAll(
            JAVA_FILES, JAVASCRIPT_FILES
    );
    
    public static int getTotalLines(String[][] fixture) {
        int lines = 0;
        for (String[] file : fixture) {
            lines += Integer.parseInt(file[1]);
        }
        return lines;
    }
    
    public static int getTotalFiles(String[][] fixture) {
        return fixture.length;
    }
    
    private CoverageFixture() {
        // hide constructor
    }
}
