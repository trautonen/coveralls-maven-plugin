package org.eluder.coveralls.maven.plugin.cobertura;

import org.eluder.coveralls.maven.plugin.AbstractCoverallsMojo;
import org.eluder.coveralls.maven.plugin.AbstractCoverallsMojoTest;
import org.eluder.coveralls.maven.plugin.util.TestIoUtil;

public class CoberturaMojoTest extends AbstractCoverallsMojoTest {

    @Override
    protected AbstractCoverallsMojo createMojo() {
        CoberturaMojo mojo = new CoberturaMojo();
        mojo.coverageFile = TestIoUtil.getFile("/cobertura.xml");
        return mojo;
    }
    
}
