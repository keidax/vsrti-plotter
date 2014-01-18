package tift.Model;

import common.Model.CommonTIFTAdapter;

import java.io.File;
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
        model.visibilityGraph.setPolar(radio);
    }

    public TreeMap<Double, Double> getVisiblityGraphPoints() {
        return getModel().getVisibilityGraph().getPoints();
    }

    public TreeMap<Double, Double> getVisiblityGraphPoints2() {
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
        model.getVisibilityGraph().reinicializePoints();
        model.getVisibilityGraph().createImageGraph();
    }

    public void setSaveFile(File f) {
        getModel().getVisibilityGraph().setSaveFile(f);
    }

    public int getExponent() {
        return model.getVisibilityGraph().getExponent();
    }

    public void setExponent(int a) {
        model.getVisibilityGraph().setExponent(a);
    }

    public TreeMap<Double, Double> getVisiblityGraphRms() {
        return getModel().getVisibilityGraph().getRms();
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

    public TreeMap<Double, Double> getRms() {
        return getModel().getVisibilityGraph().getGridedRms();
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

    public void importVisibilityGraphPoints(Double[][] parseFile) {
        getModel().getVisibilityGraph().importPoints(parseFile);
    }

    public void importVisibilityGraphRms(TreeMap<Double, Double> parseData) {
        getModel().getVisibilityGraph().importRms(parseData);
    }

    public void importImageGraphPoints(TreeMap<Double, Double> parseFile) {
        getModel().getImageGraph().getPoints().putAll(parseFile);
        getModel().getImageGraph().createVisibilityGraph();
    }

    public String exportVisibilityGraphPoints() {
        return getModel().getVisibilityGraph().toString();
    }

    public void reset() {
        getModel().getVisibilityGraph().reinicializePoints();
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
        return getModel().getVisibilityGraph().numberOfPoints;
    }

    public void setNumberOfPoints(int n) {
        getModel().getVisibilityGraph().setNumberOfPoints(n);
        model.getVisibilityGraph().reinicializePoints();
        model.getVisibilityGraph().createImageGraph();
    }

    public void removeRms(double i) {
        getModel().getVisibilityGraph().getPoints().clear();
        getModel().getVisibilityGraph().removeRms(i);
    }
}
