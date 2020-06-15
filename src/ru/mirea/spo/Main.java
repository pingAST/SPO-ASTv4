package ru.mirea.spo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws IOException {

        Lexer lexer = new Lexer("resources/program.txt");
        List<Token> tokens = lexer.do_lex();

        for (Token t : tokens){
            System.out.println(t.getValue() + " " +t.getTag() + " " +t.getPriority());
        }

        Parser parser = new Parser(tokens);
        if (parser.lang()){
            System.out.println("\nDone");
        }else{
            System.out.println("\nError");
        }

        for (String p : parser.getPoliz()){
            System.out.print(p + " ");
        }

        StackMachine stackMachine = new StackMachine(parser.getPoliz());
        stackMachine.process();
        stackMachine.print();
    }
}
