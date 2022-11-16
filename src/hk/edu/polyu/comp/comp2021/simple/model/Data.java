package hk.edu.polyu.comp.comp2021.simple.model;

import java.util.*;

/**
 * Parser that receives the user input and processes it in simple language
 */
public class Data {
    /**
     * Stores Labels and its commands (Label - Command)
     */
    private static Map<String, String> labelCMDMap = new HashMap<>();
    /**
     * Stores expRefs and its commands (Label - Command)
     */
    private static Map<String, String> expRefLabelCmd = new HashMap<>();
    /**
     * Stores Variables and Values (Variable - Value)
     */
    private static Map<String, Object> varMap = new HashMap<>();
    /**
     * Stores Results of Expressions (Label - Result)
     */
    private static Map<String, Object> resultExp = new HashMap<>();
    /**
     * Stores the programName and the label of command
     */
    private static Map<String, String> programMap = new HashMap<>();
    /**
     * Stores the programName and the label of breakpoint
     */
    private static Map<String, String> breakPointMap = new HashMap<>();
    /**
     * Stores the variable or expression reference and the list of result
     */
    private static Map<String, List<Object>> varHistoryMap = new HashMap<>();
    /**
     * Stores the program commands in a queue
     */


    private static Map<String, ArrayList<String>> debugger = new HashMap<String, ArrayList<String>>();

//    public void setLabelCMDMap() {
//
//    }

    /**
     * @return labelCMDMap HashMap
     */
    public Map<String, String> getLabelCMDMap() {
        return labelCMDMap;
    }

    /**
     * @return expRefLabelCmd HashMap
     */
    public Map<String, String> getExpRefLabelCmd() {
        return expRefLabelCmd;
    }

    /**
     * @return varMap HashMap
     */
    public Map<String, Object> getVarMap() {
        return varMap;
    }

    /**
     * @return resultExp HashMap
     */
    public Map<String, Object> getResultExp() {
        return resultExp;
    }

    /**
     * @return programMap HashMap
     */
    public Map<String, String> getProgramMap() {
        return programMap;
    }

    /**
     * @return breakPointMap HashMap
     */
    public Map<String, String> getBreakPointMap() {
        return breakPointMap;
    }

    /**
     * @return varHistoryMap HashMap
     */
    public Map<String, List<Object>> getVarHistoryMap() {
        return varHistoryMap;
    }

   
    /**
     * storeCommand function will split those command and store some value into HashMap or Stack or Queue
     * @param command: the commands input by user
     */

    public static Map<String, ArrayList<String>> getDebugger() {
        return debugger;
    }


    public static void storeCommand(String command){

        // Check if instruction is valid first
        String[] splitStr = command.split(" ");  // Split instruction into words
        
        if (checkLength(splitStr[0], splitStr.length)){

            if (splitStr[0].equals("vardef")){

                if (validVarName(splitStr[3])){
                    labelCMDMap.put(splitStr[1], command);
                    Parser.classification(command, "");
                }
            }
            else if (splitStr[0].equals("binexpr") || splitStr[0].equals("unexpr")){

                expRefLabelCmd.put(splitStr[1], command);
                Parser.classification(command, "");
                Simple.updateExp();
            }
            else if (splitStr[0].equals("block")){
                String[] instructions = Arrays.copyOfRange(splitStr, 2, splitStr.length);
                labelCMDMap.put(splitStr[1], command);
            }
            else if (splitStr[0].equals("program") || splitStr[0].equals("execute") || splitStr[0].equals("list") || splitStr[0].equals("store") || splitStr[0].equals("load") || splitStr[0].equals("inspect") || splitStr[0].equals("debug") || splitStr[0].equals("togglebreakpoint")){
                Parser.classification(command, "");
            }
            else {
                labelCMDMap.put(splitStr[1], command);
            }
        }
       

    }

    private static boolean checkLength(String command, int n){

        if (n == 1) return false;

        // 5 letter instructions
        if (command.equals("vardef") || command.equals("binexpr") || command.equals("if")){if (n != 5) return false;}
        // 4 letter instructions
        else if (command.equals("unexpr") || command.equals("assign") || command.equals("while")){if (n != 4) return false;}
        // 3 letter instructions
        else if (command.equals("print") || command.equals("program") || command.equals("store") || command.equals("load")){if (n != 3) return false;}
        // 2 letter instructions
        else if (command.equals("skip") || command.equals("execute") || command.equals("list")){if (n != 2) return false;}
        // Block case
        else if (command.equals("block")){if (n <= 2) return false;}


        return true;

    }

    private static boolean validVarName(String varName) {

        List<String> forbidden = Arrays.asList("vardef", "unexpr", "binexpr", "assign", "print", 
        "skip", "block", "if", "while", "program", "execute", "list", "store", "load",
        "debug", "quit", "togglebreakpoint", "inspect", "instrument", "int", "bool", 
        "true", "false");

        if (Character.isDigit(varName.charAt(0))) return false;    // Check first character

        if (varName.length() > 8) return false; // Check length

        if (forbidden.contains(varName)) return false;  

        for (int i = 0; i < varName.length(); i++) {
            if (Character.isDigit(varName.charAt(i)) || Character.isLetter(varName.charAt(i))){}   
            else return false;
        }


        return true;

    }

    /**
     * Put the variable history into the varHistoryMap this HashMap
     * @param varName: the variable name
     */
    public static void putVarHistoryMap(String varName) {
        List<Object> list = new ArrayList<>();

        if(!varHistoryMap.containsKey(varName)) {
            varHistoryMap.put(varName, list);
        }
        varHistoryMap.get(varName).add(varMap.get(varName));
    }

    /**
     * addResultExp function is put the result and label to resultExp this HashMap
     * @param label: the label of expression statement
     * @param object: the result of expression
     */
    public static void addResultExp(String label, Object object){resultExp.put(label, object);}


}
