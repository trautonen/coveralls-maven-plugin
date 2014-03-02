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

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicStatusLine;
import org.eluder.coveralls.maven.plugin.ProcessingException;
import org.eluder.coveralls.maven.plugin.domain.CoverallsResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CoverallsClientTest {

    @Mock
    private HttpClient httpClientMock;
    
    @Mock
    private HttpResponse httpResponseMock;
    
    @Mock
    private HttpEntity httpEntityMock;
    
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    private File file;
    
    @Before
    public void init() throws IOException {
        file = folder.newFile();
    }
    
    @Test
    public void testConstructors() {
        assertNotNull(new CoverallsClient("http://test.com/coveralls"));
        assertNotNull(new CoverallsClient("http://test.com/coveralls", httpClientMock, new ObjectMapper()));
    }
    
    @Test
    public void testSubmit() throws Exception {
        when(httpClientMock.execute(any(HttpUriRequest.class))).thenReturn(httpResponseMock);
        when(httpResponseMock.getEntity()).thenReturn(httpEntityMock);
        when(httpEntityMock.getContent()).thenReturn(coverallsResponse(new CoverallsResponse("success", false, "")));
        CoverallsClient client = new CoverallsClient("http://test.com/coveralls", httpClientMock, new ObjectMapper());
        client.submit(file);
    }
    
    @Test(expected = ProcessingException.class)
    public void testParseInvalidResponse() throws Exception {
        StatusLine statusLine = new BasicStatusLine(HttpVersion.HTTP_1_1, 200, "OK");
        when(httpClientMock.execute(any(HttpUriRequest.class))).thenReturn(httpResponseMock);
        when(httpResponseMock.getStatusLine()).thenReturn(statusLine);
        when(httpResponseMock.getEntity()).thenReturn(httpEntityMock);
        when(httpEntityMock.getContent()).thenReturn(new ByteArrayInputStream("{bogus}".getBytes()));
        CoverallsClient client = new CoverallsClient("http://test.com/coveralls", httpClientMock, new ObjectMapper());
        client.submit(file);
    }
    
    @Test(expected = ProcessingException.class)
    public void testParseErrorousResponse() throws Exception {
        StatusLine statusLine = new BasicStatusLine(HttpVersion.HTTP_1_1, 400, "Bad Request");
        when(httpClientMock.execute(any(HttpUriRequest.class))).thenReturn(httpResponseMock);
        when(httpResponseMock.getStatusLine()).thenReturn(statusLine);
        when(httpResponseMock.getEntity()).thenReturn(httpEntityMock);
        when(httpEntityMock.getContent()).thenReturn(coverallsResponse(new CoverallsResponse("failure", true, "submission failed")));
        CoverallsClient client = new CoverallsClient("http://test.com/coveralls", httpClientMock, new ObjectMapper());
        client.submit(file);
    }
    
    @Test(expected = IOException.class)
    public void testParseFailingEntity() throws Exception {
        StatusLine statusLine = new BasicStatusLine(HttpVersion.HTTP_1_1, 200, "OK");
        when(httpClientMock.execute(any(HttpUriRequest.class))).thenReturn(httpResponseMock);
        when(httpResponseMock.getStatusLine()).thenReturn(statusLine);
        when(httpResponseMock.getEntity()).thenReturn(httpEntityMock);
        when(httpEntityMock.getContent()).thenThrow(IOException.class);
        CoverallsClient client = new CoverallsClient("http://test.com/coveralls", httpClientMock, new ObjectMapper());
        client.submit(file);
    }
    
    private InputStream coverallsResponse(final CoverallsResponse coverallsResponse) throws Exception {
        String content = new ObjectMapper().writeValueAsString(coverallsResponse);
        return new ByteArrayInputStream(content.getBytes());
    }
    
}
