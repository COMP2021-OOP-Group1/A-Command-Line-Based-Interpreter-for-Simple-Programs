package hk.edu.polyu.comp.comp2021.simple.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
        assertEquals(1, value);
    }
    @Test
    public void testBinExpr3() {
        Parser.classification("vardef v1 int x 10");
        Parser.classification("binexpr exp3 x > 20");
        Object value = Parser.resultExp.get("exp3");
        assertEquals(0, value);
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

    }

    @Test
    public void testSimpleConstructor(){
        Simple simple = new Simple();
        assert true;
    }

}