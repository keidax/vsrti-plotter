package fft.Model;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

import fft.Viewer.InputFile;

/**
 * Seems to provide encapsulation for a model and not a whole lot more
 *
 */

public class Adapter {

    private Model model;

    public Adapter(Model m) {
        model = m;
    }

    public TreeMap<Double, Double> getVisiblityGraphPoints() {
        return getModel().getVisibilityGraph().getPoints();
    }

    public String getSaveFilename() {
        return getModel().getVisibilityGraph().getSaveFile().getAbsolutePath();
    }

    public double getLambda() {
        return getModel().getVisibilityGraph().getLambda();
    }

    public double getDeltaBaseline() {
        return model.getVisibilityGraph().getDeltaBaseline();
    }

    public void setDeltaBaseline(double d) {
        getModel().getVisibilityGraph().setDeltaBaseline(d);
    }

    public void setSaveFile(File f) {
        getModel().getVisibilityGraph().setSaveFile(f);
    }

    public void setLambda(double l) {
        getModel().getVisibilityGraph().setLambda(l);
    }

    public int getExponent() {
        return getModel().getVisibilityGraph().getExponent();
    }

    public void setExponent(int a) {
        this.getModel().getVisibilityGraph().setExponent(a);
    }

    public TreeMap<Double, Double> getVisibilityGraphDataPoints() {
        return this.getModel().getVisibilityGraph().getDataPoints();
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void setRawPoints(ArrayList<InputFile> f) {
        this.getModel().getVisibilityGraph().emptyRawPoints();
        this.getModel().getVisibilityGraph().inputFiles.clear();
        getModel().getVisibilityGraph().addInputFiles(f);
        for (InputFile i : f) {
            this.getModel().getVisibilityGraph().addRawPoint(i.getBaseline(), i.getAverageIntensity());
            this.getModel().getVisibilityGraph().addRms(i.getBaseline(), i.getRms());
        }
        this.getModel().updateListeners();
    }

    public void setNoise(double n){
        this.getModel().getVisibilityGraph().setNoise(n);
    }

    public TreeMap<Double, Double> getRms() {
        return getModel().getVisibilityGraph().getGridedRms();
    }

    public void moveVisibilityPoint(double currentPoint, double toy) {
        this.getModel().getVisibilityGraph().movePoint(currentPoint, toy);
    }

    public void importVisibilityGraphPoints(TreeMap<Double, Double> parseFile) {
        this.getModel().getVisibilityGraph().importPoints(parseFile);
    }

    public void importVisibilityGraphRms(TreeMap<Double, Double> parseData) {
        getModel().getVisibilityGraph().importRms(parseData);
    }

    public String exportVisibilityGraphPoints() {
        return this.getModel().getVisibilityGraph().toString();
    }

    public void removeRmsPoint(double x) {
        getModel().getVisibilityGraph().removeGridedRmsPoint(x);
    }

    public void fullReset() {
        getModel().getVisibilityGraph().emptyRawPoints();
        getModel().updateListeners();
    }
    
    public void reset() {
        getModel().getVisibilityGraph().reinitializePoints();
        getModel().updateListeners();
    }
}
