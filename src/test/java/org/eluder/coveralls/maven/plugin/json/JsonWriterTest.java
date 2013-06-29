package org.eluder.coveralls.maven.plugin.json;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eluder.coveralls.maven.plugin.domain.Git;
import org.eluder.coveralls.maven.plugin.domain.Job;
import org.eluder.coveralls.maven.plugin.domain.Source;
import org.eluder.coveralls.maven.plugin.util.TestIoUtil;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;

public class JsonWriterTest {

    private static final long TEST_TIME = 1357009200000l;
    
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    private File file;
    
    @Before
    public void init() throws IOException {
        file = folder.newFile();
    }
    
    @Test
    @SuppressWarnings("resource")
    public void testGetJob() throws Exception {
        Job job = job();
        assertSame(job, new JsonWriter(job, file).getJob());
    }
    
    @Test
    @SuppressWarnings("resource")
    public void testGetCoverallsFile() throws Exception {
        Job job = job();
        assertSame(file, new JsonWriter(job, file).getCoverallsFile());
        
    }
    
    @Test
    public void testWriteStartAndEnd() throws Exception {
        JsonWriter writer = new JsonWriter(job(), file);
        try {
            writer.writeStart();
            writer.writeEnd();
        } finally {
            writer.close();
        }
        String content = TestIoUtil.readFileContent(file);
        Map<String, Object> jsonMap = stringToJsonMap(content);
        assertEquals("service", jsonMap.get("service_name"));
        assertEquals("job123", jsonMap.get("service_job_id"));
        assertEquals(new SimpleDateFormat(JsonWriter.TIMESTAMP_FORMAT).format(new Date(TEST_TIME)), jsonMap.get("run_at"));
        assertNotNull(jsonMap.get("git"));
        assertEquals(0, ((Collection<?>) jsonMap.get("source_files")).size());
    }
    
    @Test
    public void testOnSource() throws Exception {
        JsonWriter writer = new JsonWriter(job(), file);
        try {
            writer.onSource(source());
        } finally {
            writer.close();
        }
        String content = TestIoUtil.readFileContent(file);
        Map<String, Object> jsonMap = stringToJsonMap(content);
        assertEquals("Foo.java", jsonMap.get("name"));
        assertEquals("public class Foo { }", jsonMap.get("source"));
        assertEquals(1, ((Collection<?>) jsonMap.get("coverage")).size());
    }
    
    private Job job() {
        Git.Head head = new Git.Head("aefg837fge", "john", "john@mail.com", "john", "john@mail.com", "test commit");
        Git.Remote remote = new Git.Remote("origin", "git@git.com:foo.git");
        return new Job()
            .withServiceName("service")
            .withServiceJobId("job123")
            .withTimestamp(new Date(1357009200000l))
            .withGit(new Git(head, "master", Arrays.asList(remote)));
    }
    
    private Source source() {
        return new Source("Foo.java", "public class Foo { }");
    }
    
    private Map<String, Object> stringToJsonMap(final String content) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MapType type = mapper.getTypeFactory().constructMapType(HashMap.class, String.class, Object.class);
        return mapper.readValue(content, type);
    }
}
