package fft.Mathematics;

/**
 * A Token linkedList based Stack that will return Tokens first in last out.
 * 
 * @author Adam Pere
 * @version Project6
 *
 */
public class Stack {
	
	private int count;
	private LinkedList items;

	/**
	 * Default constructor
	 */
	public Stack()
	{
		count = 0;
		items = new LinkedList();
	}
	
	/**
	 * @return the size of the Stack
	 */
	public int size()
	{
		return count;
	}
	
	/**
	 * Adds object to the top of the stack
	 * @param Token to be pushed onto the Stack
	 */
	public void push(Token obj)
	{
		ListNode temp = new ListNode(obj);
		items.insertAtHead(temp);
		count++;
	}
	
	/**
	 * removes and returns the first item on the stack.
	 * @return the Token being removed (the previous first Token)
	 */
	public Token pop()
	{
		if(count == 0)
			return null;
		else
			{
			count--;
			return items.removeHead().data;
			}
			
	}
	
	/**
	 * Returns the first Token on the Stack without removing it
	 * @return the first item on the stack
	 */
	public Token top()
	{
		return items.getFirstData();
	}
	
	/**
	 * @return a printable String of the contents of the stack
	 */
	@Override
	public String toString()
	{
		return items.toString();
	}
	
	/**
	 * @return if the Stack is empty or not
	 */
	public boolean isEmpty()
	{
		return(size() == 0);
	}
}
