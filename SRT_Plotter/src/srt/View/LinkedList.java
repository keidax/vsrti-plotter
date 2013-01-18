package srt.View;

/**
 * Linked List is a collection of data nodes. All methods here relate to how one
 * can manipulate those nodes.
 * 
 * @author Adam Pere
 * @version Lab 6
 */
public class LinkedList {
    private int length; // number of nodes
    private ListNode firstNode; // pointer to first node
    
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
     * getter
     * 
     * @return pointer to first node in the list
     */
    public ListNode getFirstNode() {
        return firstNode;
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
     * insert given node at the tail of the list\
     * 
     * @param newnode
     *            the node to add
     */
    public void insertAtTail(ListNode newnode) {
        if (length == 0) {
            firstNode = newnode;
        } else {
            ListNode temp = firstNode;
            
            while (temp.next != null) {
                temp = temp.next;
            }
            
            temp.next = newnode;
            
        }
        
        length++;
    }
    
    /**
     * @return a string representation of the list and its contents.
     */
    @Override
    public String toString() {
        String toReturn = "";
        ListNode n;
        n = firstNode;
        System.out.println(n);
        while (n != null) {
            // toReturn = toReturn + n.student.getName(); //not reusable
            toReturn = toReturn + n.toString() + "\n"; // call node's toString
                                                       // automatically
            n = n.next;
        }
        return toReturn;
    }
    
    /**
     * returns the index of the listNode with the value passed in returns -1 if
     * the string is not in the LinkedList
     * 
     * @param value
     *            the key of the ListNode being searched for
     */
    public int getIndex(String titl) {
        int count = 0;
        
        if (length == 0) {
            return -1;
        } else {
            ListNode temp = firstNode;
            while (temp != null) {
                if (temp.title.equals(titl)) {
                    return count;
                } else {
                    temp = temp.next;
                    count++;
                }
            }
            
            return -1;
        }
    }
    
    /**
     * returns the Node at the given index
     * 
     * @param index
     *            the index of the listNode
     */
    public ListNode getNode(int index) {
        ListNode temp = firstNode;
        
        for (int i = 0; i < index; i++) {
            temp = temp.next;
        }
        
        return temp;
    }
    
    /**
     * removes the first node that has has the passed in value
     * 
     * @param the
     *            value of the node being removed
     */
    public void remove(String titl) {
        int index = getIndex(titl);
        ListNode node;
        
        if (index == 0) {
            firstNode = firstNode.next;
        } else if (index > 0) {
            node = getNode(index - 1);
            node.next = node.next.next;
        }
        length--;
        
    }
    
    /**
     * removes the node at index
     * 
     * @param the
     *            value of the node being removed
     */
    public void remove(int index) {
        
        ListNode node;
        
        if (index == 0) {
            firstNode = firstNode.next;
        } else if (index > 0) {
            node = getNode(index - 1);
            node.next = node.next.next;
        }
        length--;
        
    }
    
}
