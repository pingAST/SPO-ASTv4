package ru.mirea.spo;

public class LinkedList {
    int size;
    private Node last;
    private Node first;

    public LinkedList() {
        last = new Node();
        first = new Node();
        size = 0;
    }

    public void add(Object value){
        if(this.size == 0){
            this.first = new Node(value);
            this.last = first;
        } else {
            Node oldEndNode = this.getLast();
            this.last = new Node(this.getFirst(), oldEndNode, value);
            oldEndNode.next = this.getLast();
            this.getFirst().prev = this.getLast();
        }
        this.size++;
    }

    public Node getLast() {
        return last;
    }

    public Node getFirst(){
        return first;
    }

    public int getSize(){
        return size;
    }
}
