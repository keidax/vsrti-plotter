package fft.Mathematics;

/**
 * A Token for the Exponent Operator, '^'
 * 
 * @author Adam Pere
 * @version Project6
 *
 */
public class Exponent implements Token{
	
	private final int precedence;
	
	/**
	 * Default constructor - initializes the precedence.
	 */
	public Exponent()
	{
		precedence = 3;
	}
	
	/**
	 * @return the exponent symbol as a printable String
	 */
	@Override
	public String toString()
	{
		return "^";
	}
	
	/**
	 * pops and appends to the returning String every operator on the Stack
	 * until the Stack is empty, the top of the stack is a left parenthesis(which isn't popped off)
	 * or the operator on the top of the stack has a lower precedence than the current operator.
	 * Pushes a Token of type Exponent onto the Stack
	 * @param the Stack that is being used
	 * @return A String of all the operators that this method popped off of the Stack
	 */
    @Override
	public String handle(Stack s)
    {   
    	String returning = "";
    	
    	if(s.size() != 0 && !(s.top().toString().equals("(")) && this.precedence < s.top().getPrecedence())
    	{
    		returning = returning + s.pop();
    	}
    	
    	s.push(this);
    	return returning;
    }
    
    /**
     * @return the precedence of the exponent symbol.
     */
    @Override
	public int getPrecedence()
    {
    	return precedence;
    }
    
}
