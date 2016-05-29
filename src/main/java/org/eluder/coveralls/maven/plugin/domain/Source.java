package org.eluder.coveralls.maven.plugin.domain;

/*
 * #[license]
 * coveralls-maven-plugin
 * %%
 * Copyright (C) 2013 - 2016 Tapio Rautonen
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

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Source implements JsonObject {
    
    private static final Pattern NEWLINE = Pattern.compile("\r\n|\r|\n");
    //private static final String CLASSIFIER_SEPARATOR = "#";
    
    private final String name;
    private final String digest;
    private final Integer[] coverage;
    private String classifier;
    
    public Source(final String name, final String source, final String digest) {
        this(name, getLines(source), digest, null);
    }

    protected Source(final String name, final int lines, final String digest, final String classifier) {
        this.name = name;
        this.digest = digest;
        this.coverage = new Integer[lines];
        this.classifier = classifier;
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
    
    @JsonProperty("source_digest")
    public String getDigest() {
        return digest;
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

    public Source merge(final Source source) {
        Source copy = new Source(this.name, this.coverage.length, this.digest, this.classifier);
        System.arraycopy(this.coverage, 0, copy.coverage, 0, this.coverage.length);
        if (copy.equals(source)) {
            for (int i = 0; i < copy.coverage.length; i++) {
                if (source.coverage[i] != null) {
                    int base = copy.coverage[i] != null ? copy.coverage[i] : 0;
                    copy.coverage[i] = base + source.coverage[i];
                }
            }
        }
        return copy;
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Source)) {
            return false;
        }
        Source other = (Source) obj;
        return (Objects.equals(this.name, other.name) &&
                Objects.equals(this.digest, other.digest) &&
                Objects.equals(this.coverage.length, other.coverage.length));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.digest, this.coverage.length);
    }
    
    private static int getLines(final String source) {
        int lines = 1;
        Matcher matcher = NEWLINE.matcher(source);
        while (matcher.find()) {
            lines++;
        }
        return lines;
    }
}
