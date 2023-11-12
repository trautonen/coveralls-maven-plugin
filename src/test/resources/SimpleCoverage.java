package org.eluder.coverage.sample;

public class SimpleCoverage {

    public boolean isTested() {
        return false;
    }

    public void neverRun() {
        System.out.println("oops");
    }

}
