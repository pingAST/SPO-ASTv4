package ru.mirea.spo;

public class Token {

    private String value;
    private String tag;
    private int priority;

    public Token(String value, String tag, int priority){
        this.value = value;
        this.tag = tag;
        this.priority = priority;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
