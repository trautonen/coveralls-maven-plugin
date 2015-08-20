package org.eluder.coveralls.maven.plugin.util;

import javax.xml.bind.DatatypeConverter;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5DigestInputStream extends DigestInputStream {

    public Md5DigestInputStream(final InputStream stream) throws NoSuchAlgorithmException {
        super(stream, MessageDigest.getInstance("MD5"));
    }

    public String getDigestHex() {
        return DatatypeConverter.printHexBinary(getMessageDigest().digest());
    }
}
