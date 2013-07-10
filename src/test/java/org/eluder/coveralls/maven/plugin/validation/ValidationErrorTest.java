package org.eluder.coveralls.maven.plugin.validation;

import static org.junit.Assert.assertEquals;

import org.eluder.coveralls.maven.plugin.validation.ValidationError.Level;
import org.junit.Test;

public class ValidationErrorTest {

    @Test(expected = IllegalArgumentException.class)
    public void testMissingLevel() {
        new ValidationError(null, "message");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMissingMessage() {
        new ValidationError(Level.ERROR, null);
    }
    
    @Test
    public void testToString() {
        ValidationError error = new ValidationError(Level.WARN, "message");
        assertEquals("WARN: message", error.toString());
    }
}
