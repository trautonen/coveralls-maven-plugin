package org.eluder.coveralls.maven.plugin.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface JsonObject extends Serializable {

}
