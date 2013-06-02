package org.eluder.coveralls.maven.plugin.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.plexus.util.IOUtil;
import org.eluder.coveralls.maven.plugin.domain.Git;
import org.eluder.coveralls.maven.plugin.domain.Job;
import org.eluder.coveralls.maven.plugin.domain.Source;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;

public class JsonWriterTest {

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
        String content = fileToString(file);
        Map<String, Object> jsonMap = stringToJsonMap(content);
        assertEquals(jsonMap.get("service_name"), "service");
        assertEquals(jsonMap.get("service_job_id"), "job123");
        assertEquals(((Collection<?>) jsonMap.get("source_files")).size(), 0);
        assertNotNull(jsonMap.get("git"));
    }
    
    @Test
    public void testOnSource() throws Exception {
        JsonWriter writer = new JsonWriter(job(), file);
        try {
            writer.onSource(source());
        } finally {
            writer.close();
        }
        String content = fileToString(file);
        Map<String, Object> jsonMap = stringToJsonMap(content);
        assertEquals(jsonMap.get("name"), "Foo.java");
        assertEquals(jsonMap.get("source"), "public class Foo { }");
        assertEquals(((Collection<?>) jsonMap.get("coverage")).size(), 1);
    }
    
    private Job job() {
        Git.Head head = new Git.Head("aefg837fge", "john", "john@mail.com", "john", "john@mail.com", "test commit");
        Git.Remote remote = new Git.Remote("origin", "git@git.com:foo.git");
        return new Job(null, "service", "job123", new Git(head, "master", Arrays.asList(remote)));
    }
    
    private Source source() {
        return new Source("Foo.java", "public class Foo { }");
    }
    
    private String fileToString(final File file) throws Exception {
        InputStreamReader reader = new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8"));
        try {
            return IOUtil.toString(reader);
        } finally {
            reader.close();
        }
    }
    
    private Map<String, Object> stringToJsonMap(final String content) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MapType type = mapper.getTypeFactory().constructMapType(HashMap.class, String.class, Object.class);
        return mapper.readValue(content, type);
    }
}
