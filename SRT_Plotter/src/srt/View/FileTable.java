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
    
    public FileTable(MyTableModel m) {
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
                // If the mouse is pressed on a data block while in Average TA mode, we want to leave that mode
                if(FileTable.this.getModel().viewer.canvas.getPlotMode() == VCanvas.PlotMode.PLOT_AVERAGE_TA) {
                    FileTable.this.getModel().viewer.canvas.setPlotMode(VCanvas.PlotMode.PLOT_CHANNELS);
                }
                FileTable.this.updateGraph();
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
                    menu.show(FileTable.this, arg0.getX(), arg0.getY());
                    
                    item.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (View.getView(FileTable.this).canDelete) {
                                FileTable.this.delete();
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
                        FileTable.this.delete();
                    }
                }
                
            }
        });
    }

    @Override
    public MyTableModel getModel(){
        return (MyTableModel) super.getModel();
    }
    
    @Override
    public void editingStopped(ChangeEvent e) {
        if (this.getEditingColumn() == 2) {
            try {
                Double.parseDouble((String) this.getCellEditor().getCellEditorValue());
                this.getModel().list.get(getEditingRow()).angle = Double.parseDouble((String) this.getCellEditor().getCellEditorValue());
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
        int[] index = this.getSelectedRows();

        if (index.length <= 0 || this.getModel().getValueAt(index[0]) == null) {
            JOptionPane.showMessageDialog(null, "You must select a data block before you can plot its spectrum.");
            return;
        }

        this.getModel().viewer.getCanvas().getPoints().clear();

        double[] data = this.getModel().getValueAt(index[0]).data;
        double start = this.getModel().getValueAt(index[0]).fStart;
        double step = this.getModel().getValueAt(index[0]).fStep;

        TreeMap<Double, Double> points = new TreeMap<Double, Double>();
        TreeMap<Double, Double> rms = new TreeMap<Double, Double>();

        for (int i = 0; i < data.length; i++) {
            if (data[i] != -1) {
                switch (this.getModel().viewer.canvas.getPlotMode()){
                    case PLOT_CHANNELS:
                        points.put(i + 1.0, data[i]);
                        break;
                    case PLOT_FREQUENCIES:
                        points.put(i * step, data[i]);
                        break;
                    case PLOT_VELOCITIES:
                        double freq = start + i * step;
                        points.put(ViewUtilities.frequencyToVelocity(freq), data[i]);
                        break;
                }
            }
        }
        this.getModel().viewer.adapter.getVisiblityGraphRms().clear();
        this.getModel().viewer.adapter.importVisibilityGraphPoints(points);
        this.getModel().viewer.currentDataBlock = this.getModel().getValueAt(index[0]);
    }
    
    /**
     * Plots the spectrum of the selected data block. That is Temperature vs.
     * Frequency.
     */
    public boolean plotBeam() {
        int[] index = this.getSelectedRows();
        
        if (index.length <= 0 || this.getModel().getValueAt(index[0]) == null) {
            JOptionPane.showMessageDialog(null, "You must select at least one data block to plot the beam width.");
            return false;
        }
        this.getModel().viewer.getCanvas().getPoints().clear();
        
        double avg = 0;
        double angle = 0;
        
        TreeMap<Double, Double> rms = new TreeMap<Double, Double>();
        TreeMap<Double, Double> points = new TreeMap<Double, Double>();
        
        double min = this.getModel().viewer.min;
        System.out.println(min);
        for (int i = 0; i < index.length; i++) {
            avg = this.getModel().getValueAt(index[i]).getAverageOverFrequency();
            angle = this.getModel().getValueAt(index[i]).angle - min;
            points.put(angle, avg);
            rms.put(angle, avg / Math.sqrt(index.length));
            
        }
        this.getModel().viewer.adapter.importVisibilityGraphRms(rms);
        this.getModel().viewer.adapter.importVisibilityGraphPoints(points);
        this.getModel().viewer.currentDataBlock = this.getModel().getValueAt(index[0]);
        return true;
    }
    
    /**
     * Deletes the selected data blocks as well as the AVG and SUB data blocks
     * that were created using one or more of the selected data blocks.
     */
    public void delete() {
        int[] selected = this.getSelectedRows();
        if (selected.length == 0 || this.getRowCount() == 0) {
            return;
        }
        int count = 0;
        String mess = "";
        String numb = " ";
        String[] temp, temp1;
        
        for (int i = 0; i < selected.length; i++) {
            mess = mess + selected[i] + ") " + this.getModel().viewer.dataBlockList.get(selected[i]).title + "\n";
            numb = numb + selected[i] + " ";
        }

        for(DataBlock node : this.getModel().viewer.dataBlockList) {
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
                this.getModel().removeInputFile(selected[i]);
            }
            this.getModel().viewer.currentDataBlock = null;

            for(DataBlock node : this.getModel().viewer.dataBlockList) {
                if (node.title.contains("AVG") || node.title.contains("SUB")) {
                    temp = node.title.split(" ");
                    temp1 = numb.split(" ");
                    for (int i = 0; i < temp.length; i++) {
                        if (numb.contains(" " + temp[i] + " ")) {
                            this.getModel().viewer.dataBlockList.remove(node.title);
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
            this.addNotify();
            this.clearSelection();
            this.changeSelection(0, 0, false, false);
            this.updateGraph();
        }
    }
    
}
