package beam.View;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Set;
import java.util.TreeMap;

import common.View.AbstractOrnament;
import common.View.CircleOrnament;
import common.View.SquareOrnament;

/**
 * Specific implementation of Beam graph
 * 
 */

@SuppressWarnings("serial")
public class VCanvas extends Canvas {
    
    // protected TreeMap<Double, Double> dataPoints;
    protected TreeMap<Double, Double> rmsPoints;
    protected AbstractOrnament[] ornaments = {new CircleOrnament(3), new SquareOrnament(3)};
    protected Color[] colors = {Color.BLUE, Color.BLACK};
    protected double displayFactor = 1;
    
    public VCanvas(View v, TreeMap<Double, Double> g) {
        super(v, g);
        // dataPoints = this.adapter.getVisibilityGraphDataPoints();
        rmsPoints = new TreeMap<Double, Double>();
        // dataPoints = new TreeMap<Double, Double>();
        // System.out.println(dataPoints.size());
        xAxisTitle = "Angle (degrees)";
        yAxisTitle = "Power";
        graphTitle = "Beam";
    }
    
    @Override
    public void drawPoint(Graphics2D g, double x, double y) {
        g.setColor(colors[0]);
        ornaments[1].draw(g, g2cx(x), g2cy(y));
    }
    
    @Override
    public void drawDataSet(int count, Graphics2D g) {
        if (points.size() == 0) {
            return;
        }
        Set<Double> keys = points.keySet();
        Double previousKey = points.firstKey();
        for (Double key : keys) {
            g.setColor(Color.BLACK);
            g.drawLine(g2cx(previousKey), g2cy(points.get(previousKey)), g2cx(key), g2cy(points.get(key)));
            previousKey = key;
            drawPoint(g, key, points.get(key));
            if (rmsPoints.containsKey(key)) {
                drawRms(g, key, points.get(key));
            }
            if (view.getModel().getHorizontalError() > 0) {
                drawHorizontalError(g, key, points.get(key));
            }
        }
    }
    
    public void drawRms(Graphics2D g, double x, double y) {
        double upperY = y + rmsPoints.get(x).doubleValue() * getDisplayFactor() / 2;
        double lowerY = y - rmsPoints.get(x).doubleValue() * getDisplayFactor() / 2;
        g.drawLine(g2cx(x), g2cy(lowerY), g2cx(x), g2cy(upperY));
        g.drawLine(g2cx(x) - 1, g2cy(lowerY), g2cx(x) + 1, g2cy(lowerY));
        g.drawLine(g2cx(x) - 1, g2cy(upperY), g2cx(x) + 1, g2cy(upperY));
    }
    
    public void drawHorizontalError(Graphics2D g, double x, double y) {
        double leftX = x - view.getModel().getHorizontalError();
        double rightX = x + view.getModel().getHorizontalError();
        g.drawLine(g2cx(leftX), g2cy(y), g2cx(rightX), g2cy(y));
        g.drawLine(g2cx(leftX), g2cy(y) + 1, g2cx(leftX), g2cy(y) - 1);
        g.drawLine(g2cx(rightX), g2cy(y) + 1, g2cx(rightX), g2cy(y) - 1);
    }
    
    public void update(TreeMap<Double, Double> points, TreeMap<Double, Double> rmsPoints) {
        this.points = points;
        // this.dataPoints = dataPoints;
        this.rmsPoints = rmsPoints;
    }
    
    @Override
    public void update(Graphics g) { // TODO is this necessary?...
        // dataPoints = adapter.getVisibilityGraphDataPoints();
        paint(g);
    }
    
    public double getDisplayFactor() {
        return displayFactor;
    }
    
    public void setDisplayFactor(double i) {
        displayFactor = i;
    }
    
    @Override
    public void setCurrentPoint(Double currentPoint) {
        try {
            view.removeRmsPoint(currentPoint);
        } catch (NullPointerException e) {}
        this.currentPoint = currentPoint;
    }
}
