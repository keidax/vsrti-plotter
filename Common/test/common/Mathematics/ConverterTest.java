package common.Mathematics;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author Gabriel Holodak
 * @version 12/18/13.
 */
public class ConverterTest {

    String array[] = {"1", "+", "1"};
    Converter converter = new Converter(array);


    @Test
    public void testEvaluate() throws Exception {
        Double expected = 2.0;
        Double actual = converter.evaluate();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPostfix() throws Exception {
        String expected[] = {"1", "1", "+"};
        String actual[] = converter.getPostfix();
        for (String i : actual) {
            System.out.println(i);
        }
        assertArrayEquals(expected, actual);
    }
}
