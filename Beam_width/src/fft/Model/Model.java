package fft.Model;

import java.io.File;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import fft.View.InputFile;

public class Model implements ModelInterface {
    
    private VisibilityGraph visibilityGraph;
    private Vector<ModelListener> listeners;
    
    public Model() {
        listeners = new Vector<ModelListener>();
        visibilityGraph = new VisibilityGraph(this);
    }
    
    @Override
    public void addListener(ModelListener listener) {
        listeners.add(listener);
    }
    
    @Override
    public void update() {
        System.out.println("Updating model");
        // visibilityGraph.reinitializePoints();
        for (ModelListener ml : listeners) {
            ml.updateView(visibilityGraph.getPoints(),
                    visibilityGraph.getGridedRms());
        }
        // TODO update other things?
    }
    
    @Override
    public TreeMap<Double, Double> getPoints() {
        return visibilityGraph.getPoints();
    }
    
    @Override
    public TreeMap<Double, Double> getRms() {
        return visibilityGraph.getGridedRms();
    }
    
    @Override
    public void setLambda(double l) {
        visibilityGraph.setLambda(l);
    }
    
    @Override
    public void setDeltaBaseline(double d) {
        visibilityGraph.setDeltaBaseline(d);
    }
    
    @Override
    public void setExponent(int e) {
        visibilityGraph.setExponent(e);
    }
    
    @Override
    public void setNoise(double noise) {
        visibilityGraph.setNoise(noise);
    }
    
    @Override
    public String getSaveFilename() {
        return visibilityGraph.getSaveFile().getAbsolutePath();
    }
    
    @Override
    public double getLambda() {
        return visibilityGraph.getLambda();
    }
    
    @Override
    public double getDeltaBaseline() {
        return visibilityGraph.getDeltaBaseline();
    }
    
    @Override
    public void setSaveFile(File f) {
        visibilityGraph.setSaveFile(f);
    }
    
    @Override
    public int getExponent() {
        return visibilityGraph.getExponent();
    }
    
    @Override
    public void setRawPoints(List<InputFile> f) {
        visibilityGraph.emptyRawPoints();
        visibilityGraph.getInputFiles().clear();
        visibilityGraph.addInputFiles(f);
        for (InputFile i : f) {
            visibilityGraph.addRawPoint(i.getBaseline(),
                    i.getAverageIntensity());
            visibilityGraph.addRms(i.getBaseline(), i.getRms());
        }
    }
    
    @Override
    public void emptyRawPoints() {
        visibilityGraph.emptyRawPoints();
    }
    
    @Override
    public void importPoints(TreeMap<Double, Double> parseFile) {
        visibilityGraph.importPoints(parseFile);
    }
    
    @Override
    public void importRms(TreeMap<Double, Double> parseData) {
        visibilityGraph.importRms(parseData);
    }
    
    @Override
    public void removeRmsPoint(double x) {
        visibilityGraph.removeGridedRmsPoint(x);
    }
    
    @Override
    public String exportPoints() {
        return visibilityGraph.toString();
    }
    
    @Override
    public void movePoint(double currentPoint, double toy) {
        visibilityGraph.movePoint(currentPoint, toy);
    }
    
    @Override
    public void reinitialize() {
        visibilityGraph.reinitializePoints();
    }
    
    @Override
    public double getNoise() {
        return visibilityGraph.getNoise();
    }
}
