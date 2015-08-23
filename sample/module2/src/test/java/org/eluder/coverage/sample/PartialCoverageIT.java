package org.eluder.coverage.sample;

import org.junit.Test;

public class PartialCoverageIT {

    @Test
    public void testSum() {
        new PartialCoverage().partial(false);
    }

}
