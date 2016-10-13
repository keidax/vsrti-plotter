package srt.View;

import java.util.ArrayList;

/**
 * A collection of DataBlocks.
 *
 * @author Adam Pere
 * @author Gabriel Holodak
 * @version Lab 6
 */
public class DataBlockList extends ArrayList<DataBlock>{

    public DataBlockList() {
        super();
    }

    /**
     * @return a string representation of the dataBlockList and its contents.
     */
    @Override
    public String toString() {
        String toReturn = "";
        for(DataBlock n: this){
            // call node's toString automatically
            toReturn = toReturn + n.toString() + "\n";
        }
        return toReturn;
    }

    /**
     * returns the index of the listNode with the value passed in returns -1 if
     * the string is not in the DataBlockList
     *
     * @param title the key of the DataBlock being searched for
     */
    public int getIndex(String title) {
        for (int index = 0; index < this.size(); index++) {
            if (this.get(index).title.equals(title)) {
                return index;
            }
        }

        return -1;
    }

    /**
     * removes the first node that has has the passed in value
     *
     * @param title value of the node being removed
     */
    public void remove(String title) {
        int index = getIndex(title);
        if(index >= 0) {
            this.remove(index);
        }
    }
}
