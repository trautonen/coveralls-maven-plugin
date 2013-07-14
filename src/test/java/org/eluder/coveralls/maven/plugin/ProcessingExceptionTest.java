package org.eluder.coveralls.maven.plugin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class ProcessingExceptionTest {

    private static final String MESSAGE = "message";
    private static final RuntimeException CAUSE = new RuntimeException();
    
    @Test
    public void testException() {
        ProcessingException exception = new ProcessingException();
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }
    
    @Test
    public void testExceptionWithMessage() {
        ProcessingException exception = new ProcessingException(MESSAGE);
        assertEquals(MESSAGE, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void testExceptionWithCause() {
        ProcessingException exception = new ProcessingException(CAUSE);
        assertEquals(CAUSE.toString(), exception.getMessage());
        assertSame(CAUSE, exception.getCause());
    }
    
    @Test
    public void testExceptionWithMessageAndCause() {
        ProcessingException exception = new ProcessingException(MESSAGE, CAUSE);
        assertEquals(MESSAGE, exception.getMessage());
        assertSame(CAUSE, exception.getCause());

    }
    
}
