package fringe.View;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.table.AbstractTableModel;

import common.View.InputFile;

@SuppressWarnings("serial")
public class TableModel extends AbstractTableModel {
    
    public View viewer;
    public ArrayList<InputFile> inputFiles;
    public static String[] columnNames = {"Angle", "Data Files"};
    
    public TableModel(View v) {
        super();
        viewer = v;
        inputFiles = new ArrayList<InputFile>();
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
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
                    return f.getFile().getName();
                }
            }
            i++;
        }
        return null;
    }
    
    public void addInputFile(InputFile inputFile) {
        for (InputFile f : inputFiles) {
            if (inputFile.getFile().getAbsolutePath().equals(f.getFile().getAbsolutePath())) {
                return;
            }
        }
        inputFiles.add(inputFile);
        Collections.sort(inputFiles);
    }
    
    public void removeInputFile(int i) {
        System.out.println("delete the " + i + "th row");
        try {
            inputFiles.remove(i);
        } catch (IndexOutOfBoundsException e) {}
        System.out.println("Size" + inputFiles.size());
        viewer.sendAdapterFiles();
    }
    
    public void removeAllInputFiles() {
        inputFiles.clear();
        viewer.sendAdapterFiles();
    }
    
    public void removeInputFile(String filename) {
        for (InputFile f : inputFiles) {
            if (f.getFile().getAbsolutePath().equals(filename)) {
                inputFiles.remove(f);
            }
        }
    }
}
