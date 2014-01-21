package common.Mathematics;

import java.util.*;

/**
 * This class can evaluate a mathematical equation given as a String. It automatically recognizes e and pi, and accepts
 * a map with user-defined variables. It also recognizes a number of functions. These are:
 * <table>
 * <tr><td>sin</td><td>sine</td></tr>
 * <tr><td>cos</td><td>cosine</td></tr>
 * <tr><td>tan</td><td>tangent</td></tr>
 * <tr><td>ln</td><td>natural logarithm</td></tr>
 * <tr><td>log</td><td>base 10 logarithm</td></tr>
 * <tr><td>exp</td><td>natural exponent, same as e^()</td></tr>
 * <tr><td>u</td><td>the unit step function</td></tr>
 * <tr><td>delta</td><td>the delta function</td></tr>
 * </table>
 *
 * @author Gabriel Holodak
 * @version 12/31/13
 */
public class PostfixEvaluator {

    private ArrayList<Token> postfixEquation;
    private Map<String, Double> map;
    private final double epsilon = 0.00001; // acceptable range of error for delta function

    public PostfixEvaluator(String equation) {
        this(PostfixConverter.convertToPostfix(Tokenizer.tokenize(equation)));
    }

    private PostfixEvaluator(ArrayList<Token> postfixEquation) {
        this(postfixEquation, new HashMap<String, Double>());
    }

    public PostfixEvaluator(String equation, Map<String, Double> map) {
        this(PostfixConverter.convertToPostfix(Tokenizer.tokenize(equation)), map);
    }

    private PostfixEvaluator(ArrayList<Token> postfixEquation, Map<String, Double> map) {
        this.postfixEquation = postfixEquation;
        this.map = map;
    }

    /**
     * Sets the map object used for variables in the equation
     *
     * @param map a map of variable name to variable value. Case-sensitive. The evaluator
     *            automatically checks for e and pi, so you don't need to add them to the map.
     */
    public void setMap(Map<String, Double> map) {
        this.map = map;
    }


    /**
     * Evaluates the postfix equation, using any variables given in the map. Entries in the map will override the usual
     * constant values for e and pi, and can turn functions the map recognizes, such as sin() or u(), into variables
     * (in which case they will no longer work as functions).
     *
     * @return the mathematical answer of the equation, with correct substitutions made for e, pi, and any variables set
     * by the user in the map.
     */
    public Double evaluate() {
        Stack<Token> stack = new Stack<Token>();
        // main evaluation loop
        for (Token t : postfixEquation) {
            String s = t.toString();
            switch (t.getType()) {
                case NUMBER:
                    stack.push(t);
                    break;
                case DELIMITER:
                    throw new InputMismatchException("unexpected delimiter: " + t);
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
                    if (map.containsKey(s)) {
                        val = new NumberToken(map.get(s));
                    } else if (s.equalsIgnoreCase("pi")) {
                        val = new NumberToken(Math.PI);
                    } else if (s.equalsIgnoreCase("e")) {
                        val = new NumberToken(Math.E);
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
                            if (Math.abs(arg) < epsilon) {
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
}
