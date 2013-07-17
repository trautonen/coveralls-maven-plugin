package org.eluder.coveralls.maven.plugin.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class GeneralTest {

    private Map<String, String> env() {
        Map<String, String> env = new HashMap<String, String>();
        env.put("CI_NAME", "ci_service");
        env.put("CI_BUILD_NUMBER", "build123");
        env.put("CI_BUILD_URL", "http://ci.com/build123");
        env.put("CI_BRANCH", "master");
        env.put("CI_PULL_REQUEST", "pull10");
        return env;
    }
    
    @Test
    public void testIsSelectedForNothing() {
        assertFalse(new General(new HashMap<String, String>()).isSelected());
    }
    
    @Test
    public void testIsSelectedForCi() {
        assertTrue(new General(env()).isSelected());
    }
    
    @Test
    public void testGetName() {
        assertEquals("ci_service", new General(env()).getName());
    }
    
    @Test
    public void testGetBuildNumber() {
        assertEquals("build123", new General(env()).getBuildNumber());
    }
    
    @Test
    public void testGetBuildUrl() {
        assertEquals("http://ci.com/build123", new General(env()).getBuildUrl());
    }
    
    @Test
    public void testGetBranch() {
        assertEquals("master", new General(env()).getBranch());
    }
    
    @Test
    public void testGetPullRequest() {
        assertEquals("pull10", new General(env()).getPullRequest());
    }
}
