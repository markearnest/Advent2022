package org.mystikos.aoc2022.day20;

import me.tongfei.progressbar.ProgressBar;

import java.io.BufferedReader;

class CircularLinkedList {
    private Node head;
    private Node tail;
    private int size;
    public CircularLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }
    public void add(long value) {
        Node newNode = new Node(value, size + 1);
        if (head == null) {
            head = newNode;
            tail = newNode;
            newNode.next = head;
            newNode.prev = tail;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            newNode.next = head;
            head.prev = newNode;
            tail = newNode;
        }
        size++;
    }
    public Node getFromOriginalOrder(int originalOrder) {
        Node node = head;
        while (node.originalOrder != originalOrder) {
            node = node.next;
        }
        head=node;
        tail=node.prev;
        return node;
    }
    public void moveNode(Node node, int size) {
        Node tempNode = node.prev;
        long mod = node.getValue() % (size - 1);
        if (mod == 0) {
            return;
        }
        node.next.prev = node.prev;
        node.prev.next = node.next;
        if(node.getValue() > 0) {
            for (long i = 0; i < mod; i++) {
                tempNode = tempNode.next;
            }
        } else {
            for (long i = 0; i > mod; i--) {
                tempNode = tempNode.prev;
            }
        }
        node.next = tempNode.next;
        node.prev = tempNode;
        node.next.prev = node;
        node.prev.next = node;
    }
    public int getSize() {
        return size;
    }
    public void goToValue(long value) {
        Node node = head;
        while (node.value != value) {
            node = node.next;
        }
        head=node;
        tail=node.prev;
    }
    public Node getNodeAt(long index) {
        Node node = head;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        return node;
    }

    public static class Node {
        private final long value;
        private final int originalOrder;
        private Node next;
        private Node prev;

        public Node(long value, int originalOrder) {
            this.value = value;
            this.originalOrder = originalOrder;
        }
        public long getValue() {
            return value;
        }
    }
}

public class Advent20 {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        String fileName = "input/input20.txt";
        long phase1Answer;
        long phase2Answer;
        CircularLinkedList list = new CircularLinkedList();
        CircularLinkedList list2 = new CircularLinkedList();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new java.io.FileReader(fileName));
            String line = reader.readLine();
            while (line != null) {
                list.add(Long.parseLong(line));
                list2.add(Long.parseLong(line)*811589153);
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mix(list);
        list.goToValue(0);
        long x = list.getNodeAt(1000).getValue();
        list.goToValue(0);
        long y = list.getNodeAt(2000).getValue();
        list.goToValue(0);
        long z = list.getNodeAt(3000).getValue();
        phase1Answer = x + y + z;
        for (x = 0; x < 10; x++) {
            mix(list2);
        }
        list2.goToValue(0);
        long x2 = list2.getNodeAt(1000).getValue();
        list2.goToValue(0);
        long y2 = list2.getNodeAt(2000).getValue();
        list2.goToValue(0);
        long z2 = list2.getNodeAt(3000).getValue();
        phase2Answer = x2 + y2 + z2;
        System.out.println("Phase 1 Answer: " + phase1Answer);
        System.out.println("Phase 2 Answer: " + phase2Answer);
        long currentTime = System.currentTimeMillis();
        double elapsedTime = (currentTime - startTime) / 1000.0;
        System.out.println("Time in seconds : " + elapsedTime);
    }
    private static void mix(CircularLinkedList list) {
        try (ProgressBar pb = new ProgressBar("Decryption ", list.getSize())) {
            for (int currentPosition = 1; currentPosition < list.getSize() + 1; currentPosition++) {
                pb.step();
                list.moveNode(list.getFromOriginalOrder(currentPosition), list.getSize());
            }
        }
    }
}
