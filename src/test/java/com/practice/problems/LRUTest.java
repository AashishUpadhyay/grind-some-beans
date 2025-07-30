package com.practice.problems;

import org.junit.Assert;
import org.junit.Test;

public class LRUTest {

    @Test
    public void testLRU1() {
        LRU lru = new LRUImpl(3);
        lru.put(1, 1);
        Assert.assertEquals(1, lru.get(1));
        Assert.assertEquals(-1, lru.get(2));
        Assert.assertEquals(-1, lru.get(3));
        lru.put(2, 2);
        lru.put(3, 3);
        Assert.assertEquals(1, lru.get(1));
        Assert.assertEquals(2, lru.get(2));
        lru.put(4, 4);
        Assert.assertEquals(-1, lru.get(3));
        Assert.assertEquals(4, lru.get(4));
    }

    @Test
    public void testEmptyCache() {
        LRU lru = new LRUImpl(2);
        Assert.assertEquals(-1, lru.get(1));  // Get from empty cache
    }

    @Test
    public void testUpdateExistingKey() {
        LRU lru = new LRUImpl(2);
        lru.put(1, 1);
        lru.put(1, 10);  // Update value of existing key
        Assert.assertEquals(10, lru.get(1));
    }

    @Test
    public void testLRUEviction() {
        LRU lru = new LRUImpl(2);
        lru.put(1, 1);
        lru.put(2, 2);
        lru.get(1);      // Makes 1 most recently used
        lru.put(3, 3);   // Should evict 2
        Assert.assertEquals(1, lru.get(1));
        Assert.assertEquals(-1, lru.get(2));  // 2 should be evicted
        Assert.assertEquals(3, lru.get(3));
    }

    @Test
    public void testCapacityOne() {
        LRU lru = new LRUImpl(1);
        lru.put(1, 1);
        lru.put(2, 2);
        Assert.assertEquals(-1, lru.get(1));  // 1 should be evicted
        Assert.assertEquals(2, lru.get(2));
    }

    @Test
    public void testFrequentUpdates() {
        LRU lru = new LRUImpl(2);
        lru.put(1, 1);
        lru.put(2, 2);
        lru.put(1, 10);  // Update 1's value
        lru.put(3, 3);   // Should evict 2
        Assert.assertEquals(10, lru.get(1));
        Assert.assertEquals(-1, lru.get(2));
        Assert.assertEquals(3, lru.get(3));
    }

    @Test
    public void testGetUpdatesOrder() {
        LRU lru = new LRUImpl(2);
        lru.put(1, 1);
        lru.put(2, 2);
        lru.get(1);      // Makes 1 most recently used
        lru.put(3, 3);   // Should evict 2, not 1
        Assert.assertEquals(1, lru.get(1));
        Assert.assertEquals(-1, lru.get(2));
    }

    @Test
    public void testSequentialOverwrite() {
        LRU lru = new LRUImpl(3);
        for (int i = 1; i <= 5; i++) {
            lru.put(i, i);
        }
        // Only 3, 4, 5 should remain
        Assert.assertEquals(-1, lru.get(1));
        Assert.assertEquals(-1, lru.get(2));
        Assert.assertEquals(3, lru.get(3));
        Assert.assertEquals(4, lru.get(4));
        Assert.assertEquals(5, lru.get(5));
    }
}