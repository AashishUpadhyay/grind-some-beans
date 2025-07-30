package com.practice.problems;

public interface LRU<K, V> {
    V get(K key);

    void put(K key, V value);

    void remove(K key);

    void clear();
}
