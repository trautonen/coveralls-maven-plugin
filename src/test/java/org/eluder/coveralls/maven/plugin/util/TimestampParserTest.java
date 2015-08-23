package org.eluder.coveralls.maven.plugin.util;

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