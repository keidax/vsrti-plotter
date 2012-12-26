package fft.Model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

import fft.View.InputFile;
import fft.View.View;
import fft.View.ViewListener;

public class Controller implements ViewListener{

    private Model model;
    private View view;

    public Controller(Model m, View v) {
        model = m;
        view = v;
    }

    

    

    

    public void fullReset() {
        model.emptyRawPoints();
        model.reinitialize();
        model.update();
    }
    
    public void reset() {
        model.reinitialize();
        model.update();
    }
    
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
