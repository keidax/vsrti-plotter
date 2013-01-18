package common.Mathematics;

/**
 * Linked List is a collection of data nodes of Tokens
 * 
 * @author Adam Pere
 * @version Lab 6
 */
public class LinkedList {
    private int length;
    private ListNode firstNode;
    
    /**
     * Default constructor - initializes the variables.
     */
    public LinkedList() {
        length = 0;
        firstNode = null;
    }
    
    /**
     * getter
     * 
     * @return number of nodes in the list
     */
    public int getLength() {
        return length;
    }
    
    /**
     * insert given node at head of list
     * 
     * @param newnode
     *            the node to add
     */
    public void insertAtHead(ListNode newnode) {
        if (length == 0) {
            firstNode = newnode;
        } else {
            newnode.next = firstNode;
            firstNode = newnode;
        }
        length++;
    }
    
    /**
     * @return a string representation of the list and its contents.
     */
    @Override
    public String toString() {
        String toReturn = "(";
        ListNode n;
        n = firstNode;
        while (n != null) {
            
            toReturn = toReturn + n.toString();
            n = n.next;
            
            if (n != null) {
                toReturn = toReturn + ", ";
            }
        }
        toReturn = toReturn + ")";
        return toReturn;
    }
    
    /**
     * Removes the first Node in the linked list
     * 
     * @return the node that was removed.
     */
    public ListNode removeHead() {
        if (length == 0) {
            return null;
        } else {
            ListNode temp = firstNode;
            firstNode = firstNode.next;
            length--;
            return temp;
        }
        
    }
    
    /**
     * Getter
     * 
     * @return The data of the first Node.
     */
    public Token getFirstData() {
        if (length == 0) {
            return null;
        } else {
            return firstNode.data;
        }
    }
    
}
