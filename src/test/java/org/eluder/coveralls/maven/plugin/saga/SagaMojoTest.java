package org.eluder.coveralls.maven.plugin.saga;

import java.io.IOException;

import org.eluder.coveralls.maven.plugin.AbstractCoverallsMojo;
import org.eluder.coveralls.maven.plugin.AbstractCoverallsMojoTest;
import org.eluder.coveralls.maven.plugin.CoverageFixture;
import org.eluder.coveralls.maven.plugin.util.TestIoUtil;

/**
 * @author Jakub Bednář (25/12/2013 10:03)
 */
public class SagaMojoTest extends AbstractCoverallsMojoTest {

    @Override
    protected AbstractCoverallsMojo createMojo() {
        SagaMojo mojo = new SagaMojo();
        mojo.coverageFile = TestIoUtil.getFile("saga.xml");
        mojo.jasmineURL = "";
        return mojo;
    }

    @Override
    protected String[][] getCoverageFixture() {
        return CoverageFixture.JAVASCRIPT_FILES;
    }

    protected String readFileContent(final String sourceFile) throws IOException {
        return TestIoUtil.readFileContent(TestIoUtil.getFile("/" + sourceFile));
    }
}
