package common.Mathematics;

import java.util.ArrayList;

/**
 * Created by gabriel on 12/25/13.
 */
public class Tokenizer {
    public static ArrayList<Token> tokenize(String s) {
        ArrayList<Token> tokens = new ArrayList<Token>(s.length());
        s = s.replace(" ", "");
        s = s.replace("_", "-"); //TODO maybe change this...?
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
                        //TODO error incorrect char
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
                        //TODO error incorrect char
                    }
                }
            } else {
                // temp should be empty
                if (temp.equals("")) {
                    //TODO error
                }

                if (chars[i] == '(' || chars[i] == ')') {
                    tokens.add(new Parenthesis(Character.toString(chars[i])));
                } else if (chars[i] == '-') {
                    if (i == 0 || isOperator(chars[i - 1])) { // '-' is beginning of a negative number, not an operator
                        temp = "-";
                    } else {
                        tokens.add(new OperatorToken("-"));
                    }
                } else if (isOperator(chars[i])) {
                    tokens.add(new OperatorToken(Character.toString(chars[i])));
                } else if (Character.isLetterOrDigit(chars[i]) || chars[i] == '.') {
                    temp = Character.toString(chars[i]);
                } else {
                    //TODO error unknown char
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

    public static boolean isPartOfNumber(String s) {
        if (s.length() == 0)
            return false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '-') {
                if (i != 0)
                    return false;
            } else if (c == '.') {
                return true;
            } else if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

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

    public static boolean isOperator(String s) {
        if (s.length() != 1)
            return false;
        else
            return isOperator(s.charAt(0));
    }

    public static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }
}
