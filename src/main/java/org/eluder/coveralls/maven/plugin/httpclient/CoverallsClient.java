package org.eluder.coveralls.maven.plugin.httpclient;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParamBean;
import org.apache.http.params.HttpParams;
import org.codehaus.plexus.util.IOUtil;
import org.eluder.coveralls.maven.plugin.ProcessingException;
import org.eluder.coveralls.maven.plugin.domain.CoverallsResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CoverallsClient {

    private static final String CHARSET = "utf-8";
    private static final String FILE_NAME = "coveralls.json";
    private static final String MIME_TYPE = "application/octet-stream";
    
    private static final int DEFAULT_CONNECTION_TIMEOUT = 10000;
    private static final int DEFAULT_SOCKET_TIMEOUT = 60000;
    
    private final String coverallsUrl;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    public CoverallsClient(final String coverallsUrl) {
        this(coverallsUrl, new DefaultHttpClient(defaultParams()), new ObjectMapper());
    }
    
    public CoverallsClient(final String coverallsUrl, final HttpClient httpClient, final ObjectMapper objectMapper) {
        this.coverallsUrl = coverallsUrl;
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }
    
    public CoverallsResponse submit(final File file) throws ProcessingException, IOException {
        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        entity.addPart("json_file", new FileBody(file, FILE_NAME, MIME_TYPE, CHARSET));
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
    
    private static HttpParams defaultParams() {
        HttpParams params = new BasicHttpParams();
        HttpConnectionParamBean connectionParams = new HttpConnectionParamBean(params);
        connectionParams.setConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT);
        connectionParams.setSoTimeout(DEFAULT_SOCKET_TIMEOUT);
        return params;
    }
}
