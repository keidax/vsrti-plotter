package srt.Model;

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
    public double lambda = 1;
    public SortedSet<Point> rawPoints;
    public double halfBZero = 0;
    public TreeMap<Double, Double> rms = new TreeMap<Double, Double>(), gridedRms = new TreeMap<Double, Double>();
    // public ArrayList<InputFile> inputFiles;
    public TreeMap<Double, InputFile> inputFiles = new TreeMap<Double, InputFile>();
    
    public VisibilityGraph(Model m) {
        super(m);
        rawPoints = new TreeSet<Point>();
        rms = new TreeMap<Double, Double>();
        // reinitializePoints();
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
    
    public void reinitializePoints() {
        
        getPoints().clear();
        
        gridedRms.clear();
        compl = null;
        
        // This if statement/for loop sets all points to zero
        if (getRawPoints().size() != 0) {
            for (Point p : rawPoints) {
                getPoints().put(p.x, p.y);
            }
            // else
            // for (double i = 0; i < 32; i++){
            // this.getPoints().put((i * getDeltaBaseline()) / getLambda(),
            // 0.0);
            //
            // }
        }
        
        if (!getRawPoints().isEmpty()) {
            
            // double maxkey = getPoints().lastKey();
            halfBZero = 0;
            
            for (Point p : rawPoints) {
                addPoint(p.getX(), p.getY());
            }
            
        }
        update();
        
        // Set<Double> keys = getPoints().keySet();
    }
    
    public void addPoint(double x, double y) {
        
        getPoints().put(getGridX(x), y);// -this.halfBZero);
        
    }
    
    public void removePoint(double x) {
        getPoints().put(getGridX(x), 0.0);
    }
    
    public void recountExponent() {
        if (rawPoints == null || rawPoints.size() < 2) {
            setExponent(5);
        }
        setExponent((int) Math.ceil(Math.log(rawPoints.size()) / Math.log(2)));
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
    
    public void save() {
        // not finished
    }
    
    public void emptyRawPoints() {
        rawPoints.clear();
        halfBZero = 0;
        reinitializePoints();
        // this.getPoints().clear();
    }
    
    public void addRawPoint(double x, double y) {
        getRawPoints().add(new Point(x, y));
        // System.out.println("new point e="+this.getExponent()+", baseline="+Graph.getDeltaBaseline());
        // this.recountExponent();
        // this.countDeltaBaseline();
        deltaBaseline = countDeltaBaseline();
        addPoint(x, y);
        
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
    
    public void setLambda(double lamb) {
        lambda = lamb;
        // System.out.println(this.getRawPoints().size());
        // this.reinitializePoints();
    }
    
    public SortedSet<Point> getRawPoints() {
        return rawPoints;
    }
    
    public void setRawPoints(SortedSet<Point> rawPoints) {
        this.rawPoints = rawPoints;
    }
    
    public TreeMap<Double, Double> getGridedRms() {
        if (gridedRms != null && !gridedRms.isEmpty()) {
            return gridedRms;
        }
        if (getRms() == null || getRms().isEmpty()) {
            return new TreeMap<Double, Double>();
        }
        gridedRms.clear();// = new TreeMap<Double,Double>();
        Set<Double> keys = getRms().keySet();
        // System.out.println("rms size " + keys.size());
        for (Double d : keys) {
            // System.out.println(d/getLambda() +" -> " +
            // getGridX(d/getLambda()));
            gridedRms.put(getGridX(d), getRms().get(d));
        }
        // System.out.println("returning gridedrms size = "+gridedRms.size());
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
        getPoints().clear();
        getPoints().putAll(parseFile);
        
        Set<Double> keys = getPoints().keySet();
        
        for (Double key : keys) {
            
            addRawPoint(key, getPoints().get(key));
        }
        
        // System.out.println("ImportedPoint and call updating");
        reinitializePoints();
        // update();
        // System.out.println("points imported "+getPoints().size());
    }
    
    @Override
    public String toString() {
        String s = "";
        s += "*lambda " + getLambda() + "\n";
        s += "*deltaBaseline " + getDeltaBaseline() + "\n";
        s += "X_Y_RMS" + "\n";
        Set<Double> keys = getPoints().keySet();
        for (Double key : keys) { // X Y RMS
            s += key + " " + getPoints().get(key) + " " + "\n";
        }
        // System.out.println(s);
        return s;
    }
    
    public TreeMap<Double, Double> getDataPoints() {
        TreeMap<Double, Double> al = new TreeMap<Double, Double>();
        for (Point p : rawPoints) {
            // System.out.println("gridX("+p.getX()+"/"+getLambda()+")="+getGridX(p.getX()/getLambda()));
            al.put(getGridX(p.getX()), p.getY());
        }
        // System.out.println("DataPoints = "+al.size());
        return al;
    }
    
    public TreeMap<Double, Double> getRms() {
        return rms;
    }
    
    public void importRms(TreeMap<Double, Double> parseData) {
        getRms().clear();
        // gridedRms.clear();
        getRms().putAll(parseData);
        gridedRms.putAll(parseData);
        update();
    }
    
    public void removeGridedRmsPoint(double x) {
        getGridedRms().remove(x);
    }
    
    private double countDeltaBaseline() {
        if (getRawPoints().size() < 2) {
            return 1.0;
        }
        double min = Double.MAX_VALUE;
        Point[] points = getRawPoints().toArray(new Point[0]);
        for (int i = 1; i < points.length; i++) {
            if (min > points[i].getX() - points[i - 1].getX()) {
                min = points[i].getX() - points[i - 1].getX();
            }
        }
        return min;
    }
    
}
