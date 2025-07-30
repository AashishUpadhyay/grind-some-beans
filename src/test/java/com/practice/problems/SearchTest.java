package com.practice.problems;

import org.junit.*;
import static org.junit.Assert.*;

public class SearchTest {
    @Test
    public void Test1() {
        Search src = new Search();
        int[] arr = { 1, 2, 3, 4, 5 };
        int target = 3;
        int result = src.binarySearch(arr, target);
        assertEquals(2, result);
    }

    @Test
    public void Test2() {
        Search src = new Search();
        int[] arr = { 1, 2, 3, 4, 5 };
        int target = 6;
        int result = src.binarySearch(arr, target);
        assertEquals(-1, result);
    }
}
