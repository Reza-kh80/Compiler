import java.io.*;
import java.util.*;


public class Main {

    static int cursor = 0;

    public static void main(String[] args) throws IOException {
        File input = new File(".//input.txt");

        Scanner scanner = new Scanner(new FileInputStream(input));
        String s;
        ArrayList<String> tokens = new ArrayList<>();
        while (scanner.hasNext()) {
            cursor = 0;
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
                else if(!Tools.isWhiteSpace(word)) {
                    if (Tools.isSymbol(word))
                        tokens.add("identifier:: " + Tools.getSymbol(word));
                    else {
                        tokens.add("identifier:: " + lastWord);
                        Tools.addToSymbols(word, lastWord);
                    }
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
/*
int main(){
int a=3;
float b=5;
int c=7;
int d=a+c;
for(int i=0;i<3;i++){
}

}
 */
