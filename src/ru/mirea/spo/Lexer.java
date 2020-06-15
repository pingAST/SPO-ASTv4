package ru.mirea.spo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Lexer {

    private StringBuilder input = new StringBuilder();
    private CreateLexems lexems = new CreateLexems();
    private List<Lexem> lexemList = lexems.getLexems();

    public Lexer(String path){
        try (Stream<String> st = Files.lines(Paths.get(path))) {
            st.forEach(input::append);
        } catch (IOException ex) {
            ex.fillInStackTrace();
            return;
        }
    }

    public List<Token> do_lex(){
        int pos = 0;
        List<Token> tokenList = new ArrayList<>();
        while(pos < this.input.length()){
            Boolean match = false;
            Matcher matcher;
            Pattern pattern;
            for (Lexem lexem : this.lexemList){
                pattern = Pattern.compile(lexem.getRegexp());
                matcher = pattern.matcher(input.substring(pos));
                match = matcher.find();
                if (match){
                    if (!lexem.getName().equals("WhiteSpace")) {
                        tokenList.add(new Token(matcher.group(), lexem.getName(), lexem.getPriority()));
                        pos += matcher.end();
                    }
                    else{
                        pos += matcher.end();
                    }
                    break;
                }
            }
            if (!match) {
                System.out.println("Wrong character : " + input.toString().charAt(pos));
                return tokenList;
            }
        }
        return tokenList;
    }
}
