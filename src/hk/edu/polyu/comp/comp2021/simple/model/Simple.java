package hk.edu.polyu.comp.comp2021.simple.model;

import java.util.ArrayList;
import java.util.List;

public class Simple {
    static Parser parser;

    public Simple(){
        Parser parser = new Parser();
    }
    
    protected static void vardef(String[] str) {   //* REQ1 - Works
        Parser.varMap.put(str[3], Parser.expRef(str[4]));
        updateExp();
    }


    protected static void binExpr(String[] str) { //* REQ2

        Object a = Parser.expRef(str[2]);
        Object b = Parser.expRef(str[4]);
        String operator = str[3];
        String label = str[1];

        // If integer
        if (a instanceof Integer && b instanceof Integer){evaluateIntExp((int)a, (int)b, operator, label);}

        // If boolean
        else if (a instanceof Boolean && b instanceof Boolean){evaluateBoolExp((boolean)a, (boolean)b, operator, label);}

    }

    protected static void evaluateIntExp(int a, int b, String operator, String label){   //* Req 2.1

        // Evaluate int expressions and adds int result

        switch (operator){
            case "+":
                Parser.addResultExp(label, a + b);
                break;
            case "-":
                Parser.addResultExp(label, a - b);
                break;
            case "*":
                Parser.addResultExp(label, a * b);
                break;
            case "/":
                if (b != 0) Parser.addResultExp(label, a / b);
                break;
            case ">":
                Parser.addResultExp(label, a > b);
                break;
            case "<":
                Parser.addResultExp(label, a < b);
                break;
            case ">=":
                Parser.addResultExp(label, a >= b);
                break;
            case "<=":
                Parser.addResultExp(label, a <= b);
                break;
            case "%":
                Parser.addResultExp(label, a % b);
                break;
            case "==":
                Parser.addResultExp(label, a == b);
                break;
            case "!=":
                Parser.addResultExp(label, a != b); 
                break;
        }

    }

    protected static void evaluateBoolExp(Boolean a, Boolean b, String operator, String label){ //* Req 2.2

        // Evaluates Boolean expression. 

        switch (operator){
            case "&&":
                Parser.addResultExp(label, a && b);
                break;
            case "||":
                Parser.addResultExp(label, a || b);
                break;
            case "==":
                Parser.addResultExp(label, a == b);
                break;
            case "!=":
                Parser.addResultExp(label, a != b); 
                break;
        }
    }

    protected static void unexpr(String expName, String operator, String expRef1) { //* REQ3
        

        if (operator.equals("!")){   // Negates Boolean expression
            if ((boolean)Parser.expRef(expRef1)) Parser.varMap.put(expName, false);
            else Parser.addResultExp(expName, true);
        }

        else if (operator.equals("~")){  // Negates Int Expression by switching symbols
            
            int number = (int)Parser.expRef(expRef1) * -1;
            if (operator.equals("~")) Parser.addResultExp(expName,  number);
        }
    }


    protected static void assign(String varName, String expRef) {  //* REQ4

        // Get object in variable
        Object toAdd = Parser.expRef(expRef);
        
        // Change variable value
        Parser.varMap.replace(varName, toAdd);

        updateExp();

    }

    protected static void updateExp(){
        // Update stored statements that contain this variable
        String[] command;
        for (String key: Parser.resultExp.keySet()){
            
            command = null;
            if (Parser.expRefLabelCmd.containsKey(key)){
                
                command = Parser.expRefLabelCmd.get(key).split(" ");

                if (command[0].equals("unexpr")) unexpr(command[1], command[2], command[3]);
                else if (command[0].equals("binexpr")) binExpr(command);
            }
        }
    }

    protected static void print(String label, String expRef) {   //* REQ5
        String value = "";
        value = value + Parser.expRef(expRef).toString();
        System.out.println('[' + value +']');
        Parser.addResultExp(label, '[' + value +']');
    }

    protected static void skip(){}   //* REQ6

    protected static void block(String[] instructions){  //* REQ7

        int n = instructions.length;
        for (int i = 0; i < n; i++){

            updateExp();
            if (Parser.labelCMDMap.containsKey(instructions[i])) Parser.classification(Parser.labelCMDMap.get(instructions[i]));
            else if (Parser.expRefLabelCmd.containsKey(instructions[i])) Parser.classification(Parser.labelCMDMap.get(instructions[i]));
        }

    }

    protected static void ifF(String expRef, String statementLab1, String statementLab2){    //* REQ

        // Check if the condition is TRUE or FALSE:
        // if true - <K,V> save V for label ex1 in labelCMDMap
        // if false - <K,V> save V for label ex2 in labelCMDMap

        if ((boolean)Parser.expRef(expRef)){
            Parser.classification(Parser.labelCMDMap.get(statementLab1));
        }else {
            Parser.classification(Parser.labelCMDMap.get(statementLab2));
        }

        updateExp();

    }

    protected static void whileW(String expRef, String statementLab1) {   //* REQ9

        

        while((boolean)Parser.expRef(expRef)) {
            // Check if the condition is TRUE or FALSE:
            // if true - <K,V> save V for label ex1 in labelCMDMap and
            // run the cmd
            // if false - terminate the loop
            Parser.classification(Parser.labelCMDMap.get(statementLab1));
            updateExp();
        }
    }
    protected static void program(String programName, String statementLabel) {  //* REQ10
        //ArrayList<String> statementLabelList = new ArrayList<>();
        //statementLabelList.add(statementLabel);
        Parser.programMap.put(programName, statementLabel);
    }

    protected static void execute(String programName){  //* REQ11
        Parser.classification(Parser.labelCMDMap.get(Parser.programMap.get(programName)));
    };  

    protected static void list(String instruction) {    //* REQ12
        
        // Get instruction from the map

        if (Parser.blockMap.containsKey(instruction)){  // If program statement is a block

            String block[] = Parser.blockMap.get(instruction);

            for (int i = 0; i < block.length; i++) {
                list(block[i]); // Recurse over the instructions
            }

        }
        else if (Parser.labelCMDMap.get(instruction).split(" ")[0].equals("while")){    // If while loop
            
            // Check if instruction is block or is another one. If block repeat as above else just print simple instruction

            String fullInst = Parser.labelCMDMap.get(instruction);

            if (Parser.blockMap.containsKey(fullInst.split(" ")[3])){  // If while statement is a block
                
                System.out.println(Parser.labelCMDMap.get(instruction));

                String block[] = Parser.blockMap.get(instruction);
    
                for (int i = 0; i < block.length; i++) {
                    list(block[i]); // Recurse over the instructions
                }
    
            }

            else{
                System.out.println(Parser.labelCMDMap.get(instruction));
            }

        }
        // Print if instruction is not a while or block
        else{System.out.println(Parser.labelCMDMap.get(instruction));}

        
    }

    protected void debug(String programName) {
//        ArrayList statementLabel = Parser.programMap.get(Parser.programMap);
         String CMDLabelInProgram = Parser.programMap.get(programName);
         String CMDLabel = Parser.labelCMDMap.get(CMDLabelInProgram);
         

    }

    /*

    protected void togglebreakpoint(String programName, String label) {
        ArrayList statementLabel = Parser.programMap.get(Parser.programMap);
    }

    protected void inspect(String programName, String varName) {
        List statementLabel = Parser.programMap.get(Parser.programMap);

    }

    */

}

