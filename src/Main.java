import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;


public class Main {

    static int cursor = 0;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
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
                    System.out.println("keyword:: " + word);
                } else if (Tools.isOperator(word)) {
                    System.out.println("operator:: " + word);
                }else if (Tools.isNumber(word)){
                    System.out.println("number:: " + word);
                }
                else if(!Tools.isWhiteSpace(word)) {
                    if (Tools.isSymbol(word))
                        System.out.println("identifier:: " + Tools.getSymbol(word));
                    else {
                        System.out.println("identifier:: " + lastWord);
                        Tools.addToSymbols(word, lastWord);
                    }
                }
                lastWord = word;

            }

        }

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
