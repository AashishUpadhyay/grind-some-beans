package com.practice.problems;

public class Search {
    public Search() {

    }

    public int binarySearch(int[] arr, int target) {
        int lft = 0;
        int rht = arr.length - 1;

        while (lft <= rht) {
            int mid = (lft + rht) / 2;
            if (arr[mid] == target) {
                return mid;
            }
            if (arr[mid] < target) {
                lft = mid + 1;
            } else {
                rht = mid - 1;
            }
        }
        return -1;
    }
}
