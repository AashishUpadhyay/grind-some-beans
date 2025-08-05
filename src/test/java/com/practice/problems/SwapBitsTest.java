package com.practice.problems;

import static org.junit.Assert.assertEquals;

import org.junit.*;;

public class SwapBitsTest {
    @Test
    public void test1() {
        SwapBits sb = new SwapBits();
        assertEquals(1, sb.swapBits(2));
        assertEquals(2, sb.swapBits(1));
        assertEquals(10, sb.swapBits(5));
        assertEquals(5, sb.swapBits(10));
        assertEquals(1, sb.swapBits(2));
        assertEquals(2, sb.swapBits(1));
        assertEquals(15, sb.swapBits(15));
    }
}
