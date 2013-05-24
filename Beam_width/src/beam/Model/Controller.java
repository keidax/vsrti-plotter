package beam.Model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import beam.View.ViewListener;

public class Controller implements ViewListener {
    
    private Model model;
    
    // private View view;
    
    public Controller(Model m) {
        model = m;
        // view = v;
    }
    
    @Override
    public void fullReset() {
        model.emptyRawPoints();
        reset();
    }
    
    @Override
    public void reset() {
        model.resetValuesToDefaults();
        model.reinitialize();
        model.update();
    }
    
    @Override
    public void writeSaveFile(File f) {
        
        BufferedWriter out;
        
        try {
            
            out = new BufferedWriter(new FileWriter(f));
            out.write(model.exportPoints());
            out.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
