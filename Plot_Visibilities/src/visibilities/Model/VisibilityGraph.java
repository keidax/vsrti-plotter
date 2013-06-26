package visibilities.Model;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import common.Model.Complex;
import common.Model.Graph;
import common.Model.Point;
import common.View.InputFile;

public class VisibilityGraph extends Graph {
    
    public Complex[] compl;
    public File saveFile;
    
    public static final double defaultLambda = 2.5;
    public double lambda = defaultLambda;
    public SortedSet<Point> rawPoints;
    public double halfBZero = 0;
    private TreeMap<Double, Double> rms = new TreeMap<Double, Double>(), gridedRms = new TreeMap<Double, Double>();
    public TreeMap<Double, InputFile> inputFiles = new TreeMap<Double, InputFile>();
    
    public VisibilityGraph(Model m) {
        super();
        rawPoints = new TreeSet<Point>();
        rms = new TreeMap<Double, Double>();
        // reinicializePoints();
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
    
    @Override
    public void reinitializePoints() {
        getPoints().clear();
        gridedRms.clear();
        compl = null;
        
        // This if statement/for loop sets all points to zero
        if (getRawPoints().size() != 0) {
            for (Point p : rawPoints) {
                getPoints().put(p.x / getLambda(), p.y);
            }
        }
        
        // System.out.println(getPoints());
        if (!getRawPoints().isEmpty()) {
            
            // double maxkey = getPoints().lastKey();
            halfBZero = 0;
            Point[] points = getRawPoints().toArray(new Point[getRawPoints().size()]);
            
            ArrayList<Point> subp = new ArrayList<Point>();
            for (Point p : rawPoints) {
                addPoint(p.getX(), p.getY());
            }
            
            for (int i = 0; i < points.length; i++) {
                // if(points[i].getX()>maxkey)
                // continue;
                if (!subp.isEmpty() && getGridX(points[i].getX()) != getGridX(subp.get(0).getX())) {
                    if (subp.size() == 1) {
                        addPoint(subp.get(0).getX(), subp.get(0).getY());
                        
                    } else {
                        addPoint(subp.get(0).getX(), InputFile.getCollectiveIntensityAverage(InputFile
                                .getInputFilesByX(getInputFiles(), subp)));
                        
                    }
                    subp.clear();
                    subp.add(points[i]);
                } else {
                    subp.add(points[i]);
                }
            }
            
        }
        // update();
        
        // Set<Double> keys = getPoints().keySet();
    }
    
    public void addPoint(double x, double y) {
        getPoints().put(x / getLambda(), y);// -this.halfBZero);
    }
    
    public void removePoint(double x) {
        getPoints().put(getGridX(x), 0.0);
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
            setExponent(e);
            reinitializePoints();
        }
        
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
        setDeltaBaseline(countDeltaBaseline());
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
    
    public void setLambda(double lambda) {
        this.lambda = lambda;
        reinitializePoints();
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
            gridedRms.put(d / getLambda(), getRms().get(d));
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
    
    /*
     * public void update() {
     * model.updateListeners();
     * }
     */
    
    public void movePoint(double currentPoint, double toy) {
        getPoints().put(currentPoint, toy);
        // update();
    }
    
    public void importPoints(TreeMap<Double, Double> parseFile) {
        getRawPoints().clear();
        getPoints().clear();
        getPoints().putAll(parseFile);
        
        Set<Double> keys = getPoints().keySet();
        
        for (Double key : keys) {
            getRawPoints().add(new Point(key, getPoints().get(key)));
        }
        System.out.println("-----");
        // update();
    }
    
    @Override
    public String toString() {
        String s = "";
        s += "*lambda " + getLambda() + "\n";
        s += "*deltaBaseline " + getDeltaBaseline() + "\n";
        s += "X_Y_RMS" + "\n";
        Set<Double> keys = getPoints().keySet();
        for (Double key : keys) { // X Y RMS
            s +=
                    key * lambda + " " + getPoints().get(key) + " "
                            + (getGridedRms().containsKey(key) ? getGridedRms().get(key) : 0.0) + "\n";
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
        // System.out.println("returning " + al.size() + " datapoints from visGraph");
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
        // update();
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
