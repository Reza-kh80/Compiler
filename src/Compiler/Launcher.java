package Compiler;

import java.io.IOException;


public class Launcher {
    public static void main(String[] args) {
        int fileNum = 0;
        while (true) {
            try {
                String outputFileName = ".//output" + fileNum + ".txt";
                String inputFileName = ".//input" + fileNum + ".txt";

                System.out.println("File " + inputFileName + " Reading...");
                SyntaxAnalysis.start(inputFileName, outputFileName);
                System.out.println();
            } catch (IOException e) {
                System.out.println("File " + "\"input" + fileNum + ".txt\"" + " fot found");
                break;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                fileNum++;

            }
        }
    }
}
