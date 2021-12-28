

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

import static java.util.Collections.singletonList;

public class phase3 {
    public static final String EPSILON = "ɛ";
    public static void main(String[] args) throws FileNotFoundException {
        //import grammar:
        new Grammar("<program>" , Arrays.asList("int main" , "(" , ")" , "{" ,"<statements>" , "}"  ));

        new Grammar("<if statement>" , Arrays.asList("if" , "(" , "<condition>" ,")" , "{" ,"<statements>" , "}" ) ,
                Arrays.asList("if" , "(" , "<condition>" ,")" , "{" ,"<statements>" , "}" , "else" , "{" , "<statements>" , "}"));

        new Grammar("<for statement>" , Arrays.asList("for" , "(" ,"<assignment>" , "<condition>" , ";" , "<assignment>", ")" , "{" ,"<statements>" , "}") ,
                Arrays.asList("for" , "(" ,"<var declaration>" , "<condition>" , ";" , "<assignment>", ")" , "{" ,"<statements>" , "}"  ));

        new Grammar("<while statement>" , Arrays.asList("while" , "(" , "<condition>",")" , "{" ,"<statements>" , "}"  ));

        new Grammar("<condition>" , Arrays.asList("<expression>" , "<relational operator>" , "<expression>"  ));

        new Grammar("<relational operator>" , singletonList("<"), singletonList(">"), singletonList("<="), singletonList(">="), singletonList("=="));

        new Grammar("<var declaration>" , Arrays.asList("<var_type>" , "ID" , ";" ) ,  Arrays.asList("<var_type>" , "ID" , "=" ,"<expression>" ,";" ));

        new Grammar("<expression>" , Arrays.asList("<term>" , "<expression’>"));

        new Grammar("<expression'>" , Arrays.asList("+" , "<term>" , "<expression'>")  , Arrays.asList("-" , "<term>" , "<expression'>") , singletonList(EPSILON));

        new Grammar("<term>" , Arrays.asList("<factor>" , "<term'>") );

        new Grammar("<expression'>" , Arrays.asList("*" , "<factor>" , "<term'>")  , Arrays.asList("/" , "<factor>" , "<term'>") , singletonList(EPSILON));


        new Grammar("<>" , Arrays.asList());
/*
<statements> :: <statement> <statements>

<statement> :: <assignment> | <var declaration>  | <if statement> | <for statement> | <while statement>

<assignment> :: id = <expression>; |id++;|id--;|id<opt> = <expression>;

<opt>:: +|-|/|*

<var_type> :: int | float | double | short | long | byte | char | bool

<letter>:: a | b | c | d |...| A | B | C | D |...

<digit> :: 0 |1 | 2 | 3 | 4 | 5 | 6 | 7 | 8| 9

<string_literal> :: <quote><character><quote> | <quote><characters><quote>

<character> :: <letter> | <digit>

<characters> :: <character><characters>

<quote> :: "



 */









                File input = new File(".//input2.txt");

        Scanner scanner = new Scanner(new FileInputStream(input));

    }
}
class Grammar{
    static HashMap<String , List<String>[]> allGrammars = new HashMap<>();
    String name;
    List<String>[] structures;

    @SafeVarargs
    Grammar(String name , List<String>... structures ){
        this.name = name;
        this.structures = structures;
        allGrammars.put(name , structures);
    }
}