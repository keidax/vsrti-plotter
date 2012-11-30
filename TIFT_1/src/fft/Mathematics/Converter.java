package fft.Mathematics;
/**
 * Evaluates an infix expression (that is broken up into an Array of type String by operator) by converting it
 * to postfix and evaluating the postfix expression. It can handle trig functions (sin, cos, tan), ln, log, and
 * delta functions.
 * 
 * @author Adam Pere
 * @version 06/30/2011
 *
 */
public class Converter {

		private Stack theStack;
		private String[] equation, postEq;
		private int nums; //Current index for postEq
		
		/**
		 * Default constructor, initializes the variables.
		 * @param eqn - the equation broken up into an array of type String by operators.
		 */
		public Converter(String[] eqn)
		{
			theStack = new Stack();
			equation = eqn;
			postEq = new String[equation.length];
			nums = 0;
		}
		
		/**
		 * Evaluate converts the equation from infix to postfix notation and then evaluates the postfix equation.
		 * @return answer - The mathematical answer of the equation.
		 */
		public Double evaluate()
		{
			convert();
			Double[] stack = new Double[postEq.length+1];
			int top = 0;
			Double answer = 0.0;
			
			for(int i = 0; i< postEq.length; i++)
			{
				
				if(postEq[i] != null)
				{
					if(postEq[i].equals("*"))
						{
						
						answer = stack[top]*stack[top-1];
						top = top-1;
						stack[top]= answer;
						
						}
					else if(postEq[i].equals("/"))
						{
						if(stack[top-1] != 0)
							{
							answer = stack[top-1]/ stack[top];
							top = top-1;
							stack[top]= answer;
							}
						else
						{
							top = top-1;
							stack[top]= 0.0;
							System.out.println("********* Tried to Divide by Zero ***********");
						}
						
						}
					else if(postEq[i].equals("+"))
						{
						System.out.println(stack[top] + " + " + stack[top-1]);
						answer = stack[top]+ stack[top-1];
						top = top-1;
						stack[top]= answer;
						}
					else if(postEq[i].equals("-"))
						{
						answer = stack[top-1] - stack[top];
						top = top-1;						
						stack[top]= answer;
						}
					else if(postEq[i].equals("^"))
						{
						
						answer = Math.pow(stack[top-1],stack[top]);
						top = top-1;
						stack[top]= answer;
						}
					
					else
					{
						if(postEq[i] != null && !postEq[i].equals(""))
						{
							top++;
							stack[top] = Double.parseDouble(postEq[i]);
						}
						
					}
				}
			}
			
			if(stack[top] != null)
				return stack[top];
			return answer;
		}
		
		
		/**
		 * Converts equation from infix to postfix. 
		 * If it encounters any trig functions, log, ln, or delta, it will evaluate that function and
		 * the answer is used in the postfix notation instead of the trig function.
		 */
		private void convert()
		{
			
			int num = equation.length;
			int i = 0;
			int j = 0;
			while(i<num && equation[i] != null)
			{
				
				if(equation[i].equals("sin"))
					j = sin(i);
				else if(equation[i].equals("cos"))
					j=cos(i);
				else if(equation[i].equals("tan"))
					j=tan(i);
				else if(equation[i].equals("ln"))
					j=ln(i);
				else if(equation[i].equals("log"))
					j=log(i);
				else if(equation[i].equals("delta"))
					j=delta(i);
				else if(equation[i].equals("u"))
					j=u(i);
				
				if(!equation[i].equals(""))
						identify(equation[i]);
				j++;
				i = j;
			}
			while(!theStack.isEmpty())
				{
					String temp = theStack.pop().toString();	
					if(!temp.contains("("))
					{
						postEq[nums] = temp;
						nums++;
					}				
				}			
		}
		
		/**
		 * Identify takes a String and identifies the String. If the string is equivalent
		 * to one of the 8 Tokens, a Token of the identified type is initialized and it's handle
		 * method is called and added to the postfix equation.
		 * If it is not identified as a token, it must be an operand added to the postfix equation.
		 */
		private void identify(String s)
		{
			Token t;
			if(s.equals("*"))
			{
				t = new Multiply();
				postEq[nums] = t.handle(theStack);
				if(postEq[nums] != null && !postEq[nums].equals(""))
					nums++;		
			}
			else if(s.equals("("))
			{
				t = new OpenParenthesis();
				postEq[nums] = t.handle(theStack);
				nums++;				
			}
			else if(s.equals(")"))
			{
				t = new ClosedParenthesis();
				postEq[nums] = t.handle(theStack);
				nums++;				
			}
			else if(s.equals("/"))
			{
				t = new Divide();
				postEq[nums] = t.handle(theStack);
				if(postEq[nums] != null && !postEq[nums].equals(""))
					nums++;
			}
			else if(s.equals("+"))
			{
				t = new Plus();
				postEq[nums] = t.handle(theStack);
				if(postEq[nums] != null && !postEq[nums].equals(""))
					nums++;
			}
			else if(s.equals("-"))
			{
				t = new Subtract();
				postEq[nums] = t.handle(theStack);
				if(postEq[nums] != null && !postEq[nums].equals(""))
					nums++;
			}
			else if(s.equals("^"))
			{
				t = new Exponent();
				postEq[nums] = t.handle(theStack);
				if(postEq[nums] != null && !postEq[nums].equals(""))
					nums++;
			}
			else if (s.equals(" ")){}
			else
			{
				postEq[nums] = s;
				if(postEq[nums] != null && !postEq[nums].equals(""))
					nums++;
			}
		}
		
		/**
		 * Handles the evaluation of a step function when found in the infix equation.
		 * @param j - the index of the string "u" in the infix equation
		 * @return the index, in the infix equation, of the correct closing parenthesis.
		 */
		private int u(int j)
	    {
	    	int L = equation.length;
	    	String[] inside = new String[L];
			int count = 0, k = j+2, pCounter = 0;
			boolean done = false;
			System.out.println("\n");
			while(k < equation.length && !done)
			{
				inside[count] = equation[k];
				
				if(inside[count].equals("("))
					pCounter++;
				else if(inside[count].equals(")"))
					pCounter--;
				
				equation[k] = "";
				k++;	
				count++;
							
				if(equation[k] != null && equation[k].contains(")") && pCounter == 0)
					{
						equation[k] = "";
						done = true;
					}
			}
			Converter con = new Converter(inside);	
			Double x  = con.evaluate(); 
			//System.out.println(Math.sin(x));
			if(x >= 0)
				equation[j] = 1.0 + "";
			else
				equation[j] = 0.0 + "";
			return k;		
	    }
		
		/**
		 * Handles the evaluation of a delta function when found in the infix equation.
		 * @param j - the index of the string "delta" in the infix equation
		 * @return the index, in the infix equation, of the correct closing parenthesis.
		 */
		private int delta(int j)
	    {
	    	int L = equation.length;
	    	String[] inside = new String[L];
			int count = 0, k = j+2, pCounter = 0;
			boolean done = false;
			System.out.println("\n");
			while(k < equation.length && !done)
			{
				inside[count] = equation[k];
				
				if(inside[count].equals("("))
					pCounter++;
				else if(inside[count].equals(")"))
					pCounter--;
				
				equation[k] = "";
				k++;	
				count++;
							
				if(equation[k] != null && equation[k].contains(")") && pCounter == 0)
					{
						equation[k] = "";
						done = true;
					}
			}
			Converter con = new Converter(inside);	
			Double x  = con.evaluate(); 
			//System.out.println(Math.sin(x));
			if(x == 0)
				equation[j] = 1.0 + "";
			else
				equation[j] = 0.0 + "";
			return k;		
	    }
		
		/**
		 * Handles the evaluation of a sin function when found in the infix equation.
		 * @param j - the index of the string "sin" in the infix equation
		 * @return the index, in the infix equation, of the correct closing parenthesis.
		 */
		private int sin(int j)
	    {
	    	int L = equation.length;
	    	String[] inside = new String[L];
			int count = 0, k = j+2, pCounter = 0;
			boolean done = false;
			System.out.println("\n");
			while(k < equation.length && !done)
			{
				inside[count] = equation[k];
				
				if(inside[count].equals("("))
					pCounter++;
				else if(inside[count].equals(")"))
					pCounter--;
				
				equation[k] = "";
				k++;	
				count++;
							
				if(equation[k] != null && equation[k].contains(")") && pCounter == 0)
					{
						equation[k] = "";
						done = true;
					}
			}
			Converter con = new Converter(inside);	
			Double x  = con.evaluate(); 
			//System.out.println(Math.sin(x));
			equation[j] = Math.sin(x) + "";
			return k;		
	    }
	    
		/**
		 * Handles the evaluation of a tan function when found in the infix equation.
		 * @param j - the index of the string "tan" in the infix equation
		 * @return the index, in the infix equation, of the correct closing parenthesis.
		 */
	    private int tan(int j)
	    {
	    	int L = equation.length;
	    	String[] inside = new String[L];
			int count = 0, k = j+2, pCounter = 0;
			boolean done = false;
			System.out.println("\n");
			while(k < equation.length && !done)
			{
				inside[count] = equation[k];
				
				if(inside[count].equals("("))
					pCounter++;
				else if(inside[count].equals(")"))
					pCounter--;
				
				equation[k] = "";
				k++;	
				count++;
							
				if(equation[k] != null && equation[k].contains(")") && pCounter == 0)
					{
						equation[k] = "";
						done = true;
					}
			}
			Converter con = new Converter(inside);	
			Double x  = con.evaluate(); 
			//System.out.println(Math.sin(x));
			equation[j] = Math.tan(x) + "";
			return k;	
	    }
	    
	    /**
		 * Handles the evaluation of a cos function when found in the infix equation.
		 * @param j - the index of the string "cos" in the infix equation
		 * @return the index, in the infix equation, of the correct closing parenthesis.
		 */
	    private int cos(int j)
	    {
	    	int L = equation.length;
	    	String[] inside = new String[L];
			int count = 0, k = j+2, pCounter = 0;
			boolean done = false;
			System.out.println("\n");
			while(k < equation.length && !done)
			{
				inside[count] = equation[k];
				
				if(inside[count].equals("("))
					pCounter++;
				else if(inside[count].equals(")"))
					pCounter--;
				
				equation[k] = "";
				k++;	
				count++;
							
				if(equation[k] != null && equation[k].contains(")") && pCounter == 0)
					{
						equation[k] = "";
						done = true;
					}
			}
			Converter con = new Converter(inside);	
			Double x  = con.evaluate(); 
			//System.out.println(Math.sin(x));
			equation[j] = Math.cos(x) + "";
			return k;
	    }
	    
	    /**
		 * Handles the evaluation of an ln function when found in the infix equation.
		 * @param j - the index of the string "ln" in the infix equation
		 * @return the index, in the infix equation, of the correct closing parenthesis.
		 */
	    private int ln(int j)
	    {
		    int L = equation.length;
	    	String[] inside = new String[L];
			int count = 0, k = j+2, pCounter = 0;
			boolean done = false;
			System.out.println("\n");
			while(k < equation.length && !done)
			{
				inside[count] = equation[k];
				
				if(inside[count].equals("("))
					pCounter++;
				else if(inside[count].equals(")"))
					pCounter--;
				
				equation[k] = "";
				k++;	
				count++;
							
				if(equation[k] != null && equation[k].contains(")") && pCounter == 0)
					{
						equation[k] = "";
						done = true;
					}
			}
			Converter con = new Converter(inside);	
			Double x  = con.evaluate(); 
			if(x == 0)
				equation[j] = 0.0 + "";
			else
				equation[j] = Math.log(x) + "";
			return k;		
	    }
	    
	    /**
		 * Handles the evaluation of a log (base 10) function when found in the infix equation.
		 * @param j - the index of the string "log" in the infix equation
		 * @return the index, in the infix equation, of the correct closing parenthesis.
		 */
	   private int log(int j)
	    {
		   int L = equation.length;
	    	String[] inside = new String[L];
			int count = 0, k = j+2, pCounter = 0;
			boolean done = false;
			System.out.println("\n");
			while(k < equation.length && !done)
			{
				inside[count] = equation[k];
				
				if(inside[count].equals("("))
					pCounter++;
				else if(inside[count].equals(")"))
					pCounter--;
				
				equation[k] = "";
				k++;	
				count++;
							
				if(equation[k] != null && equation[k].contains(")") && pCounter == 0)
					{
						equation[k] = "";
						done = true;
					}
			}
			Converter con = new Converter(inside);	
			Double x  = con.evaluate(); 
			if(x == 0)
				equation[j] = 0.0 + "";
			else
				equation[j] = Math.log10(x) + "";
			return k;
	    }
	   
	   /**
	    * @return The equation in postfix notation
	    */
	   public String[] getPostfix()
	   {
		   return postEq;
	   }

		
		
}
