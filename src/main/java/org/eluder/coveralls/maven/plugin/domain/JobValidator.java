package org.eluder.coveralls.maven.plugin.domain;

import org.codehaus.plexus.util.StringUtils;
import org.eluder.coveralls.maven.plugin.service.Travis;

public class JobValidator {

    private final Job job;

    public JobValidator(final Job job) {
        if (job == null) {
            throw new IllegalArgumentException("Job must be defined");
        }
        this.job = job;
    }
    
    public void validate() {
        repoTokenOrTravis();
        git();
    }
    
    private void repoTokenOrTravis() {
        if (hasValue(job.getRepoToken())) {
            return;
        }
        if (new Travis().isSelected(job.getServiceName()) && hasValue(job.getServiceJobId())) {
            return;
        }
        throw new IllegalArgumentException("Either repository token or travis service with job id must be defined");
    }
    
    private void git() {
        if (job.getGit() == null) {
            return;
        }
        if (hasValue(job.getGit().getHead().getId())) {
            return;
        }
        throw new IllegalArgumentException("Commit id for HEAD must be defined");
    }
    
    private boolean hasValue(final String value) {
        return StringUtils.isNotBlank(value);
    }
}
