package org.eluder.coveralls.maven.plugin.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class GeneralTest {

    @Test
    public void testIsSelectedForNothing() {
        assertFalse(new General(null, new HashMap<String, String>()).isSelected());
    }
    
    @Test
    public void testIsSelectedForCi() {
        Map<String, String> env = new HashMap<String, String>();
        env.put("CI_NAME", "bamboo");
        assertTrue(new General(null, env).isSelected());
    }
    
    @Test
    public void testGetName() {
        Map<String, String> env = new HashMap<String, String>();
        env.put("CI_NAME", "bamboo");
        assertEquals("bamboo", new General(null, env).getName());
    }
    
    @Test
    public void testGetBuildNumber() {
        Map<String, String> env = new HashMap<String, String>();
        env.put("CI_BUILD_NUMBER", "build123");
        assertEquals("build123", new General(null, env).getBuildNumber());
    }
    
    @Test
    public void testGetBuildUrl() {
        Map<String, String> env = new HashMap<String, String>();
        env.put("CI_BUILD_URL", "http://bamboo.ci/build123");
        assertEquals("http://bamboo.ci/build123", new General(null, env).getBuildUrl());
    }
    
    @Test
    public void testGetBranch() {
        Map<String, String> env = new HashMap<String, String>();
        env.put("CI_BRANCH", "master");
        assertEquals("master", new General(null, env).getBranch());
    }
    
    @Test
    public void testGetPullRequest() {
        Map<String, String> env = new HashMap<String, String>();
        env.put("CI_PULL_REQUEST", "pull10");
        assertEquals("pull10", new General(null, env).getPullRequest());
    }
}
