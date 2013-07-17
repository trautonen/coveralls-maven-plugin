package org.eluder.coveralls.maven.plugin.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

public class AbstractServiceSetupTest {

    @Test
    public void testGetMissingProperty() {
        AbstractServiceSetup serviceSetup = create(new HashMap<String, String>());
        assertNull(serviceSetup.getProperty("property"));
    }
    
    @Test
    public void testGetProperty() {
        Map<String, String> env = new HashMap<String, String>();
        env.put("CI_NAME", "bamboo");
        assertEquals("bamboo", create(env).getProperty("CI_NAME"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddPropertyWithoutName() {
        create(new HashMap<String, String>()).addProperty(new Properties(), null, "value");
    }
    
    @Test
    public void testAddPropertyWithoutValue() {
        Properties properties = new Properties();
        create(new HashMap<String, String>()).addProperty(properties, "prop", " ");
        assertNull(properties.getProperty("prop"));
    }
    
    @Test
    public void testAddPropertyWithValue() {
        Properties properties = new Properties();
        create(new HashMap<String, String>()).addProperty(properties, "prop", "value");
        assertEquals("value", properties.getProperty("prop"));
    }
    
    @Test
    public void testGetDefaultValues() {
        AbstractServiceSetup serviceSetup = create(new HashMap<String, String>());
        assertNull(serviceSetup.getName());
        assertNull(serviceSetup.getJobId());
        assertNull(serviceSetup.getBuildNumber());
        assertNull(serviceSetup.getBuildUrl());
        assertNull(serviceSetup.getBranch());
        assertNull(serviceSetup.getPullRequest());
        assertNull(serviceSetup.getEnvironment());
    }
    
    private AbstractServiceSetup create(final Map<String, String> env) {
        return new AbstractServiceSetup(env) {
            @Override
            public boolean isSelected() {
                return true;
            }
            @Override
            public String getName() {
                return getProperty("CI_NAME");
            }
        };
    }
    
}
