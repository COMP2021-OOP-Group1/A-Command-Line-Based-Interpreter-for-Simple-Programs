package hk.edu.polyu.comp.comp2021.simple.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.FileWriter;

public class File extends Parser {
    // store the program code to a file
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
