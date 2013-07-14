package common.View;

import java.io.File;

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
    
    public static String getShortFileName(File f) {
        String fileName = f.getName();
        String shortName = "";
        if (fileName.endsWith(".rad") && fileName.contains("'")) {
            shortName = fileName.split("'")[0];
        } else {
            int startOfFileExtension = fileName.lastIndexOf(".");
            shortName = fileName.substring(0, startOfFileExtension);
        }
        
        return shortName;
    }
    
}
