package fft.Model;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import fft.View.InputFile;

public class VisibilityGraph extends Graph {

    private File saveFile;
    private double lambda = 2.5;
    private SortedSet<Point> rawPoints;
    private TreeMap<Double, Double> rms = new TreeMap<Double, Double>(), gridedRms = new TreeMap<Double, Double>();
    private TreeMap<Double, InputFile> inputFiles = new TreeMap<Double, InputFile>();

    public VisibilityGraph(Model m) {
        super(m);
        rawPoints = new TreeSet<Point>();
        rms = new TreeMap<Double, Double>();
        reinitializePoints();
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
        if (!getRawPoints().isEmpty()) {
            for(Point p : this.getRawPoints())
                addPoint(p.getX(),p.getY());
        }
        Set<Double> keys = getPoints().keySet();
        System.out.println("Reinitializing points...");
    }

    public void addPoint(double x, double y) {
        this.getPoints().put(x, y-getNoise());//-this.halfBZero);
    }

    public void removePoint(double x) {
        this.getPoints().put(getGridX(x), 0.0);
    }

    public double getMaxRawX() {
        if (this.getRawPoints().size() != 0) {
            return this.getRawPoints().last().getX();
        } else {
            return 0.0;
        }
    }

    public double getMaxRawY() {
        if (this.getRawPoints() == null || this.getRawPoints().size() == 0) {
            return 0.0;
        }
        Point max = this.getRawPoints().first();
        for (Point p : this.getRawPoints()) {
            if (p.getY() > max.getY()) {
                max = p;
            }
        }
        return max.getY();
    }

    public double getGridX(double x) {
        if (this.getPoints().floorKey(x) != null) {
            return this.getPoints().floorKey(x);
        } else if (getPoints().ceilingKey(x) != null){
            return this.getPoints().ceilingKey(x);
        }
        else
            return x;
        //return Math.floor(new Double(x)/Graph.getDeltaBaseline());
    }
    
    @Override
    public void setExponent(int e) {
        if (e >= (int) Math.ceil(Math.log(getMaxRawX() / getDeltaBaseline()) / Math.log(2))) {
            super.setExponent(e);
            this.reinitializePoints();
        }

    }

    public void emptyRawPoints() {
        rawPoints.clear();
        
        this.reinitializePoints();
        //this.getPoints().clear();
    }

    public void addRawPoint(double x, double y) {
        this.getRawPoints().add(new Point(x, y));
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
        this.reinitializePoints();
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
        //gridedRms.clear();// = new TreeMap<Double,Double>();
        Set<Double> keys = getRms().keySet();
        //System.out.println("rms size " + keys.size());
        for (Double d : keys) {
            System.out.println(d +" -> " + d/getLambda());
            gridedRms.put(d , getRms().get(d));
        }
        //System.out.println("returning gridedrms size = "+gridedRms.size());
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

    public void movePoint(double currentPoint, double toy) {
        this.getPoints().put(currentPoint, toy);
    }

    public void importPoints(TreeMap<Double, Double> parseFile) {
        //this.getPoints().clear();
        //this.getPoints().putAll(parseFile);
        this.getRawPoints().clear();
        Set<Double> keys = parseFile.keySet();
        for(Double key : keys){
            getRawPoints().add(new Point(key,parseFile.get(key)));
        }
        this.reinitializePoints();
    }

    
    
     @Override
    public String toString() {
        String s = "";
        s += "*lambda " + this.getLambda() + "\n";
        s += "*deltaBaseline " + getDeltaBaseline() + "\n";
        s += "X_Y_RMS" + "\n";
        Set<Double> keys = this.getPoints().keySet();
        for (Double key : keys) { // X Y RMS
            s += key + " " + getPoints().get(key) + " " + (getGridedRms().containsKey(key) ? getGridedRms().get(key) : 0.0) + "\n";
        }
        System.out.println(s);
        return s;
    }
    public TreeMap<Double, Double> getDataPoints() {
        TreeMap<Double, Double> al = new TreeMap<Double, Double>();
        for (Point p : rawPoints) {
            //System.out.println("gridX("+p.getX()+"/"+getLambda()+")="+getGridX(p.getX()/getLambda()));
            al.put(getGridX(p.getX() / getLambda()), p.getY());
        }
        //System.out.println("DataPoints = "+al.size());
        return al;
    }

    public TreeMap<Double, Double> getRms() {
        return rms;
    }

    public void importRms(TreeMap<Double, Double> parseData) {
        System.out.println("Importing rms of size "+parseData.size());
        this.getRms().clear();
        //gridedRms.clear();
        getRms().putAll(parseData);
        gridedRms.putAll(parseData);
    }

    public void removeGridedRmsPoint(double x) {
        getGridedRms().remove(x);
    }
}
