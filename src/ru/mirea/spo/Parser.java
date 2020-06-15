package ru.mirea.spo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Parser {

    private List<Token> tokens;
    private int pos;
    private List<String> poliz;
    private List<Token> buffer;
    private List<Integer> addrsForFilling;
    private List<Integer> addrsJumps;
    private List<String> calls;


    public Parser(List<Token> tokens){
        this.tokens = tokens;
        poliz = new ArrayList<String>();
        buffer = new ArrayList<Token>();
        addrsForFilling = new ArrayList<>(); //адреса, куда записываются адреса переходов
        addrsJumps = new ArrayList<>(); // адреса переходов в начало условия
        calls = new ArrayList<>(); // стек вызовов операций while, if
        this.pos = 0;
    }

    public List<String> getPoliz(){
        return this.poliz;
    }

    public boolean lang(){
        while(this.pos < this.tokens.size()){
            if (!this.expr()) {
                this.printExeption(this.tokens.get(this.pos).getValue(), "Expression");
                return false;
            }
        }
        return true;
    }

    //expr -> init | if | loopwhile | loopdowhile
    private boolean expr() {
        if (!(this.init() || this.if_stmt() || this.loopwhile()))
            return false;
        return true;
    }

    // addobj -> DOT "add" BRACKET_OPEN stmt BRACKET_CLOSE
    private boolean addobj() {
        if (!(this.dot())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), ".");
            return false;
        } else if (!(this.add())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), "add");
            return false;
        } else if (!(this.bracket_open())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), "(");
            return false;
        } else if (!(this.stmt())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), "Statement");
            return false;
        } else if (!(this.bracket_close())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), ")");
            return false;
        }
        return true;
    }

    private boolean add() {
        if(this.tokens.get(this.pos).getTag().equals("Add")) {
            this.push(this.tokens.get(this.pos++));
            return true;
        } return false;
    }

    //init -> VAR (ASSIGN_OP (object | objref | stmt)) | addobj semicolon
    private boolean init() {
        if (!(this.var())) {
            return false;
        } else if (this.assign_op()) {
            if (!(this.object() || this.objref() || this.stmt())) {
                this.printExeption(this.tokens.get(this.pos).getValue(), "object or object reference or statement");
                return false;
            }
        } else if (!(this.addobj())) {
            return false;
        }
        if (!(this.semicolon())) {
            return false;
        }
        return true;
    }

    private boolean object() {
        if(this.tokens.get(this.pos).getTag().equals("LList")
                || this.tokens.get(this.pos).getTag().equals("HSet")) {
            this.push(this.tokens.get(this.pos++));
            return true;
        }
        return false;
    }

    private boolean semicolon(){
        if(this.tokens.get(this.pos).getTag().equals("SEMICOLON")) {
            this.push(this.tokens.get(this.pos++));
            return true;
        }
        return false;
    }

    private boolean var(){
        if(this.tokens.get(this.pos).getTag().equals("VAR")) {
            this.push(this.tokens.get(this.pos++));
            return true;
        }
        return false;
    }

    private boolean assign_op() {
        if(this.tokens.get(this.pos).getTag().equals("ASSIGN_OP")) {
            this.push(this.tokens.get(this.pos++));
            return true;
        }
        return false;
    }

    //stmt -> value(OP value)*
    private boolean stmt() {
        if (this.value()) {
            while (true) {
                if (this.op()) {
                    if (!(this.value())) {
                        this.printExeption(this.tokens.get(this.pos).getValue(), "Value");
                        return false;
                    }
                } else break;
            }
        } else
            return false;
        return true;
    }

    // value -> VAR|DIGIT
    private boolean value() {
        if (!(this.var() || this.digit() || this.brcktexpr()))
            return false;
        return true;
    }

    private boolean brcktexpr() {
        if (!(this.bracket_open())) {
            return false;
        } else if (!(this.stmt())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), "Statement");
            return false;
        } else if (!(this.bracket_close())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), ")");
            return false;
        }
        return true;
    }

    private boolean digit() {
        if(this.tokens.get(this.pos).getTag().equals("DIGIT")) {
            this.push(this.tokens.get(this.pos++));
            return true;
        }
        return false;
    }

    // if_stmt -> IF_KW BRACKET_OPEN comp BRACKET_CLOSE FBRACKET_OPEN stmt* FBRACKET_CLOSE
    private boolean if_stmt() {
        if (!(this.if_kw())) {
            return false;
        } else if (!(this.bracket_open())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), "(");
            return false;
        } else if (!(this.comp())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), "Comparison");
            return false;
        } else if (!(this.bracket_close())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), ")");
            return false;
        } else if (!(this.fbracket_open())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), "{");
            return false;
        } else {
            while (true) {
                if (!(this.expr())) {
                    break;
                }
            }
        }
        if (!(this.fbracket_close())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), "}");
            return false;
        }
        return true;
    }

    private boolean if_kw() {
        if(this.tokens.get(this.pos).getTag().equals("IF_KW") ) {
            this.push(this.tokens.get(this.pos++));
            return true;
        }
        return false;
    }

    private boolean bracket_open() {
        if(this.tokens.get(this.pos).getTag().equals("BRACKET_OPEN")) {
            this.push(this.tokens.get(this.pos++));
            return true;
        }
        return false;
    }

    private boolean bracket_close() {
        if(this.tokens.get(this.pos).getTag().equals("BRACKET_CLOSE")) {
            this.push(this.tokens.get(this.pos++));
            return true;
        }
        return false;
    }

    private boolean fbracket_open() {
        if(this.tokens.get(this.pos).getTag().equals("FBRACKET_OPEN")) {
            this.push(this.tokens.get(this.pos++));
            return true;
        }
        return false;
    }

    private boolean fbracket_close() {
        if(this.tokens.get(this.pos).getTag().equals("FBRACKET_CLOSE")) {
            this.push(this.tokens.get(this.pos++));
            return true;
        }
        return false;
    }

    private boolean op() {
        if(this.tokens.get(this.pos).getTag().equals("OP_PLUS")
                || this.tokens.get(this.pos).getTag().equals("OP_MINUS")
                || this.tokens.get(this.pos).getTag().equals("OP_MUL")
                || this.tokens.get(this.pos).getTag().equals("OP_DIV")) {
            this.push(this.tokens.get(this.pos++));
            return true;
        }
        return false;
    }

    //comp -> stmt COMPARE_OP stmt
    private boolean comp() {
        if (!(this.stmt())) {
            return false;
        } else if (!(this.compare_op())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), "Compare operation");
            return false;
        } else if (!(this.stmt())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), "Statement");
            return false;
        }
        return true;
    }

    private boolean compare_op() {
        if(this.tokens.get(this.pos).getTag().equals("COP_NEQ")
                || this.tokens.get(this.pos).getTag().equals("COP_EQ")
                || this.tokens.get(this.pos).getTag().equals("COP_MOR")
                || this.tokens.get(this.pos).getTag().equals("COP_LES")
                || this.tokens.get(this.pos).getTag().equals("COP_EQMOR")
                || this.tokens.get(this.pos).getTag().equals("COP_EQLES")) {
            this.push(this.tokens.get(this.pos++));
            return true;
        }
        return false;
    }

    // loopwhile -> WHILE_KW BRACKET_OPEN comp BRACKET_CLOSE FBRACKET_OPEN expr* FBRACKET_CLOSE
    private boolean loopwhile() {
        if (!(this.while_kw())) {
            return false;
        } else if (!(this.bracket_open())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), "(");
            return false;
        } else if (!(this.comp())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), "Comparison");
            return false;
        } else if (!(this.bracket_close())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), ")");
            return false;
        } else if (!(this.fbracket_open())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), "{");
            return false;
        } else {
            while (true) {
                if (!(this.expr())) {
                    break;
                }
            }
        }
        if (!(this.fbracket_close())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), "}");
            return false;
        }
        return true;
    }

    private boolean while_kw() {
        if(this.tokens.get(this.pos).getTag().equals("WHILE_KW")) {
            this.push(this.tokens.get(this.pos++));
            return true;
        }
        return false;
    }

    private Token endEl(List<Token> list){
        if (list.size() != 0)
            return list.get(list.size() - 1);
        else
            return null;
    }

    private boolean dot() {
        if(this.tokens.get(this.pos).getTag().equals("DOT")) {
            // this.push(this.tokens.get(this.pos++));
            this.pos++;
            return true;
        }
        return false;
    }

    // objref -> VAR (DOT method)+
    private boolean objref() {
        if (!(this.var())) {
            return false;
        } else{
            if (!(this.dot())) {
                this.pos--;
                this.poliz.remove(this.poliz.size() - 1);
                return false;
            }
            if (!(this.method())) {
                this.printExeption(this.tokens.get(this.pos).getValue(), "Method");
                return false;
            }
            do {
                if (!(this.dot())) {
                    break;
                }
                if (!(this.method())) {
                    return false;
                }
            } while (true) ;
            return true;
        }
    }



    private boolean method() {
        if(this.tokens.get(this.pos).getTag().equals("GetLast")
                || this.tokens.get(this.pos).getTag().equals("GetFirst")
                || this.tokens.get(this.pos).getTag().equals("GetSize")
                || this.tokens.get(this.pos).getTag().equals("GetNext")
                || this.tokens.get(this.pos).getTag().equals("GetPrev")
                || this.tokens.get(this.pos).getTag().equals("GetValue")
        ) {
            this.push(this.tokens.get(this.pos++));
            return true;
        } else if (this.isSet())
            return true;
        return false;
    }

    private boolean isSet() {

        if (!(this.isSett())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), "IsSet");
            return false;
        } else if (!(this.bracket_open())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), "(");
            return false;
        } else if (!(this.stmt())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), "Statement");
            return false;
        } else if (!(this.bracket_close())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), ")");
            return false;
        }
        return true;
    }

    private boolean isSett() {
        if(this.tokens.get(this.pos).getTag().equals("IsSet")) {
            this.push(this.tokens.get(this.pos++));
            return true;
        }
        return false;
    }

    private void push(Token token){
        if (token.getTag().equals("VAR") || token.getTag().equals("DIGIT")
                || token.getTag().equals("LList")   || token.getTag().equals("HSet")
                || token.getTag().equals("GetLast") || token.getTag().equals("GetFirst")
                || token.getTag().equals("GetSize") || token.getTag().equals("GetNext")
                || token.getTag().equals("GetPrev") || token.getTag().equals("GetValue")){
            this.poliz.add(token.getValue());
        } else {
            if (token.getValue().equals("while") || token.getValue().equals("if")){
                this.calls.add(token.getValue());
                this.buffer.add(token);
                if(token.getValue().equals("while"))
                    this.addrsJumps.add(this.poliz.size());
            }
            else if (token.getValue().equals(")")){
                while(!this.buffer.get(this.buffer.size() - 1).getValue().equals("(")){
                    poliz.add(buffer.remove(buffer.size() - 1).getValue());
                }
                buffer.remove(buffer.size() - 1);
                if (this.buffer.get(this.buffer.size() - 1).getValue().equals("while") ||
                        this.buffer.get(this.buffer.size() - 1).getValue().equals("if")){
                    this.buffer.remove(this.buffer.size() - 1);
                    this.addrsForFilling.add(this.poliz.size());
                    this.poliz.add(""); //rewrite later
                    this.poliz.add("!F");
                }
                return;
            } else if (token.getValue().equals("}")) {
                while (!this.endEl(this.buffer).getValue().equals("{")) {
                    poliz.add(buffer.remove(buffer.size() - 1).getValue());
                }
                buffer.remove(buffer.size() - 1);
                String lastCall = calls.remove(calls.size() - 1);
                if (lastCall.equals("while")) {
                    this.poliz.add(this.addrsJumps.remove(this.addrsJumps.size() - 1).toString());
                    this.poliz.add("!");
                    int buf = this.addrsForFilling.remove(this.addrsForFilling.size() - 1);
                    this.poliz.set(
                            buf,
                            String.valueOf(this.poliz.size())
                    );
                } else {
                    int buf = this.addrsForFilling.remove(this.addrsForFilling.size() - 1);
                    this.poliz.set(
                            buf,
                            String.valueOf(this.poliz.size())
                    );
                }
                return;
            } else if (!token.getValue().equals("(") && !token.getValue().equals("{") && buffer.size() != 0){
                if (token.getPriority() < this.buffer.get(this.buffer.size() - 1).getPriority()) {
                    if (token.getValue().equals(";"))
                        while (!(this.buffer.get(this.buffer.size() - 1).getValue().equals("=") ||
                                 this.buffer.get(this.buffer.size() - 1).getValue().equals("add")) ) //!!!!!!!!!!
                            this.poliz.add(buffer.remove(this.buffer.size() - 1).getValue());
                    this.poliz.add(buffer.remove(this.buffer.size() - 1).getValue());
                }
            }
            if (!token.getValue().equals(";"))
                this.buffer.add(token);
        }
    }

    private void printExeption(String detected, String expected){
        System.out.println("\nParse error: detected " + "'" + detected +
                "', but " + "'" + expected + "' are expected!");
        System.exit(0);
    }

}

