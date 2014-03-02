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

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Git implements JsonObject {

    @JsonProperty("head")
    private final Head head;
    
    @JsonProperty("branch")
    private final String branch;
    
    @JsonProperty("remotes")
    private final List<Remote> remotes;
    
    public Git(final Head head, final String branch, final List<Remote> remotes) {
        this.head = head;
        this.branch = branch;
        this.remotes = remotes;
    }
    
    public Head getHead() {
        return head;
    }
    
    public String getBranch() {
        return branch;
    }
    
    public List<Remote> getRemotes() {
        return remotes;
    }

    public static class Head implements Serializable {
        @JsonProperty("id")
        private final String id;
        
        @JsonProperty("author_name")
        private final String authorName;
        
        @JsonProperty("author_email")
        private final String authorEmail;
        
        @JsonProperty("committer_name")
        private final String committerName;
        
        @JsonProperty("committer_email")
        private final String committerEmail;
        
        @JsonProperty("message")
        private final String message;
        
        public Head(final String id, final String authorName, final String authorEmail, final String committerName, final String committerEmail, final String message) {
            this.id = id;
            this.authorName = authorName;
            this.authorEmail = authorEmail;
            this.committerName = committerName;
            this.committerEmail = committerEmail;
            this.message = message;
        }
        
        public String getId() {
            return id;
        }
        
        public String getAuthorName() {
            return authorName;
        }
        
        public String getAuthorEmail() {
            return authorEmail;
        }
        
        public String getCommitterName() {
            return committerName;
        }
        
        public String getCommitterEmail() {
            return committerEmail;
        }
        
        public String getMessage() {
            return message;
        }
    }
    
    public static class Remote implements Serializable {
        @JsonProperty("name")
        private final String name;
        
        @JsonProperty("url")
        private final String url;
        
        public Remote(final String name, final String url) {
            this.name = name;
            this.url = url;
        }

        public String getName() {
            return name;
        }
        
        public String getUrl() {
            return url;
        }
    }
}
