package hk.edu.polyu.comp.comp2021.simple.model;

import java.io.IOException;
import java.util.*;

public class Parser {
    public static Map<Integer, String> cmdMap = new HashMap<>();
    public static Map<String, String> labelCMDMap = new HashMap<>();  // Stores Labels and its commands (Label - Command)
    public static Map<String, String> expRefLabelCmd = new HashMap<>();  // Stores expRefs and its commands (Label - Command)
    public static Map<String, Object> varMap = new HashMap<>();   // Stores Variables and Values (Variable - Value)  // Stores Variables and Bool Values (Variable - Value)
    public static Map<String, Object> resultExp = new HashMap<>();   // Stores Results of Expressions (Label - Result)
    public static Map<String, String[]> blockMap = new HashMap<>(); // Stores block of commands (Label - Command Block)
    public static Map<String, String> programMap = new HashMap<>(); // Stores the programName and the label of command
    public static Map<String, String> breakPointMap = new HashMap<>();
    public static Queue<String> queue = new LinkedList<>();
    public static Queue<String> runQueue = new LinkedList<>();
    public static Stack<String> stack = new Stack<>();
    public static int count = 0;

    public static String currentDebugPoint = "";
    public static int DebugPoint = 0;
    public static void storeCommand(String command){

        // Check if instruction is valid first
        String[] splitStr = command.split(" ");  // Split instruction into words

        if (splitStr[0].equals("vardef")){
            cmdMap.put(count, command);
            labelCMDMap.put(splitStr[1], command);
            classification(command);
        }
        else if (splitStr[0].equals("binexpr") || splitStr[0].equals("unexpr")){
            cmdMap.put(count, command);
            expRefLabelCmd.put(splitStr[1], command);
            classification(command);
            Simple.updateExp();
        }
        else if (splitStr[0].equals("block")){
            String[] instructions = Arrays.copyOfRange(splitStr, 2, splitStr.length);
            blockMap.put(splitStr[1], instructions);
            cmdMap.put(count, command);
            labelCMDMap.put(splitStr[1], command);
        }
        else if (splitStr[0].equals("program") || splitStr[0].equals("execute") || splitStr[0].equals("list") || splitStr[0].equals("store") || splitStr[0].equals("load")){
            cmdMap.put(count, command);
            classification(command);
        }
        else if (splitStr[0].equals("togglebreakpoint")) {
            try {
                if (breakPointMap.get(splitStr[1]).equals(splitStr[2])) {
                    breakPointMap.remove(splitStr[1]);
                    stack.clear();
                    currentDebugPoint = "";
                }
            } catch (Exception e) {
                breakPointMap.put(splitStr[1], splitStr[2]);
            }
        }
        else {
            cmdMap.put(count, command);
            labelCMDMap.put(splitStr[1], command);
        }

    }

    protected static void storeQueue(String instruction) {    //* REQ12
        // Get instruction from the map
        String[] fullInst = labelCMDMap.get(instruction).split(" ");

        if (blockMap.containsKey(instruction)){  // If program statement is a block
            String block[] = blockMap.get(fullInst[1]);
            queue.add(labelCMDMap.get(instruction));
            runQueue.add(labelCMDMap.get(instruction));
            stack.push(labelCMDMap.get(instruction));

            for (int i = 0; i < block.length; i++) {
                storeQueue(block[i]); // Recurse over the instructions
            }
        }
        else if (fullInst[0].equals("while")){    // If while loop
            queue.add(labelCMDMap.get(instruction));
            runQueue.add(labelCMDMap.get(instruction));
            stack.push(labelCMDMap.get(instruction));
            storeQueue(fullInst[3]);
        }
        else if (fullInst[0].equals("if")){
            queue.add(labelCMDMap.get(instruction));
            runQueue.add(labelCMDMap.get(instruction));
            stack.push(labelCMDMap.get(instruction));
            storeQueue(fullInst[3]);
            storeQueue(fullInst[4]);
        }

        // Print if instruction is not a while or block or if
        else{
            queue.add(labelCMDMap.get(instruction));
            runQueue.add(labelCMDMap.get(instruction));
            stack.push(labelCMDMap.get(instruction));
        }

    }

    public static void classification(String command) {

        // Classify the commands

        String[] splitStr = command.split(" ");  // Split instruction into words
        String instruction = splitStr[0];

        
        // Call Commands based on the instruction
        switch (instruction) {
            
            case "vardef":  //* REQ1
                Simple.vardef(splitStr);
                break;
            
            case "binexpr": //* REQ2
                Simple.binExpr(splitStr);
                break;
            
            case "unexpr":  //* REQ3
                Simple.unexpr(splitStr[1], splitStr[2], splitStr[3]);
                break;

            case "assign":  //* REQ4
                Simple.assign(splitStr[2], splitStr[3]);
                break;

            case "print":   //* REQ5   
                Simple.print(splitStr[1], splitStr[2]);
                break;

            case "skip":    //* REQ6
                Simple.skip();
                break;

            case "block":   //* REQ7
                String[] instructions = Arrays.copyOfRange(splitStr, 2, splitStr.length);
                Simple.block(instructions);
                break;
                
            case "if":  //* REQ8
                Simple.ifF(splitStr[2], splitStr[3], splitStr[4]);
                break;

            case "while":   //* REQ9
                Simple.whileW(splitStr[2], splitStr[3]);
                break;

            case "program": //* REQ10
                Simple.program(splitStr[1], splitStr[2]);
                break;

            case "execute": //* REQ11
                Simple.execute(splitStr[1]);
                System.out.println();
                break;

            case "list":    //* REQ12
                Simple.list(programMap.get(splitStr[1]));
                break;

            case "store":   //* REQ13
                // Generated by vs-code
                try { File.store(splitStr[1], splitStr[2]);} 
                catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;

            case "load":    //* REQ1
                try { File.load(splitStr[1],splitStr[2]);} 
                catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;

            case "debug":
                Simple.debug(splitStr[1]);
                break;

            case "togglebreakpoint":
                Simple.togglebreakpoint(splitStr[1],splitStr[2]);
                break;

            case "inspect":
                Simple.inspect(splitStr[1], splitStr[2]);
                break;

        }

    }

    public static Object expRef(String expression){    // Check if the expression is a literal, var name or expression name.
        
        // If expression is a literal
        if (expression.equals("true")) return true;
        else if (expression.equals("false")) return false;

        
        // If expRef is a variable return variable from map
        if (varMap.containsKey(expression)) return varMap.get(expression);
            
        // If variable is expression reference
        if (resultExp.containsKey(expression)){
            if (resultExp.get(expression) instanceof Integer){return (int) resultExp.get(expression);}
            else return (boolean)resultExp.get(expression);
        }

        try{return Integer.parseInt(expression);}
        catch (Exception NumberFormatException){}

        return null;

    }

    public static void addResultExp(String label, Object object){resultExp.put(label, object);}

    // Use for input command, but just model, Application will be call this
    public void inputCMD() {

        Scanner inputLine = new Scanner(System.in);

        while(inputLine.hasNextLine()){

            String input = inputLine.nextLine();
            if (input != null){
                count++;
                if (input.equals("quit")) System.exit(0);


//                if (!inputValidator(input)){
//                    System.out.println("ERROR: The input command is invalid, please enter a new command");
//                    input = inputLine.nextLine();
//                }

                storeCommand(input);
                // classification(input);
            }
            if (input.contains("debug")) {
                classification(input);
            }

        }

        inputLine.close();
    }

    //! Separate store instructions and execute instructions in if while print block cases

    public boolean inputValidator(String input){

//        System.out.print("first: ");
//        System.out.println(input);

        // Check if the cmd string is empty
        if (input.isEmpty()){
            return false;
        }

        String[] strTemp = input.split(" ");  // Split instruction into words
        int n = strTemp.length;

//        System.out.print("second: ");
//        System.out.println(strTemp[0]);

        // For checking word length for input string split and if any illegal char are used
        for (int i=0; i<n; i++){
            // Initializing loop variables
            int nrChar = strTemp[i].length();

            // Check if input strings != cmd name or type
            switch (strTemp[0]){
                case "vardef":
                    if(i != 0 && i != 2) {
                        if (strTemp[i].matches("vardef|binexpr|unexpr|print|skip|block|if|while|execute|list|assign|program|store|quit|int|bool")) {
                            System.out.print("third: ");
                            System.out.println("case vardef i != 0");
                            return false;
                        }
                    }
                    break;
                case "binexpr": case "unexpr": case "assign": case "print": case "skip": case "block": case "ifF": case "whileW": case "program": case "execute": case "list": case "store": case "load":
                    if(i != 0) {
                        if (strTemp[i].matches("vardef|binexpr|unexpr|print|skip|block|if|while|execute|list|assign|program|store|quit|int|bool|true|false")) {
                            System.out.print("third: ");
                            System.out.println("case 'the rest' i != 0");
                            return false;
                        }
                    }
                    break;
            }

            // Check length longer than 8 characters & if strTemp[i] is
            if (strTemp[0].equals("store") || strTemp[0].equals("load")){
                switch (strTemp[0]){
                case "store":
                    if (i == 2){
                    }else {
                        if(nrChar > 8){
                            System.out.print("forth: ");
                            System.out.println("nrChar < 8");
                            return false;
                        }
                    }break;
                case "load":
                    if (i == 1){
                    }else {
                        if(nrChar > 8){
                            System.out.print("forth: ");
                            System.out.println("nrChar < 8");
                            return false;
                        }
                    }break;
                case "binexpr": case "unexpr": case "assign": case "print": case "skip": case "block": case "ifF": case "whileW": case "program": case "execute": case "list":
                    if(nrChar > 8){
                        System.out.print("forth: ");
                        System.out.println("nrChar < 8");
                        return false;
                    }break;
                }
            }
//            if(nrChar > 8){
//                System.out.print("forth: ");
//                System.out.println("nrChar < 8");
//                return false;
//            }

            // Loop for checking if any invalid characters are in the String cmd
            switch (strTemp[0]){
                case "store": case "load":
                    if (strTemp[0].equals("store") && i == 2){
                    } else if (strTemp[0].equals("load") && i == 1) {
                    }else {
                        for (int x = 0; x < nrChar; x++) {
                            boolean validChar = Character.isLetterOrDigit(strTemp[i].charAt(x));
                            String temp = String.valueOf(strTemp[i]);
                            if (strTemp[i].matches("-|/|#|~|>|>=|<|<=|==|!=|&&|!|:") || strTemp[i].charAt(x) == '-' || temp.equals("*") || temp.equals("+") || temp.equals("||")) {
                                validChar = true;
                            }
                            if (validChar == false) {
                                System.out.println("The following input is invalid:" + strTemp[i]);
                                System.out.print("fifth: ");
                                System.out.println("validChar == false");
                                return false;
                            }
                        }
                    }break;
                case "vardef": case "binexpr": case "unexpr": case "assign": case "print": case "skip": case "block": case "ifF": case "whileW": case "program": case "execute": case "list":
                    for (int x = 0; x < nrChar; x++) {
                        boolean validChar = Character.isLetterOrDigit(strTemp[i].charAt(x));
                        String temp = String.valueOf(strTemp[i]);
                        if (strTemp[i].matches("-|/|#|~|>|>=|<|<=|==|!=|&&|!|:") || strTemp[i].charAt(x) == '-' || temp.equals("*") || temp.equals("+") || temp.equals("||")) {
                            validChar = true;
                        }
                        if (validChar == false) {
                            System.out.println("The following input is invalid:" + strTemp[i]);
                            System.out.print("fifth: ");
                            System.out.println("validChar == false");
                            return false;
                        }
                    }
            }
        }

        // Checks if the int or bool value is out of bounds
          if(strTemp.length >= 2){
            switch (strTemp[2]) {
                case "int":
                    int intValue = Integer.parseInt(strTemp[4]);
                    if (-99999 > intValue || intValue > 99999) {
                        System.out.print("sixth: ");
                        System.out.println("case int != (-99999, 99999)");
                        return false;
                    }
                    break;
                case "bool":
                    String boolValue = strTemp[4];
                    if (!boolValue.matches("false|true")) {
                        System.out.print("sixth: ");
                        System.out.println("case bool != false or true");
                        return false;
                    }
                    break;
            }
        }
        return true;
    }

}
