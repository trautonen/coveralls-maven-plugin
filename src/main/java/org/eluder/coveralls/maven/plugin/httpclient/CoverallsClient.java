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
import org.apache.http.params.HttpParams;
import org.eluder.coveralls.maven.plugin.domain.CoverallsResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CoverallsClient {

    private static final String CHARSET = "utf-8";
    private static final String FILE_NAME = "coveralls.json";
    private static final String MIME_TYPE = "application/octet-stream";
    
    protected final HttpClient client;
    protected final ObjectMapper objectMapper;
    protected final String coverallsUrl;
    
    public CoverallsClient(final String coverallsUrl) {
        HttpParams params = new BasicHttpParams();
        this.client = new DefaultHttpClient(params);
        this.objectMapper = new ObjectMapper();
        this.coverallsUrl = coverallsUrl;
    }
    
    public CoverallsResponse submit(final File file) throws IOException {
        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        entity.addPart("json_file", new FileBody(file, FILE_NAME, MIME_TYPE, CHARSET));
        HttpPost post = new HttpPost(coverallsUrl);
        post.setEntity(entity);
        HttpResponse response = client.execute(post);
        return parseResponse(response);
    }
    
    protected CoverallsResponse parseResponse(final HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        ContentType contentType = ContentType.getOrDefault(entity);
        InputStreamReader reader = new InputStreamReader(entity.getContent(), contentType.getCharset());
        return objectMapper.readValue(reader, CoverallsResponse.class);
    }
}
