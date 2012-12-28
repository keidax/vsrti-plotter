package fft.Model;

import java.util.TreeMap;

public abstract class Graph {
    
    public TreeMap<Double, Double> points;
    private int exponent = 5;
    private double deltaBaseline = 1;
    private double noise;
    
    public Graph() {
        points = new TreeMap<Double, Double>();
    }
    
    public double getDeltaBaseline() {
        return deltaBaseline;
    }
    
    public void setDeltaBaseline(double b) {
        deltaBaseline = b;
        reinitializePoints();
    }
    
    public int getExponent() {
        return exponent;
    }
    
    public void setExponent(int e) {
        exponent = e;
        reinitializePoints();
    }
    
    public double getNoise() {
        return noise;
    }
    
    public void setNoise(double noise) {
        this.noise = noise;
        reinitializePoints();
    }
    
    public TreeMap<Double, Double> getPoints() {
        return points;
    }
    
    public void setPoints(TreeMap<Double, Double> points) {
        this.points = points;
        reinitializePoints();
    }
    
    public abstract void reinitializePoints();
}
