package org.eluder.coveralls.maven.plugin.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class BambooTest {

    private Map<String, String> env() {
        Map<String, String> env = new HashMap<String, String>();
        env.put("bamboo.buildNumber", "build123");
        env.put("bamboo.buildResultsUrl", "http://company.com/bamboo/build123");
        env.put("bamboo.repository.git.branch", "master");
        return env;
    }
    
    @Test
    public void testIsSelectedForNothing() {
        assertFalse(new Bamboo(new HashMap<String, String>()).isSelected());
    }
    
    @Test
    public void testIsSelectedForBamboo() {
        assertTrue(new Bamboo(env()).isSelected());
    }
    
    @Test
    public void testGetName() {
        assertEquals("bamboo", new Bamboo(env()).getName());
    }
    
    @Test
    public void testGetBuildNumber() {
        assertEquals("build123", new Bamboo(env()).getBuildNumber());
    }
    
    @Test
    public void testGetBuildUrl() {
        assertEquals("http://company.com/bamboo/build123", new Bamboo(env()).getBuildUrl());
    }
    
    @Test
    public void testGetBranch() {
        assertEquals("master", new Bamboo(env()).getBranch());
    }
    
}
