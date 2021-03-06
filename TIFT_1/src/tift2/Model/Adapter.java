package tift2.Model;

import common.Model.CommonTIFTAdapter;

import java.io.File;
import java.util.Set;
import java.util.TreeMap;

/**
 * Adapter is a interface between View and Model. Viewers can use only methods
 * that this Adapter offers to them
 *
 * @author Karel Durktoa
 */

public class Adapter extends CommonTIFTAdapter {

    private Model model;
    private final double defaultMaxTime = 1.5, defaultMaxFrequency = 100;
    private double maxTime = defaultMaxTime, maxFrequency = defaultMaxFrequency;

    public double getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(double maxTime) {
        this.maxTime = maxTime;
    }

    public double getMaxFrequency() {
        return maxFrequency;
    }

    public void setMaxFrequency(double maxFrequency) {
        this.maxFrequency = maxFrequency;
    }

    public Adapter(Model m) {
        model = m;
    }

//    public void graphEquation(String eqn) {
//        int num = getNumberOfPoints();
//    }

    public TreeMap<Double, Double> getVisiblityGraphPoints() {
        return getModel().getVisibilityGraph().getPoints();
    }

    public void evaluate(String equation) {
        getModel().getVisibilityGraph().evaluate(equation);
        getModel().getVisibilityGraph().createImageGraph();
        model.updateListeners();
    }

    // GETTERS AND SETTERS
    public double getLambda() {
        return getModel().getVisibilityGraph().getLambda();
    }

    public double getDeltaBaseline() {
        return model.getVisibilityGraph().getDeltaBaseline();
    }

    public void setDeltaBaseline(double d) {
        getModel().getVisibilityGraph().setDeltaBaseline(d);
        getModel().getImageGraph().setDeltaBaseline(d);
    }

    public void setSaveFile(File f) {
        getModel().getVisibilityGraph().setSaveFile(f);
    }

    public void setLambda(double l) {
        getModel().getVisibilityGraph().setLambda(l);
    }

    public int getExponent() {
        return model.getVisibilityGraph().getExponent();
    }

    public void setExponent(int a) {
        model.getVisibilityGraph().setExponent(a);
    }

    public TreeMap<Double, Double> getVisibilityGraphDataPoints() {
        return getModel().getVisibilityGraph().getDataPoints();
    }

    public TreeMap<Double, Double> getVisiblityGraphRms() {
        return getModel().getVisibilityGraph().getRms();
    }

    public TreeMap<Double, Double> getImageGraphPoints() {
        TreeMap<Double, Double> reduced = new TreeMap<Double, Double>();
        Set<Double> keys = getModel().getImageGraph().getPoints().keySet();
        int sum = keys.size();
        int count = 0;
        for (Double key : keys) {
            count++;
            reduced.put(key, getModel().getImageGraph().getPoints().get(key));
            if (count == sum / 2) {
                break;
            }
        }
        reduced.putAll(getModel().getImageGraph().getPoints().headMap(
                getModel().getImageGraph().getPoints().lastKey() / 2 + 1));
        // System.out.println("Size of reduced "+reduced.size());
        // return reduced;
        return getModel().getImageGraph().getPoints();
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

    public void moveImagePoint(Double currentPoint, double toy) {
        getModel().getImageGraph().movePoint(currentPoint, toy);
    }

    public void importVisibilityGraphPoints(TreeMap<Double, Double> parseFile) {
        getModel().getVisibilityGraph().importPoints(parseFile);
    }

    public void importVisibilityGraphRms(TreeMap<Double, Double> parseData) {
        getModel().getVisibilityGraph().importRms(parseData);
    }

    public void importImageGraphPoints(TreeMap<Double, Double> parseFile) {
        getModel().getImageGraph().points.putAll(parseFile);
        getModel().getImageGraph().createVisibilityGraph();
    }

    public String exportVisibilityGraphPoints() {
        return getModel().getVisibilityGraph().toString();
    }

    public void removeRmsPoint(double x) {
        getModel().getVisibilityGraph().removeGridedRmsPoint(x);
    }

    public void reset() {
        getModel().getVisibilityGraph().reinicializePoints();
    }

    public void fullReset() {

        getModel().getVisibilityGraph().fullReset();

    }

    public int getNumberOfPoints() {
        return getModel().getVisibilityGraph().numberOfPoints;
    }

    public void setNumberOfPoints(int n) {
        getModel().getVisibilityGraph().setNumberOfPoints(n);
    }

    public void removeRms(double i) {
        getModel().getVisibilityGraph().getRawPoints().clear();
        getModel().getVisibilityGraph().removeRms(i);
    }


}
