# Compiler
University Project

## توضیحات خان دوم

در خان دوم پروژه سعی بر بررسی کلمات، اپراتورها، کلمات کلیدی، آیدی ها و... را داشتیم. هدف این بود تا به برنامه یک قطعه کد به عنوان
ورودی داده شود و برنامه با بررسی جزئیات برنامه دنباله ای از توکن های بدست امده را به خروجی تحویل دهد.
پس به عنوان مدل بندی
ابتدا NFA مسیر رسم و کلمات کلیدی و اپراتورها شناسایی و نقشه راه مشخص شد. در مرحله بعد NFA به DFA جهت سهولت در پیاده
سازی تبدیل گردید و DFA حاصل پیاده سازی شد.


### DFA:

![DFA](https://s4.uupload.ir/files/screenshot_2021-11-29_170022_4q1d.jpg)

### Code:
#### Main
``` 
import java.io.*;
import java.util.*;


public class Main {

    static int cursor = 0;

    public static void main(String[] args) throws IOException {
        File input = new File(".//input.txt");

        Scanner scanner = new Scanner(new FileInputStream(input));
        String s;
        ArrayList<String> tokens = new ArrayList<>();
        int lineNum = 0;
        while (scanner.hasNext()) {
            cursor = 0;
            lineNum++;
            s = scanner.nextLine();
            char[] line = s.toCharArray();
            if (s.length() == 0)
                continue;


            String lastWord = "";
            while (cursor < line.length) {
                String word = readNextWord(line);

                if (Tools.isKeyword(word)) {
                    tokens.add("keyword:: " + word);
                } else if (Tools.isOperator(word)) {
                    tokens.add("operator:: " + word);
                }else if (Tools.isNumber(word)){
                    tokens.add("number:: " + word);
                }
                else if(Tools.isIdentifier(word)) {
                    if (Tools.isSymbol(word))
                        tokens.add("id:: " + word);
                    else if (Tools.isVarType(lastWord)) {
                        tokens.add("id:: " + word);
                        Tools.addToSymbols(word, lastWord);
                    }else {
                        System.out.println("error in line " + lineNum + " : " + word + " is not define" );
                        return;
                    }
                }else if (!Tools.isWhiteSpace(word)){
                    System.out.println("error in line " + lineNum + " : " + word + " is not define" );
                    return;
                }
                lastWord = word;

            }

        }

        writeToOutputFile(".//output2.txt" , tokens);

    }

    static String readNextWord(char[] line) {
        StringBuilder tempStr = new StringBuilder();
        while (cursor < line.length && !Tools.isWhiteSpace(line[cursor])) {

            if (Tools.isOperator(line[cursor])){
                if (tempStr.toString().isEmpty()){
                    tempStr.append(line[cursor]);
                    cursor++;
                    if (cursor >= line.length) {
                        return tempStr.toString();
                    }

                    String temp2 = tempStr.toString() + line[cursor];

                    if (Tools.isOperator(temp2)) {
                        cursor++;
                        return temp2;
                    }
                    else {
                        return tempStr.toString();
                    }

                }
                else
                    return tempStr.toString();
            }else {

                tempStr.append(line[cursor]);
                cursor++;
            }
        }
        cursor++;
        return tempStr.toString();
    }

    static void writeToOutputFile(String fileName , List<String> tokens) throws IOException {
        File phase2Output = new File(fileName);
        phase2Output.createNewFile();
        Formatter formatter = new Formatter(phase2Output);
        for (String token : tokens) {

            formatter.format(token + "\n");
        }
        formatter.close();
    }
}
```

### Tools:
```
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
    private static final HashSet<String> varTypes = new HashSet<>(Arrays.asList("int" , "float" , "double" , "short" , "long"
            , "byte" , "char" , "bool"));
    private static final HashSet<String> operator = new HashSet<>(Arrays.asList("(",")","{","}","[","]",
            ";","=","==","!=","+","-","++","--","<",">","<=",">=","&","&&","|","||" , "+=" , "/=" , "-=" , "*="));

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
        try {
            Double.parseDouble(n);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isVarType(String word){
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

```
