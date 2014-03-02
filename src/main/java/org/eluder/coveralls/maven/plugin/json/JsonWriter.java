package org.eluder.coveralls.maven.plugin.json;

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

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map.Entry;
import java.util.Properties;

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

    protected static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss Z";
    
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
            writeOptionalString("repo_token", job.getRepoToken());
            writeOptionalString("service_name", job.getServiceName());
            writeOptionalString("service_job_id", job.getServiceJobId());
            writeOptionalString("service_number", job.getServiceBuildNumber());
            writeOptionalString("service_build_url", job.getServiceBuildUrl());
            writeOptionalString("service_branch", job.getBranch());
            writeOptionalString("service_pull_request", job.getPullRequest());
            writeOptionalTimestamp("run_at", job.getTimestamp());
            writeOptionalEnvironment("environment", job.getServiceEnvironment());
            writeOptionalObject("git", job.getGit());
            generator.writeArrayFieldStart("source_files");
        } catch (JsonProcessingException ex) {
            throw new ProcessingException(ex);
        }
    }
    
    public void writeEnd() throws ProcessingException, IOException {
        try {
            generator.writeEndArray();
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
    
    private void writeOptionalString(final String field, final String value) throws ProcessingException, IOException {
        if (StringUtils.isNotBlank(value)) {
            generator.writeStringField(field, value);
        }
    }
    
    private void writeOptionalObject(final String field, final Object value) throws ProcessingException, IOException {
        if (value != null) {
            generator.writeObjectField(field, value);
        }
    }
    
    private void writeOptionalTimestamp(final String field, final Date value) throws ProcessingException, IOException {
        if (value != null) {
            SimpleDateFormat format = new SimpleDateFormat(TIMESTAMP_FORMAT);
            writeOptionalString(field, format.format(value));
        }
    }
    
    private void writeOptionalEnvironment(final String field, final Properties properties) throws ProcessingException, IOException {
        if (properties != null) {
            generator.writeObjectFieldStart(field);
            for (Entry<Object, Object> property : properties.entrySet()) {
                writeOptionalString(property.getKey().toString(), property.getValue().toString());
            }
            generator.writeEndObject();
        }
    }
}
