package fft.Model;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;

import fft.View.InputFile;

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
        getModel().getVisibilityGraph().setExponent(a);
    }
    
    public TreeMap<Double, Double> getVisibilityGraphDataPoints() {
        return getModel().getVisibilityGraph().getDataPoints();
    }
    
    /*
     * public TreeMap<Double, Double> getImageGraphPoints() {
     * TreeMap<Double, Double> reduced = new TreeMap<Double, Double>();
     * Set<Double> keys = getModel().getImageGraph().getPoints().keySet();
     * int sum = keys.size();
     * int count = 0;
     * for (Double key : keys) {
     * count++;
     * reduced.put(key, getModel().getImageGraph().getPoints().get(key));
     * if (count == sum / 2) {
     * break;
     * }
     * }
     * reduced.putAll(getModel().getImageGraph().getPoints().headMap(getModel().getImageGraph().getPoints().lastKey() / 2 + 1));
     * // System.out.println("Size of reduced "+reduced.size());
     * // return reduced;
     * return getModel().getImageGraph().getPoints();
     * }
     */
    
    public Model getModel() {
        return model;
    }
    
    public void setModel(Model model) {
        this.model = model;
    }
    
    public void setRawPoints(ArrayList<InputFile> f) {
        getModel().getVisibilityGraph().emptyRawPoints();
        getModel().getVisibilityGraph().inputFiles.clear();
        getModel().getVisibilityGraph().addInputFiles(f);
        for (InputFile i : f) {
            getModel().getVisibilityGraph().addRawPoint(i.getBaseline(), i.getAverageIntensity());
            getModel().getVisibilityGraph().addRms(i.getBaseline(), i.getRms());
        }
        getModel().updateListeners();
    }
    
    public TreeMap<Double, Double> getRms() {
        return getModel().getVisibilityGraph().getGridedRms();
    }
    
    public void moveVisibilityPoint(double currentPoint, double toy) {
        getModel().getVisibilityGraph().movePoint(currentPoint, toy);
    }
    
    /*public void moveImagePoint(Double currentPoint, double toy) {
        getModel().getImageGraph().movePoint(currentPoint, toy);
    }*/
    
    public void importVisibilityGraphPoints(TreeMap<Double, Double> parseFile) {
        getModel().getVisibilityGraph().importPoints(parseFile);
    }
    
    public void importVisibilityGraphRms(TreeMap<Double, Double> parseData) {
        getModel().getVisibilityGraph().importRms(parseData);
    }
    
    public String exportVisibilityGraphPoints() {
        return getModel().getVisibilityGraph().toString();
    }
    
    public void removeRmsPoint(double x) {
        getModel().getVisibilityGraph().removeGridedRmsPoint(x);
    }
    
    public void fullReset() {
        getModel().getVisibilityGraph().emptyRawPoints();
        getModel().updateListeners();
    }
    
    public void reset() {
        getModel().getVisibilityGraph().reinicializePoints();
        getModel().updateListeners();
    }
}
