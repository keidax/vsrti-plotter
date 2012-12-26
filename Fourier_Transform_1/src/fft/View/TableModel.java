package fft.View;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel {

    private ArrayList<InputFile> inputFiles;
    private String[] columnNames = {"Baseline", "File path"}; //TODO get right names

    public TableModel() {
        super();
        inputFiles = new ArrayList<InputFile>();
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
    
    public ArrayList<InputFile> getInputFiles(){
    	return inputFiles;
    }

    public void addInputFile(InputFile inputFile) {
        for (InputFile f : inputFiles) {
            if (inputFile.file.getAbsolutePath().equals(f.file.getAbsolutePath())) {
                return;
            }
        }
        this.inputFiles.add(inputFile);
        Collections.sort(inputFiles);
    }

    public void removeInputFile(int i) {
        System.out.println("delete the " + i + "th row");
        try {
            inputFiles.remove(i);
        } catch (IndexOutOfBoundsException e) {
        }
        System.out.println("Size" + inputFiles.size());
    }
    
    public void removeAllInputFiles()
    {	/*
    	for(int i = 0; i < inputFiles.size(); i++)
    	{
	        	viewer.adapter.removeRms(inputFiles.get(i).getBaseline());
    	}*/
    	inputFiles.clear();
    }

    public void removeInputFile(String filename) {
        for (InputFile f : inputFiles) {
            if (f.file.getAbsolutePath().equals(filename)) {
                inputFiles.remove(f);
            }
        }
    }
}
