package fft.Model;

import java.util.TreeMap;

public abstract class Graph {
    
    private TreeMap<Double, Double> points;
    private int exponent = 5;
    private double deltaBaseline = 1;
    private Model model;
    private double noise;
    
    public Graph(Model m) {
        model = m;
        points = new TreeMap<Double, Double>();
    }
    
    public double getNoise() {
        return noise;
    }
    
    public void setNoise(double noise) {
        this.noise = noise;
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
    
    public void setDeltaBaseline(double b) {
        deltaBaseline = b;
    }
    
    public TreeMap<Double, Double> getPoints() {
        return points;
    }
    
    public void setPoints(TreeMap<Double, Double> points) {
        this.points = points;
    }
}
