package org.eluder.coveralls.maven.plugin.domain;

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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Source implements JsonObject {
    
    private static final Pattern NEWLINE = Pattern.compile("\r\n|\r|\n");
    
    private final String name;
    private final String source;
    private final Integer[] coverage;
    private String classifier;
    
    public Source(final String name, final String source) {
        int lines = 1;
        StringBuffer replaced = new StringBuffer(source.length());
        Matcher matcher = NEWLINE.matcher(source);
        while (matcher.find()) {
            lines++;
            matcher.appendReplacement(replaced, "\n");
        }
        matcher.appendTail(replaced);
        this.source = replaced.toString();
        this.coverage = new Integer[lines];
        this.name = name;
    }
    
    @JsonIgnore
    public String getName() {
        return name;
    }
    
    @JsonProperty("name")
    public String getFullName() {
        return (classifier == null ? name : name + classifier);
    }
    
    @JsonProperty("source")
    public String getSource() {
        return source;
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
        this.coverage[lineNumber - 1] = coverage;
    }
}
