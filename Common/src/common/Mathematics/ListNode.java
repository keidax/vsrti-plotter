package common.Mathematics;

/**
 * A ListNode for objects of Type Token
 * 
 * @author Adam Pere
 * @date Project6
 */
public class ListNode {
    public Token data;
    public ListNode next;
    
    /**
     * Non-Default constructor
     * 
     * @param newData
     *            the Token for the ListNode to contain
     */
    public ListNode(Token newData) {
        data = newData;
        next = null;
    }
    
    /**
     * @return The Node's data as a printable String
     */
    @Override
    public String toString() {
        return data.toString();
    }
}