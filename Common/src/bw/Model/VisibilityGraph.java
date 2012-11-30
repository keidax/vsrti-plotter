package bw.Model;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import fft.Model.Model;
import fft.Model.Point;
import fft.Viewer.InputFile;

public class VisibilityGraph extends fft.Model.VisibilityGraph {

    public VisibilityGraph(Model m) {
        super(m);
        reinicializePoints();
    }



	public void reinicializePoints() {
        getPoints().clear();
        gridedRms.clear();
        compl = null;
        if (!getRawPoints().isEmpty()) {
            for(Point p : this.getRawPoints())
                addPoint(p.getX(),p.getY());
        }
        update();
        Set<Double> keys = getPoints().keySet();
    }

    public void addPoint(double x, double y) {
        this.getPoints().put(x, y-noise);//-this.halfBZero);
    }

    public void removePoint(double x) {
        this.getPoints().put(getGridX(x), 0.0);
    }
    
    public void emptyRawPoints() {
        rawPoints.clear();
        
        this.reinicializePoints();
        //this.getPoints().clear();
    }

    public void addRawPoint(double x, double y) {
        this.getRawPoints().add(new Point(this, x, y));
        addPoint(x, y);
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

    

    public void importPoints(TreeMap<Double, Double> parseFile) {
        //this.getPoints().clear();
        //this.getPoints().putAll(parseFile);
        this.getRawPoints().clear();
        Set<Double> keys = parseFile.keySet();
        for(Double key : keys){
            getRawPoints().add(new Point(this,key,parseFile.get(key)));
        }
        this.reinicializePoints();
        update();
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
    
    void setNoise(double n) {
        noise=n;
        this.reinicializePoints();
    }
}
