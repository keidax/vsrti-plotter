package beam.Model;

import java.io.File;
import java.util.List;
import java.util.Observable;
import java.util.TreeMap;

import common.View.InputFile;

public class Model extends Observable {
    
    private final double defaultLambda = 2.5, defaultDeltaBaseline = 1, defaultNoise = 0, defaultDisplayFactor = 1,
            defaultDiameter = 5, defaultPeakValue = 20, defaultHorizontalError = 0;
    private double peakValue = defaultPeakValue, diameter = defaultDiameter, horizontalError = defaultHorizontalError;
    private final int defaultExponent = 5;
    private boolean beamPatternVisible = false;
    
    private VisibilityGraph visibilityGraph;
    
    public Model() {
        visibilityGraph = new VisibilityGraph(this);
    }
    
    public TreeMap<Double, Double> getPoints() {
        return visibilityGraph.getPoints();
    }
    
    public TreeMap<Double, Double> getRms() {
        return visibilityGraph.getGridedRms();
    }
    
    public void setLambda(double l) {
        visibilityGraph.setLambda(l);
        setChanged();
    }
    
    public void setDeltaBaseline(double d) {
        visibilityGraph.setDeltaBaseline(d);
        setChanged();
    }
    
    public void setDisplayFactor(double d) {
        visibilityGraph.setDisplayFactor(d);
        setChanged();
    }
    
    public void setExponent(int e) {
        visibilityGraph.setExponent(e);
        setChanged();
    }
    
    public void setNoise(double noise) {
        visibilityGraph.setNoise(noise);
        setChanged();
    }
    
    public String getSaveFilename() {
        return visibilityGraph.getSaveFile().getAbsolutePath();
    }
    
    public double getLambda() {
        return visibilityGraph.getLambda();
    }
    
    public double getDeltaBaseline() {
        return visibilityGraph.getDeltaBaseline();
    }
    
    public double getDisplayFactor() {
        return visibilityGraph.getDisplayFactor();
    }
    
    public void setSaveFile(File f) {
        visibilityGraph.setSaveFile(f);
        setChanged();
        notifyObservers();
    }
    
    public int getExponent() {
        return visibilityGraph.getExponent();
    }
    
    public void setPeakValue(double peakValue) {
        this.peakValue = peakValue;
        setChanged();
    }
    
    public void setDiameter(double diameter) {
        this.diameter = diameter;
        setChanged();
    }
    
    public void setHorizontalError(double horizontalError) {
        this.horizontalError = horizontalError;
        setChanged();
    }
    
    public double getHorizontalError() {
        return horizontalError;
    }
    
    public void setRawPoints(List<InputFile> f) {
        visibilityGraph.emptyRawPoints();
        visibilityGraph.getInputFiles().clear();
        visibilityGraph.addInputFiles(f);
        for (InputFile i : f) {
            visibilityGraph.addRawPoint(i.getBaseline(), i.getAverageIntensity());
            visibilityGraph.addRms(i.getBaseline(), i.getRms());
        }
        findPeakValue();
        setChanged();
        notifyObservers();
    }
    
    private void findPeakValue() {
        if (getPoints().containsKey(0.0)) {
            peakValue = getPoints().get(0.0);
        } else {
            peakValue = defaultPeakValue;
        }
    }
    
    public void emptyRawPoints() {
        visibilityGraph.emptyRawPoints();
    }
    
    public void importPoints(TreeMap<Double, Double> parseFile) {
        visibilityGraph.importPoints(parseFile);
        findPeakValue();
        setChanged();
        notifyObservers();
    }
    
    public void importRms(TreeMap<Double, Double> parseData) {
        visibilityGraph.importRms(parseData);
        setChanged();
        notifyObservers();
    }
    
    public void removeRmsPoint(double x) {
        visibilityGraph.removeGridedRmsPoint(x);
        setChanged();
        notifyObservers();
    }
    
    public String exportPoints() {
        return visibilityGraph.toString();
    }
    
    public void movePoint(double currentPoint, double toy) {
        visibilityGraph.movePoint(currentPoint, toy);
        setChanged();
        notifyObservers();
    }
    
    public void reinitialize() {
        visibilityGraph.reinitializePoints();
        setChanged();
        notifyObservers();
    }
    
    public double getNoise() {
        return visibilityGraph.getNoise();
    }
    
    public void resetValuesToDefaults() {
        setExponent(defaultExponent);
        setLambda(defaultLambda);
        setDeltaBaseline(defaultDeltaBaseline);
        setNoise(defaultNoise);
        setDisplayFactor(defaultDisplayFactor);
        setDiameter(defaultDiameter);
        setHorizontalError(defaultHorizontalError);
        findPeakValue();
        setChanged();
        notifyObservers();
    }
    
    public double getPeakValue() {
        return peakValue;
    }
    
    public double getDiameter() {
        return diameter;
    }
    
    public boolean getBeamPatternVisible() {
        return beamPatternVisible;
    }
    
    public void setBeamPatternVisible(boolean isVisible) {
        beamPatternVisible = isVisible;
        setChanged();
    }
}
