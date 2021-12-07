import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Tools {
    private static final HashSet<String> keyWords = new HashSet<>(Arrays.asList(
            "main",
            "auto", "break", "case", "char", "const", "continue", "default",
            "do", "double", "else", "enum", "extern", "float", "for", "goto",
            "if", "int", "long", "register", "return", "short", "signed",
            "sizeof", "static", "struct", "switch", "typedef", "union",
            "unsigned", "void", "volatile", "while"));
    private static final HashSet<String> operator = new HashSet<>(Arrays.asList("(", ")", "{", "}", "[", "]",
            ";", "=", "==", "!=", "+", "-", "++", "--", "<", ">", "<=", ">=", "&", "&&", "|", "||"));

    private static final HashMap<String, String> symbolTable = new HashMap<>();

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

}
