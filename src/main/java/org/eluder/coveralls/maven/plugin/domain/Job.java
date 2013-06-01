package org.eluder.coveralls.maven.plugin.domain;

public class Job {

    private final String repoToken;
    private final String serviceName;
    private final String serviceJobId;
    private final Git git;
    
    public Job(final String repoToken, final String serviceName, final String serviceJobId, final Git git) {
        this.repoToken = repoToken;
        this.serviceName = serviceName;
        this.serviceJobId = serviceJobId;
        this.git = git;
    }

    public String getRepoToken() {
        return repoToken;
    }
    
    public String getServiceName() {
        return serviceName;
    }
    
    public String getServiceJobId() {
        return serviceJobId;
    }
    
    public Git getGit() {
        return git;
    }
    
    
}
