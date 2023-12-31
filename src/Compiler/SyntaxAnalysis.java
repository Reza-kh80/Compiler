package Compiler;

import static java.util.Collections.singletonList;
import static Compiler.SyntaxAnalysis.identifiers;
import static Compiler.SyntaxAnalysis.numbers;
import static Compiler.SyntaxAnalysis.print;
import static Compiler.SyntaxAnalysis.stringLiterals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.swing.tree.DefaultMutableTreeNode;


public class SyntaxAnalysis {
    static ArrayList<String> identifiers;
    static List<String> numbers, stringLiterals;
    static boolean testMode = false;

    public static void start(String inputFileName, String outputFileName) throws IOException {

        identifiers = new ArrayList<>();
        numbers = new ArrayList<>();
        stringLiterals = new ArrayList<>();
        LexicalAnalysis.start(inputFileName, outputFileName);

        //import grammar:
        Grammar.importGrammar();
        Grammar.printGrammar();


        LL1Table.createTable();
        LL1Table.printTable();


        parse(outputFileName);
    }

    private static void parse(String outputFileName) throws IOException {
        List<String> tokens = LexicalAnalysis.tokens;

        print("..........PARS...........");
        print("Tokens: " + tokens);

        Stack<String> stack = new Stack<>();
        Stack<DefaultMutableTreeNode> nodeStack = new Stack<>();
        stack.push("<program>");
        DefaultMutableTreeNode treeRoot = new DefaultMutableTreeNode("<program>");
        nodeStack.push(treeRoot);

        int i = 0;
        while (i < tokens.size()) {
            print("\n");
            String stackTop = stack.lastElement();
            String[] theToken = tokens.get(i).split(":: ");
            print("theToken: " + theToken[1] + " stackTop: " + stackTop);
            print("Stack: " + stack);
            if (stackTop.equals(theToken[1])) {
                nodeStack.pop();
                stack.pop();
                i++;
                continue;
            } else if (!Grammar.allGrammars.containsKey(stackTop)) {
                Tools.writeErrorToFile(Tools.SYNTAX_ANALYSIS,
                        outputFileName, "Stack top does not match with the token" + "The token: " + theToken[1] + " Stack top: " + stackTop);
                return;
            }
            List<String>[] struct = LL1Table.getStructure(stackTop, theToken[1]);
            if (struct == null) {
                Tools.writeErrorToFile(Tools.SYNTAX_ANALYSIS,
                        outputFileName, "This cell in table is empty: " + "[" + theToken[1] + "," + stackTop + "]");
                return;
            } else {
                List<String> theStruct = null;

                if (struct.length == 1) {
                    theStruct = struct[0];
                } else {

                    if (stackTop.equals("<var declaration>")) {
                        boolean hasEqual = false;
                        for (int j = i; j < tokens.size(); j++) {
                            String t = tokens.get(j).split(":: ")[1];

                            if (t.equals("=")) {
                                hasEqual = true;
                                break;
                            } else if (t.equals(";"))
                                break;
                        }
                        print("hasEqual : " + hasEqual);
                        String v = tokens.get(i + 1).split(":: ")[1];
                        print("variable: " + v);
                        for (List<String> str : struct) {
                            if (v.equals(str.get(1))) {
                                if (str.size() < 4 && !hasEqual) {
                                    theStruct = str;
                                    break;
                                } else if (str.size() > 4 && hasEqual) {
                                    theStruct = str;
                                    break;
                                }
                            }
                        }
                    } else if (stackTop.equals("<if statement>")) {
                        for (int j = i + 1; j < tokens.size(); j++) {
                            String t = tokens.get(j).split(":: ")[1];
                            if (t.equals("if")) {
                                theStruct = struct[0];
                                break;
                            } else if (t.equals("else")) {
                                theStruct = struct[1];
                                break;
                            }
                        }
                        if (theStruct == null)
                            theStruct = struct[0];

                    } else if (stackTop.equals("<for statement>")) {
                        String t = tokens.get(i + 2).split(":: ")[1];
                        print(t);
                        if (Tools.isVarType(t))
                            theStruct = struct[1];
                        else
                            theStruct = struct[0];
                    } else if (stackTop.equals("<assignment>")) {
                        String t = tokens.get(i + 1).split(":: ")[1];
                        print(t);
                        if (t.equals("=")) {
                            theStruct = struct[0];
                        } else if (t.equals("++")) {
                            theStruct = struct[1];
                        } else if (t.equals("--")) {
                            theStruct = struct[2];
                        } else {
                            theStruct = struct[3];
                        }
                    }

                }

                stack.pop();
                DefaultMutableTreeNode treeNode = nodeStack.pop();
                print(theStruct.toString());
                if (!theStruct.contains(Grammar.EPSILON)) {
                    ArrayList<DefaultMutableTreeNode> children = new ArrayList<>();
                    for (int j = theStruct.size() - 1; j > -1; j--) {
                        String st = theStruct.get(j);
                        stack.push(st);
                        DefaultMutableTreeNode d = new DefaultMutableTreeNode(st);
                        nodeStack.push(d);
                        children.add(0, d);
                    }
                    for (DefaultMutableTreeNode child : children) {
                        treeNode.add(child);
                    }

                }

            }

        }

        System.out.println(outputFileName + " successfully parsed");
        SwingDemo.showTree(treeRoot, outputFileName);
    }

    static void print(String s) {
        if (testMode) {
            System.out.println(s);
        }
    }

    static void print() {
        print("");
    }
}


class Grammar {
    static final String EPSILON = "ɛ", ID = "?ID?", NUMBER = "?NUMBER?", STRING_LITERAL = "?STRING?";
    static HashMap<String, List<String>[]> allGrammars = new HashMap<>();
    String name;
    List<String>[] structures;

    @SafeVarargs
    Grammar(String name, List<String>... structures) {
        this.name = name;
        this.structures = structures;
        allGrammars.put(name, structures);
    }

    // function importgrammer
    static void importGrammar() {
        new Grammar("<program>", Arrays.asList("int", "main", "(", ")", "{", "<statements>", "}"));

        new Grammar("<if statement>", Arrays.asList("if", "(", "<condition>", ")", "{", "<statements>", "}"),
                Arrays.asList("if", "(", "<condition>", ")", "{", "<statements>", "}", "else", "{", "<statements>", "}"));

        new Grammar("<for statement>", Arrays.asList("for", "(", "<assignment>", ";", "<condition>", ";", "<assignment>", ")", "{", "<statements>", "}"),
                Arrays.asList("for", "(", "<var declaration>", "<condition>", ";", "<assignment>", ")", "{", "<statements>", "}"));

        new Grammar("<while statement>", Arrays.asList("while", "(", "<condition>", ")", "{", "<statements>", "}"));

        new Grammar("<condition>", Arrays.asList("<expression>", "<relational operator>", "<expression>"));

        new Grammar("<relational operator>", singletonList("<"), singletonList(">"), singletonList("<="),
                singletonList(">="), singletonList("=="), singletonList("!="));

        createGrammar("<var declaration>",
                Arrays.asList("<var_type>",
                        ID, ";"), Arrays.asList("<var_type>", ID, "=", "<expression>", ";"));

        new Grammar("<expression>", Arrays.asList("<term>", "<expression'>"));

        new Grammar("<expression'>", Arrays.asList("+", "<term>", "<expression'>"),
                Arrays.asList("-", "<term>", "<expression'>"), singletonList(EPSILON));

        new Grammar("<term>", Arrays.asList("<factor>", "<term'>"));

        new Grammar("<term'>", Arrays.asList("*", "<factor>", "<term'>"),
                Arrays.asList("/", "<factor>", "<term'>"), singletonList(EPSILON));


        List<String>[] struct = new List[identifiers.size() + numbers.size() + 1 + stringLiterals.size()];
        int index = 0;
        for (int i = 0; i < identifiers.size(); i++) {
            struct[index++] = singletonList(identifiers.get(i));
        }
        for (int i = 0; i < numbers.size(); i++) {
            struct[index++] = singletonList(numbers.get(i));
        }
        for (int i = 0; i < stringLiterals.size(); i++) {
            struct[index++] = singletonList(stringLiterals.get(i));
        }
        struct[index] = Arrays.asList("(", "<expression>", ")");
        new Grammar("<factor>", struct);

        new Grammar("<statements>", Arrays.asList("<statement>", "<statements>"));

        new Grammar("<statement>", Arrays.asList("<assignment>", ";"), singletonList("<var declaration>"),
                singletonList("<if statement>"), singletonList("<for statement>"), singletonList("<while statement>"));

        createGrammar("<assignment>", Arrays.asList(ID, "=", "<expression>"), Arrays.asList(ID, "++"),
                Arrays.asList(ID, "--"), Arrays.asList(ID, "<opt>", "<expression>"));

        new Grammar("<opt>", singletonList("+="), singletonList("-="), singletonList("/="), singletonList("*="));


        List<String> varTypes = new ArrayList<>(Tools.getVarTypes());
        List<String>[] varTypeLists = new List[varTypes.size()];

        for (int i = 0; i < varTypeLists.length; i++) {
            varTypeLists[i] = singletonList(varTypes.get(i));
        }
        new Grammar("<var_type>", varTypeLists);


    }


    private static void createGrammar(String grammarName, List<String>... textHolders) {
        List<String>[] struct = new List[textHolders.length * identifiers.size()];
        int index = 0;
        for (List<String> rule : textHolders) {
            for (String id : identifiers) {
                List<String> idRule = new ArrayList<>();
                for (String part : rule) {
                    if (part.equals(ID))
                        idRule.add(id);
                    else
                        idRule.add(part);
                }
                struct[index++] = idRule;
            }
        }

        new Grammar(grammarName, struct);
    }

    static void printGrammar() {
        print("\t\t" + "GRAMMAR");
        Set<String> keys = allGrammars.keySet();
        for (String key : keys) {
            print(key + " : ");
            List<String>[] structure = allGrammars.get(key);
            for (List<String> list : structure) {
                print(list + " , ");
            }
            print();
        }
        print("\t\t" + "---------");
    }
}

class LL1Table {
    static HashMap<String, Map<String, List<String>[]>> table = new HashMap<>();

    static void createTable() {
        HashMap<String, List<String>[]> allGrammars = Grammar.allGrammars;
        // <program> : { {} }
        table.put("<program>", Map.of("int", allGrammars.get("<program>")));
        table.put("<if statement>", Map.of("if", allGrammars.get("<if statement>")));
        table.put("<for statement>", Map.of("for", allGrammars.get("<for statement>")));
        table.put("<while statement>", Map.of("while", allGrammars.get("<while statement>")));

        //"<condition>"
        List<String>[] conditionStructure = allGrammars.get("<condition>");
        Map<String, List<String>[]> tRow = new HashMap<>();
        tRow.put("(", conditionStructure);
        for (String id : identifiers) {
            tRow.put(id, conditionStructure);
        }
        for (String number : numbers) {
            tRow.put(number, conditionStructure);
        }
        for (String literal : stringLiterals) {
            tRow.put(literal, conditionStructure);
        }
        table.put("<condition>", tRow);
        tRow = new HashMap<>();
        //....................


        //<relational operator>
        List<String>[] relationOperators = allGrammars.get("<relational operator>");
        for (List<String> list : relationOperators) {
            List[] lists = new List[1];
            lists[0] = singletonList(list.get(0));
            tRow.put(list.get(0), lists);
        }
        table.put("<relational operator>", tRow);
        tRow = new HashMap<>();

        //<var declration>
        List<String>[] varTypeStructure = allGrammars.get("<var declaration>");
        for (String varType : Tools.getVarTypes()) {
            tRow.put(varType, varTypeStructure);
        }
        table.put("<var declaration>", tRow);
        tRow = new HashMap<>();
        //....................

        //<expression>
        List<String>[] expStructure = allGrammars.get("<expression>");
        tRow.put("(", expStructure);
        for (String id : identifiers) {
            tRow.put(id, expStructure);
        }
        for (String num : numbers) {
            tRow.put(num, expStructure);
        }
        for (String str : stringLiterals) {
            tRow.put(str, expStructure);
        }
        table.put("<expression>", tRow);
        tRow = new HashMap<>();
        //...................

        //<expression'>
        List<String>[] expPrimStructure = allGrammars.get("<expression'>");
        List<String>[] epsilon = new List[1];
        epsilon[0] = Arrays.asList(Grammar.EPSILON);
        String arr[] = {">=", "<=", ">", "<", "==", "!=", ";", ")"};
        for (String op : arr) {
            tRow.put(op, epsilon);
        }
        String arr2[] = {"+", "-"};
        for (String op1 : arr2) {
            if (op1.equals("+")) {
                List[] listPlus = new List[1];
                listPlus[0] = expPrimStructure[0];
                tRow.put(op1, listPlus);
            } else {
                List[] listMinus = new List[1];
                listMinus[0] = expPrimStructure[1];
                tRow.put(op1, listMinus);
            }
        }
        table.put("<expression'>", tRow);
        tRow = new HashMap<>();
        //.................

        //<term>
        List<String>[] termStructure = allGrammars.get("<term>");
        tRow.put("(", termStructure);
        for (String id : identifiers) {
            tRow.put(id, termStructure);
        }
        for (String num : numbers) {
            tRow.put(num, termStructure);
        }
        for (String str : stringLiterals) {
            tRow.put(str, termStructure);
        }
        table.put("<term>", tRow);
        tRow = new HashMap<>();
        //....................

        //<term'>
        List<String>[] termPrimStructure = allGrammars.get("<term'>");
        String arr3[] = {">=", "<=", ">", "<", "==", "!=", "+", "-", ";", "(", ")"};
        for (String op : arr3) {
            tRow.put(op, epsilon);
        }
        String arr4[] = {"*", "/"};
        for (String op1 : arr4) {
            if (op1.equals("*")) {
                List[] listMult = new List[1];
                listMult[0] = termPrimStructure[0];
                tRow.put(op1, listMult);
            } else {
                List[] listDivide = new List[1];
                listDivide[0] = termPrimStructure[1];
                tRow.put(op1, listDivide);
            }
        }
        table.put("<term'>", tRow);
        tRow = new HashMap<>();
        //..................

        //<factor>
        List<String>[] factorStructure = allGrammars.get("<factor>");
        List<String>[] list = new List[1];
        list[0] = factorStructure[factorStructure.length - 1];
        tRow.put("(", list);
        ArrayList<String> total = new ArrayList<>();
        total.addAll(identifiers);
        total.addAll(numbers);
        total.addAll(stringLiterals);

        for (String check : total) {
            List<String>[] FS = new List[1];
            FS[0] = Arrays.asList(check);
            tRow.put(check, FS);
        }

        table.put("<factor>", tRow);
        tRow = new HashMap<>();
        //................

        //<statement>
        List<String>[] stateStructure = allGrammars.get("<statement>");
        for (String vartype : Tools.getVarTypes()) {
            List[] vartp = new List[1];
            vartp[0] = stateStructure[1];
            tRow.put(vartype, vartp);
        }
        for (String id : identifiers) {
            List[] iden = new List[1];
            iden[0] = stateStructure[0];
            tRow.put(id, iden);
        }
        String arr5[] = {"if", "for", "while"};
        for (String op : arr5) {
            if (op.equals("if")) {
                List[] listIf = new List[1];
                listIf[0] = stateStructure[2];
                tRow.put(op, listIf);
            } else if (op.equals("for")) {
                List[] listIf = new List[1];
                listIf[0] = stateStructure[3];
                tRow.put(op, listIf);
            } else {
                List[] listWhile = new List[1];
                listWhile[0] = stateStructure[4];
                tRow.put(op, listWhile);
            }
        }
        table.put("<statement>", tRow);
        tRow = new HashMap<>();
        //........................................

        //<statements>
        List<String>[] statesStructure = allGrammars.get("<statements>");
        tRow.put("}", epsilon);
        for (String varType : Tools.getVarTypes()) {
            tRow.put(varType, statesStructure);
        }
        for (String id : identifiers) {
            tRow.put(id, statesStructure);
        }
        String arr6[] = {"if", "for", "while"};
        for (String op : arr6) {
            if (op.equals("if")) {
                tRow.put(op, statesStructure);
            } else if (op.equals("for")) {
                tRow.put(op, statesStructure);
            } else {
                tRow.put(op, statesStructure);
            }
        }
        table.put("<statements>", tRow);
        tRow = new HashMap<>();
        //........................................

        //<opt>
        List<String>[] optStructure = allGrammars.get("<opt>");
        String arr7[] = {"+=", "-=", "*=", "/="};
        for (String op : arr7) {
            if (op.equals("*=")) {
                List[] _M_ = new List[1];
                _M_[0] = optStructure[3];
                tRow.put(op, _M_);
            } else if (op.equals("/=")) {
                List[] _D_ = new List[1];
                _D_[0] = optStructure[2];
                tRow.put(op, _D_);
            } else if (op.equals("+=")) {
                List[] _P_ = new List[1];
                _P_[0] = optStructure[0];
                tRow.put(op, _P_);
            } else {
                List[] _MM_ = new List[1];
                _MM_[0] = optStructure[1];
                tRow.put(op, _MM_);
            }
        }
        table.put("<opt>", tRow);
        tRow = new HashMap<>();

        //..............................

        //<var_type>
        List<String>[] vartypeStructure = allGrammars.get("<var_type>");
        int i = 0;
        for (String vartype : Tools.getVarTypes()) {
            List[] Type = new List[1];
            Type[0] = vartypeStructure[i];
            tRow.put(vartype, Type);
            i++;
        }
        table.put("<var_type>", tRow);
        tRow = new HashMap<>();

        //...................

        for (String id : identifiers) {
            List<String>[] list2 = new List[]{Arrays.asList(id, "=", "<expression>"), Arrays.asList(id, "++"),
                    Arrays.asList(id, "--"), Arrays.asList(id, "<opt>", "<expression>")};
            tRow.put(id, list2);

        }
        table.put("<assignment>", tRow);
        tRow = new HashMap<>();
    }

    static void printTable() {
        ArrayList<String> terminals = new ArrayList<>(Grammar.allGrammars.keySet());
        for (String terminal : terminals) {
            Map<String, List<String>[]> map = table.get(terminal);
            if (map == null)
                continue;
            print(terminal + "  : ");
            for (String key :
                    map.keySet()) {
                List<String>[] lists = map.get(key);
                print(key + " : ");
                for (List<String> list : lists) {
                    print(list.toString());
                }

            }
            print();
        }
    }


    static List<String>[] getStructure(String nonTerminal, String terminal) {
        return table.get(nonTerminal).get(terminal);
    }


}