package tift.Model;

import java.util.TreeMap;

public abstract class Graph {
    
    public TreeMap<Double, Double> points, points2;
    public double deltaBaseline = .01;
    public int exponent = 5;
    public Model model;
    
    public Graph(Model m) {
        model = m;
        points = new TreeMap<Double, Double>();
        points2 = new TreeMap<Double, Double>();
    }
    
    public void setDeltaBaseline(double b) {
        deltaBaseline = b;
        model.visibilityGraph.reinicializePoints();
    }
    
    public int getExponent() {
        return exponent;
    }
    
    public void setExponent(int e) {
        exponent = e;
        model.visibilityGraph.reinicializePoints();
    }
    
    public double getDeltaBaseline() {
        return deltaBaseline;
    }
    
    public Graph() {
        points = new TreeMap<Double, Double>();
    }
    
    public TreeMap<Double, Double> getPoints() {
        return points;
    }
    
    public TreeMap<Double, Double> getPoints2() {
        return points2;
    }
    
    public void setPoints(TreeMap<Double, Double> points) {
        // this.points = points;
    }
    
    public void setPoints2(TreeMap<Double, Double> points2) {
        this.points2 = points2;
    }
}
