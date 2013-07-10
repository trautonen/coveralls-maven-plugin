package org.eluder.coveralls.maven.plugin.logging;

import java.io.File;

import org.apache.maven.plugin.logging.Log;

public class DryRunLogger implements Logger {

    private final boolean dryRun;
    private final File coverallsFile;
    
    public DryRunLogger(final boolean dryRun, final File coverallsFile) {
        if (coverallsFile == null) {
            throw new IllegalArgumentException("coverallsFile must be defined");
        }
        this.dryRun = dryRun;
        this.coverallsFile = coverallsFile;
    }

    @Override
    public Position getPosition() {
        return Position.AFTER;
    }
    
    @Override
    public void log(final Log log) {
        if (dryRun) {
            log.info("Dry run enabled, Coveralls report will NOT be submitted to API");
            log.info(coverallsFile.length() + " bytes of data was recorded in " + coverallsFile.getAbsolutePath());
        }
    }
    
}
