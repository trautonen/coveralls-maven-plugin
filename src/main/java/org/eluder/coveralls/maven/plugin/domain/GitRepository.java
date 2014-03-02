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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

public class GitRepository {
    
    private final File sourceDirectory;
    
    public GitRepository(final File sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }
    
    public Git load() throws IOException {
        Repository repository = new RepositoryBuilder().findGitDir(this.sourceDirectory).build();
        try {
            Git.Head head = getHead(repository);
            String branch = getBranch(repository);
            List<Git.Remote> remotes = getRemotes(repository);
            return new Git(head, branch, remotes);
        } finally {
            repository.close();
        }
    }

    private Git.Head getHead(final Repository repository) throws IOException {
        ObjectId revision = repository.resolve(Constants.HEAD);
        RevCommit commit = new RevWalk(repository).parseCommit(revision);
        Git.Head head = new Git.Head(
                revision.getName(),
                commit.getAuthorIdent().getName(),
                commit.getAuthorIdent().getEmailAddress(),
                commit.getCommitterIdent().getName(),
                commit.getCommitterIdent().getEmailAddress(),
                commit.getFullMessage()
        );
        return head;
    }
    
    private String getBranch(final Repository repository) throws IOException {
        return repository.getBranch();
    }
    
    private List<Git.Remote> getRemotes(final Repository repository) {
        Config config = repository.getConfig();
        List<Git.Remote> remotes = new ArrayList<Git.Remote>();
        for (String remote : config.getSubsections("remote")) {
            String url = config.getString("remote", remote, "url");
            remotes.add(new Git.Remote(remote, url));
        }
        return remotes;
    }
}
