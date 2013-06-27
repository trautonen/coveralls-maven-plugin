package org.eluder.coveralls.maven.plugin;

import org.codehaus.plexus.util.StringUtils;
import org.eluder.coveralls.maven.plugin.service.ServiceSetup;

public final class Environment {
    
    private final AbstractCoverallsMojo mojo;
    private final Iterable<ServiceSetup> services;

    public Environment(final AbstractCoverallsMojo mojo, final Iterable<ServiceSetup> services) {
        if (mojo == null) {
            throw new IllegalArgumentException("mojo must be defined");
        }
        if (services == null) {
            throw new IllegalArgumentException("services must be defined");
        }
        this.mojo = mojo;
        this.services = services;
    }
    
    public void setup() {
        for (ServiceSetup service : services) {
            if (service.isSelected(mojo.serviceName)) {
                setupEnvironment(service);
                break;
            }
        }
    }
    
    private void setupEnvironment(final ServiceSetup service) {
        String serviceJobId = service.getServiceJobId();
        if (StringUtils.isNotBlank(serviceJobId)) {
            mojo.serviceJobId = serviceJobId;
        }
        
        String repoToken = service.getRepoToken();
        if (StringUtils.isNotBlank(repoToken)) {
            mojo.repoToken = repoToken;
        }
        
        String branch = service.getBranch();
        if (StringUtils.isNotBlank(branch)) {
            mojo.branch = branch;
        }
    }
}
