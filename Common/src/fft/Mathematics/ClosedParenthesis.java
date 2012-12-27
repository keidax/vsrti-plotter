package fft.Mathematics;
/**
 * A token for Closed Parenthesis, ')'
 * 
 * @author Adam Pere
 * @version Project6
 */
public class ClosedParenthesis implements Token{
	
	private final int precedence;
	
	/**
	 * Default Constructor, sets the precedence to 0 because
	 * precedence does not affect parentheses. 
	 */
	public ClosedParenthesis()
	{
		precedence = 0;
	}
	
	/**
	 * @return a parenthesis as a string
	 */
	@Override
	public String toString()
	{
		return ")";
	}
	
	/**
	 * @param the Stack that is being used
	 * @return String of all the operators on the stack in order until it reaches
	 * an open parenthesis (which is not part of the String being returned)
	 */
    @Override
	public String handle(Stack s)
    {   
    	String returning = "";
    	
    	if(s.size()!= 0 && !(s.top().toString().equals("(")))
    	{
    		returning = returning + s.pop();
    	}
    	    	
    	return returning;
    }
    
    /**
     * @return the precedence of the closed parenthesis, which is 0
     */
    @Override
	public int getPrecedence()
    {
    	return precedence;
    }
}
