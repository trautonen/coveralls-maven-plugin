package org.eluder.coverage.sample;

import org.junit.Test;

public class PartialCoverageTest {

    @Test
    public void testPartial() {
        new PartialCoverage().partial(true);
    }
    
}
