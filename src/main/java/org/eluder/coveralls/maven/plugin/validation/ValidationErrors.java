package org.eluder.coveralls.maven.plugin.validation;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.logging.Log;
import org.eluder.coveralls.maven.plugin.validation.ValidationError.Level;

public class ValidationErrors extends ArrayList<ValidationError> {

    public void throwOrInform(final Log log) {
        List<ValidationError> errors = filter(Level.ERROR);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors.get(0).getMessage());
        }
        for (ValidationError error : filter(Level.WARN)) {
            log.warn(error.getMessage());
        }
    }
    
    private List<ValidationError> filter(final Level level) {
        List<ValidationError> filtered = new ArrayList<ValidationError>();
        for (ValidationError error : this) {
            if (level.equals(error.getLevel())) {
                filtered.add(error);
            }
        }
        return filtered;
    }
}
