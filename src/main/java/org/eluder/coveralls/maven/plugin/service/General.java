package org.eluder.coveralls.maven.plugin.service;

import java.util.Map;

/**
 * General implementation for any continuous integration service that provides the required
 * environment properties.
 */
public class General extends AbstractServiceSetup {

    public static final String CI_NAME = "CI_NAME";
    public static final String CI_BUILD_NUMBER = "CI_BUILD_NUMBER";
    public static final String CI_BUILD_URL = "CI_BUILD_URL";
    public static final String CI_BRANCH = "CI_BRANCH";
    public static final String CI_PULL_REQUEST = "CI_PULL_REQUEST";
    
    public General(final Map<String, String> env) {
        super(env);
    }

    @Override
    public boolean isSelected() {
        return (getProperty(CI_NAME) != null);
    }

    @Override
    public String getName() {
        return getProperty(CI_NAME);
    }

    @Override
    public String getBuildNumber() {
        return getProperty(CI_BUILD_NUMBER);
    }
    
    @Override
    public String getBuildUrl() {
        return getProperty(CI_BUILD_URL);
    }
    
    @Override
    public String getBranch() {
        return getProperty(CI_BRANCH);
    }
    
    @Override
    public String getPullRequest() {
        return getProperty(CI_PULL_REQUEST);
    }
}
