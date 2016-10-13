package srt.View;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.TreeMap;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelListener;

import common.View.ViewUtilities;

/**
 * 
 * @author Adam Pere and Karel Durkota
 * 
 */
@SuppressWarnings("serial")
public class FileTable extends JTable implements TableModelListener {
    
    public FileTable me = this;
    
    public FileTable(TableModel m) {
        super(m);
        getModel().addTableModelListener(this);
        setEnabled(true);
        setDragEnabled(false);
        setFillsViewportHeight(true);
        setMinimumSize(new Dimension(50, 50));
        getColumnModel().getColumn(0).setHeaderValue("Data Block");
        if (m.getColumnCount() == 3) {
            getColumnModel().getColumn(2).setHeaderValue("Angle"); // TODO find out what this is about with 2 or 3 columns
            getColumnModel().getColumn(2).setMaxWidth(60);
        }
        
        addMouseListener(new MouseAdapter() {
            
            /**
             * Graphs the selected data block.
             */
            @Override
            public void mousePressed(MouseEvent e) {
                me.updateGraph();
            }
            
            /**
             * Right click to delete data block
             */
            @Override
            public void mouseClicked(MouseEvent arg0) {
                if (arg0.getButton() == MouseEvent.BUTTON3) {
                    if (View.getView(FileTable.this) == null || !View.getView(FileTable.this).canDelete) {
                        return;
                    }
                    arg0.consume();
                    JPopupMenu menu = new JPopupMenu();
                    JMenuItem item = new JMenuItem("Delete Data Block(s)");
                    menu.add(item);
                    menu.show(me, arg0.getX(), arg0.getY());
                    
                    item.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (View.getView(FileTable.this).canDelete) {
                                me.delete();
                            }
                        }
                    });
                }
            }
        });
        
        addKeyListener(new KeyAdapter() {
            
            /**
             * Delete key to delete selected data block.
             */
            @Override
            public void keyPressed(KeyEvent e) {
                int c = e.getKeyCode();
                if (c == KeyEvent.VK_DELETE || c == KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                    if (View.getView(FileTable.this) != null && View.getView(FileTable.this).canDelete) {
                        me.delete();
                    }
                }
                
            }
        });
    }
    
    @Override
    public void editingStopped(ChangeEvent e) {
        if (me.getEditingColumn() == 2) {
            try {
                Double.parseDouble((String) this.getCellEditor().getCellEditorValue());
                ((TableModel) me.getModel()).list.get(getEditingRow()).angle = Double.parseDouble((String) this.getCellEditor().getCellEditorValue());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "You can only set the angle to a number.");
            }
        }
        cellEditor.cancelCellEditing();
    }
    
    /**
     * Updates the Visibility graph with the points of the selected data set.
     * This is necessary if any points are deleted or changed.
     */
    public void updateGraph() {
        int[] index = me.getSelectedRows();
        ((TableModel) me.getModel()).viewer.getVGraph().getPoints().clear();
        
        if (index.length > 0 && ((TableModel) me.getModel()).getRowCount() > index[0]) {
            double[] data = ((TableModel) me.getModel()).getValueAt(index[0]).data;
            
            TreeMap<Double, Double> points = new TreeMap<Double, Double>();
            // TreeMap<Double, Double> rms = new TreeMap<Double, Double>();
            
            for (int i = 0; i < data.length; i++) {
                if (data[i] != -1) {
                    points.put(i + 1.0, data[i]);
                    
                }
            }
            ((TableModel) me.getModel()).viewer.adapter.getVisiblityGraphRms().clear();
            ((TableModel) me.getModel()).viewer.adapter.importVisibilityGraphPoints(points);
            ((TableModel) me.getModel()).viewer.currentDataBlock = ((TableModel) me.getModel()).getValueAt(index[0]);
            
        }
    }

    /**
     * Plots the velocity of the selected data block on x-axis.
     */
    public void plotVelocity() {
        int[] index = me.getSelectedRows();

        if (index.length <= 0 || ((TableModel) me.getModel()).getValueAt(index[0]) == null) {
            JOptionPane.showMessageDialog(null, "You must select a data block before you can plot its spectrum.");
            return;
        }
        ((TableModel) me.getModel()).viewer.getVGraph().getPoints().clear();

        double[] data = ((TableModel) me.getModel()).getValueAt(index[0]).data;
        double start = ((TableModel) me.getModel()).getValueAt(index[0]).fStart;
        double step = ((TableModel) me.getModel()).getValueAt(index[0]).fStep;
        TreeMap<Double, Double> points = new TreeMap<Double, Double>();

        double velOffset = ViewUtilities.frequencyToVelocity(start);


        for (int i = 0; i < data.length; i++) {
            if (data[i] >= 0) {
                double freq = start + i * step;
                points.put(ViewUtilities.frequencyToVelocity(freq) - velOffset, data[i]);
            }
        }

        ((TableModel) me.getModel()).viewer.adapter.getVisiblityGraphRms().clear();
        ((TableModel) me.getModel()).viewer.adapter.importVisibilityGraphPoints(points);
        ((TableModel) me.getModel()).viewer.currentDataBlock = ((TableModel) me.getModel()).getValueAt(index[0]);
    }
    
    /**
     * Plots the spectrum of the selected data block. That is Temperature vs.
     * Frequency.
     */
    public void plotSpectrum() {
        int[] index = me.getSelectedRows();
        
        if (index.length <= 0 || ((TableModel) me.getModel()).getValueAt(index[0]) == null) {
            JOptionPane.showMessageDialog(null, "You must select a data block before you can plot its spectrum.");
            return;
        }
        ((TableModel) me.getModel()).viewer.getVGraph().getPoints().clear();
        
        double[] data = ((TableModel) me.getModel()).getValueAt(index[0]).data;
        double step = ((TableModel) me.getModel()).getValueAt(index[0]).fStep;
        TreeMap<Double, Double> points = new TreeMap<Double, Double>();
        
        for (int i = 0; i < data.length; i++) {
            if (data[i] >= 0) {
                points.put(i * step, data[i]);
            }
            
        }
        ((TableModel) me.getModel()).viewer.adapter.getVisiblityGraphRms().clear();
        ((TableModel) me.getModel()).viewer.adapter.importVisibilityGraphPoints(points);
        ((TableModel) me.getModel()).viewer.currentDataBlock = ((TableModel) me.getModel()).getValueAt(index[0]);
    }
    
    /**
     * Plots the spectrum of the selected data block. That is Temperature vs.
     * Frequency.
     */
    public boolean plotBeam() {
        int[] index = me.getSelectedRows();
        
        if (index.length <= 0 || ((TableModel) me.getModel()).getValueAt(index[0]) == null) {
            JOptionPane.showMessageDialog(null, "You must select at least one data block to plot the beam width.");
            return false;
        }
        ((TableModel) me.getModel()).viewer.getVGraph().getPoints().clear();
        
        double avg = 0;
        double angle = 0;
        
        TreeMap<Double, Double> rms = new TreeMap<Double, Double>();
        TreeMap<Double, Double> points = new TreeMap<Double, Double>();
        
        double min = ((TableModel) me.getModel()).viewer.min;
        System.out.println(min);
        for (int i = 0; i < index.length; i++) {
            avg = ((TableModel) me.getModel()).getValueAt(index[i]).getAverageOverFrequency();
            angle = ((TableModel) me.getModel()).getValueAt(index[i]).angle - min;
            points.put(angle, avg);
            rms.put(angle, avg / Math.sqrt(index.length));
            
        }
        ((TableModel) me.getModel()).viewer.adapter.importVisibilityGraphRms(rms);
        ((TableModel) me.getModel()).viewer.adapter.importVisibilityGraphPoints(points);
        ((TableModel) me.getModel()).viewer.currentDataBlock = ((TableModel) me.getModel()).getValueAt(index[0]);
        return true;
    }
    
    /**
     * Deletes the selected data blocks as well as the AVG and SUB data blocks
     * that were created using one or more of the selected data blocks.
     */
    public void delete() {
        int[] selected = me.getSelectedRows();
        if (selected.length == 0 || me.getRowCount() == 0) {
            return;
        }
        int count = 0;
        String mess = "";
        String numb = " ";
        String[] temp, temp1;
        
        for (int i = 0; i < selected.length; i++) {
            mess = mess + selected[i] + ") " + ((TableModel) me.getModel()).viewer.dataBlockList.get(selected[i]).title + "\n";
            numb = numb + selected[i] + " ";
        }

        for(DataBlock node : ((TableModel) me.getModel()).viewer.dataBlockList) {
            if (node.title.contains("AVG") || node.title.contains("SUB")) {
                temp = node.title.split(" ");
                for (int i = 0; i < temp.length; i++) {
                    if (numb.contains(temp[i])) {
                        mess = mess + count + ") " + node.title + "\n";
                        break;
                    }
                }
            }
            count++;
        }
        
        int delete = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to delete these data blocks? The associated SUB and AVG data blocks will also be deleted.\n" + mess);
        
        if (delete == 0) {
            for (int i = selected.length - 1; i >= 0; i--) {
                ((TableModel) me.getModel()).removeInputFile(selected[i]);
            }
            ((TableModel) me.getModel()).viewer.currentDataBlock = null;

            for(DataBlock node : ((TableModel) me.getModel()).viewer.dataBlockList) {
                if (node.title.contains("AVG") || node.title.contains("SUB")) {
                    temp = node.title.split(" ");
                    temp1 = numb.split(" ");
                    for (int i = 0; i < temp.length; i++) {
                        if (numb.contains(" " + temp[i] + " ")) {
                            ((TableModel) me.getModel()).viewer.dataBlockList.remove(node.title);
                            break;
                        }
                        
                        for (int j = 0; j < temp1.length; j++) {
                            try {
                                Integer.parseInt(temp[i]);
                                if (Integer.parseInt(temp[i]) > Integer.parseInt(temp1[j])) {
                                    temp[i] = Integer.parseInt(temp[i]) - 1 + "";
                                }
                            } catch (Exception ex) {
                                
                            }
                        }
                        
                    }
                    node.title = temp[0];
                    for (int k = 1; k < temp.length; k++) {
                        node.title = node.title + " " + temp[k];
                    }
                }
            }
            me.addNotify();
            me.clearSelection();
            me.changeSelection(0, 0, false, false);
            me.updateGraph();
        }
    }
    
}
