package hk.edu.polyu.comp.comp2021.simple;

import hk.edu.polyu.comp.comp2021.simple.model.Parser;

import java.io.IOException;

public class Application {
    /**
     *
     * @param args Argument for main function
     * @throws IOException Since it manages files
     */
    public static void main(String[] args) throws IOException {

        Parser input = new Parser();
        input.inputCMD();

    }

}
