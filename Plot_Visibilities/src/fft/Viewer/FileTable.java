package fft.Viewer;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTable;
import javax.swing.event.TableModelListener;

public class FileTable extends JTable implements TableModelListener {

    private static final long serialVersionUID = 1L;

    ;
    public FileTable me = this;
    public JTable table;
    //public SortedSet<InputFile> inputFiles;
    //public AbstractTableModel model;

    public FileTable(TableModel m) {
        super(m);
        //model=m;
        //table = new JTable();
        //inputFiles = new TreeSet<InputFile>();

        getModel().addTableModelListener(this);
        this.setEnabled(true);
        this.setDragEnabled(true);
        this.setFillsViewportHeight(true);
        this.setMinimumSize(new Dimension(100, 600));
        this.getColumnModel().getColumn(0).setMaxWidth(55);
        this.getColumnModel().getColumn(0).setHeaderValue("Baseline");
        this.getColumnModel().getColumn(1).setPreferredWidth(50);
        this.getColumnModel().getColumn(1).setHeaderValue("Data file names");

        this.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                int c = e.getKeyCode();
                if (c == KeyEvent.VK_DELETE) {
                    int[] index = me.getSelectedRows();
                    e.consume();
                    for (int i = index.length - 1; i >= 0; --i) {
                        ((TableModel) me.getModel()).removeInputFile(index[i]);
                        //me.getModel().getValueAt(me.getSelectedRow(), 1));
                        //model.removeRow(index[i]);
                    }
                }
            }
        });

        //this.getColumnModel().getColumn(0).setCellEditor(new MyTableCellEditorForDouble());
    }
}
