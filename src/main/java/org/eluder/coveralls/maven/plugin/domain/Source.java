package org.eluder.coveralls.maven.plugin.domain;

/*
 * #[license]
 * coveralls-maven-plugin
 * %%
 * Copyright (C) 2013 - 2015 Tapio Rautonen
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Source implements JsonObject {
    
    private static final Pattern NEWLINE = Pattern.compile("\r\n|\r|\n");
    private final String name;
    private final File source;
    private final Charset sourceEncoding;
    private final Integer[] coverage;
    private String classifier;
    
    public Source(final String name, final File source, final Charset sourceEncoding) {
        this.source = source;
        this.sourceEncoding = sourceEncoding;
        int lines = 1;
        // Checkstyle OFF: EmptyBlock
        try {
            String src = new String(Files.readAllBytes(source.toPath()));
            Matcher matcher = NEWLINE.matcher(src);
            while (matcher.find()) {
                lines++;
            }
        } catch (IOException e) {
        }
        // Checkstyle ON: EmptyBlock
        this.coverage = new Integer[lines];
        this.name = name;
    }

    @JsonIgnore
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public String getFullName() {
        return name;

        // #45: cannot use identifier due to unfetchable source files
        //return (classifier == null ? name : name + CLASSIFIER_SEPARATOR + classifier);
    }

    @JsonProperty("source")
    public String getSource() {
        try {
            String src = new String(Files.readAllBytes(source.toPath()), sourceEncoding);
            return src.replaceAll(NEWLINE.pattern(), "\n");
        } catch (IOException e) {
            return "";
        }
    }

    @JsonProperty("coverage")
    public Integer[] getCoverage() {
        return coverage;
    }

    @JsonIgnore
    public String getClassifier() {
        return classifier;
    }

    public void setClassifier(final String classifier) {
        this.classifier = classifier;
    }

    public void addCoverage(final int lineNumber, final Integer coverage) {
        int index = lineNumber - 1;
        if (index >= this.coverage.length) {
            throw new IllegalArgumentException("Line number " + lineNumber + " is greater than the source file " + name + " size");
        }
        this.coverage[lineNumber - 1] = coverage;
    }

    public void merge(final Source source) {
        for (int i = 0; i < this.coverage.length && i < source.coverage.length; i++) {
            if (this.coverage[i] == null && source.coverage[i] != null) {
                this.coverage[i] = source.coverage[i];
            } else if (this.coverage[i] != null && source.coverage[i] != null) {
                this.coverage[i] += source.coverage[i];
            }
        }
    }
}
