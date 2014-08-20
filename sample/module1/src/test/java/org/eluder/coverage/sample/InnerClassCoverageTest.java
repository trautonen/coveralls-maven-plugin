package org.eluder.coverage.sample;

import org.junit.Test;

public class InnerClassCoverageTest {

    @Test
    public void testAnonymous() {
        new InnerClassCoverage().anonymous();
    }
    
    @Test
    public void testDelegate() {
        new InnerClassCoverage().delegate();
    }
    
}
