package org.eluder.coveralls.maven.plugin.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class ValidationExceptionTest {
    
    private static final String MESSAGE = "message";
    private static final RuntimeException CAUSE = new RuntimeException();
    
    @Test
    public void testException() {
        ValidationException exception = new ValidationException();
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }
    
    @Test
    public void testExceptionWithMessage() {
        ValidationException exception = new ValidationException(MESSAGE);
        assertEquals(MESSAGE, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void testExceptionWithCause() {
        ValidationException exception = new ValidationException(CAUSE);
        assertEquals(CAUSE.toString(), exception.getMessage());
        assertSame(CAUSE, exception.getCause());
    }
    
    @Test
    public void testExceptionWithMessageAndCause() {
        ValidationException exception = new ValidationException(MESSAGE, CAUSE);
        assertEquals(MESSAGE, exception.getMessage());
        assertSame(CAUSE, exception.getCause());

    }
}
