package org.eluder.coveralls.maven.plugin.util;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;

import org.junit.Test;

public class MD5SumInputStreamTest {

    @Test
    public void testRead() throws Exception {
        byte[] data = new byte[] { (byte)0xAA, (byte)0xBB, (byte)0xCC, (byte)0xDD };
        try (MD5SumInputStream is = new MD5SumInputStream(new ByteArrayInputStream(data))) {
            assertEquals(0xAA, is.read());
            assertEquals(0xBB, is.read());
            assertEquals(0xCC, is.read());
            assertEquals(0xDD, is.read());
            assertEquals(-1, is.read());
            assertEquals("ca6ffbf95b47864fd4e73f2601326304", is.digest());
        }
    }

    @Test
    public void testReadArray() throws Exception {
        byte[] data = new byte[] { (byte)0xAA, (byte)0xBB, (byte)0xCC, (byte)0xDD };
        try (MD5SumInputStream is = new MD5SumInputStream(new ByteArrayInputStream(data))) {
            byte[] buff = new byte [5];
            assertEquals(4, is.read(buff));
            assertEquals(-1, is.read());
            for (int i = 0; i < data.length; i++) {
                assertEquals(data[i], buff[i]);
            }
            assertEquals("ca6ffbf95b47864fd4e73f2601326304", is.digest());
        }
    }
}
