package org.eluder.coveralls.maven.plugin.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class Source implements JsonObject {
    
    private static final Pattern NEWLINE = Pattern.compile("\r\n|\r|\n");
    
    @JsonProperty("name")
    private final String name;
    
    @JsonProperty("source")
    private final String source;
    
    @JsonProperty("coverage")
    private final Integer[] coverage;
    
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
    
    public String getName() {
        return name;
    }
    
    public String getSource() {
        return source;
    }
    
    public Integer[] getCoverage() {
        return coverage;
    }
    
    public void addCoverage(final int lineNumber, final Integer coverage) {
        this.coverage[lineNumber - 1] = coverage;
    }
}
