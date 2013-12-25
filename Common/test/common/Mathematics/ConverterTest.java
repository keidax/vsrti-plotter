package common.Mathematics;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Gabriel Holodak
 * @version 12/18/13.
 */
public class ConverterTest {

    @Test
    public void testEvaluate1() throws Exception {
        String test = "1+1";
        Converter converter = new Converter(test);
        Double expected = 2.0;
        Double actual = converter.evaluate();
        assertEquals(expected, actual);
    }

    @Test
    public void testEvaluate2() throws Exception {
        String test = "1.3457";
        Converter converter = new Converter(test);
        Double expected = 1.3457;
        Double actual = converter.evaluate();
        assertEquals(expected, actual);
    }

    @Test
    public void testEvaluate3() throws Exception {
        String test = "cos(0)";
        Converter converter = new Converter(test);
        Double expected = 1.0;
        Double actual = converter.evaluate();
        assertEquals(expected, actual);
    }

    @Test
    public void testEvaluate4() throws Exception {
        String test = "log(1000)";
        Converter converter = new Converter(test);
        Double expected = 3.0;
        Double actual = converter.evaluate();
        assertEquals(expected, actual);
    }

    /*@Test
    public void testGetPostfix1() throws Exception {
        String test = "1+1";
        Converter converter = new Converter(test);
        String expected = "11+";
        converter.evaluate();
        String actual = converter.getPostfix();
        assertEquals(expected, actual);
    }*/


}
