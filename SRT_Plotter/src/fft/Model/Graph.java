package fft.Model;

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

    public  void setDeltaBaseline(double b){
        deltaBaseline = b;
        model.visibilityGraph.reinicializePoints();
    }

    public  int getExponent() {
        return exponent;
    }

    public void setExponent(int e) {
        exponent = e;
        model.getVisibilityGraph().reinicializePoints();
    }

    public  double getDeltaBaseline() {
        return deltaBaseline;
    }
    public Graph() {
        points = new TreeMap<Double, Double>();
    }

    public TreeMap<Double, Double> getPoints() {
        return points;
    }

    public void setPoints(TreeMap<Double, Double> points) {
        this.points = points;
    }
}
