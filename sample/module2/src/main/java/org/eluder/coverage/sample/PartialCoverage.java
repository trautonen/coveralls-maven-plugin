package org.eluder.coverage.sample;

public class PartialCoverage {
    
    public void partial(boolean test) {
        if (test) {
            System.out.println("test");
        } else {
            System.out.println("not test");
        }
    }
    
}
