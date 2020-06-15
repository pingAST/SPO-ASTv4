package ru.mirea.spo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexem {
    private String name;
    private String regexp;
    private int priority;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegexp() {
        return regexp;
    }

    public void setRegexp(String regexp) {
        this.regexp = regexp;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    Lexem() {

    }

    Lexem(String n, String r, int p) {
        name=n;
        regexp=r;
        priority=p;
   }

}