package ru.mirea.spo;

public class Node {
    Object value;
    Node prev;
    Node next;

    public Node getNext() {
        return next;
    }

    public Node getPrev() {
        return prev;
    }

    public Object getValue() {
        return value;
    }

    public Node () {
        this.next = null;
        this.prev = null;
        this.value = null;
    }

    public Node (Object value) {
        this.next = null;
        this.prev = null;
        this.value = value;
    }

    public Node (Node next, Node prev, Object value ) {
        this.next = next;
        this.prev = prev;
        this.value = value;
    }
}
