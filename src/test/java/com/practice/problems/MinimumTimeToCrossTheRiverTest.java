package com.practice.problems;

import org.junit.Test;
import static org.junit.Assert.*;

public class MinimumTimeToCrossTheRiverTest {

    private static final double DELTA = 0.00001;

    @Test
    public void test1() {
        MinimumTimeToCrossTheRiver solution = new MinimumTimeToCrossTheRiver();
        double result = solution.minTime(1, 1, 2, new int[] { 5 }, new double[] { 1.0, 1.3 });
        assertEquals(5.0, result, DELTA);
    }

    @Test
    public void test2() {
        MinimumTimeToCrossTheRiver solution = new MinimumTimeToCrossTheRiver();
        double result = solution.minTime(3, 2, 3, new int[] { 2, 5, 8 }, new double[] { 1.0, 1.5, 0.75 });
        assertEquals(14.5, result, DELTA);
    }

    @Test
    public void test3() {
        MinimumTimeToCrossTheRiver solution = new MinimumTimeToCrossTheRiver();
        double result = solution.minTime(2, 1, 2, new int[] { 10, 10 }, new double[] { 2.0, 2.0 });
        assertEquals(-1.0, result, DELTA);
    }

    @Test
    public void test4() {
        MinimumTimeToCrossTheRiver solution = new MinimumTimeToCrossTheRiver();
        double result = solution.minTime(3, 2, 4, new int[] { 57, 80, 46 }, new double[] { 1.37, 1.81, 0.52, 1.66 });
        assertEquals(240.53, result, DELTA);
    }

    @Test
    public void test5() {
        MinimumTimeToCrossTheRiver solution = new MinimumTimeToCrossTheRiver();
        double result = solution.minTime(5, 2, 4, new int[] { 31, 8, 2, 50, 52 },
                new double[] { 1.36, 0.63, 0.55, 1.33 });
        assertEquals(73.47, result, DELTA);
    }

    @Test
    public void test6() {
        MinimumTimeToCrossTheRiver solution = new MinimumTimeToCrossTheRiver();
        double result = solution.minTime(12, 3, 4,
                new int[] { 88, 86, 9, 19, 17, 77, 19, 54, 93, 35, 4, 79 },
                new double[] { 1.23, 1.4, 1.07, 0.97 });
        assertEquals(285.1, result, DELTA);
    }
}