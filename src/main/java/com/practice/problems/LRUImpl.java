package com.practice.problems;

import java.util.HashMap;
import java.util.Objects;

public class LRUImpl implements LRU {

    private class LinkedListNode {
        int data;
        LinkedListNode next;
        LinkedListNode prev;

        public LinkedListNode(int data) {
            this.data = data;
        }

        @Override
        public boolean equals(Object other) {
            LinkedListNode o = (LinkedListNode) other;
            if (o == null) {
                return false;
            }

            if (this.data == o.data) {
                return true;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.data);
        }
    }

    private class LinkedList {

        public LinkedList() {
            this.head = null;
            this.tail = null;
            this.size = 0;
        }

        LinkedListNode head;
        LinkedListNode tail;
        int size;

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
            }
            size = size - 1;
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

            LinkedListNode ptr = this.head;
            while (ptr.next != null) {
                if (ptr.equals(node)) {
                    ptr.next.prev = ptr.prev;
                    size = size - 1;
                    return ptr;
                }
                ptr = ptr.next;
            }
            return null;
        }
    }

    private final int capacity;
    private final HashMap<Integer, LinkedListNode> map;
    private final LinkedList linkedList;

    public LRUImpl(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<Integer, LinkedListNode>();
        this.linkedList = new LinkedList();
    }

    public int get(int key) {
        int rv = -1;
        if (this.map.containsKey(key)) {
            LinkedListNode keyNode = this.map.get(key);
            rv = keyNode.data;
            this.linkedList.moveToFirst(keyNode);
        }
        return rv;
    }

    public void put(int key, int value) {
        if (this.map.containsKey(key)) {
            LinkedListNode keyNode = this.map.get(key);
            keyNode.data = value;
            this.linkedList.moveToFirst(keyNode);
        } else {
            LinkedListNode newNode = new LinkedListNode(value);
            while (this.linkedList.size >= this.capacity) {
                LinkedListNode lastNode = this.linkedList.removeLast();
                this.map.remove(lastNode.data);
            }
            this.linkedList.addFirst(newNode);
            this.map.put(key, newNode);
        }
    }

    public void remove(int key) {
        if (this.map.containsKey(key)) {
            LinkedListNode keyNode = this.map.get(key);
            this.map.remove(key);
            this.linkedList.remove(keyNode);
        }
    }

    public void clear() {
        this.map.clear();
        this.linkedList.clear();
    }
}
