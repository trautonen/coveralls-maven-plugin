package org.eluder.coveralls.maven.plugin.logging;

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

import org.apache.maven.plugin.logging.Log;
import org.eluder.coveralls.maven.plugin.domain.Job;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JobLogger implements Logger {

    private static final int ABBREV = 7;
    
    private final Job job;
    private final ObjectMapper jsonMapper;
    
    public JobLogger(final Job job) {
        this(job, null);
    }
    
    public JobLogger(final Job job, final ObjectMapper jsonMapper) {
        if (job == null) {
            throw new IllegalArgumentException("job must be defined");
        }
        this.job = job;
        this.jsonMapper = (jsonMapper != null ? jsonMapper : createDefaultJsonMapper());
    }

    @Override
    public Position getPosition() {
        return Position.BEFORE;
    }
    
    @Override
    public void log(final Log log) {
        StringBuilder starting = new StringBuilder("Starting Coveralls job");
        if (job.getServiceName() != null) {
            starting.append(" for " + job.getServiceName());
            if (job.getServiceJobId() != null) {
                starting.append(" (" + job.getServiceJobId() + ")");
            } else if (job.getServiceBuildNumber() != null) {
                starting.append(" (" + job.getServiceBuildNumber());
                if (job.getServiceBuildUrl() != null) {
                    starting.append(" / " + job.getServiceBuildUrl());
                }
                starting.append(")");
            }
        }
        if (job.isDryRun()) {
            starting.append(" in dry run mode");
        }
        log.info(starting.toString());
        
        if (job.getRepoToken() != null) {
            log.info("Using repository token <secret>");
        }
        
        if (job.getGit() != null) {
            String commit = job.getGit().getHead().getId();
            String branch = (job.getBranch() != null ? job.getBranch() : job.getGit().getBranch());
            log.info("Git commit " + commit.substring(0, ABBREV) + " in " + branch);
        }
        
        if (log.isDebugEnabled()) {
            try {
                log.debug("Complete Job description:\n" + jsonMapper.writeValueAsString(job));
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    
    private ObjectMapper createDefaultJsonMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        return mapper;
    }
    
}
