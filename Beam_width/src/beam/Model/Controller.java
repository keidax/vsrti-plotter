package beam.Model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

import javax.swing.JFileChooser;

import beam.View.View;

import common.View.ViewUtilities;

public class Controller {
    
    private Model model;
    private View view;
    
    public Controller(Model m, View v) {
        model = m;
        view = v;
        
        view.addOpenButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = ViewUtilities.getFileChooser();
                fileChooser.showOpenDialog(view);
                File f = fileChooser.getSelectedFile();
                if (f == null || !f.canRead()) {
                    return;
                }
                TreeMap<Double, Double>[] tm = view.parseFile(f);
                if (tm != null) {
                    model.importPoints(tm[0]);
                    model.importRms(tm[1]);
                    view.getCanvas().setGraphTitle("Beam (" + ViewUtilities.getShortFileName(f) + ")");
                }
                model.notifyObservers();
            }
        });
        
        view.addSaveButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser fileChooser = ViewUtilities.getFileChooser();
                fileChooser.showSaveDialog(view);
                File f = fileChooser.getSelectedFile();
                if (f == null || !f.canRead()) {
                    return;
                } else if (!f.getName().endsWith(".dat")) {
                    f = new File(f.getAbsolutePath() + ".dat");
                }
                writeSaveFile(f);
            }
        });
        
        view.addResetButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                reset();
                model.notifyObservers();
            }
        });
        
        view.addDeleteButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                ((beam.View.TableModel) view.getFileTable().getModel()).removeAllInputFiles();
                fullReset();
                view.getCanvas().setGraphTitle("Beam");
                model.notifyObservers();
            }
        });
        
        view.addHideButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setBeamPatternVisible(!model.getBeamPatternVisible());
                model.notifyObservers();
            }
        });
        
        view.addUpdateParametersActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    double tempNoise = Double.parseDouble(view.fNoise.getText());
                    model.setNoise(tempNoise);
                } catch (NumberFormatException e) {}
                try {
                    double tempDiameter = Double.parseDouble(view.fD.getText());
                    model.setDiameter(tempDiameter);
                } catch (NumberFormatException e) {}
                try {
                    double tempLambda = Double.parseDouble(view.fLambda.getText());
                    model.setLambda(tempLambda);
                } catch (NumberFormatException e) {}
                try {
                    double tempSigma = Double.parseDouble(view.fSigma.getText());
                    model.setDisplayFactor(tempSigma);
                } catch (NumberFormatException e) {}
                try {
                    double tempPeakValue = Double.parseDouble(view.fPeakValue.getText());
                    model.setPeakValue(tempPeakValue);
                } catch (NumberFormatException e) {}
                try {
                    double tempHorizontalError = Double.parseDouble(view.fHorizontalError.getText());
                    model.setHorizontalError(tempHorizontalError);
                } catch (NumberFormatException e) {}
                model.notifyObservers();
            }
        });
    }
    
    public void fullReset() {
        model.emptyRawPoints();
        reset();
    }
    
    public void reset() {
        model.resetValuesToDefaults();
        model.reinitialize();
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
