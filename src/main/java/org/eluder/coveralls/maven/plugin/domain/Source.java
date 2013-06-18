package org.eluder.coveralls.maven.plugin.domain;

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
