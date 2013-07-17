package org.eluder.coveralls.maven.plugin.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

public class CircleTest {

    private Map<String, String> env() {
        Map<String, String> env = new HashMap<String, String>();
        env.put("CIRCLECI", "true");
        env.put("CIRCLE_BUILD_NUM", "build123");
        env.put("CIRCLE_BRANCH", "master");
        env.put("CIRCLE_SHA1", "a3562fgcd2");
        return env;
    }
    
    @Test
    public void testIsSelectedForNothing() {
        assertFalse(new Circle(new HashMap<String, String>()).isSelected());
    }
    
    @Test
    public void testIsSelectedForCircle() {
        assertTrue(new Circle(env()).isSelected());
    }
    
    @Test
    public void testGetName() {
        assertEquals("circleci", new Circle(env()).getName());
    }
    
    @Test
    public void testGetBuildNumber() {
        assertEquals("build123", new Circle(env()).getBuildNumber());
    }
    
    @Test
    public void testGetBranch() {
        assertEquals("master", new Circle(env()).getBranch());
    }
    
    @Test
    public void testGetEnvironment() {
        Properties properties = new Circle(env()).getEnvironment();
        assertEquals(3, properties.size());
        assertEquals("build123", properties.getProperty("circleci_build_num"));
        assertEquals("master", properties.getProperty("branch"));
        assertEquals("a3562fgcd2", properties.getProperty("commit_sha"));
    }
}
