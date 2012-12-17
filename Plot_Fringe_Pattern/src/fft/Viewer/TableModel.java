package fft.Viewer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel {

    public Viewer viewer;
    public ArrayList<InputFile> inputFiles;
    public static String[] columnNames = {"Baseline", "Data file names"};

    public TableModel(Viewer v) {
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
                    return f.getBaseline();
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
        Collections.sort(inputFiles);
        viewer.sendAdapterFiles();
        //viewer.adapter.setRawPoints(inputFiles);
    }

    public void removeInputFile(int i) {
        System.out.println("delete the " + i + "th row");
        try {
            inputFiles.remove(i);
        } catch (IndexOutOfBoundsException e) {
        }
        System.out.println("Size" + inputFiles.size());
        viewer.sendAdapterFiles();
    }
    
    public void removeAllInputFiles()
    {
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
