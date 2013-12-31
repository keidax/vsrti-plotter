package common.Mathematics;

import java.util.ArrayList;

/**
 * Handles tokenization of a String. See method documentation for more details.
 *
 * @author Gabriel Holodak
 * @version 12/31/13.
 */
public class Tokenizer {

    /**
     * Turns an equation represented by a string into a list of Tokens. Can handle:
     * <ul>
     * <li>positive and negative numbers</li>
     * <li>basic mathematical operators</li>
     * <li>functions and variables</li>
     * <li>parentheses</li>
     * <li>unary negation of numbers, variables, and functions</li>
     * </ul>
     *
     * @param s the the string to tokenize
     * @return a list of tokens representing s
     */
    public static ArrayList<Token> tokenize(String s) {
        ArrayList<Token> tokens = new ArrayList<Token>(s.length());
        s = s.replace(" ", "");
        char chars[] = s.toCharArray();
        String temp = "";
        for (int i = 0; i < chars.length; i++) {
            if (isPartOfNumber(temp)) { // already building a number
                if (Character.isDigit(chars[i]) || chars[i] == '.') {
                    // continue the number
                    temp += Character.toString(chars[i]);
                } else {
                    // finish the number
                    tokens.add(new NumberToken(temp)); // ensures token string is e.g. 1.0 instead of 1
                    temp = "";
                    if (isOperator(chars[i])) {
                        tokens.add(new OperatorToken(Character.toString(chars[i])));
                    } else if (chars[i] == ')') {
                        tokens.add(new Parenthesis(")"));
                    } else {
                        throw new RuntimeException("incorrect character following a number: " + chars[i]);
                    }
                }
            } else if (isPartOfVariableOrString(temp)) { // already building a string
                if (Character.isLetterOrDigit(chars[i]) || chars[i] == '_') {
                    // continue the string
                    temp += Character.toString(chars[i]);
                } else {
                    // finish the string
                    tokens.add(new StringToken(temp));
                    temp = "";
                    if (isOperator(chars[i])) {
                        tokens.add(new OperatorToken(Character.toString(chars[i])));
                    } else if (chars[i] == '(' || chars[i] == ')') {
                        tokens.add(new Parenthesis(Character.toString(chars[i])));
                    } else {
                        throw new RuntimeException("incorrect character following a string: " + chars[i]);
                    }
                }
            } else {
                // temp should be empty
                if (!temp.equals("")) {
                    throw new RuntimeException("temporary string should be empty");
                }

                if (chars[i] == '(' || chars[i] == ')') {
                    tokens.add(new Parenthesis(Character.toString(chars[i])));
                } else if (chars[i] == '-') {
                    if (i == 0 || isOperator(chars[i - 1]) || chars[i - 1] == '(') { // '-' is not the subtraction operator
                        if (i + 1 == chars.length) {
                            throw new RuntimeException("input cannot end with a minus sign");
                        }

                        if (Character.isDigit(chars[i + 1]) || chars[i + 1] == '.') { // a number follows the '-'
                            temp = "-";
                        } else { // a variable or function follows the '-'
                            tokens.add(new OperatorToken("-", true)); // unary negation
                        }
                    } else {
                        tokens.add(new OperatorToken("-"));
                    }
                } else if (isOperator(chars[i])) {
                    tokens.add(new OperatorToken(Character.toString(chars[i])));
                } else if (Character.isLetterOrDigit(chars[i]) || chars[i] == '.') {
                    temp = Character.toString(chars[i]);
                } else {
                    throw new RuntimeException("unknown character: " + chars[i]);
                }
            }
        }
        // add the last token
        if (isPartOfNumber(temp)) {
            tokens.add(new NumberToken(temp));
        } else if (isPartOfVariableOrString(temp)) {
            tokens.add(new StringToken(temp));
        }
        return tokens;
    }

    /**
     * Checks if a string could be the beginning of a number. Uses the following rules:
     * <ul>
     * <li>The string may have a minus sign, but only as the character</li>
     * <li>The string may have at most one decimal place</li>
     * <li>Other than the above-mentioned symbols, all characters </li>
     * </ul>
     *
     * @param s the string to check
     * @return true if s follows the rules for a partial number, false otherwise
     */
    public static boolean isPartOfNumber(String s) {
        if (s.length() == 0)
            return false;
        boolean hasPeriod = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '-') {
                if (i != 0)
                    return false;
            } else if (c == '.') {
                if (hasPeriod)
                    return false;
                else hasPeriod = true;
            } else if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a string could be the beginning of a variable or function name.
     *
     * @param s the string to check
     * @return true if the string contains only alphanumeric and underscore characters, false otherwise
     */
    public static boolean isPartOfVariableOrString(String s) {
        if (s.length() == 0)
            return false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if ((c < '0' || c > '9') && (c < 'a' || c > 'z') && (c < 'A' || c > 'Z') && c != '_') {
                return false;
            }
        }
        return true;
    }

    /**
     * Convenience method for isOperator(char)
     *
     * @param s the string to check
     * @return true if the string contains a single character and that character is an operator
     */
    public static boolean isOperator(String s) {
        if (s.length() != 1)
            return false;
        else
            return isOperator(s.charAt(0));
    }

    /**
     * Determines if the character is a mathematical operator
     *
     * @param c the character to check
     * @return true if c is a +, -, *, /, or ^ sign; false otherwise
     */
    public static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }
}
