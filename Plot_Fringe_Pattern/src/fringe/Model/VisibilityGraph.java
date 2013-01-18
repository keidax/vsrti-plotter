package fringe.Model;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import common.Model.Complex;
import common.Model.Point;
import common.View.InputFile;

public class VisibilityGraph extends Graph {
    
    public Complex[] compl;
    public File saveFile;
    public double lambda = 1; // TODO originally 2.5; why?
    public SortedSet<Point> rawPoints;
    public double halfBZero = 0;
    private TreeMap<Double, Double> rms = new TreeMap<Double, Double>(), gridedRms = new TreeMap<Double, Double>();
    public TreeMap<Double, InputFile> inputFiles = new TreeMap<Double, InputFile>();
    
    public VisibilityGraph(Model m) {
        super(m);
        rawPoints = new TreeSet<Point>();
        rms = new TreeMap<Double, Double>();
        reinicializePoints();
    }
    
    public TreeMap<Double, InputFile> getInputFiles() {
        return inputFiles;
    }
    
    public void setInputFiles(TreeMap<Double, InputFile> inputFiles) {
        this.inputFiles = inputFiles;
    }
    
    public void addInputFiles(ArrayList<InputFile> files) {
        getInputFiles().clear();
        for (InputFile i : files) {
            getInputFiles().put(i.getBaseline(), i);
        }
    }
    
    public void setRms(TreeMap<Double, Double> rms) {
        this.rms = rms;
    }
    
    public void setGridedRms(TreeMap<Double, Double> gridedRms) {
        this.gridedRms = gridedRms;
    }
    
    public void reinicializePoints() {
        getPoints().clear();
        gridedRms.clear();
        compl = null;
        if (!getRawPoints().isEmpty()) {
            for (Point p : getRawPoints()) {
                if (p.getX() == 0.0) {
                    halfBZero = p.getY();
                }
            }
            if (halfBZero == 0) {
                halfBZero = 1;
            }
            for (Point p : getRawPoints()) {
                System.out.println("Adding point " + p.getX() + "," + p.getY());
                addPoint(p.getX(), p.getY());
            }
        }
        
        recalculateGridedRms();
        update();
        Set<Double> keys = getPoints().keySet();
    }
    
    public void addPoint(double x, double y) {
        getPoints().put(x, y);// -this.halfBZero);
    }
    
    public void removePoint(double x) {
        getPoints().put(x, 0.0);
    }
    
    public double getMaxRawX() {
        if (getRawPoints().size() != 0) {
            return getRawPoints().last().getX();
        } else {
            return 0.0;
        }
    }
    
    public double getMaxRawY() {
        if (getRawPoints() == null || getRawPoints().size() == 0) {
            return 0.0;
        }
        Point max = getRawPoints().first();
        for (Point p : getRawPoints()) {
            if (p.getY() > max.getY()) {
                max = p;
            }
        }
        return max.getY();
    }
    
    public double getGridX(double x) {
        if (getPoints().floorKey(x) != null) {
            return getPoints().floorKey(x);
        } else if (getPoints().ceilingKey(x) != null) {
            return getPoints().ceilingKey(x);
        } else {
            return x;
            // return Math.floor(new Double(x)/Graph.getDeltaBaseline());
        }
    }
    
    @Override
    public void setExponent(int e) {
        if (e >= (int) Math.ceil(Math.log(getMaxRawX() / getDeltaBaseline()) / Math.log(2))) {
            exponent = e;
            reinicializePoints();
        }
        
    }
    
    public void save() {
        // not finished
    }
    
    public void emptyRawPoints() {
        rawPoints.clear();
        
        reinicializePoints();
        // this.getPoints().clear();
    }
    
    public void addRawPoint(double x, double y) {
        int eold = getExponent();
        getRawPoints().add(new Point(x, y));
        if (eold == getExponent() && x != 0.0) {
            addPoint(x, y);
        } else {
            reinicializePoints();
        }
    }
    
    public File getSaveFile() {
        return saveFile;
    }
    
    public void setSaveFile(File saveFile) {
        this.saveFile = saveFile;
    }
    
    public double getLambda() {
        return lambda;
    }
    
    public void setLambda(double lambda) {
        this.lambda = lambda;
        reinicializePoints();
    }
    
    public SortedSet<Point> getRawPoints() {
        return rawPoints;
    }
    
    public void setRawPoints(SortedSet<Point> rawPoints) {
        this.rawPoints = rawPoints;
    }
    
    public void recalculateGridedRms() {
        if (getRms() == null || getRms().isEmpty()) {
            System.out.println("RMS IS EMPTY");
            gridedRms = new TreeMap<Double, Double>();
        }
        gridedRms.clear();
        Set<Double> keys = getRms().keySet();
        for (Double d : keys) {
            gridedRms.put(d / getLambda(), getRms().get(d));
        }
    }
    
    public TreeMap<Double, Double> getGridedRms() {
        
        return gridedRms;
    }
    
    public void addRms(double x, double rms) {
        getRms().put(x, rms);
        gridedRms.clear();
    }
    
    public void moveRawPoint(Point p, double x, double y) {
        p.setX(x);
        p.setY(y);
    }
    
    public void update() {
        model.updateListeners();
    }
    
    public void movePoint(double currentPoint, double toy) {
        getPoints().put(currentPoint, toy);
        update();
    }
    
    public void importPoints(TreeMap<Double, Double> parseFile) {
        getRawPoints().clear();
        Set<Double> keys = parseFile.keySet();
        for (Double key : keys) {
            getRawPoints().add(new Point(key, parseFile.get(key)));
            System.out.println("****** " + key + " : " + parseFile.get(key));
        }
        reinicializePoints();
        update();
    }
    
    @Override
    public String toString() {
        String s = "";
        s += "*lambda " + getLambda() + "\n";
        s += "*deltaBaseline " + getDeltaBaseline() + "\n";
        s += "*exponent " + getExponent() + "\n";
        s += "BASELINE_POWER_RMS\n";
        Set<Double> keys = getPoints().keySet();
        for (Double key : keys) { // X Y RMS
            s += key + " " + getPoints().get(key) + " " + getRms().get(key) + "\n";
        }
        // System.out.println(s);
        return s;
    }
    
    public TreeMap<Double, Double> getDataPoints() {
        TreeMap<Double, Double> al = new TreeMap<Double, Double>();
        for (Point p : rawPoints) {
            // System.out.println("gridX("+p.getX()+"/"+getLambda()+")="+getGridX(p.getX()/getLambda()));
            al.put(p.getX() / getLambda(), p.getY());
        }
        // System.out.println("DataPoints = "+al.size());
        return al;
    }
    
    public TreeMap<Double, Double> getRms() {
        return rms;
    }
    
    public void importRms(TreeMap<Double, Double> parseData) {
        getRms().clear();
        getRms().putAll(parseData);
        recalculateGridedRms();
        update();
    }
    
    public void removeGridedRmsPoint(double x) {
        getGridedRms().remove(x);
    }
}
