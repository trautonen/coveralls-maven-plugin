package org.eluder.coveralls.maven.plugin.validation;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.apache.maven.plugin.logging.Log;
import org.eluder.coveralls.maven.plugin.validation.ValidationError.Level;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ValidationErrorsTest {

    @Mock
    private Log logMock;
    
    @Test(expected = ValidationException.class)
    public void testThrowOrInformWithError() {
        createValidationErrors(new ValidationError(Level.ERROR, "message")).throwOrInform(logMock);
    }
    
    @Test
    public void testThrowOrInformWithWarnings() {
        createValidationErrors(new ValidationError(Level.WARN, "error1"), new ValidationError(Level.WARN, "error2")).throwOrInform(logMock);
        verify(logMock, times(2)).warn(any(CharSequence.class));
    }
    
    private ValidationErrors createValidationErrors(final ValidationError... errors) {
        ValidationErrors validationErrors = new ValidationErrors();
        for (ValidationError error : errors) {
            validationErrors.add(error);
        }
        return validationErrors;
    }
}
