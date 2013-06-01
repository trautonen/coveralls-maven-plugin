package org.eluder.coveralls.maven.plugin.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CoverallsResponse implements JsonObject {

    private final String message;
    private final boolean error;
    private final String url;
    
    @JsonCreator
    public CoverallsResponse(
            @JsonProperty("message") final String message,
            @JsonProperty("error") final boolean error,
            @JsonProperty("url") final String url) {
        this.message = message;
        this.error = error;
        this.url = url;
    }
    
    public String getMessage() {
        return message;
    }

    public boolean isError() {
        return error;
    }
    
    public String getUrl() {
        return url;
    }
    
}
