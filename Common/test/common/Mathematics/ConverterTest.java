package common.Mathematics;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Gabriel Holodak
 * @version 12/28/13.
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

    @Test
    public void testEvaluate5() throws Exception {
        String test = "e^-x";
        Converter converter = new Converter(test);
        converter.setX(1);

        System.out.println(test + " = " + converter.evaluate());
    }

    @Test
    public void testEvaluate6() throws Exception {
        String test = "e^x^2";
        Converter converter = new Converter(test);
        converter.setX(1);
        converter.evaluate();
    }

    @Test
    public void testEvaluate7() throws Exception {
        String test = "-2*-3+-x/-7";
        Converter converter = new Converter(test);
        converter.setX(1);
        converter.evaluate();
    }

    @Test
    public void testEvaluate8() throws Exception {
        String test = "e^(x)";
        Converter converter = new Converter(test);
        converter.setX(1);
        converter.evaluate();
    }

}
