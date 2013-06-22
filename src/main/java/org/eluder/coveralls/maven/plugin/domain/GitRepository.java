package org.eluder.coveralls.maven.plugin.domain;

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
    private final String branch;
    
    public GitRepository(final File sourceDirectory, final String branch) {
        this.sourceDirectory = sourceDirectory;
        this.branch = branch;
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
        if (this.branch != null) {
            return this.branch;
        } else {
            return repository.getBranch();
        }
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
