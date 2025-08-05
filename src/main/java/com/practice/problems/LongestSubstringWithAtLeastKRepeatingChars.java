package com.practice.problems;

public class LongestSubstringWithAtLeastKRepeatingChars {

    public int length(String s, int k) {
        return lengthInternal(s, k, 0, s.length() - 1);
    }

    public int lengthInternal(String s, int k, int si, int ei) {
        int[] charCount = new int[26];
        for (int i = si; i <= ei; i++) {
            charCount[s.charAt(i) - 97] += 1;
        }

        for (int i = si; i <= ei; i++) {
            if (charCount[s.charAt(i) - 97] < k) {
                return Math.max(lengthInternal(s, k, si, i - 1), lengthInternal(s, k, i + 1, ei));
            }
        }

        return ei - si + 1;
    }
}