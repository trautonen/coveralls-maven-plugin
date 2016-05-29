package org.eluder.coveralls.maven.plugin.httpclient;

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

    HttpClientFactory(final String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public HttpClientFactory proxy(final Proxy proxy) {
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
