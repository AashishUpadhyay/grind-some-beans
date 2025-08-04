package com.practice.problems;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.*;
import java.util.Queue;

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

    @Test
    public void testConcurrentAccess1() {
        List<Thread> threads = new ArrayList<>();
        try {

            LRU<UUID, String> lru = new LRUImpl<>(100);

            threads.clear();
            int availableProcessors = Runtime.getRuntime().availableProcessors();
            int producers = availableProcessors / 2;
            int consumers = availableProcessors - producers;
            Queue<UUID> concurrentQueue = new ConcurrentLinkedQueue<>();
            for (int i = 0; i < producers; ++i) {
                threads.add(new Thread(() -> {
                    long endTime = System.currentTimeMillis() + 12000;
                    while (System.currentTimeMillis() < endTime) {
                        UUID newUUID = UUID.randomUUID();
                        System.out.println(String.format("Adding %s to the cache", newUUID));
                        lru.put(newUUID, newUUID.toString());
                        concurrentQueue.add(newUUID);
                        try {
                            Thread.sleep(50); // Add small delay between operations
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }));
            }

            for (int j = 0; j < consumers; ++j) {
                threads.add(new Thread(() -> {
                    long endTime = System.currentTimeMillis() + 18000;
                    while (System.currentTimeMillis() < endTime) {
                        UUID uuid = concurrentQueue.poll();
                        if (uuid != null) {
                            String value = lru.get(uuid);
                            if (value != null) {
                                Assert.assertEquals(uuid.toString(), value);
                                System.out.println(String.format("Read %s from the cache", uuid));
                            }
                        }
                        try {
                            Thread.sleep(50); // Add small delay between operations
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }));
            }
            threads.forEach(Thread::start);
            threads.forEach(thread -> {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        } catch (Exception ex) {
            Assert.fail("Test failed: " + ex.getMessage());
        } finally {
            // Interrupt all threads to ensure they stop
            for (Thread thread : threads) {
                thread.interrupt();
            }
            // Wait for all threads to finish
            for (Thread thread : threads) {
                try {
                    thread.join(1000); // Wait max 1 second per thread
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @Test
    public void testConcurrentAccess2() {
        LRU<UUID, String> lru = new LRUImpl<>(100);
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(availableProcessors);
        int producers = availableProcessors / 2;
        int consumers = availableProcessors - producers;
        Queue<UUID> uuidsQ = new ConcurrentLinkedQueue<UUID>();

        for (int i = 0; i < producers; i++) {
            executor.submit(() -> {
                long endTime = System.currentTimeMillis() + 120000;
                while (System.currentTimeMillis() < endTime) {
                    UUID newUUID = UUID.randomUUID();
                    lru.put(newUUID, newUUID.toString());
                    System.out.println(String.format("Added %s to the cache", newUUID));
                    uuidsQ.add(newUUID);
                }
            });
        }

        for (int i = 0; i < consumers; i++) {
            executor.submit(() -> {
                long endTime = System.currentTimeMillis() + 180000;
                while (System.currentTimeMillis() < endTime || (uuidsQ.size() > 0)) {
                    UUID uuidToFetch = uuidsQ.poll();
                    String result = lru.get(uuidToFetch);
                    if (result != null) {
                        assertEquals(result, uuidToFetch.toString());
                        System.out.println(String.format("Retrieved %s from the cache", result));
                    }
                }
            });
        }
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    @Test
    public void testConcurrentAccess3() {
        LRU<UUID, String> lru = new LRUImpl<>(100);
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newCachedThreadPool();
        int producers = availableProcessors / 2;
        int consumers = availableProcessors - producers;
        Queue<UUID> uuidsQ = new ConcurrentLinkedQueue<UUID>();

        for (int i = 0; i < producers; i++) {
            executor.submit(() -> {
                long endTime = System.currentTimeMillis() + 120000;
                while (System.currentTimeMillis() < endTime) {
                    UUID newUUID = UUID.randomUUID();
                    lru.put(newUUID, newUUID.toString());
                    System.out.println(String.format("Added %s to the cache", newUUID));
                    uuidsQ.add(newUUID);
                }
            });
        }

        for (int i = 0; i < consumers; i++) {
            executor.submit(() -> {
                long endTime = System.currentTimeMillis() + 180000;
                while (System.currentTimeMillis() < endTime || (uuidsQ.size() > 0)) {
                    UUID uuidToFetch = uuidsQ.poll();
                    String result = lru.get(uuidToFetch);
                    if (result != null) {
                        assertEquals(result, uuidToFetch.toString());
                        System.out.println(String.format("Retrieved %s from the cache", result));
                    }
                }
            });
        }
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}