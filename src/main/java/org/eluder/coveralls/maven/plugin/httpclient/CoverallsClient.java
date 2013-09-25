package org.eluder.coveralls.maven.plugin.httpclient;

/*
 * #[license]
 * coveralls-maven-plugin
 * %%
 * Copyright (C) 2013 Tapio Rautonen
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

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Provider;
import java.security.Security;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.codehaus.plexus.util.IOUtil;
import org.eluder.coveralls.maven.plugin.ProcessingException;
import org.eluder.coveralls.maven.plugin.domain.CoverallsResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CoverallsClient {

    static {
        for (Provider provider : Security.getProviders()) {
            if (provider.getName().startsWith("SunPKCS11")) {
                Security.removeProvider(provider.getName());
            }
        }
    }
    
    private static final String FILE_NAME = "coveralls.json";
    private static final ContentType MIME_TYPE = ContentType.create("application/octet-stream", "utf-8");
    
    private static final int DEFAULT_CONNECTION_TIMEOUT = 10000;
    private static final int DEFAULT_SOCKET_TIMEOUT = 60000;
    
    private final String coverallsUrl;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    public CoverallsClient(final String coverallsUrl) {
        this(coverallsUrl, createDefaultClient(), new ObjectMapper());
    }
    
    public CoverallsClient(final String coverallsUrl, final HttpClient httpClient, final ObjectMapper objectMapper) {
        this.coverallsUrl = coverallsUrl;
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }
    
    public CoverallsResponse submit(final File file) throws ProcessingException, IOException {
        HttpEntity entity = MultipartEntityBuilder.create()
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .addBinaryBody("json_file", file, MIME_TYPE, FILE_NAME)
                .build();
        HttpPost post = new HttpPost(coverallsUrl);
        post.setEntity(entity);
        HttpResponse response = httpClient.execute(post);
        return parseResponse(response);
    }
    
    private CoverallsResponse parseResponse(final HttpResponse response) throws ProcessingException, IOException {
        HttpEntity entity = response.getEntity();
        ContentType contentType = ContentType.getOrDefault(entity);
        InputStreamReader reader = new InputStreamReader(entity.getContent(), contentType.getCharset());
        try {
            return objectMapper.readValue(reader, CoverallsResponse.class);
        } catch (JsonProcessingException ex) {
            throw new ProcessingException(ex);
        } finally {
            IOUtil.close(reader);
        }
    }
    
    private static HttpClient createDefaultClient() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT)
                .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)
                .build();
        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .build();
    }
}
