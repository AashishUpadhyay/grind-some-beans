package com.practice.problems;

import java.util.HashMap;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thread-safe LRU Cache implementation.
 * 
 * @param <K> Type of cache keys
 * @param <V> Type of cache values
 */
public class LRUImpl<K, V> implements LRU<K, V> {
    private static final Logger logger = LoggerFactory.getLogger(LRUImpl.class);

    private class LinkedListNode {
        K key;
        V value;
        LinkedListNode next;
        LinkedListNode prev;

        public LinkedListNode(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other)
                return true;
            if (other == null || getClass() != other.getClass())
                return false;
            LinkedListNode that = (LinkedListNode) other;
            return Objects.equals(key, that.key);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key);
        }
    }

    private class LinkedList {
        LinkedListNode head;
        LinkedListNode tail;
        int size;

        public LinkedList() {
            this.head = null;
            this.tail = null;
            this.size = 0;
        }

        public void moveToFirst(LinkedListNode node) {
            if (node == null) {
                throw new UnsupportedOperationException("Empty node being moved!");
            }

            if (node == this.head) {
                return;
            }

            // Save the node's current neighbors
            LinkedListNode prevNode = node.prev;
            LinkedListNode nextNode = node.next;

            // Update node's pointers
            node.next = this.head;
            node.prev = null;

            // Update head
            if (this.head != null) {
                this.head.prev = node;
            }
            this.head = node;

            // Update the old neighbors to point to each other
            if (prevNode != null) {
                prevNode.next = nextNode;
            }
            if (nextNode != null) {
                nextNode.prev = prevNode;
            }

            // Update tail if needed
            if (node == this.tail) {
                this.tail = prevNode;
            }
        }

        public void addFirst(LinkedListNode node) {
            if (node == null) {
                throw new UnsupportedOperationException("Empty node being added!");
            }
            node.prev = null;
            if (this.head == null) {
                add(node);
            } else {
                node.next = this.head;
                this.head.prev = node;
                this.head = node;
                size++;
            }
        }

        public void add(LinkedListNode node) {
            if (this.head == null) {
                node.prev = null;
                this.head = node;
                this.tail = node;
            } else {
                this.tail.next = node;
                node.prev = tail;
                this.tail = node;
            }
            size++;
        }

        public LinkedListNode removeLast() {
            if (this.tail == null) {
                throw new UnsupportedOperationException("List is empty!");
            }
            LinkedListNode rv = this.tail;
            this.tail = rv.prev;
            if (this.tail != null) {
                this.tail.next = null;
            } else {
                this.head = null; // List is now empty
            }
            size--;
            return rv;
        }

        public void clear() {
            this.head = null;
            this.tail = null;
            this.size = 0;
        }

        public LinkedListNode remove(LinkedListNode node) {
            if (node == null) {
                throw new UnsupportedOperationException("Empty node being removed!");
            }

            if (node == head) {
                head = node.next;
                if (head != null) {
                    head.prev = null;
                } else {
                    tail = null;
                }
            } else if (node == tail) {
                tail = node.prev;
                tail.next = null;
            } else {
                node.prev.next = node.next;
                node.next.prev = node.prev;
            }

            size--;
            return node;
        }
    }

    private final int capacity;
    private final HashMap<K, LinkedListNode> map;
    private final LinkedList linkedList;

    private void assertInvariants() {
        assert map.size() == linkedList.size
                : String.format("Map size (%d) != LinkedList size (%d)", map.size(), linkedList.size);
        assert linkedList.size <= capacity
                : String.format("LinkedList size (%d) > capacity (%d)", linkedList.size, capacity);
        assert map.size() <= capacity : String.format("Map size (%d) > capacity (%d)", map.size(), capacity);
    }

    public LRUImpl(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.capacity = capacity;
        this.map = new HashMap<>();
        this.linkedList = new LinkedList();
        logger.info("LRU Cache initialized with capacity={}", capacity);
    }

    @Override
    public synchronized V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        assertInvariants();
        LinkedListNode node = map.get(key);
        if (node == null) {
            logger.trace("Cache miss: key={}", key);
            return null;
        }

        logger.trace("Cache hit: key={}", key);
        linkedList.moveToFirst(node);
        assertInvariants();
        return node.value;
    }

    @Override
    public synchronized void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        assertInvariants();
        LinkedListNode node = map.get(key);
        if (node != null) {
            node.value = value;
            linkedList.moveToFirst(node);
        } else {
            LinkedListNode newNode = new LinkedListNode(key, value);
            if (map.size() >= capacity) {
                LinkedListNode lru = linkedList.removeLast();
                map.remove(lru.key);
                logger.debug("Cache eviction: key={}", lru.key);
                assertInvariants();
            }
            linkedList.addFirst(newNode);
            map.put(key, newNode);
        }
        assertInvariants();
    }

    @Override
    public synchronized void remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        LinkedListNode node = map.remove(key);
        if (node != null) {
            linkedList.remove(node);
        }
    }

    @Override
    public synchronized void clear() {
        map.clear();
        linkedList.clear();
    }

    public synchronized int size() {
        return map.size();
    }

    public synchronized boolean isEmpty() {
        return map.isEmpty();
    }
}
