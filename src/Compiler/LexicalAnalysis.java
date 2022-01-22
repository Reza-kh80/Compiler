package Compiler;

import static Compiler.SyntaxAnalysis.identifiers;
import static Compiler.SyntaxAnalysis.numbers;
import static Compiler.SyntaxAnalysis.stringLiterals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class LexicalAnalysis {

    static List<String> tokens;
    static int cursor = 0;


    public static void start(String inputFileName , String outputFileName) throws IOException {
        File input = new File(inputFileName);

        Scanner scanner = new Scanner(new FileInputStream(input));
        String s;

        tokens = new ArrayList<>();
        Tools.symbolTable = new HashMap<>();

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
                if (line[cursor] == '\"') {
                    StringBuilder stringLiteral = new StringBuilder("\"");
                    do {
                        cursor++;
                        if (cursor >= line.length) {
                            Tools.writeErrorToFile(Tools.LEXICAL_ANALYSIS , outputFileName ,"\" expected in line " + lineNum);
                            return;
                        }
                        stringLiteral.append(line[cursor]);
                    } while (line[cursor] != '\"');

                    tokens.add("string_literal:: " + stringLiteral.toString());
                    stringLiterals.add(stringLiteral.toString());
                    cursor++;
                    continue;
                }

                String word = readNextWord(line);
                if (Tools.isKeyword(word)) {
                    tokens.add("keyword:: " + word);
                } else if (Tools.isOperator(word)) {
                    tokens.add("operator:: " + word);
                } else if (Tools.isNumber(word)) {
                    tokens.add("number:: " + word);
                    numbers.add(word);
                } else if (Tools.isIdentifier(word)) {
                    if (Tools.isSymbol(word))
                        tokens.add("id:: " + word);
                    else if (Tools.isVarType(lastWord)) {
                        tokens.add("id:: " + word);
                        identifiers.add(word);
                        Tools.addToSymbols(word, lastWord);
                    } else {
                        Tools.writeErrorToFile(Tools.LEXICAL_ANALYSIS , outputFileName ,
                                Tools.LEXICAL_ANALYSIS + " error in line " + lineNum + " : " + word + " is not define");
                        return;
                    }
                } else if (!Tools.isWhiteSpace(word)) {
                    Tools.writeErrorToFile(Tools.LEXICAL_ANALYSIS , outputFileName
                            , Tools.LEXICAL_ANALYSIS + " error in line " + lineNum + " : " + word + " is not define");
                    return;
                }
                lastWord = word;

            }


        }

        Tools.writeTokensToOutputFile(outputFileName , tokens);

    }

    static String readNextWord(char[] line) {// i++;
        StringBuilder tempStr = new StringBuilder(); //+
        while (cursor < line.length && !Tools.isWhiteSpace(line[cursor])) {

            if (Tools.isOperator(line[cursor]) || line[cursor] == '!') {
                if (tempStr.toString().isEmpty()) {
                    tempStr.append(line[cursor]);
                    cursor++;
                    if (cursor >= line.length) {
                        return tempStr.toString();
                    }

                    String temp2 = tempStr.toString() + line[cursor]; //++

                    if (Tools.isOperator(temp2)) {
                        cursor++;
                        return temp2;
                    } else {
                        return tempStr.toString();
                    }

                } else
                    return tempStr.toString();
            } else {

                tempStr.append(line[cursor]);
                cursor++;
            }
        }
        cursor++;
        return tempStr.toString();
    }







}
/*
int start(){
int a=3;
float b=5;
int c=7;
int d=a+c;
for(int i=0;i<3;i++){
}

}
 */
