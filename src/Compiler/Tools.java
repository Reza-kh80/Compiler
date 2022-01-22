package Compiler;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Tools {
    public static String LEXICAL_ANALYSIS = "Lexical analysis" , SYNTAX_ANALYSIS = "syntax analysis" , SEMANTIC_ANALYSIS = "semantic analysis";
    private static final HashSet<String> keyWords = new HashSet<>(Arrays.asList(
            "main" , "char","double", "else", "float", "for",
            "if", "int", "long", "short", "while"));
    private static final HashSet<String> varTypes = new HashSet<>(Arrays.asList("int", "float", "double", "short", "long"
            , "byte", "char", "bool"));
    private static final HashSet<String> operator = new HashSet<>(Arrays.asList("(", ")", "{", "}", "[", "]" , "*" , "/" ,
            ";", "=", "==", "!=", "+", "-", "++", "--", "<", ">", "<=", ">=", "&", "&&", "|", "||", "+=", "/=", "-=", "*="));

    public static HashSet<String> getVarTypes() {
        return  varTypes;
    }

    static HashMap<String, String> symbolTable;

    public static boolean isKeyword(String word) {
        return keyWords.contains(word);
    }

    public static boolean isOperator(String opt) {
        return operator.contains(opt);
    }

    public static boolean isOperator(char opt) {
        return isOperator(String.valueOf(opt));
    }

    public static boolean isNumber(String n) {
        try {
            Double.parseDouble(n);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
//comment
    public static boolean isVarType(String word) {
        return varTypes.contains(word);
    }

    public static boolean isIdentifier(String phrase) {
        if (phrase.isEmpty())
            return false;
        char[] p = phrase.toCharArray();
        if (!((p[0] >= 'a' && p[0] <= 'z')
                || (p[0] >= 'A' && p[0] <= 'Z')
                || p[0] == '_'))
            return false;
        for (int i = 1; i < phrase.length(); i++) {
            if (!((p[i] >= 'a' && p[i] <= 'z')
                    || (p[i] >= 'A' && p[i] <= 'Z')
                    || (p[i] >= '0' && p[i] <= '9')
                    || p[i] == '_'))
                return false;
        }
        return true;
    }

    public static boolean isWhiteSpace(String s) {
        return s.matches("^\\s*$");
    }

    public static boolean isWhiteSpace(char c) {
        String s = String.valueOf(c);
        return isWhiteSpace(s);
    }

    public static void addToSymbols(String name, String type) {
        symbolTable.put(name, type);
    }

    public static String getSymbol(String name) {
        return symbolTable.get(name);
    }

    public static boolean isSymbol(String name) {
        return symbolTable.containsKey(name);
    }


    static void writeTokensToOutputFile(String outputFileName , List<String> tokens) throws IOException {
        Formatter formatter = Tools.createOutPutFile(outputFileName);
        formatter.format("........" + Tools.LEXICAL_ANALYSIS + " Output" + "........" );
        for (String token : tokens) {
            formatter.format(token + "\n");
        }
        formatter.close();
    }

    private static Formatter createOutPutFile( String outputFileName) throws IOException {
        File phase2Output = new File(outputFileName);
        phase2Output.createNewFile();
        return new Formatter(phase2Output);
    }

    static void writeErrorToFile(String tag , String outputFileName,String error) throws IOException {
        Formatter formatter = createOutPutFile(outputFileName);
        formatter.format(tag + " Error: " + error);
        formatter.close();
    }
}
