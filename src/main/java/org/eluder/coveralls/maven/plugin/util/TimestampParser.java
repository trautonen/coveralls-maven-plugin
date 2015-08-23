package org.eluder.coveralls.maven.plugin.util;

import org.codehaus.plexus.util.StringUtils;
import org.eluder.coveralls.maven.plugin.ProcessingException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampParser {

    public static final String DEFAULT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    private final DateFormat format;

    public TimestampParser(final String format) {
        try {
            if (StringUtils.isNotBlank(format)) {
                this.format = new SimpleDateFormat(format);
            } else {
                this.format = new SimpleDateFormat(DEFAULT_FORMAT);
            }
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid timestamp format \"" + format + "\"", ex);
        }
    }

    public synchronized Date parse(final String timestamp) throws ProcessingException {
        if (StringUtils.isBlank(timestamp)) {
            return null;
        }
        try {
            return format.parse(timestamp);
        } catch (ParseException ex) {
            throw new ProcessingException("Unable to parse timestamp \"" + timestamp + "\"", ex);
        }
    }

}
