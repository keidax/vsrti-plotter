package common.View;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public abstract class BaseView extends JFrame {
    
    public BaseView(String title) {
        super(title);
    }
    
    /**
     * Gets the values for various constants from the model.
     * I don't know if it's better to have the view pull or the model
     * push, but for now I think it's the view's responsibility
     */
    public abstract void updateValuesFromModel();
    
}
