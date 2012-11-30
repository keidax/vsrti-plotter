package fft.Viewer;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

public class MyTableCellEditorForDouble extends AbstractCellEditor implements TableCellEditor {

    JComponent component = new JTextField();

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int rowIndex, int vColIndex) {
        // 'value' is value contained in the cell located at (rowIndex, vColIndex)

        if (isSelected) {
            // cell (and perhaps other cells) are selected
        }

        // Configure the component with the specified value
        ((JTextField) component).setText(((Double) value).toString());

        // Return the configured component
        return component;
    }

    @Override
    public void addCellEditorListener(CellEditorListener arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void cancelCellEditing() {
        // TODO Auto-generated method stub
    }

    @Override
    public Object getCellEditorValue() {
        // TODO Auto-generated method stub
        return ((JTextField) component).getText();
    }

    @Override
    public boolean isCellEditable(EventObject arg0) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void removeCellEditorListener(CellEditorListener arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean shouldSelectCell(EventObject arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean stopCellEditing() {
        // TODO Auto-generated method stub
        return true;
    }
}
