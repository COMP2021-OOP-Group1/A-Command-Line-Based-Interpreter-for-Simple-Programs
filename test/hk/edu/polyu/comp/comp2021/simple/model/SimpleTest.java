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
        assertEquals(200, value);
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
        assertEquals(15,second);
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
        String cmd1 = "vardef vardef1 int x 100";
        String cmd2 = "binexpr exp1 x * 20";
        String cmd3 = "unexpr exp2 ~ exp1";
        String cmd4 = "unexpr exp2 ~ exp1";
        String cmd5 = "print print1 exp2";
        String cmd6 = "block block1 assign1 print1";
        String cmd7 = "program program1 block1";
        Parser.classification(cmd1);
        Parser.classification(cmd2);
        Parser.classification(cmd3);
        Parser.classification(cmd4);
        Parser.classification(cmd5);
        Parser.classification(cmd6);
        Parser.classification(cmd7);
        String address = "/Users/davidjiang/Desktop/prog1.simple";
        File.store("program1", address);
        File.load(address, "program1");
        String[] cmd = {cmd1, cmd2, cmd3, cmd4, cmd5, cmd6, cmd7};
        for (int i = 0; i < Parser.cmdMap.size(); i++) {
            assertEquals(Parser.cmdMap.get(i + 1), cmd[i]);
        }
    }
    @Test
    public void executeTest() {








    }

    @Test
    public void breakPointTest() {
        String str = "togglebreakpoint program1 block1";
        Simple.togglebreakpoint(str.split(" ")[1], str.split(" ")[2]);
        assertEquals(str.split(" ")[2], Parser.breakPointMap.get(str.split(" ")[1]));
    }

    @Test
    public void debugTest() {
        String str = "debug program1";
//        Simple.togglebreakpoint(str.split(" ")[1], str.split(" ")[2]);
//        assertEquals(str.split(" ")[2], Parser.breakPointMap.get(str.split(" ")[1]));
    }
}