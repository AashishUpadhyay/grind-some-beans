package com.practice.problems;

import org.junit.*;

public class ProblemsTest {
    @Test
    public void testMinInteger1() {
        Problems problems = new Problems();
        int[] sweets = new int[] { 3, 6, 7, 11 };
        Assert.assertEquals(4, problems.minInteger(sweets, 8));
    }

    @Test
    public void testMinInteger2() {
        Problems problems = new Problems();
        int[] sweets = new int[] { 30, 11, 23, 4, 20 };
        Assert.assertEquals(30, problems.minInteger(sweets, 5));
        Assert.assertEquals(23, problems.minInteger(sweets, 6));
    }

    @Test
    public void testMinInteger3() {
        Problems problems = new Problems();
        int[] sweets = new int[] { 1, 1, 1, 2 };
        Assert.assertEquals(1, problems.minInteger(sweets, 5));
    }

    @Test
    public void testMinInteger4() {
        Problems problems = new Problems();
        int[] sweets = new int[] { 3, 2, 4, 5 };
        Assert.assertEquals(1, problems.minInteger(sweets, 20));
    }
}