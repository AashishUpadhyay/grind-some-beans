package com.practice.problems;

import org.junit.Assert;
import org.junit.Test;

public class LRUTest {

    @Test
    public void testIntegerLRU() {
        LRU<Integer, Integer> lru = new LRUImpl<>(3);
        lru.put(1, 1);
        Assert.assertEquals(Integer.valueOf(1), lru.get(1));
        Assert.assertNull(lru.get(2));
        Assert.assertNull(lru.get(3));
        lru.put(2, 2);
        lru.put(3, 3);
        Assert.assertEquals(Integer.valueOf(1), lru.get(1));
        Assert.assertEquals(Integer.valueOf(2), lru.get(2));
        lru.put(4, 4);
        Assert.assertNull(lru.get(3));
        Assert.assertEquals(Integer.valueOf(4), lru.get(4));
    }

    @Test
    public void testStringLRU() {
        LRU<String, String> lru = new LRUImpl<>(2);
        lru.put("key1", "value1");
        lru.put("key2", "value2");
        Assert.assertEquals("value1", lru.get("key1"));
        lru.put("key3", "value3"); // should evict key2
        Assert.assertNull(lru.get("key2"));
        Assert.assertEquals("value3", lru.get("key3"));
    }

    @Test
    public void testEmptyCache() {
        LRU<Integer, String> lru = new LRUImpl<>(2);
        Assert.assertNull(lru.get(1));
    }

    @Test
    public void testUpdateExistingKey() {
        LRU<Integer, String> lru = new LRUImpl<>(2);
        lru.put(1, "one");
        lru.put(1, "ONE");
        Assert.assertEquals("ONE", lru.get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKey() {
        LRU<String, Integer> lru = new LRUImpl<>(2);
        lru.put(null, 1);
    }

    @Test
    public void testNullValue() {
        LRU<Integer, String> lru = new LRUImpl<>(2);
        lru.put(1, null);
        Assert.assertNull(lru.get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCapacity() {
        new LRUImpl<String, String>(0);
    }

    @Test
    public void testClear() {
        LRU<Integer, Integer> lru = new LRUImpl<>(2);
        lru.put(1, 1);
        lru.put(2, 2);
        lru.clear();
        Assert.assertNull(lru.get(1));
        Assert.assertNull(lru.get(2));
    }
}