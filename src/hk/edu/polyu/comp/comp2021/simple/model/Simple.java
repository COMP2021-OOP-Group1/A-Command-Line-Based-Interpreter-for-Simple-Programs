package hk.edu.polyu.comp.comp2021.simple.model;

import java.util.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.FileWriter;

public class Simple extends Parser {
    final static int maxInt = 99999;
    final static int minInt = -99999;

    public Simple(){
        Parser parser = new Parser();
    }
    
    protected static void vardef(String[] str) {   //* REQ1 - Works
        varMap.put(str[3], expRef(str[4]));
        putVarHistoryMap(str[3]);
        updateExp();
    }


    protected static void binExpr(String[] str) { //* REQ2

        Object a = expRef(str[2]);
        Object b = expRef(str[4]);
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
                if (a + b > maxInt) Parser.addResultExp(label, maxInt);
                else if (a + b < minInt) Parser.addResultExp(label, minInt);
                else Parser.addResultExp(label, a + b);
                break;
            case "-":
                addResultExp(label, a - b);
                if (a - b > maxInt) Parser.addResultExp(label, maxInt);
                else if (a - b < minInt) Parser.addResultExp(label, minInt);
                else Parser.addResultExp(label, a - b);
                break;
            case "*":
                if (a * b > maxInt) Parser.addResultExp(label, maxInt);
                else if (a * b < minInt) Parser.addResultExp(label, minInt);
                else Parser.addResultExp(label, a * b);
                break;
            case "/":
                if (b != 0){
                    if (a / b > maxInt) Parser.addResultExp(label, maxInt);
                    else if (a / b < minInt) Parser.addResultExp(label, minInt);
                    else Parser.addResultExp(label, a / b);
                }
                break;
            case ">":
                addResultExp(label, a > b);
                break;
            case "<":
                addResultExp(label, a < b);
                break;
            case ">=":
                addResultExp(label, a >= b);
                break;
            case "<=":
                addResultExp(label, a <= b);
                break;
            case "%":
                addResultExp(label, a % b);
                break;
            case "==":
                addResultExp(label, a == b);
                break;
            case "!=":
                addResultExp(label, a != b);
                break;
        }

    }

    protected static void evaluateBoolExp(Boolean a, Boolean b, String operator, String label){ //* Req 2.2

        // Evaluates Boolean expression. 

        switch (operator){
            case "&&":
                addResultExp(label, a && b);
                break;
            case "||":
                addResultExp(label, a || b);
                break;
            case "==":
                addResultExp(label, a == b);
                break;
            case "!=":
                addResultExp(label, a != b);
                break;
        }
    }

    protected static void unexpr(String expName, String operator, String expRef1) { //* REQ3
        

        if (operator.equals("!")){   // Negates Boolean expression
            if ((boolean)expRef(expRef1)) {
                varMap.put(expName, false);
                putVarHistoryMap(expName);
            }
            else {
                addResultExp(expName, true);
            }
        }

        else if (operator.equals("~")){  // Negates Int Expression by switching symbols
            
            int number = (int)expRef(expRef1) * -1;
            if (operator.equals("~"))
                addResultExp(expName, number);
        }
    }


    protected static void assign(String varName, String expRef) {  //* REQ4

        // Get object in variable
        Object toAdd = expRef(expRef);
        
        // Change variable value
        varMap.replace(varName, toAdd);
        putVarHistoryMap(varName);
        updateExp();

    }

    protected static void updateExp(){
        // Update stored statements that contain this variable
        String[] command;
        for (String key: resultExp.keySet()){
            
            command = new String[]{};
            if (expRefLabelCmd.containsKey(key)){
                
                command = expRefLabelCmd.get(key).split(" ");

                if (command[0].equals("unexpr")) unexpr(command[1], command[2], command[3]);
                else if (command[0].equals("binexpr")) binExpr(command);
            }
        }
    }

    protected static void print(String label, String expRef) {   //* REQ5
        String value = "";
        value = value + expRef(expRef).toString();
        System.out.print('[' + value +']');
        addResultExp(label, '[' + value +']');
    }

    protected static void skip(){}   //* REQ6

    protected static void block(String[] instructions){  //* REQ7

        int n = instructions.length;
        for (int i = 0; i < n; i++){

            updateExp();
            if (labelCMDMap.containsKey(instructions[i]))
                classification(labelCMDMap.get(instructions[i]));
            else if (expRefLabelCmd.containsKey(instructions[i]))
                classification(labelCMDMap.get(instructions[i]));
        }

    }

    protected static void ifF(String expRef, String statementLab1, String statementLab2){    //* REQ

        // Check if the condition is TRUE or FALSE:
        // if true - <K,V> save V for label ex1 in labelCMDMap
        // if false - <K,V> save V for label ex2 in labelCMDMap

        if ((boolean)expRef(expRef)){
            classification(labelCMDMap.get(statementLab1));
        }else {
            classification(labelCMDMap.get(statementLab2));
        }

        updateExp();

    }

    protected static void whileW(String expRef, String statementLab1) {   //* REQ9

        

        while((boolean)expRef(expRef)) {
            classification(labelCMDMap.get(statementLab1));
            updateExp();
        }
    }
    protected static void program(String programName, String statementLabel) {  //* REQ10
        programMap.put(programName, statementLabel);
    }

    protected static void execute(String programName){  //* REQ11
        classification(Parser.labelCMDMap.get(Parser.programMap.get(programName)));
    }

    protected static void list(String instruction, ArrayList<String> added) {    //* REQ12

        // Get instruction from the map
        
        String[] fullInst;
        if (labelCMDMap.containsKey(instruction)) fullInst = labelCMDMap.get(instruction).split(" ");
        else{
            fullInst = expRefLabelCmd.get(instruction).split(" ");
        }

        if (!added.contains(instruction)){
            
            if (blockMap.containsKey(instruction)){  // If program statement is a block
                String block[] = blockMap.get(fullInst[1]);
                
                if (!added.contains(instruction)) System.out.println(labelCMDMap.get(instruction));
                
                for (int i = 0; i < block.length; i++) list(block[i], added); // Recurse over the instructions

            }
            else if (fullInst[0].equals("while")){    // If while loop
                if (!added.contains(instruction)) System.out.println(labelCMDMap.get(instruction));
                list(fullInst[2], added);
                list(fullInst[3], added);
            }
            else if (fullInst[0].equals("if")){
                if (!added.contains(instruction)) System.out.println(labelCMDMap.get(instruction));
                list(fullInst[2], added);
                list(fullInst[3], added);
                list(fullInst[4], added);
            }
        
            // Print if instruction is not a while or block or if
            else{
                queue.add(labelCMDMap.get(instruction));
                if (!added.contains(instruction)){   
                    if (labelCMDMap.containsKey(instruction)) System.out.println(labelCMDMap.get(instruction));
                    else if (expRefLabelCmd.containsKey(instruction)) System.out.println(expRefLabelCmd.get(instruction));
                    added.add(instruction);
                }

                if (fullInst[0].equals("vardef")){
                    if (expRefLabelCmd.containsKey(fullInst[4])) list(fullInst[4], added);
                }
                else if (fullInst[0].equals("unexpr") || fullInst[0].equals("assign")){
                    if (expRefLabelCmd.containsKey(fullInst[3])) list(fullInst[3], added);
                }
                else if (fullInst[0].equals("print")){
                    if (expRefLabelCmd.containsKey(fullInst[2])) list(fullInst[2], added);
                }
                else if (fullInst[0].equals("binexpr")){
                    if (expRefLabelCmd.containsKey(fullInst[2])) list(fullInst[2], added);
                    if (expRefLabelCmd.containsKey(fullInst[4])) list(fullInst[4], added);
                }
            
            
            }


        }

        

        
    }

    protected static void togglebreakpoint(String programName, String label) {

    }



    protected static void debug(String programName) {
        storeQueue(programMap.get(programName));
        if (DebugPoint < 1) {
            currentDebugPoint = breakPointMap.get(programName);
            currentInspect = currentDebugPoint;
            DebugPoint++;
        }
        try {
            while (true) {
                if (!stack.peek().split(" ")[1].equals(currentDebugPoint) || currentDebugPoint.equals("")) {
                    stack.pop();
                } else {
//                    if (stack.peek().split(" ")[0].equals("while")) {
//                        System.out.println((boolean)resultExp.get(stack.peek().split(" ")[2]));
//                        String cmd = expRefLabelCmd.get(stack.peek().split(" ")[2]);
//                        System.out.println(cmd.replace(cmd.split(" ")[2], currentVarValue));
//                        if (!(boolean)resultExp.get(stack.peek().split(" ")[2])) {
//                            break;
//                        }
//                        for (int i = runArray.size(); i >= 0; i--) {
//                            stack.add(runArray.get(i));
//                        }
//                    }
//                    System.out.println(stack);
//                    System.out.println(stack.peek());
                    String peekCMD = stack.peek();
                    System.out.println("Debugging ==> " + peekCMD);
                    inspect(peekCMD.split(" "));
                    runArray.add(peekCMD);
                    stack.pop();
                    currentInspect = currentDebugPoint;
                    currentDebugPoint = stack.peek().split(" ")[1];
                    stack.clear();
                    break;
                }
            }
        } catch (Exception e) {
            System.out.print("Finish Debug!");
            System.out.println();
        }
    }

    protected static void inspect(String[] strSplit) {
        String programName = strSplit[1];
        String varName = strSplit[2];
        if (strSplit[0].equals("inspect")) {
            System.out.println("<" + varHistoryMap.get(varName).get(index) + ">");
        } else {
            index++;
        }
    }

    public static void store(String programName, String address) throws IOException {

        // Create File
        try {
            java.io.File myObj = new java.io.File(address + ".txt");
            if (myObj.createNewFile()) {
                System.out.println("File created");
            } else {
                System.out.println("Program already exists.");
            }
        } catch (IOException e) {
            System.out.println("Error!");
            e.printStackTrace();
        }

        // Write the code from map into file
        try {
            FileWriter myWriter = new FileWriter(address + ".txt");
            String CMD = "";

            storeQueue(programMap.get(programName));
            int sizeCount = 0;
            for (int i = cmdMap.size(); i >= 1; i--) {
                if (cmdMap.get(i).contains("vardef") || cmdMap.get(i).contains("binexpr") || cmdMap.get(i).contains("unexpr")) {
                    queue.add(cmdMap.get(i));
                    sizeCount = sizeCount + 1;
                }
            }
            for (int i = 0; i <= queue.size() + 1 + sizeCount; i++) {
                CMD = queue.peek() + "\n" + CMD;
                queue.remove();
            }

            myWriter.write(CMD);
            myWriter.close();
            System.out.println("Program Created!");
        } catch (IOException e) {
            System.out.println("Error!");
            e.printStackTrace();
        }
    }

    public static void load(String fileAddress, String programName) throws IOException {
        // Read the code in the file to a list and use loop to store the lines to map by
//        ArrayList<String> cmdList = new ArrayList<>();
        List ProgramCMDList = Files.readAllLines(Paths.get(fileAddress + ".txt"));
        for (int i = 0; i < ProgramCMDList.size(); i++) {
            String cmd = (String)ProgramCMDList.toArray()[i];
            cmdMap.put(i + 1, cmd);
        }
        for (int i = 1; i < ProgramCMDList.size(); i++) {
            storeCommand(cmdMap.get(i));
        }
    }
}

