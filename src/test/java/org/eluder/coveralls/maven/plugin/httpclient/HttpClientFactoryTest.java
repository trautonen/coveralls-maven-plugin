package org.eluder.coveralls.maven.plugin.httpclient;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.maven.settings.Proxy;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class HttpClientFactoryTest {

    private final int PROXY_PORT = 9797;
    private final int TARGET_PORT = 9696;
    private final String TARGET_URL = "http://localhost:" + TARGET_PORT;

    @Rule
    public WireMockRule targetServer = new WireMockRule(TARGET_PORT);

    @Rule
    public WireMockRule proxyServer = new WireMockRule(PROXY_PORT);


    @Test
    public void testSimpleRequest() throws Exception {
        targetServer.stubFor(get(urlMatching(".*")).willReturn(aResponse().withBody("Hello World!")));

        HttpClient client = new HttpClientFactory(TARGET_URL).create();
        String body = EntityUtils.toString(client.execute(new HttpGet(TARGET_URL)).getEntity());

        Assert.assertEquals("Hello World!", body);
    }

    @Test
    public void testUnAuthorizedProxyRequest() throws Exception {
        targetServer.stubFor(get(urlMatching(".*")).willReturn(aResponse().withBody("Hello World!")));

        proxyServer.stubFor(get(urlMatching(".*")).willReturn(aResponse().withBody("Hello Proxy!")));

        Proxy proxy = new Proxy();
        proxy.setHost("localhost");
        proxy.setPort(PROXY_PORT);
        proxy.setProtocol("http");

        HttpClient client = new HttpClientFactory(TARGET_URL).proxy(proxy).create();
        String body = EntityUtils.toString(client.execute(new HttpGet(TARGET_URL)).getEntity());

        Assert.assertEquals("Hello Proxy!", body);
    }

    @Test
    public void testAuthorixedProxyRequest() throws Exception {
        targetServer.stubFor(get(urlMatching(".*")).willReturn(aResponse().withBody("Hello World!")));

        proxyServer.stubFor(get(urlMatching(".*")).withHeader("Proxy-Authorization", matching("Basic Zm9vOmJhcg=="))
                .willReturn(aResponse().withBody("Hello Proxy!"))
                .atPriority(1));
        proxyServer.stubFor(any(urlMatching(".*"))
                .willReturn(aResponse().withStatus(407).withHeader("Proxy-Authenticate", "Basic"))
                .atPriority(2));

        Proxy proxy = new Proxy();
        proxy.setHost("localhost");
        proxy.setPort(PROXY_PORT);
        proxy.setProtocol("http");
        proxy.setUsername("foo");
        proxy.setPassword("bar");

        HttpClient client = new HttpClientFactory(TARGET_URL).proxy(proxy).create();
        String body = EntityUtils.toString(client.execute(new HttpGet(TARGET_URL)).getEntity());

        Assert.assertEquals("Hello Proxy!", body);
    }

    @Test
    public void testNonProxiedHostRequest() throws Exception {
        targetServer.stubFor(get(urlMatching(".*")).willReturn(aResponse().withBody("Hello World!")));

        proxyServer.stubFor(get(urlMatching(".*")).willReturn(aResponse().withBody("Hello Proxy!")));

        Proxy proxy = new Proxy();
        proxy.setHost("localhost");
        proxy.setPort(PROXY_PORT);
        proxy.setProtocol("http");
        proxy.setNonProxyHosts("localhost|example.com");

        HttpClient client = new HttpClientFactory(TARGET_URL).proxy(proxy).create();
        String body = EntityUtils.toString(client.execute(new HttpGet(TARGET_URL)).getEntity());

        Assert.assertEquals("Hello World!", body);
    }
}