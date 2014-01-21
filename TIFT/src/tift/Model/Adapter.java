package tift.Model;

import common.Model.CommonTIFTAdapter;

import java.util.TreeMap;

/**
 * Adapter is a interface between View and Model. Viewers can use only methods
 * that this Adapter offers to them
 *
 * @author Karel Durktoa
 * @author Gabriel Holodak
 */

public class Adapter extends CommonTIFTAdapter {

    private Model model;

    private final double defaultDelta = 0.01;
    private final int defaultNumberOfPoints = 512;
    private double maxTime, minTime, maxFrequency, minFrequency;

    public Adapter(Model m) {
        model = m;
        resetValues();
    }

    public double getMinFrequency() {
        return minFrequency;
    }

    public void setMinFrequency(double minFrequency) {
        this.minFrequency = minFrequency;
    }

    public double getMaxFrequency() {
        return maxFrequency;
    }

    public void setMaxFrequency(double maxFrequency) {
        this.maxFrequency = maxFrequency;
    }

    public double getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(double maxTime) {
        this.maxTime = maxTime;
    }

    public double getMinTime() {
        return minTime;
    }

    public void setMinTime(double minTime) {
        this.minTime = minTime;
    }

    public void setPolar(boolean radio) {
        model.imageGraph.setPolar(radio);
        model.visibilityGraph.setPolar(radio);
    }

    public TreeMap<Double, Double> getVisibilityGraphPoints() {
        return getModel().getVisibilityGraph().getPoints();
    }

    public TreeMap<Double, Double> getVisibilityGraphPoints2() {
        return getModel().getVisibilityGraph().getPoints2();
    }

    public void evaluate(String equation, String iEquation) {
        getModel().getVisibilityGraph().evaluate(equation, iEquation);
        getModel().getVisibilityGraph().createImageGraph();
        model.updateListeners();
    }

    // GETTERS AND SETTERS
    public double getDeltaBaseline() {
        return model.getVisibilityGraph().getDeltaBaseline();
    }

    public void setDeltaBaseline(double d) {
        //TODO this should check equation too, before reinitializing points
        getModel().getVisibilityGraph().setDeltaBaseline(d);
        getModel().getImageGraph().setDeltaBaseline(d);
        model.getVisibilityGraph().reinitializePoints();
        model.getVisibilityGraph().createImageGraph();
    }

    public TreeMap<Double, Double> getImageGraphPoints() {
        return getModel().getImageGraph().getPoints();
    }

    public TreeMap<Double, Double> getImageGraphPoints2() {
        return getModel().getImageGraph().getPoints2();
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void moveVisibilityPoint(double currentPoint, double toy) {
        getModel().getVisibilityGraph().movePoint(currentPoint, toy);
    }

    public void moveVisibilityPoint2(double currentPoint, double toy) {
        getModel().getVisibilityGraph().movePoint2(currentPoint, toy);
    }

    public void moveImagePoint(Double currentPoint, double toy) {
        getModel().getImageGraph().movePoint(currentPoint, toy);
    }

    public void moveImagePoint2(Double currentPoint, double toy) {
        getModel().getImageGraph().movePoint2(currentPoint, toy);
    }

    public void reset() {
        getModel().getVisibilityGraph().reinitializePoints();
    }

    public void fullReset() {
        resetValues();
        getModel().getVisibilityGraph().fullReset();

    }

    private void resetValues() {
        setDeltaBaseline(defaultDelta);
        setNumberOfPoints(defaultNumberOfPoints);
        resetTimeRange();
        resetFrequencyRange();
    }

    public void resetTimeRange() {
        minTime = 0;
        maxTime = getDeltaBaseline() * getNumberOfPoints();
    }

    public void resetFrequencyRange() {
        maxFrequency = 0.5 / getDeltaBaseline();
        minFrequency = -maxFrequency;
    }

    public int getNumberOfPoints() {
        return getModel().getVisibilityGraph().getNumberOfPoints();
    }

    public void setNumberOfPoints(int n) {
        getModel().getVisibilityGraph().setNumberOfPoints(n);
        model.getVisibilityGraph().reinitializePoints();
        model.getVisibilityGraph().createImageGraph();
    }
}
