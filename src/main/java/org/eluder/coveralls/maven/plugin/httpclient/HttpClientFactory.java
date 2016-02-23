package org.eluder.coveralls.maven.plugin.httpclient;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.maven.settings.Proxy;
import org.codehaus.plexus.util.StringUtils;
import org.eluder.coveralls.maven.plugin.util.UrlUtils;
import org.eluder.coveralls.maven.plugin.util.Wildcards;

class HttpClientFactory {

    private static final int DEFAULT_CONNECTION_TIMEOUT = 10000;
    private static final int DEFAULT_SOCKET_TIMEOUT = 60000;

    private final String targetUrl;

    private final HttpClientBuilder hcb = HttpClientBuilder.create();
    private final RequestConfig.Builder rcb = RequestConfig.custom()
            .setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT)
            .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT);

    public HttpClientFactory(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public HttpClientFactory proxy(Proxy proxy) {
        if (proxy != null && isProxied(targetUrl, proxy)) {
            rcb.setProxy(new HttpHost(proxy.getHost(), proxy.getPort(), proxy.getProtocol()));
            if (StringUtils.isNotBlank(proxy.getUsername())) {
                CredentialsProvider cp = new BasicCredentialsProvider();
                cp.setCredentials(
                        new AuthScope(proxy.getHost(), proxy.getPort()),
                        new UsernamePasswordCredentials(proxy.getUsername(), proxy.getPassword())
                );
                hcb.setDefaultCredentialsProvider(cp);
            }
        }
        return this;
    }

    public HttpClient create() {
        return hcb.setDefaultRequestConfig(rcb.build()).build();
    }

    private boolean isProxied(final String url, final Proxy proxy) {
        if (StringUtils.isNotBlank(proxy.getNonProxyHosts())) {
            String host = UrlUtils.create(url).getHost();
            String[] excludes = proxy.getNonProxyHosts().split("\\|");
            for (String exclude : excludes) {
                if (StringUtils.isNotBlank(exclude) && Wildcards.matches(host, exclude.trim())) {
                    return false;
                }
            }
        }
        return true;
    }
}
