package beam.Model;

import java.io.File;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import common.Model.ModelInterface;
import common.Model.ModelListener;
import common.View.InputFile;

public class Model implements ModelInterface {
    
    private final double defaultLambda = 2.5, defaultDeltaBaseline = 1, defaultNoise = 0, defaultDisplayFactor = 1,
            defaultDiameter = 5, defaultPeakValue = 20, defaultHorizontalError = 0;
    private double peakValue = defaultPeakValue, diameter = defaultDiameter, horizontalError = defaultHorizontalError;
    private final int defaultExponent = 5;
    
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
        System.out.println("called model.update()");
        for (ModelListener ml : listeners) {
            ml.updateView(visibilityGraph.getPoints(), visibilityGraph.getGridedRms());
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
    
    public void setDisplayFactor(double d) {
        visibilityGraph.setDisplayFactor(d);
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
    
    public double getDisplayFactor() {
        return visibilityGraph.getDisplayFactor();
    }
    
    @Override
    public void setSaveFile(File f) {
        visibilityGraph.setSaveFile(f);
    }
    
    @Override
    public int getExponent() {
        return visibilityGraph.getExponent();
    }
    
    public void setPeakValue(double peakValue) {
        this.peakValue = peakValue;
    }
    
    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }
    
    public void setHorizontalError(double horizontalError) {
        this.horizontalError = horizontalError;
    }
    
    public double getHorizontalError() {
        return horizontalError;
    }
    
    @Override
    public void setRawPoints(List<InputFile> f) {
        visibilityGraph.emptyRawPoints();
        visibilityGraph.getInputFiles().clear();
        visibilityGraph.addInputFiles(f);
        for (InputFile i : f) {
            visibilityGraph.addRawPoint(i.getBaseline(), i.getAverageIntensity());
            visibilityGraph.addRms(i.getBaseline(), i.getRms());
        }
        findPeakValue();
    }
    
    private void findPeakValue() {
        if (getPoints().containsKey(0.0)) {
            peakValue = getPoints().get(0.0);
        } else {
            peakValue = defaultPeakValue;
        }
    }
    
    @Override
    public void emptyRawPoints() {
        visibilityGraph.emptyRawPoints();
    }
    
    @Override
    public void importPoints(TreeMap<Double, Double> parseFile) {
        visibilityGraph.importPoints(parseFile);
        findPeakValue();
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
    
    public void resetValuesToDefaults() {
        setExponent(defaultExponent);
        setLambda(defaultLambda);
        setDeltaBaseline(defaultDeltaBaseline);
        setNoise(defaultNoise);
        setDisplayFactor(defaultDisplayFactor);
        setDiameter(defaultDiameter);
        setHorizontalError(defaultHorizontalError);
        findPeakValue();
    }
    
    public double getPeakValue() {
        return peakValue;
    }
    
    public double getDiameter() {
        return diameter;
    }
}
