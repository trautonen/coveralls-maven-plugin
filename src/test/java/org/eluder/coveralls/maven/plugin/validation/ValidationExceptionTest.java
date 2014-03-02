package org.eluder.coveralls.maven.plugin.validation;

/*
 * #[license]
 * coveralls-maven-plugin
 * %%
 * Copyright (C) 2013 - 2014 Tapio Rautonen
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * %[license]
 */

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
