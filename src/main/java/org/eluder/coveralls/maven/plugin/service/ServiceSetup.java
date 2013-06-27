package org.eluder.coveralls.maven.plugin.service;

/**
 * Service specific mojo properties.
 */
public interface ServiceSetup {

    /**
     * @param name the service name
     * @return <code>true</code> if this service is selected, otherwise <code>false</code>
     */
    boolean isSelected(String name);
    
    /**
     * @return coveralls service job id, or <code>null</code> if not defined
     */
    String getServiceJobId();
    
    /**
     * @return coveralls repository token, or <code>null</code> if not defined
     */
    String getRepoToken();
    
    /**
     * @return git branch name, or <code>null</code> if not defined
     */
    String getBranch();
    
}
