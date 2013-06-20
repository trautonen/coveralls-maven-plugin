package org.eluder.coveralls.maven.plugin;

public class CoverageFixture {

    public static final String[][] COVERAGE_FILES = new String[][] {
        // file                      lines  covered lines            missed lines
        { "SimpleCoverage.java",     "14",  "3,6",                   "10,11" },
        { "InnerClassCoverage.java", "30",  "3,6,9,10,12,15,18,21",  "25,26" }
    };
    
}
