package hk.edu.polyu.comp.comp2021.simple.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.FileWriter;

public class File {
    // store the program code to a file
    public static void store(String programName, String address) throws IOException {

        // Create File
        try {
            java.io.File myObj = new java.io.File(address + programName + ".simple.txt");
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
        Parser cmdMap = new Parser();
        try {
            FileWriter myWriter = new FileWriter(address + programName + ".simple.txt");
            String CMD = "";
            Map<Integer, String> CMDMap = Parser.cmdMap;
//            System.out.println(CMDMap);
            for (int i = 1; i < CMDMap.size() + 1; i++) {
//                System.out.println(CMDMap.get(i));
                CMD += CMDMap.get(i) + "\n";
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
            Parser.cmdMap.put(i + 1, cmd);
        }
        for (int i = 1; i < ProgramCMDList.size(); i++) {
            Parser.classification(Parser.cmdMap.get(i));
        }
//        System.out.println(Parser.cmdMap);

    }
}
