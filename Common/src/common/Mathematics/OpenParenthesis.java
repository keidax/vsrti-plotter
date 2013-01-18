package common.Mathematics;

/**
 * A Token for Open Parenthesis
 * 
 * @author Adam Pere
 * @version Project6
 * 
 */
public class OpenParenthesis implements Token {
    
    private final int precedence;
    
    /**
     * Default Constructor - sets the precedence.
     */
    public OpenParenthesis() {
        precedence = 0;
    }
    
    /**
     * @return The Open Parenthesis as a printable String
     */
    @Override
    public String toString() {
        return "(";
    }
    
    /**
     * Pushes a Token of type OpenParenthesis onto the stack
     * 
     * @param the
     *            Stack that is being used
     * @return a String of ""
     */
    @Override
    public String handle(Stack s) {
        s.push(new OpenParenthesis());
        
        return "";
    }
    
    /**
     * @return the precedence
     */
    @Override
    public int getPrecedence() {
        return precedence;
    }
    
}
