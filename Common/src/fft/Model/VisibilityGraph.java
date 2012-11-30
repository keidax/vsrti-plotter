package fft.Model;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import fft.Viewer.InputFile;

public class VisibilityGraph extends Graph {
	public Complex[] compl;
    public File saveFile;
    public double lambda = 2.5;
    public SortedSet<Point> rawPoints;
    public double halfBZero = 0;
    protected TreeMap<Double, Double> rms = new TreeMap<Double, Double>(), gridedRms = new TreeMap<Double, Double>();
    public TreeMap<Double, InputFile> inputFiles = new TreeMap<Double, InputFile>();
    
    public VisibilityGraph(Model m){
    	super(m);
        rawPoints = new TreeSet<Point>();
        rms = new TreeMap<Double, Double>();
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
    
    void reinicializePoints(){
    	
    }
    
    public void setRms(TreeMap<Double, Double> rms) {
        this.rms = rms;
    }

    public void setGridedRms(TreeMap<Double, Double> gridedRms) {
        this.gridedRms = gridedRms;
    }
    
    public TreeMap<Double, Double> getGridedRms(){
    	return gridedRms;
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
    }
    
    public void setExponent(int e) {
        if (e >= (int) Math.ceil(Math.log(getMaxRawX() / getDeltaBaseline()) / Math.log(2))) {
            exponent = e;
            this.reinicializePoints();
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
        this.reinicializePoints();
    }

    public SortedSet<Point> getRawPoints() {
        return rawPoints;
    }

    public void setRawPoints(SortedSet<Point> rawPoints) {
        this.rawPoints = rawPoints;
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
        this.getPoints().put(currentPoint, toy);
        update();
    }
    

    public TreeMap<Double, Double> getRms() {
        return rms;
    }
    
    public void removeGridedRmsPoint(double x) {
        getGridedRms().remove(x);
    }
    
    

    public void importRms(TreeMap<Double, Double> parseData) {
        this.getRms().clear();
        getRms().putAll(parseData);
        gridedRms.putAll(parseData);
        update();
    }
    
    public void importPoints(TreeMap<Double, Double> parseFile) {
    	
    }
    
    public void emptyRawPoints() {
    }

    public void addRawPoint(double x, double y) {
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

}
