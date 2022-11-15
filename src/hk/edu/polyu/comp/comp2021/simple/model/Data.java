package hk.edu.polyu.comp.comp2021.simple.model;

import java.util.*;

public class Data {
    public Data() {
        private static Map<String, String> labelCMDMap = new HashMap<>();  // Stores Labels and its commands (Label - Command)
        private static Map<String, String> expRefLabelCmd = new HashMap<>();  // Stores expRefs and its commands (Label - Command)
        private static Map<String, Object> varMap = new HashMap<>();   // Stores Variables and Values (Variable - Value)
        private static Map<String, Object> resultExp = new HashMap<>();   // Stores Results of Expressions (Label - Result)
        private static Map<String, String> programMap = new HashMap<>(); // Stores the programName and the label of command
        private static Map<String, String> breakPointMap = new HashMap<>();
        private static Map<String, List<Object>> varHistoryMap = new HashMap<>();
        private static Queue<String> queue = new LinkedList<>();
        private static List<String> runArray = new ArrayList<>();
        private static Stack<String> stack = new Stack<>();
        private static String currentVarValue = "";
        private static String currentDebugPoint = "";
        private static String currentInspect = "";
        private static int DebugPoint = 0;
        private static int index = 0;
    }

    public void setLabelCMDMap() {

    }

    public Map<String, String> getLabelCMDMap() {
        return labelCMDMap;
    }

}
