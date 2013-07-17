package org.eluder.coveralls.maven.plugin.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

public class JenkinsTest {

    private Map<String, String> env() {
        Map<String, String> env = new HashMap<String, String>();
        env.put("JENKINS_URL", "http://company.com/jenkins");
        env.put("BUILD_NUMBER", "build123");
        env.put("BUILD_URL", "http://company.com/jenkins/build123");
        env.put("GIT_BRANCH", "master");
        env.put("GIT_COMMIT", "a3562fgcd2");
        return env;
    }
    
    @Test
    public void testIsSelectedForNothing() {
        assertFalse(new Jenkins(new HashMap<String, String>()).isSelected());
    }
    
    @Test
    public void testIsSelectedForJenkins() {
        assertTrue(new Jenkins(env()).isSelected());
    }
    
    @Test
    public void testGetName() {
        assertEquals("jenkins", new Jenkins(env()).getName());
    }
    
    @Test
    public void testGetBuildNumber() {
        assertEquals("build123", new Jenkins(env()).getBuildNumber());
    }
    
    @Test
    public void testGetBuildUrl() {
        assertEquals("http://company.com/jenkins/build123", new Jenkins(env()).getBuildUrl());
    }
    
    @Test
    public void testGetBranch() {
        assertEquals("master", new Jenkins(env()).getBranch());
    }
    
    @Test
    public void testGetEnvironment() {
        Properties properties = new Jenkins(env()).getEnvironment();
        assertEquals(4, properties.size());
        assertEquals("build123", properties.getProperty("jenkins_build_num"));
        assertEquals("http://company.com/jenkins/build123", properties.getProperty("jenkins_build_url"));
        assertEquals("master", properties.getProperty("branch"));
        assertEquals("a3562fgcd2", properties.getProperty("commit_sha"));
    }
}
