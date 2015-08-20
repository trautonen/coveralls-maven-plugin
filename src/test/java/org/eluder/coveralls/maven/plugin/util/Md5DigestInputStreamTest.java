package org.eluder.coveralls.maven.plugin.util;

import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertEquals;

public class Md5DigestInputStreamTest {

    @Test
    public void testRead() throws Exception {
        byte[] data = new byte[] { (byte) 0xAA, (byte) 0xBB, (byte) 0xCC, (byte) 0xDD };
        try (Md5DigestInputStream is = new Md5DigestInputStream(new ByteArrayInputStream(data))) {
            assertEquals(0xAA, is.read());
            assertEquals(0xBB, is.read());
            assertEquals(0xCC, is.read());
            assertEquals(0xDD, is.read());
            assertEquals(-1, is.read());
            assertEquals("CA6FFBF95B47864FD4E73F2601326304", is.getDigestHex());
        }
    }

    @Test
    public void testReadArray() throws Exception {
        byte[] data = new byte[] { (byte) 0xAA, (byte) 0xBB, (byte) 0xCC, (byte) 0xDD };
        try (Md5DigestInputStream is = new Md5DigestInputStream(new ByteArrayInputStream(data))) {
            byte[] buff = new byte[5];
            assertEquals(4, is.read(buff));
            assertEquals(-1, is.read());
            for (int i = 0; i < data.length; i++) {
                assertEquals(data[i], buff[i]);
            }
            assertEquals("CA6FFBF95B47864FD4E73F2601326304", is.getDigestHex());
        }
    }
}
