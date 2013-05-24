package fringe.Model;

import java.util.TreeMap;

public abstract class Graph {
    
    public TreeMap<Double, Double> points;
    public int exponent = 5;
    public double deltaBaseline = 1;
    public Model model;
    
    public Graph(Model m) {
        model = m;
        points = new TreeMap<Double, Double>();
    }
    
    public Graph() {
        points = new TreeMap<Double, Double>();
    }
    
    public void setDeltaBaseline(double b) {
        deltaBaseline = b;
    }
    
    public int getExponent() {
        return exponent;
    }
    
    public void setExponent(int e) {
        exponent = e;
    }
    
    public double getDeltaBaseline() {
        return deltaBaseline;
    }
    
    public TreeMap<Double, Double> getPoints() {
        return points;
    }
    
    public void setPoints(TreeMap<Double, Double> points) {
        this.points = points;
    }
}
