package org.eluder.coveralls.maven.plugin.service;

import java.util.Map;
import java.util.Properties;

/**
 * Service implementation for Jenkins.
 * <p>
 * http://jenkins-ci.org/
 */
public class Jenkins extends AbstractServiceSetup {

    public static final String JENKINS_NAME = "jenkins";
    public static final String JENKINS_URL = "JENKINS_URL";
    public static final String JENKINS_BUILD_NUMBER = "BUILD_NUMBER";
    public static final String JENKINS_BUILD_URL = "BUILD_URL";
    public static final String JENKINS_BRANCH = "GIT_BRANCH";
    public static final String JENKINS_COMMIT = "GIT_COMMIT";
    
    public Jenkins(final Map<String, String> env) {
        super(env);
    }

    @Override
    public boolean isSelected() {
        return (getProperty(JENKINS_URL) != null);
    }
    
    @Override
    public String getName() {
        return JENKINS_NAME;
    }
    
    @Override
    public String getBuildNumber() {
        return getProperty(JENKINS_BUILD_NUMBER);
    }
    
    @Override
    public String getBuildUrl() {
        return getProperty(JENKINS_BUILD_URL);
    }
    
    @Override
    public String getBranch() {
        return getProperty(JENKINS_BRANCH);
    }
    
    @Override
    public Properties getEnvironment() {
        Properties environment = new Properties();
        addProperty(environment, "jenkins_build_num", getProperty(JENKINS_BUILD_NUMBER));
        addProperty(environment, "jenkins_build_url", getProperty(JENKINS_BUILD_URL));
        addProperty(environment, "branch", getProperty(JENKINS_BRANCH));
        addProperty(environment, "commit_sha", getProperty(JENKINS_COMMIT));
        return environment;
    }
}
