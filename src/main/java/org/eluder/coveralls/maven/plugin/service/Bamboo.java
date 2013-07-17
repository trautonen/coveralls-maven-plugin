package org.eluder.coveralls.maven.plugin.service;

import java.util.Map;

/**
 * Service implementation for Atlassian Bamboo.
 * <p>
 * https://www.atlassian.com/software/bamboo/
 */
public class Bamboo extends AbstractServiceSetup {

    public static final String BAMBOO_NAME = "bamboo";
    public static final String BAMBOO_BUILD_NUMBER = "bamboo.buildNumber";
    public static final String BAMBOO_BUILD_URL = "bamboo.buildResultsUrl";
    public static final String BAMBOO_BRANCH = "bamboo.repository.git.branch";
    
    public Bamboo(final Map<String, String> env) {
        super(env);
    }

    @Override
    public boolean isSelected() {
        return (getProperty(BAMBOO_BUILD_NUMBER) != null);
    }
    
    @Override
    public String getName() {
        return BAMBOO_NAME;
    }
    
    @Override
    public String getBuildNumber() {
        return getProperty(BAMBOO_BUILD_NUMBER);
    }
    
    @Override
    public String getBuildUrl() {
        return getProperty(BAMBOO_BUILD_URL);
    }
    
    @Override
    public String getBranch() {
        return getProperty(BAMBOO_BRANCH);
    }
}
