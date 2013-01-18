package common.Mathematics;

/**
 * A Token for the Plus Operator, '+'
 * 
 * @author Adam Pere
 * @version Project6
 * 
 */
public class Plus implements Token {
    
    private final int precedence;
    
    /**
     * Default Constructor - sets the precedence
     */
    public Plus() {
        precedence = 1;
    }
    
    /**
     * @return Plus as a printable String
     */
    @Override
    public String toString() {
        return "+";
    }
    
    /**
     * pops and appends to the returning String every operator on the Stack
     * until the Stack is empty, the top of the stack is a left
     * parenthesis(which isn't popped off) or the operator on the top of the
     * stack has a lower precedence than the current operator. Pushes a Token of
     * type Plus onto the stack.
     * 
     * @param the
     *            Stack that is being used
     * @return A String of all the operators that this method popped off of the
     *         Stack
     */
    @Override
    public String handle(Stack s) {
        String returning = "";
        
        if (s.size() != 0 && !s.top().toString().equals("(") && precedence <= s.top().getPrecedence()) {
            returning = returning + s.pop();
        }
        
        s.push(this);
        return returning;
    }
    
    /**
     * Returns the precedence
     */
    @Override
    public int getPrecedence() {
        return precedence;
    }
    
}
