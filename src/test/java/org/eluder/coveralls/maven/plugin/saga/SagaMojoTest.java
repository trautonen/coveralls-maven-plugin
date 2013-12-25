package org.eluder.coveralls.maven.plugin.saga;

import org.apache.maven.plugin.logging.Log;
import org.eluder.coveralls.maven.plugin.AbstractCoverallsMojo;
import org.eluder.coveralls.maven.plugin.AbstractCoverallsMojoTest;
import org.eluder.coveralls.maven.plugin.CoverageFixture;
import org.eluder.coveralls.maven.plugin.util.TestIoUtil;

import static org.mockito.Mockito.verify;

/**
 * @author Jakub Bednář (25/12/2013 10:03)
 */
public class SagaMojoTest extends AbstractCoverallsMojoTest {

    @Override
    protected AbstractCoverallsMojo createMojo() {
        SagaMojo mojo = new SagaMojo();
        mojo.coverageFile = TestIoUtil.getFile("/saga.xml");

        return mojo;
    }

    @Override
    protected String[][] getCoverageFiles() {
        return CoverageFixture.COVERAGE_FILES_SAGA;
    }

    @Override
    protected void verifySuccesfull(final Log logMock) {
        verify(logMock).info("Gathered code coverage metrics for 1 source files with 5 lines of code:");
        verify(logMock).info("*** It might take hours for Coveralls to update the actual coverage numbers for a job");
    }
}
