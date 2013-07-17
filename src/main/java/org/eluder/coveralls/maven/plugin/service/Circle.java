package org.eluder.coveralls.maven.plugin.service;

import java.util.Map;
import java.util.Properties;

/**
 * Service implementation for CircleCI.
 * <p>
 * https://circleci.com/
 */
public class Circle extends AbstractServiceSetup {

    public static final String CIRCLE_NAME = "circleci";
    public static final String CIRCLE = "CIRCLECI";
    public static final String CIRCLE_BUILD_NUMBER  = "CIRCLE_BUILD_NUM";
    public static final String CIRCLE_BRANCH = "CIRCLE_BRANCH";
    public static final String CIRCLE_COMMIT = "CIRCLE_SHA1";
    
    public Circle(final Map<String, String> env) {
        super(env);
    }

    @Override
    public boolean isSelected() {
        return (getProperty(CIRCLE) != null);
    }
    
    @Override
    public String getName() {
        return CIRCLE_NAME;
    }
    
    @Override
    public String getBuildNumber() {
        return getProperty(CIRCLE_BUILD_NUMBER);
    }
    
    @Override
    public String getBranch() {
        return getProperty(CIRCLE_BRANCH);
    }
    
    @Override
    public Properties getEnvironment() {
        Properties environment = new Properties();
        addProperty(environment, "circleci_build_num", getProperty(CIRCLE_BUILD_NUMBER));
        addProperty(environment, "branch", getProperty(CIRCLE_BRANCH));
        addProperty(environment, "commit_sha", getProperty(CIRCLE_COMMIT));
        return environment;
    }
}
