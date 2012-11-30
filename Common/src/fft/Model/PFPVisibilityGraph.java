package fft.Model;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import fft.Viewer.InputFile;

public class PFPVisibilityGraph extends VisibilityGraph {

    public PFPVisibilityGraph(Model m) {
        super(m);
        reinicializePoints();
    }

    public void reinicializePoints() {
        getPoints().clear();
        gridedRms.clear();
        compl = null;
        if (!getRawPoints().isEmpty()) {
            for(Point p : this.getRawPoints()){
                if(p.getX()==0.0)
                    this.halfBZero=p.getY();
            }
            if(this.halfBZero == 0)
                this.halfBZero=1;
            for(Point p : this.getRawPoints()){
                System.out.println("Adding point "+p.getX()+","+p.getY());
                addPoint(p.getX(),p.getY());
            }
        }

        recalculateGridedRms();
        update();
        Set<Double> keys = getPoints().keySet();
    }

    public void addPoint(double x, double y) {
        this.getPoints().put(x, y);//-this.halfBZero);
    }

    public void removePoint(double x) {
        this.getPoints().put(x, 0.0);
    }

    public void emptyRawPoints() {
        rawPoints.clear();
        
        this.reinicializePoints();
        //this.getPoints().clear();
    }

    public void addRawPoint(double x, double y) {
        int eold = getExponent();
        this.getRawPoints().add(new Point(this, x, y));
        if (eold == getExponent() && x != 0.0) {
            addPoint(x, y);
        } else {
            this.reinicializePoints();
        }
    }

    public void recalculateGridedRms(){
        if (getRms() == null || getRms().isEmpty()) {
            System.out.println("RMS IS EMPTY");
            gridedRms= new TreeMap<Double, Double>();
        }
        gridedRms.clear();
        Set<Double> keys = getRms().keySet();
        for (Double d : keys) {
            gridedRms.put(d , getRms().get(d));
        }
    }

    public TreeMap<Double, Double> getGridedRms() {
        
        return gridedRms;
    }
    

    

    public void importPoints(TreeMap<Double, Double> parseFile) {
        this.getRawPoints().clear();
        Set<Double> keys = parseFile.keySet();
        for(Double key : keys){
            getRawPoints().add(new Point(this,key,parseFile.get(key)));
            System.out.println("****** "+ key + " : " + parseFile.get(key));
        }
        this.reinicializePoints();
        update();
    }

    
    
    
     @Override
    public String toString() {
        String s = "";
        s += "*lambda " + this.getLambda() + "\n";
        s += "*deltaBaseline " + getDeltaBaseline() + "\n";
        s += "*exponent " + getExponent() + "\n";
        s += "BASELINE_POWER_RMS\n";
        Set<Double> keys = this.getPoints().keySet();
        for (Double key : keys) { // X Y RMS
            s += key + " " + getPoints().get(key) + " " + getRms().get(key) + "\n";
        }
        //System.out.println(s);
        return s;
    }

    public void importRms(TreeMap<Double, Double> parseData) {
        this.getRms().clear();
        getRms().putAll(parseData);
        recalculateGridedRms();
        update();
    }
}
