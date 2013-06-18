package org.eluder.coveralls.maven.plugin.domain;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SourceTest {

    
    @Test
    public void foo() throws Exception {
        Source source = new Source("sdfds", "sdfsdfdfs");
        source.setClassifier("$foo");
        System.out.println(new ObjectMapper().writeValueAsString(source));
    }
}
