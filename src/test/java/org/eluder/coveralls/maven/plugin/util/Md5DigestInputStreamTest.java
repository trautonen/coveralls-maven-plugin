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
