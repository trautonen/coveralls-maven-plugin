package org.eluder.coveralls.maven.plugin.logging;

import org.apache.maven.plugin.logging.Log;
import org.eluder.coveralls.maven.plugin.domain.Job;

public class JobLogger implements Logger {

    private final Job job;
    
    public JobLogger(final Job job) {
        if (job == null) {
            throw new IllegalArgumentException("Job must be defined");
        }
        this.job = job;
    }

    @Override
    public Position getPosition() {
        return Position.BEFORE;
    }
    
    @Override
    public void log(final Log log) {
        String starting = "Starting Coveralls job";
        if (job.getServiceName() != null) {
            starting += " for " + job.getServiceName();
            if (job.getServiceJobId() != null) {
                starting += " (" + job.getServiceJobId() + ")";
            }
        }
        log.info(starting);
        
        if (job.getRepoToken() != null) {
            log.info("Using repository token <secret>");
        }
        
        if (job.getGit() != null) {
            String commit = job.getGit().getHead().getId();
            String branch = job.getGit().getBranch();
            log.info("Git commit " + commit.substring(0, 7) + " in " + branch);
        }
    }
    
}
