package srt.View;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class TableModel extends AbstractTableModel {
    
    public View viewer;
    public JTable supreme;
    public LinkedList list;
    private int numCol;
    
    public TableModel(View v, LinkedList l) {
        super();
        viewer = v;
        list = l;
        numCol = 2;
    }
    
    public TableModel(View v, LinkedList l, int columns) {
        super();
        viewer = v;
        list = l;
        numCol = columns;
    }
    
    @Override
    public int getRowCount() {
        return list.getLength();
    }
    
    @Override
    public int getColumnCount() {
        return numCol;
    }
    
    @Override
    public Object getValueAt(int arg0, int arg1) {
        if (arg0 < list.getLength() && arg0 >= 0 && arg1 < numCol && arg1 >= 0) {
            if (arg1 == 0) {
                return arg0 + ") " + list.getNode(arg0).title;
            } else if (arg1 == 1) {
                ListNode n = list.getNode(arg0);
                String ret = n.fStart + ", " + n.fStep + ", " + n.data.length;
                return ret;
            } else if (arg1 == 2) {
                return list.getNode(arg0).angle;
            }
        }
        
        return -1;
    }
    
    /**
     * returns the list node in the inputed row
     * 
     * @param arg0
     * @return
     */
    public Object getValueAt(int arg0) {
        return list.getNode(arg0);
    }
    
    /**
     * Removes the input file at index i
     * 
     * @param i
     */
    public void removeInputFile(int i) {
        list.remove(i);
        
    }
    
    /**
     * Removes all input files
     */
    public void removeAllInputFiles() {
        int size = list.getLength();
        int i = 0;
        
        while (i < size) {
            list.remove(0);
            i++;
        }
    }
}
