package org.eluder.coveralls.maven.plugin.util;

/**
 * Wildcard text utilities.
 */
public final class Wildcards {

    /**
     * Matches text against a wildcard pattern where ? is single letter and * is zero or more
     * letters.
     *
     * @param text the text to test against
     * @param wildcard the wildcard pattern
     * @return
     */
    public static boolean matches(final String text, final String wildcard) {
        String pattern = wildcard.replace("?", "\\w").replace("*", "\\w*");
        return (text != null && text.matches(pattern));
    }

    private Wildcards() {
        // hide constructor
    }
}
