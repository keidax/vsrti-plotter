package fft.View;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.TreeMap;

import fft.Model.Adapter;

public class VCanvas extends Canvas {// JPanel implements MouseListener,
                                     // MouseMotionListener {

    protected TreeMap<Double, Double> dataPoints;
    protected static AbstractOrnament[] ornaments = { new CircleOrnament(),
            new SquareOrnament() };
    protected static Color[] colors = { Color.BLUE, Color.BLACK };
    protected int sigma = 1;
    
    public VCanvas(View v, Adapter a, TreeMap<Double, Double> g) {
        super(v, a, g);
        dataPoints = adapter.getVisibilityGraphDataPoints();
        xAxis = "Channel";
        yAxis = "Antenna Temperature (K)";
        graphTitle = "Antenna Temperature vs. Channel";
    }
    
    /**
     * Draws an individual point as a black circle
     */
    @Override
    public void drawPoint(Graphics2D g, double x, double y) {
        if (adapter.getVisibilityGraphDataPoints().containsKey(x)
                && adapter.getVisibilityGraphDataPoints().get(x) == y) {
            g.setColor(Color.BLACK);
            ornaments[1].draw(g, g2cx(x), g2cy(y));
            if (adapter.getRms().containsKey(x)) {
                drawRms(g, g2cx(x), g2cy(y - adapter.getRms().get(x)
                        * getSigma() / 2), g2cy(y + adapter.getRms().get(x)
                        * getSigma() / 2));
            }
        } else {
            g.setColor(Color.BLACK);
            ornaments[0].draw(g, g2cx(x), g2cy(y));
        }
    }
    
    /**
     * Draws an individual point as a red circle or a red triangle
     */
    @Override
    public void drawPoint(Graphics2D g, double x, double y, boolean b) {
        
        g.setColor(Color.RED);
        TriangleOrnament t = new TriangleOrnament();
        if (b == true) {
            ornaments[1].draw(g, g2cx(x), g2cy(y));
        } else {
            t.draw(g, g2cx(x), g2cy(y));
        }
    }
    
    /**
     * Draws RMS values
     * 
     * @param g
     * @param x
     * @param y1
     * @param y2
     */
    public void drawRms(Graphics2D g, int x, int y1, int y2) {
        g.drawLine(x, y1, x, y2);
        g.drawLine(x - 1, y1, x + 1, y1);
        g.drawLine(x - 1, y2, x + 1, y2);
    }
    
    /**
     * Updates the data points
     */
    public void update() {
        dataPoints = adapter.getVisibilityGraphDataPoints();
    }
    
    /**
     * updates the data points and paints to the graphics
     */
    @Override
    public void update(Graphics g) {
        dataPoints = adapter.getVisibilityGraphDataPoints();
        paint(g);
    }
    
    public int getSigma() {
        return sigma;
    }
    
    public void setSigma(int i) {
        sigma = i;
    }
    
    @Override
    public void setCurrentPoint(Double currentPoint) {
        try {
            adapter.removeRmsPoint(currentPoint);
        } catch (NullPointerException e) {
        }
        this.currentPoint = currentPoint;
    }
}
