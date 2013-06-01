package org.eluder.coveralls.maven.plugin.json;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

import org.codehaus.plexus.util.StringUtils;
import org.eluder.coveralls.maven.plugin.ProcessingException;
import org.eluder.coveralls.maven.plugin.SourceCallback;
import org.eluder.coveralls.maven.plugin.domain.Job;
import org.eluder.coveralls.maven.plugin.domain.Source;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingJsonFactory;

public class JsonWriter implements SourceCallback, Closeable {

    private final Job job;
    private final File coverallsFile;
    private final JsonGenerator generator;
    
    public JsonWriter(final Job job, final File coverallsFile) throws IOException {
        this.job = job;
        this.coverallsFile = coverallsFile;
        this.generator = new MappingJsonFactory().createGenerator(coverallsFile, JsonEncoding.UTF8);
    }
    
    public final Job getJob() {
        return job;
    }
    
    public final File getCoverallsFile() {
        return coverallsFile;
    }
    
    public void writeStart() throws ProcessingException, IOException {
        try {
            generator.writeStartObject();
            write("service_name", job.getServiceName());
            writeOptional("service_job_id", job.getServiceJobId());
            writeOptional("repo_token", job.getRepoToken());
            generator.writeArrayFieldStart("source_files");
        } catch (JsonProcessingException ex) {
            throw new ProcessingException(ex);
        }
    }
    
    public void writeEnd() throws ProcessingException, IOException {
        try {
            generator.writeEndArray();
            generator.writeFieldName("git");
            generator.writeObject(job.getGit());
            generator.writeEndObject();
        } catch (JsonProcessingException ex) {
            throw new ProcessingException(ex);
        }
    }
    
    @Override
    public void onSource(final Source source) throws ProcessingException, IOException {
        try {
            generator.writeObject(source);
        } catch (JsonProcessingException ex) {
            throw new ProcessingException(ex);
        }
    }
    
    @Override
    public void close() throws IOException {
        generator.close();
    }
    
    private void write(final String field, final String value) throws ProcessingException, IOException {
        generator.writeStringField(field, value);
    }
    
    private void writeOptional(final String field, final String value) throws ProcessingException, IOException {
        if (StringUtils.isNotBlank(value)) {
            write(field, value);
        }
    }
}
