import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Tools {
    private static final HashSet<String> keyWords = new HashSet<>(Arrays.asList(
            "main" ,
            "auto","break","case","char","const","continue","default",
            "do","double","else","enum","extern","float","for","goto",
            "if","int","long","register","return","short","signed",
            "sizeof","static","struct","switch","typedef","union",
            "unsigned","void","volatile","while"));
    private static final HashSet<String> operator = new HashSet<>(Arrays.asList("(",")","{","}","[","]",
            ";","=","==","!=","+","-","++","--","<",">","<=",">=","&","&&","|","||"));

    private static final HashMap<String , String> symbolTable = new HashMap<>();

    public static boolean isKeyword(String word){
        return keyWords.contains(word);
    }
    public static boolean isOperator(String opt){
        return operator.contains(opt);
    }
    public static boolean isOperator(char opt){
        return isOperator(String.valueOf(opt));
    }
    public static boolean isNumber(String n){
        if (n.isEmpty())
            return false;
        char num = n.charAt(0);
        return num >= '0' && num <= '9';
    }

    public static boolean isIdentifier(String phrase) {
        if (!((phrase.charAt(0) >= 'a' && phrase.charAt(0) <= 'z')
                || (phrase.charAt(0)>= 'A' && phrase.charAt(0) <= 'Z')
                || phrase.charAt(0) == '_'))
            return false;
        for (int i = 1; i < phrase.length(); i++) {
            if (!((phrase.charAt(i) >= 'a' && phrase.charAt(i) <= 'z')
                    || (phrase.charAt(i) >= 'A' && phrase.charAt(i) <= 'Z')
                    || (phrase.charAt(i) >= '0' && phrase.charAt(i) <= '9')
                    || phrase.charAt(i) == '_'))
                return false;
        }
        return true;
    }
    public static boolean isWhiteSpace(String s){
        return s.matches("^\\s*$");
    }
    public static boolean isWhiteSpace(char c){
        String s = String.valueOf(c);
        return isWhiteSpace(s);
    }

    public static void addToSymbols(String name , String type){
        symbolTable.put(name, type);
    }
    public static String getSymbol(String name){
        return symbolTable.get(name);
    }
    public static boolean isSymbol(String name){
        return symbolTable.containsKey(name);
    }

}
