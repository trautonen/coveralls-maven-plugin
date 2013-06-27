package org.eluder.coveralls.maven.plugin.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TravisTest {

    private boolean travis;
    
    @Before
    public void init() {
        travis = Boolean.valueOf(System.getenv("TRAVIS")).booleanValue();
    }
    
    @Test
    public void testIsSelected() {
        Travis service = new Travis();
        assertTrue(service.isSelected("travis-ci"));
        assertTrue(service.isSelected("travis-pro"));
        assertFalse(service.isSelected("travis"));
        assertFalse(service.isSelected(""));
        assertFalse(service.isSelected(null));
    }
    
    @Test
    public void testValues() {
        Travis service = new Travis();
        if (travis) {
            assertNotNull(service.getServiceJobId());
            assertNotNull(service.getBranch());
        } else {
            assertNull(service.getServiceJobId());
            assertNull(service.getBranch());
        }
        assertNull(service.getRepoToken());
    }
}
