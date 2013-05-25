package fringe.Model;

import java.io.File;
import java.util.Vector;

public class Model {
    
    private VisibilityGraph visibilityGraph;
    // private ImageGraph imageGraph;
    private Vector<ModelListener> listeners;
    private double defaultDisplayFactor = 1, defaultLambda = 2.5, defaultBaseline = 10, defaultDiameter = 5, defaultT1 = 10,
            defaultT2 = 10;
    private int defaultExponent = 5;
    private double displayFactor = defaultDisplayFactor, diameter = defaultDiameter, t1 = defaultT1, t2 = defaultT2;
    
    public Model() {
        listeners = new Vector<ModelListener>();
        // imageGraph = new ImageGraph(this);
        visibilityGraph = new VisibilityGraph(this);
    }
    
    public VisibilityGraph getVisibilityGraph() {
        return visibilityGraph;
    }
    
    public void setVisibilityGraph(VisibilityGraph visibilityGraph) {
        this.visibilityGraph = visibilityGraph;
    }
    
    /*public ImageGraph getImageGraph() {
        return imageGraph;
    }*/
    
    /*public void setImageGraph(ImageGraph imageGraph) {
        this.imageGraph = imageGraph;
    }*/
    
    public void updateListeners() {
        // System.out.println("Model is updating all listeners");
        for (ModelListener ml : listeners) {
            ml.update();
        }
    }
    
    public Vector<ModelListener> getListeners() {
        return listeners;
    }
    
    public void setListeners(Vector<ModelListener> listeners) {
        this.listeners = listeners;
    }
    
    public int getExponent() {
        return getVisibilityGraph().getExponent();
    }
    
    public void setExponent(int a) {
        getVisibilityGraph().setExponent(a);
    }
    
    public String getSaveFilename() {
        return getVisibilityGraph().getSaveFile().getAbsolutePath();
    }
    
    public double getLambda() {
        return getVisibilityGraph().getLambda();
    }
    
    public double getDeltaBaseline() {
        return getVisibilityGraph().getDeltaBaseline();
    }
    
    public void setDeltaBaseline(double d) {
        getVisibilityGraph().setDeltaBaseline(d);
    }
    
    public void setSaveFile(File f) {
        getVisibilityGraph().setSaveFile(f);
    }
    
    public void setLambda(double l) {
        getVisibilityGraph().setLambda(l);
    }
    
    public double getDiameter() {
        return diameter;
    }
    
    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }
    
    public double getDisplayFactor() {
        return displayFactor;
    }
    
    public void setDisplayFactor(double i) {
        displayFactor = i;
    }
    
    public double getT1() {
        return t1;
    }
    
    public void setT1(double t) {
        t1 = t;
    }
    
    public double getT2() {
        return t2;
    }
    
    public void setT2(double t) {
        t2 = t;
    }
    
    public void resetValuesToDefaults() {
        setDisplayFactor(defaultDisplayFactor);
        setExponent(defaultExponent);
        setDeltaBaseline(defaultBaseline);
        setLambda(defaultLambda);
        setDiameter(defaultDiameter);
        setT1(defaultT1);
        setT2(defaultT2);
    }
}
