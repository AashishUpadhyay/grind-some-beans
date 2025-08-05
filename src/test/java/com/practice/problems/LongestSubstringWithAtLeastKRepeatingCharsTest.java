package com.practice.problems;

import static org.junit.Assert.assertEquals;

import org.junit.*;

public class LongestSubstringWithAtLeastKRepeatingCharsTest {

    @Test
    public void test1() {
        LongestSubstringWithAtLeastKRepeatingChars lskrc = new LongestSubstringWithAtLeastKRepeatingChars();
        assertEquals(5, lskrc.length("aaabb", 2));
        assertEquals(5, lskrc.length("aaabb", 1));
        assertEquals(3, lskrc.length("aaabb", 3));
        assertEquals(5, lskrc.length("ababbc", 2));

    }

}
