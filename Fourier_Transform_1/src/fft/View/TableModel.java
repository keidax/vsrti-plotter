package fft.View;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import fft.View.InputFile;
import fft.View.View;

public class TableModel extends AbstractTableModel {

    public View viewer;
    public ArrayList<InputFile> inputFiles;
    public static String[] columnNames = {"Baseline", "Data file name"};

    public TableModel(View v) {
        super();
        viewer = v;
        inputFiles = new ArrayList<InputFile>();
        InputFile.tableModel = this;
    }
    
    @Override
    public String getColumnName(int columnIndex){
    	return columnNames [columnIndex];
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
        viewer.sendAdapterFiles();
    }
    
    public void removeAllInputFiles()
    {
    	for(int i = 0; i < inputFiles.size(); i++)
    	{
	        	viewer.adapter.removeRms(inputFiles.get(i).getBaseline());
    	}
    	inputFiles.clear();
        viewer.sendAdapterFiles();
    }

    public void removeInputFile(String filename) {
        for (InputFile f : inputFiles) {
            if (f.file.getAbsolutePath().equals(filename)) {
                inputFiles.remove(f);
            }
        }
    }
}
