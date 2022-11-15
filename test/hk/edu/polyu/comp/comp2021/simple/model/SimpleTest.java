package hk.edu.polyu.comp.comp2021.simple.model;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SimpleTest {

    @Test
    public void testVardef(){
        Parser.classification("vardef v1 int x 10");
        Object value = Parser.varMap.get("x");
        assertEquals(10, value);
    }
    @Test
    public void testBinExpr1() {
        Parser.classification("vardef v1 int x 10");
        Parser.classification("binexpr exp1 x * 20");
        Object value = Parser.resultExp.get("exp1");
        int need = Integer.parseInt("200");
        assertEquals(need, value);
    }
    @Test
    public void testBinExpr2() {
        Parser.classification("vardef v1 int x 10");
        Parser.classification("binexpr exp2 x == 10");
        Object value = Parser.resultExp.get("exp2");
        assertEquals(true, value);
    }
    @Test
    public void testBinExpr3() {
        Parser.classification("vardef v1 int x 10");
        Parser.classification("binexpr exp3 x > 20");
        Object value = Parser.resultExp.get("exp3");
        assertEquals(false, value);
    }
    @Test
    public void testBinExpr4() {
        Parser.classification("vardef v1 bool x true");
        Parser.classification("vardef v2 bool y false");
        Parser.classification("binexpr exp4 x && y");
        Object value = Parser.resultExp.get("exp4");
        assertEquals(false, value);
    }
    @Test
    public void testUnaryEx(){




    }
    @Test
    public void testAssign(){
        Simple simple=new Simple();
        Parser.classification("vardef v1 int x 10");
        Object first = Parser.varMap.get("x");
        Simple.assign("x", "15");
        Object second=Parser.varMap.get("x");
        int need = Integer.parseInt("15");
        assertEquals(need,second);
    }
    @Test
    public void testPrint(){
        Parser.classification("vardef vardef1 int x 100");
        Parser.classification("binexpr exp1 x * 20");
        Parser.classification("unexpr exp2 ~ exp1");
        Parser.classification("print print1 exp2");
        Object value = Parser.resultExp.get("exp2");
        assertEquals("[-2000]","[" + value + "]");
    }
    @Test
    public void testSkip(){

        assertNull(null);

    }

    @Test
    public void testSimpleConstructor(){
        Simple simple = new Simple();
        assert true;
    }

    @Test
    public void fileTest() throws IOException {
        String cmd1 = "vardef vardef1 int x 100\n";
        String cmd2 = "binexpr exp1 x * 20\n";
        String cmd3 = "unexpr exp2 ~ exp1\n";
        String cmd4 = "print print1 exp2\n";
        String cmd5 = "block block1 assign1 print1\n";
        String cmd6 = "program program1 block1\n";
        String cmd7 = "store program1 /Users/davidjiang/Desktop/prog1.simple\n";
        Parser.storeCommand(cmd1);
        Parser.storeCommand(cmd2);
        Parser.storeCommand(cmd3);
        Parser.storeCommand(cmd4);
        Parser.storeCommand(cmd5);
        Parser.storeCommand(cmd6);
        Parser.classification(cmd7);
        String address = "/Users/davidjiang/Desktop/prog1.simple";
//        File.store("program1", address);
//        File.load(address, "program1");
//        String[] cmd = {cmd1, cmd2, cmd3, cmd4, cmd5};
//        for (int i = 0; i < Parser.cmdMap.size(); i++) {
//            assertEquals(Parser.cmdMap.get(i + 1), cmd[i]);
//        }
    }
    @Test
    public void executeTest() {








    }

    @Test
    public void breakPointTest() {
        String str = "togglebreakpoint program1 block1";
        Simple.storeCommand(str);
        assertEquals(str.split(" ")[2], Parser.breakPointMap.get(str.split(" ")[1]));
    }

    @Test
    public void debugTest() {
        String str = "debug program1";
//        Simple.togglebreakpoint(str.split(" ")[1], str.split(" ")[2]);
//        assertEquals(str.split(" ")[2], Parser.breakPointMap.get(str.split(" ")[1]));
    }
}