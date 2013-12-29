package common.Mathematics;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by gabriel on 12/25/13.
 */
public class TokenizerTest {
    @Test
    public void testIsPartOfNumber1() {
        String test = "123456789012345678901234567890";
        assertTrue(Tokenizer.isPartOfNumber(test));
    }

    @Test
    public void testIsPartOfNumber2() {
        String test = "-123456789012345678901234567890";
        assertTrue(Tokenizer.isPartOfNumber(test));
    }

    @Test
    public void testIsPartOfNumber3() {
        String test = "12345678901234567890-1234567890";
        assertFalse(Tokenizer.isPartOfNumber(test));
    }

    @Test
    public void testIsPartOfNumber4() {
        String test = "-1-2-3-4-5-6-";
        assertFalse(Tokenizer.isPartOfNumber(test));
    }

    @Test
    public void testIsPartOfNumber5() {
        String test = "12345678901234567a8901234567890";
        assertFalse(Tokenizer.isPartOfNumber(test));
    }

    @Test
    public void testIsPartOfNumber6() {
        String test = "109";
        assertTrue(Tokenizer.isPartOfNumber(test));
    }

    @Test
    public void testIsPartOfNumber7() {
        String test = "-4";
        assertTrue(Tokenizer.isPartOfNumber(test));
    }

    @Test
    public void testIsPartOfVariableOrString1() {
        String test = "sin";
        assertTrue(Tokenizer.isPartOfVariableOrString(test));
    }

    @Test
    public void testIsPartOfVariableOrString2() {
        String test = "abcdefghijklmnopqrstuVWXYZ____1234509";
        assertTrue(Tokenizer.isPartOfVariableOrString(test));
    }

    @Test
    public void testIsPartOfVariableOrString3() {
        String test = "log_10";
        assertTrue(Tokenizer.isPartOfVariableOrString(test));
    }

    @Test
    public void testIsPartOfVariableOrString4() {
        String test = "_123";
        assertTrue(Tokenizer.isPartOfVariableOrString(test));
    }

    @Test
    public void testIsPartOfVariableOrString5() {
        String test = "a b c";
        assertFalse(Tokenizer.isPartOfVariableOrString(test));
    }

    @Test
    public void testIsPartOfVariableOrString6() {
        String test = "a_B+c";
        assertFalse(Tokenizer.isPartOfVariableOrString(test));
    }

    @Test
    public void testIsPartOfVariableOrString7() {
        String test = " _";
        assertFalse(Tokenizer.isPartOfVariableOrString(test));
    }

    @Test
    public void testIsPartOfVariableOrString8() {
        String test = "1234567890qwertyuiopasdfghjklzxcvbnm;";
        assertFalse(Tokenizer.isPartOfVariableOrString(test));
    }

    @Test
    public void testIsOperator1() {
        String test = "+";
        assertTrue(Tokenizer.isOperator(test));
    }

    @Test
    public void testIsOperator2() {
        String test = "-";
        assertTrue(Tokenizer.isOperator(test));
    }

    @Test
    public void testIsOperator3() {
        String test = "*";
        assertTrue(Tokenizer.isOperator(test));
    }

    @Test
    public void testIsOperator4() {
        String test = "/";
        assertTrue(Tokenizer.isOperator(test));
    }

    @Test
    public void testIsOperator5() {
        String test = "^";
        assertTrue(Tokenizer.isOperator(test));
    }

    @Test
    public void testIsOperator6() {
        String test = "?";
        assertFalse(Tokenizer.isOperator(test));
    }

    @Test
    public void testIsOperator7() {
        String test = "_";
        assertFalse(Tokenizer.isOperator(test));
    }

    @Test
    public void testTokenize1() {
        String input = "7+e^.3-(sin(pi) + -2.3471)/x^2.5";
        ArrayList<Token> expected = new ArrayList<Token>();
        expected.add(new NumberToken(7.0));
        expected.add(new OperatorToken("+"));
        expected.add(new StringToken("e"));
        expected.add(new OperatorToken("^"));
        expected.add(new NumberToken(0.3));
        expected.add(new OperatorToken("-"));
        expected.add(new Parenthesis("("));
        expected.add(new StringToken("sin"));
        expected.add(new Parenthesis("("));
        expected.add(new StringToken("pi"));
        expected.add(new Parenthesis(")"));
        expected.add(new OperatorToken("+"));
        expected.add(new NumberToken(-2.3471));
        expected.add(new Parenthesis(")"));
        expected.add(new OperatorToken("/"));
        expected.add(new StringToken("x"));
        expected.add(new OperatorToken("^"));
        expected.add(new NumberToken(2.5));
        ArrayList<Token> result = Tokenizer.tokenize(input);
        assertEquals(expected.size(), result.size());
        for (int i = 0; i < expected.size(); i++) {
            assertTrue("#" + i + ": expected " + expected.get(i).getClass() + ", actually " + result.get(i).getClass(),
                    expected.get(i).getClass().equals(result.get(i).getClass()));
            assertTrue("#" + i + ": expected " + expected.get(i) + ", actually " + result.get(i),
                    expected.get(i).toString().equals(result.get(i).toString()));
        }
    }
}
