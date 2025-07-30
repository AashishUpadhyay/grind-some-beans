package com.practice.problems;

public interface LRU {
    public void put(int key, int value);

    public int get(int key);

    public void remove(int key);

    public void clear();
}
