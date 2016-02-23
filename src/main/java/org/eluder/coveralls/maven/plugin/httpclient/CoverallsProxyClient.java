package org.eluder.coveralls.maven.plugin.httpclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.maven.settings.Proxy;

public class CoverallsProxyClient extends CoverallsClient {

    public CoverallsProxyClient(final String coverallsUrl, final Proxy proxy) {
        super(coverallsUrl, new HttpClientFactory(coverallsUrl).proxy(proxy).create(), new ObjectMapper());
    }

}
