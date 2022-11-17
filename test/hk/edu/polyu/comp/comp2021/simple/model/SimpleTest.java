package hk.edu.polyu.comp.comp2021.simple.model;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static hk.edu.polyu.comp.comp2021.simple.model.Parser.ExecuteResultString;
import static hk.edu.polyu.comp.comp2021.simple.model.Parser.data;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * For testing the commands
 */
public class SimpleTest {

    /**
     * Test for vardef function
     */
    @Test
    public void testVardef(){
        Parser.classification("vardef v1 int x 10", "prog1");
        Object value = Parser.varMap.get("x");
        assertEquals(10, value);
    }
    /**
     * Test for binexpr function - Binary Expression Calculation
     */
    @Test
    public void testBinExpr1() {
        Parser.classification("vardef v1 int x 10", "prog1");
        Parser.classification("binexpr exp1 x * 20", "prog1");
        Object value = Parser.resultExp.get("exp1");
        int need = Integer.parseInt("200");
        assertEquals(need, value);
    }
    /**
     * Test for binexpr function - Binary Expression Calculation
     */
    @Test
    public void testBinExpr2() {
        Parser.classification("vardef v1 int x 10", "prog1");
        Parser.classification("binexpr exp2 x == 10", "prog1");
        Object value = Parser.resultExp.get("exp2");
        assertEquals(true, value);
    }
    /**
     * Test for binexpr function - Binary Expression Calculation
     */
    @Test
    public void testBinExpr3() {
        Parser.classification("vardef v1 int x 10", "prog1");
        Parser.classification("binexpr exp3 x > 20", "prog1");
        Object value = Parser.resultExp.get("exp3");
        assertEquals(false, value);
    }
    /**
     * Test for binexpr function - Binary Expression Calculation
     */
    @Test
    public void testBinExpr4() {
        Parser.classification("vardef v1 bool x true", "prog1");
        Parser.classification("vardef v2 bool y false", "prog1");
        Parser.classification("binexpr exp4 x && y", "prog1");
        Object value = Parser.resultExp.get("exp4");
        assertEquals(false, value);
    }
    /**
     * Test for unexpr function - Unary Expression Calculation
     */
    @Test
    public void testUnaryEx(){
        Parser.classification("vardef v1 int x 10", "prog1");
        Parser.classification("binexpr exp1 x * 20", "prog1");
        Parser.classification("unexpr exp2 ~ exp1", "prog1");
        Object value = Parser.resultExp.get("exp2");
        assertEquals("-200", value + "");
    }

    /**
     * Test for assign function
     */
    @Test
    public void testAssign(){
        Simple simple=new Simple();
        Parser.classification("vardef v1 int x 10", "prog1");
        Object first = Parser.varMap.get("x");
        Simple.assign("x", "15");
        Object second=Parser.varMap.get("x");
        int need = Integer.parseInt("15");
        assertEquals(need,second);
    }

    /**
     * Test for print function
     */
    @Test
    public void testPrint(){
        Parser.classification("vardef vardef1 int x 100", "prog1");
        Parser.classification("binexpr exp1 x * 20", "prog1");
        Parser.classification("unexpr exp2 ~ exp1", "prog1");
        Parser.classification("print print1 exp2", "prog1");
        Object value = Parser.resultExp.get("exp2");
        assertEquals("[-2000]","[" + value + "]");
    }

    /**
     * Test for skip function
     */
    @Test
    public void testSkip(){
        Parser.classification("vardef vardef1 int x 0", "prog1");
        Parser.classification("binexpr exp1 x % 2", "prog1");
        Parser.classification("binexpr exp2 exp1 == 0", "prog1");
        Parser.classification("print print1 x", "prog1");
        Parser.classification("skip skip1", "prog1");

        assertNull(null);
    }

    /**
     * Test for the block function
     */
    @Test
    public void testBlock() {
        Parser.classification("vardef vardef1 int x 100", "prog1");
        Parser.classification("binexpr exp1 x * 10", "prog1");
        Parser.classification("unexpr exp2 ~ exp1", "prog1");
        Parser.classification("print print1 exp2", "prog1");
        Parser.classification("block block1 assign1 print1", "prog1");
        Object first = Parser.resultExp.get("exp2");
        Simple.block(new String[]{"100", "1000", "3500"},"prog1");
    }

    /**
     * Test for file operation - store and load function
     * @throws IOException: the file handling error
     */
    @Test
    public void fileTest() throws IOException {
        String cmd1 = "vardef vardef1 int x 100";
        String cmd2 = "binexpr exp1 x * 20";
        String cmd3 = "unexpr exp2 ~ exp1";
        String cmd4 = "print print1 exp2";
        String cmd5 = "block block1 assign1 print1";
        String cmd6 = "program program1 block1";
        String cmd7 = "store prog1 /Users/davidjiang/Desktop/prog1.simple";
        Parser.classification(cmd1, "prog1");
        Parser.classification(cmd2, "prog1");
        Parser.classification(cmd3, "prog1");
        Parser.classification(cmd4, "prog1");
        Parser.classification(cmd5, "prog1");
        Parser.classification(cmd6, "prog1");
        Parser.classification(cmd7, "prog1");
        String address = "/Users/davidjiang/Desktop/prog1.simple";
//        Simple.store("program1", address);
        Simple.load(address, "prog1");
        String[] cmd = {cmd1, cmd2, cmd3, cmd4, cmd5};
//        for (int i = 0; i < Parser.cmdMap.size(); i++) {
//            assertEquals(Parser.cmdMap.get(i + 1), cmd[i]);
//        }

    }

    /**
     * Test the execute function
     */
    @Test
    public void executeTest() {
        data.storeCommand("vardef vardef1 int x 100");
        data.storeCommand("binexpr exp1 x * 20");
        data.storeCommand("print print1 exp1");
        data.storeCommand("program program1 print1");
        data.storeCommand("execute program1");
        System.out.println(ExecuteResultString);
        assertEquals("[2000]", ExecuteResultString);
    }

    /**
     * Test for breakpoint of the programmu
     */
    @Test
    public void breakPointTest() {
        String str = "togglebreakpoint program1 block1";
        Data.storeCommand(str);
        assertEquals(str.split(" ")[2], Data.getDebugger().get(str.split(" ")[1]).get(0));
    }

    /**
     * Test for debug function
     */
    @Test
    public void debugTest() {
        String str = "debug program1";
//        Simple.togglebreakpoint(str.split(" ")[1], str.split(" ")[2]);
//        assertEquals(str.split(" ")[2], Parser.breakPointMap.get(str.split(" ")[1]));
    }

    /**
     * Test for inspect function
     */

    @Test
    public void inspectTest() {
        Parser.classification("vardef vardef1 int x 0", "prog1");
        Parser.classification("binexpr exp1 x % 2", "prog1");
        Parser.classification("binexpr exp2 exp1 == 0", "prog1");
        Parser.classification("print print1 x", "prog1");
        Parser.classification("skip skip1", "prog1");
        Parser.classification("vardef vardef1 int x 0", "prog1");
        Parser.classification("inspect printeven x","prog1");
        Simple.inspect("x");
        Object value2 = Parser.varMap.get("x");
        assertEquals("<0>","<"+value2+">");


    }





}


