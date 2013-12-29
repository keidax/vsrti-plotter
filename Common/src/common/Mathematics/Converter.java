package common.Mathematics;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Stack;

/**
 * Evaluates an infix expression (that is broken up into an Array of type String
 * by operator) by converting it to postfix and evaluating the postfix
 * expression. It can handle trig functions (sin, cos, tan), ln, log, and delta
 * functions.
 *
 * @author Adam Pere
 * @author Gabriel Holodak
 * @version 06/30/2011
 */
public class Converter {
    private ArrayList<Token> equation, postEq;
    private double x;

    public void setX(double x) {
        this.x = x;
    }

    /**
     * Default constructor, initializes the variables.
     *
     * @param eqn the equation as a String
     */
    public Converter(String eqn) {
        this(Tokenizer.tokenize(eqn));
    }

    private Converter(ArrayList<Token> tokens) {
        equation = tokens;
        postEq = new ArrayList<Token>(equation.size());
        convertToPostFix();
    }

    /**
     * Evaluate converts the equation from infix to postfix notation and then
     * evaluates the postfix equation.
     *
     * @return the mathematical answer of the equation.
     */
    public Double evaluate() {
        Stack<Token> stack = new Stack<Token>();
        // main evaluation loop
        for (Token t : postEq) {
            String s = t.toString();
            switch (t.getType()) {
                case NUMBER:
                    stack.push(t);
                    break;
                case DELIMITER:
                    throw new InputMismatchException("unexpected delimiter: " + t);
//                    break;
                case OPERATOR:
                    Token temp;

                    if (((OperatorToken) t).isUnary()) {
                        Double b = Double.parseDouble(stack.pop().toString());
                        if (s.equals("-")) { //unary negation
                            temp = new NumberToken(-b);
                        } else {
                            throw new UnsupportedOperationException("unknown unary operator " + t);
                        }
                    } else {
                        Double b = Double.parseDouble(stack.pop().toString());
                        Double a = Double.parseDouble(stack.pop().toString());

                        if (s.equals("-")) {
                            temp = new NumberToken(a - b);
                        } else if (s.equals("+")) {
                            temp = new NumberToken(a + b);
                        } else if (s.equals("*")) {
                            temp = new NumberToken(a * b);
                        } else if (s.equals("/")) {
                            if (b == 0.0) {
                                //TODO throw div by 0 error
                                //throw new ArithmeticException("division by zero");
                                temp = new NumberToken(0.0);
                            } else
                                temp = new NumberToken(a / b);
                        } else if (s.equals("^")) {
                            temp = new NumberToken(Math.pow(a, b));
                        } else {
                            throw new UnsupportedOperationException("unknown binary operator " + t);
                        }
                    }
                    stack.push(temp);
                    break;
                case STRING:
                    Token val = null;
                    if (s.equalsIgnoreCase("pi")) {
                        val = new NumberToken(Math.PI);
                    } else if (s.equalsIgnoreCase("e")) {
                        val = new NumberToken(Math.E);
                    } else if (s.equals("x")) {
                        //TODO insert real value of x, maybe using a map?
                        val = new NumberToken((double) x);
                    } else {
                        Double arg = Double.parseDouble(stack.pop().toString());
                        if (s.equals("sin")) {
                            val = new NumberToken(Math.sin(arg));
                        } else if (s.equals("cos")) {
                            val = new NumberToken(Math.cos(arg));
                        } else if (s.equals("tan")) {
                            val = new NumberToken(Math.tan(arg));
                        } else if (s.equals("ln")) {
                            val = new NumberToken(Math.log(arg));
                        } else if (s.equals("log")) {
                            val = new NumberToken(Math.log10(arg));
                        } else if (s.equals("u")) {
                            if (arg >= 0) {
                                val = new NumberToken(1.0);
                            } else {
                                val = new NumberToken(0.0);
                            }
                        } else if (s.equals("delta")) {
                            if (arg == 0) {
                                val = new NumberToken(1.0);
                            } else {
                                val = new NumberToken(0.0);
                            }
                        } else if (s.equals("exp")) {
                            val = new NumberToken(Math.exp(arg));
                        } else {
                            throw new RuntimeException("unknown variable or function " + s);
                        }
                    }
                    stack.push(val);
                    break;
            }
        }
        if (stack.size() > 1) {
            throw new RuntimeException("too many tokens; maybe an operator is missing?");
        }
        return Double.parseDouble(stack.peek().toString());
    }

    /**
     * Converts equation from infix to postfix. If it encounters any trig
     * functions, log, ln, or delta, it will evaluate that function and the
     * answer is used in the postfix notation instead of the trig function.
     */
    private void convertToPostFix() {
        Stack<Token> opStack = new Stack<Token>();
        for (Token t : equation) {
            String s = t.toString();
            switch (t.getType()) {
                case NUMBER:
                    postEq.add(t);
                    break;
                case DELIMITER:
                    if (s.equals("("))
                        opStack.push(t);
                    else if (s.equals(")")) {
                        while (!opStack.empty() && !opStack.peek().toString().equals("(")) {
                            postEq.add(opStack.pop());
                        }
                        if (opStack.isEmpty()) {
                            throw new InputMismatchException("mismatched parentheses");
                        }
                        opStack.pop(); //pop the '('
                        if (!opStack.empty() && opStack.peek().getType() == Token.TOKEN_TYPE.STRING) { //it's a function
                            // pop and add the function
                            postEq.add(opStack.pop());
                        }
                    } else {
                        throw new RuntimeException("unknown delimiter: " + t);
                    }
                    break;
                case OPERATOR:
                    if (!((OperatorToken) t).isUnary()) {
                        while (!opStack.empty() && ((!s.equals("^") && t.getPrecedence() == opStack.peek().getPrecedence())
                                || (t.getPrecedence() < opStack.peek().getPrecedence()))) {
                            postEq.add(opStack.pop());
                        }
                    }
                    opStack.push(t);
                    break;
                case STRING:
                    if (s.equals("x") ||
                            s.equalsIgnoreCase("e") ||
                            s.equalsIgnoreCase("pi")) { // it's a variable //TODO maybe we want a better way of detecting if it's a variable -- a map?
                        postEq.add(t);
                    } else { // it's a function
                        opStack.push(t);
                    }
                    break;
            }
        }
        while (!opStack.isEmpty()) {
            if (opStack.peek().toString().contains("(")) {
                throw new InputMismatchException("mismatched parentheses");
            }
            postEq.add(opStack.pop());
        }
    }
}
