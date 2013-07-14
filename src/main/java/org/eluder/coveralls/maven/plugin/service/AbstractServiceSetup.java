package org.eluder.coveralls.maven.plugin.service;

import java.util.Map;
import java.util.Properties;

import org.codehaus.plexus.util.StringUtils;

public abstract class AbstractServiceSetup implements ServiceSetup {

    private final String serviceName;
    private final Map<String, String> env;
    
    public AbstractServiceSetup(final String serviceName, final Map<String, String> env) {
        this.serviceName = serviceName;
        this.env = env;
    }
    
    @Override
    public String getName() {
        return serviceName;
    }

    @Override
    public String getJobId() {
        return null;
    }

    @Override
    public String getBuildNumber() {
        return null;
    }

    @Override
    public String getBuildUrl() {
        return null;
    }

    @Override
    public String getBranch() {
        return null;
    }

    @Override
    public String getPullRequest() {
        return null;
    }

    @Override
    public Properties getEnvironment() {
        return null;
    }
    
    protected final String getProperty(final String name) {
        return env.get(name);
    }
    
    protected final void addProperty(final Properties properties, final String name, final String value) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("name must be defined");
        }
        if (StringUtils.isNotBlank(value)) {
            properties.setProperty(name, value);
        }
    }
}
