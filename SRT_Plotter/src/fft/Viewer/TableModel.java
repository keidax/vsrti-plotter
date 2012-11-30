package fft.Viewer;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;


public class TableModel extends AbstractTableModel {

    public Viewer viewer;
    public JTable supreme;
    public LinkedList list;
    private int numCol;
 

    public TableModel(Viewer v, LinkedList l) {
        super();
        viewer = v;
        list = l;
        numCol = 2;
    }
    
    public TableModel(Viewer v, LinkedList l, int columns) {
        super();
        viewer = v;
        list = l;
        numCol = columns;
    }
   
    /**
     * Returns column name
     */
    @Override
    public String getColumnName(int column) {
        return "Start Freq, Step Freq, # Channels";
    }
   
    /**
     * Returns number of rows
     */
    @Override
    public int getRowCount() {
        return list.getLength();
    }
    
    /**
     * Returns number of columns
     */
    @Override
    public int getColumnCount() {
        return numCol;
    }
    
    /**
     * returns column class name
     */
    @Override
    public Class getColumnClass(int column) {
        return String.class;
    }
    
    /**
     * returns the value at row, column
     */
    @Override
    public Object getValueAt(int arg0, int arg1) {
    	if(arg0<list.getLength() && arg0 >= 0 && arg1 < numCol && arg1 >=0)
    	{
    		if(arg1 == 0)
    		{
    			return arg0 + ") " + list.getNode(arg0).title;
    		}
    		else if(arg1 ==1)
    		{
    			ListNode n =  list.getNode(arg0);
    			String ret = n.fStart + ", " + n.fStep + ", " + n.data.length;
    			return ret;
    		}
    		else if(arg1 == 2)
    		{
    			return list.getNode(arg0).angle;
    		}
    	}
    		
    	return -1;
    }
    
    /**
     * returns the list node in the inputed row
     * @param arg0
     * @return
     */
    public Object getValueAt(int arg0) {
        return list.getNode(arg0);
    }
    
    @Override
    public void setValueAt(Object value, int row, int column) {
       
    }
    
    /**
     * returns whether or not a cell is editable
     */
    @Override
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        if (col < 1 || col == numCol-1) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Removes the input file at index i
     * @param i
     */
    public void removeInputFile(int i) {
       list.remove(i);
     
    }
    
    /**
     * Removes all input files
     */
    public void removeAllInputFiles()
    {
    	int size = list.getLength();
    	int i = 0;
    	
    	while(i < size)
    	{
    		list.remove(0);
    		i++;
    	}
    }
}
