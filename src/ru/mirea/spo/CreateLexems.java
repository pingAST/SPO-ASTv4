package ru.mirea.spo;

import java.util.ArrayList;
import java.util.List;

public class CreateLexems extends Lexem {

    public static List<Lexem> lexemList;

    public CreateLexems(){
        lexemList = new ArrayList<>();
        lexemList.add(new Lexem("WhiteSpace", "^[ \t\n]+", 0));
        lexemList.add(new Lexem("IF_KW", "^if", 1));
        lexemList.add(new Lexem("WHILE_KW", "^while", 1));
        lexemList.add(new Lexem("LList", "^LList", 1));
        lexemList.add(new Lexem("HSet", "^HSet", 1));
        lexemList.add(new Lexem("GetLast", "^getLast", 1));
        lexemList.add(new Lexem("GetFirst", "^getFirst", 1));
        lexemList.add(new Lexem("GetSize", "^getSize", 1));
        lexemList.add(new Lexem("GetNext", "^getNext", 1));
        lexemList.add(new Lexem("GetPrev", "^getPrev", 1));
        lexemList.add(new Lexem("GetValue", "^getValue", 1));
        lexemList.add(new Lexem("Add", "^add", 1));
        lexemList.add(new Lexem("IsSet", "^isSet", 1));
        lexemList.add(new Lexem("VAR", "^[A-Za-z][A-Za-z0-9]*", 0));
        lexemList.add(new Lexem("DIGIT", "^0|(^[1-9][0-9]*)", 0));
        lexemList.add(new Lexem("BRACKET_OPEN", "^\\(", 0));
        lexemList.add(new Lexem("BRACKET_CLOSE", "^\\)", 0));
        lexemList.add(new Lexem("FBRACKET_OPEN", "^\\{", 0));
        lexemList.add(new Lexem("FBRACKET_CLOSE", "^\\}", 0));
        lexemList.add(new Lexem("OP_PLUS", "^\\+", 6));
        lexemList.add(new Lexem("OP_MINUS", "^-", 6));
        lexemList.add(new Lexem("OP_MUL", "^\\*", 7));
        lexemList.add(new Lexem("OP_DIV", "^/", 7));
        lexemList.add(new Lexem("COP_NEQ", "^!=", 4));
        lexemList.add(new Lexem("COP_EQ", "^==", 4));
        lexemList.add(new Lexem("ASSIGN_OP", "^=", 1));
        lexemList.add(new Lexem("COP_EQMOR", "^>=", 5));
        lexemList.add(new Lexem("COP_MOR", "^>", 5));
        lexemList.add(new Lexem("COP_EQLES", "^<=", 5));
        lexemList.add(new Lexem("COP_LES", "^<", 5));
        lexemList.add(new Lexem("SEMICOLON", "^;", 0));
        lexemList.add(new Lexem("DOT", "^\\.", 1));
    }

    public List<Lexem> getLexems() {
        return lexemList;
    }

}
