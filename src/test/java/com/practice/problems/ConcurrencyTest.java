package com.practice.problems;

import org.junit.Test;;

public class ConcurrencyTest {
    @Test
    public void testUsingExecutor() {
        Concurrency concurrency = new Concurrency();
        concurrency.usingExecutor();
    }

    @Test
    public void testDemonstrateAsyncMethods() {
        Concurrency concurrency = new Concurrency();
        try {
            concurrency.demonstrateAsyncMethods();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
