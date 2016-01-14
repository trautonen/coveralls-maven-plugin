package org.eluder.coveralls.maven.plugin.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WildcardsTest {

    @Test
    public void testMatchesAgainstNull() throws Exception {
        assertFalse(Wildcards.matches(null, "*"));
    }

    @Test
    public void testMatchesAgainstJoker() throws Exception {
        assertTrue(Wildcards.matches("a", "?"));
    }

    @Test
    public void testMatchesAgainstStar() throws Exception {
        assertTrue(Wildcards.matches("abc", "*"));
    }

    @Test
    public void testMatchesAgainstWildcards() throws Exception {
        assertTrue(Wildcards.matches("abcdefg", "a*d??g"));
        assertFalse(Wildcards.matches("abcdefg", "a*d?g?"));
    }

    @Test
    public void testMatchesAgainstText() throws Exception {
        assertTrue(Wildcards.matches("abc", "abc"));
        assertFalse(Wildcards.matches("abc", "cba"));
    }

}