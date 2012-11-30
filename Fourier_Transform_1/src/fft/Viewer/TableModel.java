package fft.Viewer;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel {

    public Viewer viewer;
    public JTable supreme;
    public ArrayList<InputFile> inputFiles;
    public static String[] columnNames = {"Baseline", "Data file name"};

    public TableModel(Viewer v) {
        super();
        viewer = v;
        inputFiles = new ArrayList<InputFile>();
        InputFile.tableModel = this;

        //this.addTableModelListener(this);
    }

//	public void setValueAt(Object value, int row, int col) {
//		if(col==0){
//			inputFiles.get(row).baseline = (Double)value;
//			fireTableCellUpdated(row, col);
//		}
//        
//    }
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return inputFiles.size();
    }

    @Override
    public Class getColumnClass(int column) {
        if (column == 0) {
            return Double.class;
        } else {
            return File.class;
        }
    }

    @Override
    public Object getValueAt(int arg0, int arg1) {
        int i = 0;
        for (InputFile f : inputFiles) {
            if (i == arg0) {
                if (arg1 == 0) {
                    return f.baseline;
                } else {
                    return f.file.getName();
                }
            }
            i++;
        }
        return null;
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        if (column == 0) {
            this.inputFiles.get(row).setBaseline((Double) value);
        }
    }

    public void addEptyRow() {
        inputFiles.add(new InputFile());
        fireTableRowsInserted(inputFiles.size() - 1, inputFiles.size() - 1);
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        if (col < 1) {
            return true;
        } else {
            return false;
        }
    }

    public void addInputFile(InputFile inputFile) {
        for (InputFile f : inputFiles) {
            if (inputFile.file.getAbsolutePath().equals(f.file.getAbsolutePath())) {
                return;
            }
        }
        this.inputFiles.add(inputFile);
    }

    public void removeInputFile(int i) {
        System.out.println("delete the " + i + "th row");
        viewer.adapter.removeRms(inputFiles.get(i).getBaseline());
        try {
            inputFiles.remove(i);
        } catch (IndexOutOfBoundsException e) {
        }
        System.out.println("Size" + inputFiles.size());
        supreme.repaint();
        viewer.sendAdapterFiles();
    }
    
    public void removeAllInputFiles()
    {
    	for(int i = 0; i < inputFiles.size(); i++)
    	{
	        	viewer.adapter.removeRms(inputFiles.get(i).getBaseline());
    	}
    	inputFiles.clear();
    	supreme.repaint();
        viewer.sendAdapterFiles();
    }

    public void removeInputFile(String filename) {
        for (InputFile f : inputFiles) {
            if (f.file.getAbsolutePath().equals(filename)) {
                inputFiles.remove(f);
            }
        }
    }
//	@Override
//	public void tableChanged(TableModelEvent e) {
//		// TODO Auto-generated method stub
//		int row = e.getFirstRow();
//        int column = e.getColumn();
//        TableModel model = (TableModel)e.getSource();
//        String columnName = model.getColumnName(column);
//        inputFiles.get(row).baseline = (Double)model.getValueAt(row, column);
//	}
}
