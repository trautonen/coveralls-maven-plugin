package org.eluder.coveralls.maven.plugin.util;

/*
 * #[license]
 * coveralls-maven-plugin
 * %%
 * Copyright (C) 2013 - 2016 Tapio Rautonen
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

import org.eluder.coveralls.maven.plugin.ProcessingException;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TimestampParserTest {

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() {
        new TimestampParser("scsscdfsd");
    }

    @Test
    public void testParseEpochMillis() throws Exception {
        String format = TimestampParser.EPOCH_MILLIS;
        long time = System.currentTimeMillis();
        Date parsed = new TimestampParser(format).parse(String.valueOf(time));

        assertEquals(time, parsed.getTime());
    }

    @Test
    public void testParseSimpleFormat() throws Exception {
        String format = "yyyy-MM-dd";
        Date parsed = new TimestampParser(format).parse("2015-08-20");
        String formatted = new SimpleDateFormat(format).format(parsed);

        assertEquals("2015-08-20", formatted);
    }

    @Test
    public void testParseDefaultFormat() throws Exception {
        String format = TimestampParser.DEFAULT_FORMAT;
        Date parsed = new TimestampParser(null).parse("2015-08-20T20:10:00Z");
        String formatted = new SimpleDateFormat(format).format(parsed);

        assertEquals("2015-08-20T20:10:00Z", formatted);
    }

    @Test
    public void testParseNull() throws Exception {
        Date parsed = new TimestampParser(null).parse(null);

        assertNull(parsed);
    }

    @Test(expected = ProcessingException.class)
    public void testParseInvalidTimestamp() throws Exception {
        new TimestampParser(null).parse("2015-08-20");
    }
}
