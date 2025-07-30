package com.practice.problems;

import java.util.HashMap;
import java.util.Objects;

public class LRUImpl<K, V> implements LRU<K, V> {

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

            if (node == this.tail) {
                this.tail = node.prev;
                this.tail.next = null;
            }

            if (node.prev != null) {
                node.prev.next = node.next;
                node.prev = null;
            }

            if (node.next != null) {
                node.next.prev = node.prev;
            }

            node.next = this.head;
            this.head.prev = node;
            this.head = node;
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

    public LRUImpl(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.capacity = capacity;
        this.map = new HashMap<>();
        this.linkedList = new LinkedList();
    }

    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        LinkedListNode node = map.get(key);
        if (node == null) {
            return null;
        }

        linkedList.moveToFirst(node);
        return node.value;
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        LinkedListNode node = map.get(key);
        if (node != null) {
            node.value = value;
            linkedList.moveToFirst(node);
        } else {
            LinkedListNode newNode = new LinkedListNode(key, value);
            if (map.size() >= capacity) {
                LinkedListNode lru = linkedList.removeLast();
                map.remove(lru.key);
            }
            linkedList.addFirst(newNode);
            map.put(key, newNode);
        }
    }

    @Override
    public void remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        LinkedListNode node = map.remove(key);
        if (node != null) {
            linkedList.remove(node);
        }
    }

    @Override
    public void clear() {
        map.clear();
        linkedList.clear();
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }
}
