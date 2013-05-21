package common.View;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public abstract class BaseView extends JFrame {
    protected JFileChooser fileChooser;
    
    public BaseView(String title) {
        super(title);
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("DAT file", "dat"));
    }
    
    /**
     * Gets the values for various constants from the model.
     * I don't know if it's better to have the view pull or the model
     * push, but for now I think it's the view's responsibility
     */
    public abstract void updateValuesFromModel();
    
    public abstract void updateModelFromValues();
    
}
