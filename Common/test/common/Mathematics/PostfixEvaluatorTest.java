package common.Mathematics;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author Gabriel Holodak
 * @version 12/28/13.
 */
public class PostfixEvaluatorTest {

    @Test
    public void testEvaluate1() throws Exception {
        String test = "1+1";
        PostfixEvaluator evaluator = new PostfixEvaluator(test);
        Double expected = 2.0;
        Double actual = evaluator.evaluate();
        assertEquals(expected, actual);
    }

    @Test
    public void testEvaluate2() throws Exception {
        String test = "1.3457";
        PostfixEvaluator evaluator = new PostfixEvaluator(test);
        Double expected = 1.3457;
        Double actual = evaluator.evaluate();
        assertEquals(expected, actual);
    }

    @Test
    public void testEvaluate3() throws Exception {
        String test = "cos(0)";
        PostfixEvaluator evaluator = new PostfixEvaluator(test);
        Double expected = 1.0;
        Double actual = evaluator.evaluate();
        assertEquals(expected, actual);
    }

    @Test
    public void testEvaluate4() throws Exception {
        String test = "log(1000)";
        PostfixEvaluator evaluator = new PostfixEvaluator(test);
        Double expected = 3.0;
        Double actual = evaluator.evaluate();
        assertEquals(expected, actual);
    }

    @Test
    public void testEvaluate5() throws Exception {
        String test = "e^-x";
        PostfixEvaluator evaluator = new PostfixEvaluator(test);
        Map<String, Double> map = new HashMap<String, Double>();
        map.put("x", 1.0);
        evaluator.setMap(map);

        System.out.println(test + " = " + evaluator.evaluate());
    }

    @Test
    public void testEvaluate6() throws Exception {
        String test = "e^x^2";
        PostfixEvaluator evaluator = new PostfixEvaluator(test);
        Map<String, Double> map = new HashMap<String, Double>();
        map.put("x", 1.0);
        evaluator.setMap(map);
        evaluator.evaluate();

        System.out.println(test + " = " + evaluator.evaluate());

    }


    @Test
    public void testEvaluate7() throws Exception {
        String test = "-2*-3+-x/-7";
        PostfixEvaluator evaluator = new PostfixEvaluator(test);
        Map<String, Double> map = new HashMap<String, Double>();
        map.put("x", 1.0);
        evaluator.setMap(map);
        evaluator.evaluate();

        System.out.println(test + " = " + evaluator.evaluate());

    }

    @Test
    public void testEvaluate8() throws Exception {
        String test = "e^(x)";
        PostfixEvaluator evaluator = new PostfixEvaluator(test);
        Map<String, Double> map = new HashMap<String, Double>();
        map.put("x", 1.0);
        evaluator.setMap(map);
        evaluator.evaluate();

        System.out.println(test + " = " + evaluator.evaluate());

    }

}
