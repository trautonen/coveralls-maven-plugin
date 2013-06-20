package org.eluder.coveralls.maven.plugin.jacoco;

import org.eluder.coveralls.maven.plugin.AbstractCoverallsMojo;
import org.eluder.coveralls.maven.plugin.AbstractCoverallsMojoTest;
import org.eluder.coveralls.maven.plugin.util.TestIoUtil;

public class JaCoCoMojoTest extends AbstractCoverallsMojoTest {

    @Override
    protected AbstractCoverallsMojo createMojo() {
        JaCoCoMojo mojo = new JaCoCoMojo();
        mojo.coverageFile = TestIoUtil.getFile("/jacoco.xml");
        return mojo;
    }
        
}
