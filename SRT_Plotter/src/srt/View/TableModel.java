package srt.View;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class TableModel extends AbstractTableModel {
    
    public View viewer;
    public JTable supreme;
    public DataBlockList list;
    private int numCol;
    
    public TableModel(View v, DataBlockList l) {
        super();
        viewer = v;
        list = l;
        numCol = 2;
    }
    
    public TableModel(View v, DataBlockList l, int columns) {
        super();
        viewer = v;
        list = l;
        numCol = columns;
    }
    
    @Override
    public int getRowCount() {
        return list.size();
    }
    
    @Override
    public int getColumnCount() {
        return numCol;
    }
    
    @Override
    public Object getValueAt(int arg0, int arg1) {
        if (arg0 < list.size() && arg0 >= 0 && arg1 < numCol && arg1 >= 0) {
            if (arg1 == 0) {
                return arg0 + ") " + list.get(arg0).title;
            } else if (arg1 == 1) {
                DataBlock n = list.get(arg0);
                String ret = n.fStart + ", " + n.fStep + ", " + n.data.length;
                return ret;
            } else if (arg1 == 2) {
                return list.get(arg0).angle;
            }
        }
        
        return -1;
    }
    
    /**
     * returns the data block in the given row
     * 
     * @param arg0
     * @return
     */
    public DataBlock getValueAt(int arg0) {
        return list.get(arg0);
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
        list.clear();
    }
}
