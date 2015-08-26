package org.eluder.coveralls.maven.plugin.util;

/*
 * #[license]
 * coveralls-maven-plugin
 * %%
 * Copyright (C) 2013 - 2015 Tapio Rautonen
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
