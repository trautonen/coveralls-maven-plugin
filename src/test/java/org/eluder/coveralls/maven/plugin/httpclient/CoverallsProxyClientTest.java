package org.eluder.coveralls.maven.plugin.httpclient;

import org.apache.maven.settings.Proxy;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class CoverallsProxyClientTest {

    @Test
    public void testConstructorWithoutProxy() {
        assertNotNull(new CoverallsProxyClient("http://test.com/coveralls", null));
    }

    @Test
    public void testConstructorWithProxy() {
        Proxy proxy = new Proxy();
        proxy.setHost("localhost");
        proxy.setPort(8080);
        proxy.setProtocol("http");

        assertNotNull(new CoverallsProxyClient("http://test.com/coveralls", proxy));
    }

}
