package fft.Model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

import fft.Viewer.InputFile;
import fft.Viewer.Viewer;
import fft.Viewer.ViewListener;

public class Adapter implements ViewListener{

    private Model model;
    private Viewer view;

    public Adapter(Model m, Viewer v) {
        model = m;
        view = v;
    }

    public TreeMap<Double, Double> getVisiblityGraphPoints() {
        return model.getVisibilityGraph().getPoints();
    }

    public String getSaveFilename() {
        return model.getVisibilityGraph().getSaveFile().getAbsolutePath();
    }

    public double getLambda() {
        return model.getVisibilityGraph().getLambda();
    }

    public double getDeltaBaseline() {
        return model.getVisibilityGraph().getDeltaBaseline();
    }

    public void setDeltaBaseline(double d) {
        model.getVisibilityGraph().setDeltaBaseline(d);
        view.setDeltaBaseline(d);
    }

    public void setSaveFile(File f) {
        model.getVisibilityGraph().setSaveFile(f);
    }
    
    @Override
    public void setLambda(double l) {
        model.getVisibilityGraph().setLambda(l);
        view.setLambda(l);
    }

    public int getExponent() {
        return model.getVisibilityGraph().getExponent();
    }

    public void setExponent(int a) {
        this.model.getVisibilityGraph().setExponent(a);
    }

    public TreeMap<Double, Double> getVisibilityGraphDataPoints() {
        return this.model.getVisibilityGraph().getDataPoints();
    }

    public void setRawPoints(ArrayList<InputFile> f) {
        this.model.getVisibilityGraph().emptyRawPoints();
        this.model.getVisibilityGraph().inputFiles.clear();
        model.getVisibilityGraph().addInputFiles(f);
        for (InputFile i : f) {
            this.model.getVisibilityGraph().addRawPoint(i.getBaseline(), i.getAverageIntensity());
            this.model.getVisibilityGraph().addRms(i.getBaseline(), i.getRms());
        }
        this.model.updateListeners();
    }

    public void setNoise(double n){
        this.model.getVisibilityGraph().setNoise(n);
    }

    public TreeMap<Double, Double> getRms() {
        return model.getVisibilityGraph().getGridedRms();
    }

    public void moveVisibilityPoint(double currentPoint, double toy) {
        this.model.getVisibilityGraph().movePoint(currentPoint, toy);
    }

    public void importVisibilityGraphPoints(TreeMap<Double, Double> parseFile) {
        this.model.getVisibilityGraph().importPoints(parseFile);
    }

    public void importVisibilityGraphRms(TreeMap<Double, Double> parseData) {
        model.getVisibilityGraph().importRms(parseData);
    }

    public String exportVisibilityGraphPoints() {
        return this.model.getVisibilityGraph().toString();
    }

    public void removeRmsPoint(double x) {
        model.getVisibilityGraph().removeGridedRmsPoint(x);
    }

    public void fullReset() {
        model.getVisibilityGraph().emptyRawPoints();
        model.updateListeners();
    }
    
    public void reset() {
        model.getVisibilityGraph().reinitializePoints();
        model.updateListeners();
    }
    
    public void writeSaveFile(File f) {

        BufferedWriter out;

        try {

            out = new BufferedWriter(new FileWriter(f));
            out.write(exportVisibilityGraphPoints());
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
