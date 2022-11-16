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
    private static Queue<String> queue = new LinkedList<>();
    /**
     * Stores the running commands in an array list
     */
    private static List<String> runArray = new ArrayList<>();
    /**
     * Stores the commands in to stack
     */
    private static Stack<String> stack = new Stack<>();
    /**
     * Stores the current variable value
     */
    private static String currentVarValue = "";
    /**
     * Store the current debug statement
     */
    private static String currentDebugPoint = "";
    /**
     * Store the current Inspect value
     */
    private static String currentInspect = "";
    /**
     * Store the current debug times
     */
    private static int DebugPoint = 0;
    /**
     * Count the current index
     */
    private static int index = 0;

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
     * @return DebugPoint value
     */
    public int getDebugPoint() {
        return DebugPoint;
    }

    /**
     * @param DebugPoint: the debug index point
     */
    public void setDebugPoint(int DebugPoint) {
        Data.DebugPoint = DebugPoint;
    }

    /**
     * @return index value
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index: the program running index for inspect
     */
    public void setIndex(int index) {
        Data.index = index;
    }

    /**
     * @return currentVarValue value
     */
    public String getCurrentVarValue() {
        return currentVarValue;
    }

    /**
     * @param currentVarValue: the current variable value
     */
    public void setCurrentVarValue(String currentVarValue) {
        Data.currentVarValue = currentVarValue;
    }

    /**
     * @return currentDebugPoint value
     */
    public String getCurrentDebugPoint() {
        return currentDebugPoint;
    }

    /**
     * @param currentDebugPoint: the current statement label for debugging
     */
    public void setCurrentDebugPoint(String currentDebugPoint) {
        Data.currentDebugPoint = currentDebugPoint;
    }

    /**
     * @return currentInspect value
     */
    public String getCurrentInspect() {
        return currentInspect;
    }

    /**
     * @param currentInspect: the current inspect statement
     */
    public void setCurrentInspect(String currentInspect) {
        Data.currentInspect = currentInspect;
    }

    /**
     * @return stack
     */
    public Stack<String> getStack() {
        return stack;
    }

    /**
     * @return queue
     */
    public Queue<String> getQueue() {
        return queue;
    }

    /**
     * @return runArray List
     */
    public List<String> getRunArray() {
        return runArray;
    }
    /**
     * storeCommand function will split those command and store some value into HashMap or Stack or Queue
     * @param command: the commands input by user
     */
    public static void storeCommand(String command){

        // Check if instruction is valid first
        String[] splitStr = command.split(" ");  // Split instruction into words


        if (splitStr[0].equals("vardef")){

            if (validVarName(splitStr[2])){
                labelCMDMap.put(splitStr[1], command);
                Parser.classification(command);
            }
        }
        else if (splitStr[0].equals("binexpr") || splitStr[0].equals("unexpr")){

            expRefLabelCmd.put(splitStr[1], command);
            Parser.classification(command);
            Simple.updateExp();
        }
        else if (splitStr[0].equals("block")){
            String[] instructions = Arrays.copyOfRange(splitStr, 2, splitStr.length);
            labelCMDMap.put(splitStr[1], command);
        }
        else if (splitStr[0].equals("program") || splitStr[0].equals("execute") || splitStr[0].equals("list") || splitStr[0].equals("store") || splitStr[0].equals("load") || splitStr[0].equals("inspect")){
            Parser.classification(command);
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
            labelCMDMap.put(splitStr[1], command);
        }
        // System.out.println(varMap);

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


}
