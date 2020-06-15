package ru.mirea.spo;

import javax.naming.event.ObjectChangeListener;
import java.io.SyncFailedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StackMachine {
    private int pos;
    private List<String> poliz;
    private List<Object> stack;
    private HashMap<Object, Object> variables;

    public StackMachine(List<String> poliz){
        this.poliz = poliz;
        this.stack = new ArrayList<>();
        this.pos = 0;
        this.variables = new HashMap<>();
    }

    public void print(){
        for (Object key : variables.keySet()){
            System.out.println("\n" + key + " = " + variables.get(key));
        }
    }

    public void process(){
        while (this.pos < this.poliz.size()){
            this.stack.add(this.poliz.get(this.pos++));
            Object stackHead = this.stack.remove(this.stack.size() - 1);
            if (stackHead.equals("!")){
                this.pos = Integer.valueOf(this.stack.remove(this.stack.size() - 1).toString());
            } else if (stackHead.equals("!F")){
                int addr = Integer.valueOf(this.stack.remove(this.stack.size() - 1).toString());
                if (!Boolean.valueOf(this.stack.remove(this.stack.size() - 1).toString()))
                    this.pos = addr;
            } else if (stackHead.equals("*") || stackHead.equals("/") || stackHead.equals("=") ||
                    stackHead.equals("+") || stackHead.equals("-") || stackHead.equals(">") ||
                    stackHead.equals("<") || stackHead.equals("==") || stackHead.equals("!=") ||
                    stackHead.equals("<=") || stackHead.equals(">=") || stackHead.equals("add")
                    || stackHead.equals("getValue") || stackHead.equals("getSize")
                    || stackHead.equals("getNext") || stackHead.equals("getPrev")
                    || stackHead.equals("getLast") || stackHead.equals("getFirst")
                    || stackHead.equals("isSet")){
                this.calculate(String.valueOf(stackHead));
            } else
                this.stack.add(stackHead);
        }
    }

    private Object check (Object a) {
        if (variables.get(a) != null) {
            a = variables.get(a);
        }
        return a;
    }

    /*private void checkDef(Object a){
        if (!a.equals("true") || !a.equals("false")) { // проверка на объявление переменной
            try {
                int buf = Integer.valueOf(a.toString());
            } catch (Exception e){
                System.out.println("\nПеременная не объявлена: " + a);
                System.exit(0);
                e.fillInStackTrace();
            }
        }
    } */

    private void calculate(String op){
        Object b = new Object();
        Object a = new Object();
        if (!(op.equals("getValue") || op.equals("getSize") ||
                op.equals("getNext") || op.equals("getPrev") ||
                op.equals("getLast") || op.equals("getFirst"))){
            b = this.stack.remove(this.stack.size() - 1);
            a = this.stack.remove(this.stack.size() - 1);
        } else {
            a = this.stack.remove(this.stack.size() - 1);

            Object objRef = this.variables.get(a) != null ? this.variables.get(a) : a;

            switch (op){
                case "getValue":{
                    this.getValue(objRef);
                    break;
                }
                case "getSize":{
                    this.getSize(objRef);
                    break;
                }
                case "getNext":{
                    this.getNext(objRef);
                    break;
                }
                case "getPrev":{
                    this.getPrev(objRef);
                    break;
                }
                case "getLast":{
                    this.getLast(objRef);
                    break;
                }
                case "getFirst":{
                    this.getFirst(objRef);
                    break;
                }
            }
        }

        b = check(b);
        if (op.equals("=")) {
            if (b.equals("LList")){
                this.initLL(a);
            } else if (b.equals("HSet")){
                this.initHSet(a);
            } else {
                this.assign(a, b);
            }
            return;
        }
        a = check(a);

        //checkDef(a);
        //checkDef(b);

        switch (op){
            case "add": {
                this.add(a, b);
                break;
            }
            case "isSet": {
                this.isSet(a, b);
                break;
            }
            case "*": {
                this.mul(a, b);
                break;
            }
            case "-": {
                this.minus(a, b);
                break;
            }
            case "+": {
                this.plus(a, b);
                break;
            }
            case "/": {
                this.div(a, b);
                break;
            }
            case ">": {
                this.greater(a, b);
                break;
            }
            case ">=": {
                this.greater_eq(a, b);
                break;
            }
            case "<": {
                this.less(a, b);
                break;
            }
            case "<=": {
                this.less_eq(a, b);
                break;
            }
            case "==": {
                this.equal(a, b);
                break;
            }
            case "!=": {
                this.not_equal(a, b);
                break;
            }
        }

    }

    private void getNext(Object objRef) {
        try{
            this.stack.add(((Node) objRef).getNext());
        }catch (Exception e){
            e.fillInStackTrace();
        }
    }

    private void getSize(Object objRef) {
        if(LinkedList.class.isInstance(objRef)) {
            this.stack.add(((LinkedList) objRef).getSize());
        } else {
            this.stack.add(((HashSet) objRef).getSize());
        }
    }

    private void getPrev(Object objRef) {
        try{
            this.stack.add(((Node) objRef).getPrev());
        }catch (Exception e){
            e.fillInStackTrace();
        }
    }

    private void isSet(Object obj, Object value) {
        try{
            this.stack.add(((HashSet) obj).isSet(value));
        }catch (Exception e){
            e.fillInStackTrace();
        }
    }

    private void getFirst(Object objRef) {
        try{
            this.stack.add(((LinkedList) objRef).getFirst());
        }catch (Exception e){
            e.fillInStackTrace();
        }
    }

    private void getValue(Object objRef) {
        try {
            this.stack.add(((Node) objRef).getValue());
        } catch (Exception e){
            e.fillInStackTrace();
        }
    }

    private void getLast(Object objRef) {
        try{
            this.stack.add(((LinkedList) objRef).getLast());
        }catch (Exception e){
            e.fillInStackTrace();
        }
    }

    private void assign(Object a, Object b){
        if (variables.get(b) != null) {
            b = variables.get(b);
        }
        variables.put(a, b);
    }

    private void mul(Object a, Object b) {
        this.stack.add(Integer.valueOf(a.toString()) * Integer.valueOf(b.toString()));
    }

    private void minus(Object a, Object b) {
        this.stack.add(Integer.valueOf(a.toString()) - Integer.valueOf(b.toString()));
    }

    private void greater(Object a, Object b) {
        this.stack.add(Integer.valueOf(a.toString()) > Integer.valueOf(b.toString()));
    }

    private void greater_eq(Object a, Object b) {
        this.stack.add(Integer.valueOf(a.toString()) >= Integer.valueOf(b.toString()));
    }

    private void less(Object a, Object b) {
        this.stack.add(Integer.valueOf(a.toString()) < Integer.valueOf(b.toString()));
    }

    private void less_eq(Object a, Object b) {
        this.stack.add(Integer.valueOf(a.toString()) <= Integer.valueOf(b.toString()));
    }

    private void div(Object a, Object b) {
        this.stack.add(Integer.valueOf(a.toString()) / Integer.valueOf(b.toString()));
    }

    private void plus(Object a, Object b) {
        this.stack.add(Integer.valueOf(a.toString()) + Integer.valueOf(b.toString()));
    }

    private void equal(Object a, Object b) {
        this.stack.add(Integer.valueOf(a.toString()) == Integer.valueOf(b.toString()));
    }

    private void not_equal(Object a, Object b) {
        this.stack.add(Integer.valueOf(a.toString()) != Integer.valueOf(b.toString()));
    }

    private void initLL(Object a){
        try{
            this.variables.put(a.toString(), new LinkedList());
        } catch (Exception e){
            e.fillInStackTrace();
        }
    }

    private void initHSet(Object a){
        try{
            this.variables.put(a.toString(), new HashSet());
        } catch (Exception e){
            e.fillInStackTrace();
        }
    }

    private void add(Object obj, Object value){
        if(LinkedList.class.isInstance(obj)) {
            ((LinkedList) obj).add(value);
        } else {
            ((HashSet) obj).add(value);
        }
    }
    
    


}
