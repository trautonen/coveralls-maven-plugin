package org.eluder.coveralls.maven.plugin.service;

public class Travis implements ServiceSetup {

    @Override
    public boolean isSelected(final String name) {
        return ("travis-ci".equals(name) ||
                "travis-pro".equals(name));
    }

    @Override
    public String getServiceJobId() {
        return System.getenv("TRAVIS_JOB_ID");
    }

    @Override
    public String getRepoToken() {
        return null;
    }

    @Override
    public String getBranch() {
        return System.getenv("TRAVIS_BRANCH");
    }

}
