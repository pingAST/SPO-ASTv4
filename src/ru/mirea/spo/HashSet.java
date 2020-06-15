package ru.mirea.spo;

import java.util.ArrayList;
import java.util.List;

public class HashSet {
    private int cells;
    private int size;
    List<LinkedList> list;

    public HashSet(){
        this.cells = 4;
        this.size = 0;
        this.list = new ArrayList<>();
        for (int i = 0; i < cells; i++)
            this.list.add(new LinkedList());
    }

    public void add(Object value) {
        int index = Math.abs(value.hashCode()) % cells;
        Node current = list.get(index).getFirst();
        for (int i = 0; i < list.get(index).getSize(); i++){
            if (!current.getValue().equals(value)){
                current.getNext();
            } else
                return;
        }
        list.get(index).add(value);
        this.size++;
    }

    public boolean isSet(Object value){
        int index = Math.abs(value.hashCode()) % cells;
        Node current = list.get(index).getFirst();
        for (int i = 0; i < list.get(index).getSize(); i++){
            if (!current.getValue().equals(value)){
                current.getNext();
            } else
                return true;
        }
        return false;
    }

    public int getSize() {
        return size;
    }
}
