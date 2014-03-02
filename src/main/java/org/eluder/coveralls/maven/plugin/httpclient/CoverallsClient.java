package org.eluder.coveralls.maven.plugin.httpclient;

/*
 * #[license]
 * coveralls-maven-plugin
 * %%
 * Copyright (C) 2013 - 2014 Tapio Rautonen
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.codehaus.plexus.util.StringUtils;
import org.eluder.coveralls.maven.plugin.ProcessingException;
import org.eluder.coveralls.maven.plugin.domain.CoverallsResponse;

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
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(entity.getContent(), contentType.getCharset());
            CoverallsResponse cr = objectMapper.readValue(reader, CoverallsResponse.class);
            if (cr.isError()) {
                throw new ProcessingException(getResponseErrorMessage(response, cr.getMessage()));
            }
            return cr;
        } catch (JsonProcessingException ex) {
            throw new ProcessingException(getResponseErrorMessage(response, ex.getMessage()), ex);
        } catch (IOException ex) {
            throw new IOException(getResponseErrorMessage(response, ex.getMessage()), ex);
        } finally {
            IOUtil.close(reader);
        }
    }
    
    private String getResponseErrorMessage(final HttpResponse response, final String message) {
        int status = response.getStatusLine().getStatusCode();
        String reason = response.getStatusLine().getReasonPhrase();
        String errorMessage = "Report submission to Coveralls API failed with HTTP status " + status + ":";
        if (StringUtils.isNotBlank(reason)) {
            errorMessage += " " + reason;
        }
        if (StringUtils.isNotBlank(message)) {
            errorMessage += " (" + message + ")";
        }
        return errorMessage;
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
