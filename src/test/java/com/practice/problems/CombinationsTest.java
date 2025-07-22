package com.practice.problems;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.*;

public class CombinationsTest {

    private void sortNestedList(List<List<Integer>> list) {
        // Sort each inner list
        for (List<Integer> inner : list) {
            Collections.sort(inner);
        }
        // Sort outer list based on inner list contents
        Collections.sort(list, (a, b) -> {
            for (int i = 0; i < Math.min(a.size(), b.size()); i++) {
                int cmp = a.get(i).compareTo(b.get(i));
                if (cmp != 0)
                    return cmp;
            }
            return Integer.compare(a.size(), b.size());
        });
    }

    @Test
    public void test1() {
        Combinations comb = new Combinations();
        List<Integer> input = Arrays.asList(1, 2, 3);
        List<List<Integer>> received = comb.combine(input, 2);
        List<List<Integer>> expected = Arrays.asList(
                Arrays.asList(1, 2),
                Arrays.asList(1, 3),
                Arrays.asList(2, 3));
        sortNestedList(expected);
        sortNestedList(received);
        assertEquals(expected, received);
    }

    @Test
    public void test2() {
        Combinations comb = new Combinations();
        List<Integer> input = Arrays.asList(1, 2, 3);
        List<List<Integer>> received = comb.combine(input, 3);
        List<List<Integer>> expected = Arrays.asList(
                Arrays.asList(1, 2, 3));
        sortNestedList(expected);
        sortNestedList(received);
        assertEquals(expected, received);
    }

    @Test
    public void test3() {
        Combinations comb = new Combinations();
        List<Integer> input = Arrays.asList(1, 2, 3, 4, 5);
        List<List<Integer>> received = comb.combine(input, 3);
        List<List<Integer>> expected = Arrays.asList(
                Arrays.asList(3, 4, 5),
                Arrays.asList(2, 3, 4),
                Arrays.asList(2, 3, 5),
                Arrays.asList(1, 2, 3),
                Arrays.asList(1, 2, 4),
                Arrays.asList(1, 3, 4),
                Arrays.asList(1, 3, 5),
                Arrays.asList(1, 4, 5),
                Arrays.asList(1, 2, 5),
                Arrays.asList(2, 4, 5));

        assertEquals(expected.size(), received.size());
        sortNestedList(expected);
        sortNestedList(received);
        assertEquals(expected, received);
    }
}