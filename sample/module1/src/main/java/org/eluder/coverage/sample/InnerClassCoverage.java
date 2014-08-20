package org.eluder.coverage.sample;

public class InnerClassCoverage {

    public void anonymous() {
        InnerClass i = new InnerClass() {
            @Override
            public void run() {
                System.out.println("overridden");
            }
        };
        i.run();
    }
    
    public boolean delegate() {
        return new InnerClass().isInner();
    }
    
    public static class InnerClass {
        
        public boolean isInner() {
            return true;
        }
        
        public void run() {
            System.out.println("run");
        }
    }
    
}
