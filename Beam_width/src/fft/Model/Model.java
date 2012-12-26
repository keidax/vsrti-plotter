package fft.Model;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Vector;

import fft.View.InputFile;

public class Model {

    private VisibilityGraph visibilityGraph;
    private Vector<ModelListener> listeners;

    public Model() {
        listeners = new Vector<ModelListener>();
        visibilityGraph = new VisibilityGraph(this);
    }

    private void updateListeners() {
        for (ModelListener ml : listeners) {
            ml.updateView(visibilityGraph.getPoints(), visibilityGraph.getGridedRms());
        }
    }
    
    public void addListener(ModelListener listener){
    	listeners.add(listener);
    }
    
    public void update(){
    	System.out.println("Updating model");
//    	visibilityGraph.reinitializePoints();
    	this.updateListeners();
    	//TODO update everything
    }
    
    public TreeMap<Double, Double> getPoints() {
        return visibilityGraph.getPoints();
    }
    
    public TreeMap<Double, Double> getRms() {
        return visibilityGraph.getGridedRms();
    }
    
    public void setLambda( double l){
    	visibilityGraph.setLambda(l);
    }
    
    public void setDeltaBaseline(double d) {
        visibilityGraph.setDeltaBaseline(d);
    }
    
    public void setExponent(int e){
    	visibilityGraph.setExponent(e);
    }
    
    public void setNoise(double noise) {
		visibilityGraph.setNoise(noise);
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

    

    public void setSaveFile(File f) {
        visibilityGraph.setSaveFile(f);
    }
    
    public int getExponent() {
        return visibilityGraph.getExponent();
    }
    
    public void setRawPoints(ArrayList<InputFile> f) {
        visibilityGraph.emptyRawPoints();
        visibilityGraph.getInputFiles().clear();
        visibilityGraph.addInputFiles(f);
        for (InputFile i : f) {
            this.visibilityGraph.addRawPoint(i.getBaseline(), i.getAverageIntensity());
            visibilityGraph.addRms(i.getBaseline(), i.getRms());
        }
    }
    
    public void emptyRawPoints(){
    	visibilityGraph.emptyRawPoints();
    }
    
    public void importPoints(TreeMap<Double, Double> parseFile) {
        visibilityGraph.importPoints(parseFile);
    }

    public void importRms(TreeMap<Double, Double> parseData) {
        visibilityGraph.importRms(parseData);
    }
    
    public void removeRmsPoint(double x) {
        visibilityGraph.removeGridedRmsPoint(x);
    }
    
    public String exportPoints() {
        return visibilityGraph.toString();
    }
    
    public void movePoint(double currentPoint, double toy) {
        visibilityGraph.movePoint(currentPoint, toy);
    }
    
    public void reinitialize(){
    	visibilityGraph.reinitializePoints();
    }
}
