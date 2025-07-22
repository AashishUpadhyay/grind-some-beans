package com.practice.problems;

import java.util.List;
import java.util.ArrayList;

public class Combinations {
    public List<List<Integer>> combine(List<Integer> nums, int k) {
        int n = nums.size();
        List<List<Integer>> result = new ArrayList<>();
        int visited = 0;
        for (int i = 0; i <= n - k; i++) {
            combine(i, n, k - 1, (visited | (1 << i)), nums, result);
        }
        return result;
    }

    private void combine(int si, int n, int k, int visited, List<Integer> nums, List<List<Integer>> result) {
        if (k == 0) {
            translate(visited, nums, result);
            return;
        }

        for (int i = si + 1; i <= n - k; i++) {
            combine(i, n, k - 1, (visited | (1 << i)), nums, result);
        }
    }

    private void translate(int visited, List<Integer> nums, List<List<Integer>> result) {
        List<Integer> grp = new ArrayList<>();
        for (int i = 0; i < nums.size(); i++) {
            if ((visited & (1 << i)) != 0) {
                grp.add(nums.get(i));
            }
        }
        result.add(grp);
    }
}