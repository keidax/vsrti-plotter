package common.Mathematics;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Evaluates an infix expression (that is broken up into an Array of type String
 * by operator) by converting it to postfix and evaluating the postfix
 * expression. It can handle trig functions (sin, cos, tan), ln, log, and delta
 * functions.
 *
 * @author Adam Pere
 * @version 06/30/2011
 */
public class Converter {
    private ArrayList<Token> equation, postEq;

    public void setX(double x) {
        this.x = x;
    }

    private double x;

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
        System.out.println(postEq);
        // main evaluation loop
        for (Token t : postEq) {
            switch (t.getType()) {
                case NUMBER:
                    stack.push(t);
                    break;
                case DELIMITER:
                    //TODO shouldn't have any of these
                    break;
                case OPERATOR:
                    Token temp = null;
                    Double b = Double.parseDouble(stack.pop().toString());
                    Double a = Double.parseDouble(stack.pop().toString());
                    String s = t.toString();
                    if (s.equals("+")) {
                        temp = new NumberToken(a + b);
                    } else if (s.equals("-")) {
                        temp = new NumberToken(a - b);
                    } else if (s.equals("*")) {
                        temp = new NumberToken(a * b);
                    } else if (s.equals("/")) {
                        if (b == 0.0) {
                            //TODO throw div by 0 error
                        }
                        temp = new NumberToken(a / b);
                    } else if (s.equals("^")) {
                        temp = new NumberToken(Math.pow(a, b));
                    }

                    if (temp == null) {
                        //TODO error
                    }
                    stack.push(temp);
                    break;
                case STRING:
                    Token val = null;
                    if (t.toString().equalsIgnoreCase("pi")) {
                        val = new NumberToken(Math.PI);
                    } else if (t.toString().equalsIgnoreCase("e")) {
                        val = new NumberToken(Math.E);
                    } else if (t.toString().equals("x")) {
                        //TODO insert real value of x, maybe using a map?
                        val = new NumberToken((double) x);
                    } else {
                        if (stack.empty())
                            System.out.println("empty stack, t = " + t);
                        Double arg = Double.parseDouble(stack.pop().toString());
                        if (t.toString().equals("sin")) {
                            val = new NumberToken(Math.sin(arg));
                        } else if (t.toString().equals("cos")) {
                            val = new NumberToken(Math.cos(arg));
                        } else if (t.toString().equals("tan")) {
                            val = new NumberToken(Math.tan(arg));
                        } else if (t.toString().equals("ln")) {
                            val = new NumberToken(Math.log(arg));
                        } else if (t.toString().equals("log")) {
                            val = new NumberToken(Math.log10(arg));
                        } else if (t.toString().equals("u")) {
                            if (arg >= 0) {
                                val = new NumberToken(1.0);
                            } else {
                                val = new NumberToken(0.0);
                            }
                        } else if (t.toString().equals("delta")) {
                            if (arg == 0) {
                                val = new NumberToken(1.0);
                            } else {
                                val = new NumberToken(0.0);
                            }
                        } else if (t.toString().equals("exp")) {
                            val = new NumberToken(Math.exp(arg));
                        }
                    }
                    if (val == null) {
                        //TODO error
                    }
                    stack.push(val);
                    break;
            }
        }
        if (stack.size() != 1) {
            //TODO error
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
            switch (t.getType()) {
                case NUMBER:
                    postEq.add(t);
                    break;
                case DELIMITER:
                    if (t.toString().equals("("))
                        opStack.push(t);
                    else if (t.toString().equals(")")) {
                        while (!opStack.empty() && !opStack.peek().toString().equals("(")) {
                            postEq.add(opStack.pop());
                        }
                        if (opStack.isEmpty()) {
                            //TODO error mismatched parens
                        }
                        opStack.pop(); //pop the '('
                        if (!opStack.empty() && opStack.peek().getType() == Token.TOKEN_TYPE.STRING) { //it's a function
                            // pop and add the function
                            postEq.add(opStack.pop());
                        }
                    } else {
                        //TODO error unknown delimiter
                    }
                    break;
                case OPERATOR:
                    while (!opStack.empty() && ((!t.toString().equals("^") && t.getPrecedence() == opStack.peek().getPrecedence())
                            || (t.getPrecedence() < opStack.peek().getPrecedence()))) {
                        postEq.add(opStack.pop());
                    }
                    opStack.push(t);
                    break;
                case STRING:
                    if (t.toString().equals("x")) { // it's a variable //TODO maybe we want a better way of detecting if it's a variable -- a map?
                        postEq.add(t);
                    } else { // it's a function
                        opStack.push(t);
                    }
                    break;
            }
        }
        while (!opStack.isEmpty()) {
            if (opStack.peek().toString().contains("(")) {
                //TODO error mismatched parens
            }
            postEq.add(opStack.pop());
        }
    }

    /**
     * @return The equation in postfix notation
     */
    public String getPostfix() {
        String ret = "";
        for (Token t : postEq) {
            ret += t.toString();
        }
        return ret;
    }

    private static ArrayList<Token> extract_inner(ArrayList<Token> eq, int start_index) {
        ArrayList<Token> inner_eq = new ArrayList<Token>();
        int count = 0, tracking_index = start_index + 2, pCounter = 1;
        while (tracking_index < eq.size()) {
            inner_eq.add(eq.get(tracking_index));
            if (inner_eq.get(count).toString().equals("(")) {
                pCounter++;
            } else if (inner_eq.get(count).toString().equals(")")) {
                pCounter--;
                if (pCounter == 0)
                    break;
            }
            tracking_index++;
            count++;
        }
        if (pCounter != 0) {
            //TODO throw error: mismatched parens
        }
        return inner_eq;
    }
}
