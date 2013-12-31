package common.Mathematics;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Stack;

/**
 * Converts an infix equation to a postfix equation. See documentation for convertToPostfix for more details.
 *
 * @author Adam Pere
 * @author Gabriel Holodak
 * @version 12/31/2013
 */
public class PostfixConverter {

    private ArrayList<Token> postfixEquation;

    /**
     * This constructor takes the equation as a string in infix and automatically tokenizes and converts to postfix.
     * The result is accessible via the getPostfixEquation() getter.
     *
     * @param infixEquation the equation as a String
     */
    public PostfixConverter(String infixEquation) {
        this(Tokenizer.tokenize(infixEquation));
    }

    private PostfixConverter(ArrayList<Token> tokens) {
        postfixEquation = convertToPostfix(tokens);
    }

    /**
     * @return the list of tokens in postfix form
     */
    public ArrayList<Token> getPostfixEquation() {
        return postfixEquation;
    }


    /**
     * Converts infixEquation from infix to postfix. Uses the shunting-yard algorithm. Will throw an error if there are
     * mismatched parentheses
     *
     * @param infixEquation a list of tokens in infix form
     * @return a list of tokens in postfix form
     */
    public static ArrayList<Token> convertToPostfix(ArrayList<Token> infixEquation) {
        Stack<Token> opStack = new Stack<Token>();
        ArrayList<Token> postfixEquation = new ArrayList<Token>(infixEquation.size());
        for (Token t : infixEquation) {
            String s = t.toString();
            switch (t.getType()) {
                case NUMBER:
                    postfixEquation.add(t);
                    break;
                case DELIMITER:
                    if (s.equals("("))
                        opStack.push(t);
                    else if (s.equals(")")) {
                        while (!opStack.empty() && !opStack.peek().toString().equals("(")) {
                            postfixEquation.add(opStack.pop());
                        }
                        if (opStack.isEmpty()) {
                            throw new InputMismatchException("mismatched parentheses");
                        }
                        opStack.pop(); //pop the '('
                        if (!opStack.empty() && opStack.peek().getType() == Token.TOKEN_TYPE.STRING) { //it's a function
                            // pop and add the function
                            postfixEquation.add(opStack.pop());
                        }
                    } else {
                        throw new RuntimeException("unknown delimiter: " + t);
                    }
                    break;
                case OPERATOR:
                    if (!((OperatorToken) t).isUnary()) {
                        while (!opStack.empty() && ((!s.equals("^") && t.getPrecedence() == opStack.peek().getPrecedence())
                                || (t.getPrecedence() < opStack.peek().getPrecedence()))) {
                            postfixEquation.add(opStack.pop());
                        }
                    }
                    opStack.push(t);
                    break;
                case STRING:
                    if (s.equals("x") || s.equals("t") ||
                            s.equalsIgnoreCase("e") ||
                            s.equalsIgnoreCase("pi")) { // it's a variable //TODO maybe we want a better way of detecting if it's a variable -- a map?
                        postfixEquation.add(t);
                    } else { // it's a function
                        opStack.push(t);
                    }
                    break;
            }
        }
        while (!opStack.isEmpty()) {
            if (opStack.peek().toString().equals("(")) {
                throw new InputMismatchException("mismatched parentheses");
            }
            postfixEquation.add(opStack.pop());
        }
        return postfixEquation;
    }
}
