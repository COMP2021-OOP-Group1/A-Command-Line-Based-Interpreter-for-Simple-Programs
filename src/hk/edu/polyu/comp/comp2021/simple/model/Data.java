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

    public Map<String, String> getLabelCMDMap() {
        return labelCMDMap;
    }

    public Map<String, String> getExpRefLabelCmd() {
        return expRefLabelCmd;
    }

    public Map<String, Object> getVarMap() {
        return varMap;
    }

    public Map<String, Object> getResultExp() {
        return resultExp;
    }

    public Map<String, String> getProgramMap() {
        return programMap;
    }

    public Map<String, String> getBreakPointMap() {
        return breakPointMap;
    }

    public Map<String, List<Object>> getVarHistoryMap() {
        return varHistoryMap;
    }

    public int getDebugPoint() {
        return DebugPoint;
    }

    public int getIndex() {
        return index;
    }

    public String getCurrentVarValue() {
        return currentVarValue;
    }

    public String getCurrentDebugPoint() {
        return currentDebugPoint;
    }

    public String getCurrentInspect() {
        return currentInspect;
    }

    public Stack<String> getStack() {
        return stack;
    }

    public Queue<String> getQueue() {
        return queue;
    }

    public List<String> getRunArray() {
        return runArray;
    }



}
