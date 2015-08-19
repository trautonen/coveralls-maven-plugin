package org.eluder.coveralls.maven.plugin.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5SumInputStream extends FilterInputStream {
  private final MessageDigest digest;
  private static final int BYTE_MASK = 0xFF;
  public MD5SumInputStream(final InputStream in) throws NoSuchAlgorithmException {
    super(in);
    digest = MessageDigest.getInstance("MD5");
  }
  @Override
  public int read() throws IOException {
    int ret = super.read();
    if (ret > -1) {
      digest.update((byte) (ret & BYTE_MASK));
    }
    return ret;
  }
  @Override
  public int read(final byte[] buff, final int off, final int len) throws IOException {
    int ret = super.read(buff, off, len);
    if (ret > 0) {
      digest.update(buff, off, ret);
    }
    return ret;
  }
  public String digest() {
      byte[] ret = digest.digest();
      StringBuilder sb = new StringBuilder(ret.length * 2);
      for (byte b : ret) {
         sb.append(String.format("%02x", b & BYTE_MASK));
      }
      return sb.toString();
  }
}
