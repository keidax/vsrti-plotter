package fft.Model;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import fft.Viewer.InputFile;

public class PVVisibilityGraph extends VisibilityGraph {

    public PVVisibilityGraph(Model m) {
        super(m);
    }

    public void reinicializePoints() {
    	getPoints().clear();
        gridedRms.clear();
        compl = null;
        
      
        //This if statement/for loop sets all points to zero
        if(getRawPoints().size()!=0)
            for (Point p : this.rawPoints){
                this.getPoints().put(p.x/ getLambda(), p.y);
            }
        else
            for (double i = 0; i < 32; i++){
                this.getPoints().put((i * getDeltaBaseline()) / getLambda(), 0.0);
                
            }
        
        System.out.println(this.getPoints());
        if (!getRawPoints().isEmpty()) {
        	
           // double maxkey = getPoints().lastKey();
            this.halfBZero = 0;
            Point[] points = getRawPoints().toArray(new Point[getRawPoints().size()]);
           
            ArrayList<Point> subp = new ArrayList<Point>();
            for (Point p : rawPoints) {
                addPoint(p.getX(), p.getY());
            }
            
            for (int i = 0; i < points.length; i++) {
//                if(points[i].getX()>maxkey)
//                    continue;
                if (!subp.isEmpty() && getGridX(points[i].getX()) != getGridX(subp.get(0).getX())) {
                    if (subp.size() == 1) {
                        addPoint(subp.get(0).getX(), subp.get(0).getY());
                       
                    } else {
                        addPoint(subp.get(0).getX(), InputFile.getCollectiveIntensityAverage(InputFile.getInputFilesByX(getInputFiles(), subp)));
                 
                    }
                    subp.clear();
                    subp.add(points[i]);
                } else {
                    subp.add(points[i]);
                }
            }
            
        }
        update();

        //Set<Double> keys = getPoints().keySet();
    }

    
    public void addPoint(double x, double y) {
        this.getPoints().put(getGridX(x / getLambda()), y);//-this.halfBZero);
    }

    public void removePoint(double x) {
        this.getPoints().put(getGridX(x), 0.0);
    }

    public void emptyRawPoints() {
        rawPoints.clear();
        this.halfBZero = 0;
        this.reinicializePoints();
        //this.getPoints().clear();
    }

     public void addRawPoint(double x, double y) {
        this.getRawPoints().add(new Point(this, x, y));
        //System.out.println("new point e="+this.getExponent()+", baseline="+Graph.getDeltaBaseline());
        //this.recountExponent();
        //this.countDeltaBaseline();
        deltaBaseline=this.countDeltaBaseline();
        addPoint(x, y);
        
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
        //System.out.println("rms size " + keys.size());
        for (Double d : keys) {
            //System.out.println(d/getLambda() +" -> " + getGridX(d/getLambda()));
            gridedRms.put(getGridX(d / getLambda()), getRms().get(d));
        }
        //System.out.println("returning gridedrms size = "+gridedRms.size());
        return gridedRms;
    }

    

    

    public void importPoints(TreeMap<Double, Double> parseFile) {
    	this.getRawPoints().clear();
    	this.getPoints().clear();
        this.getPoints().putAll(parseFile);
        
        Set<Double> keys = this.getPoints().keySet();
        
        for(Double key : keys){
            getRawPoints().add(new Point(this,key,this.getPoints().get(key)));
        }
        System.out.println("-----"  );
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
            s += key*this.lambda + " " + getPoints().get(key) + " " + (getGridedRms().containsKey(key) ? getGridedRms().get(key) : 0.0) + "\n";
        }
        //System.out.println(s);
        return s;
    }

    private double countDeltaBaseline() {
        if(getRawPoints().size()<2)
            return 1.0;
        double min = Double.MAX_VALUE;
        Point[] points = this.getRawPoints().toArray(new Point[0]);
        for(int i=1;i<points.length;i++){
            if(min>points[i].getX()-points[i-1].getX())
                min=points[i].getX()-points[i-1].getX();
        }
        return min;
    }

}
