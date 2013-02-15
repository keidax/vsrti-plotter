package beam.View;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTable;
import javax.swing.event.TableModelListener;

@SuppressWarnings("serial")
public class FileTable extends JTable implements TableModelListener {
    
    private static View v;
    
    public FileTable(TableModel m, View v) {
        super(m);
        this.v = v;
        // model=m;
        // table = new JTable();
        // inputFiles = new TreeSet<InputFile>();
        
        getModel().addTableModelListener(this);
        setEnabled(true);
        setDragEnabled(true);
        setFillsViewportHeight(true);
        setMinimumSize(new Dimension(100, 600));
        getColumnModel().getColumn(0).setMaxWidth(55);
        getColumnModel().getColumn(1).setPreferredWidth(50);
        
        addKeyListener(new KeyAdapter() {
            
            @Override
            public void keyPressed(KeyEvent e) {
                int c = e.getKeyCode();
                if (c == KeyEvent.VK_DELETE) {
                    int[] index = FileTable.this.getSelectedRows();
                    e.consume();
                    for (int i = index.length - 1; i >= 0; --i) {
                        ((TableModel) FileTable.this.getModel()).removeInputFile(index[i]);
                        // me.getModel().getValueAt(me.getSelectedRow(), 1));
                        // model.removeRow(index[i]);
                    }
                    
                    FileTable.v.sendAdapterFiles();
                }
            }
        });
    }
}
