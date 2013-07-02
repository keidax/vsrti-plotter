package fringe.View;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Set;
import java.util.TreeMap;

import common.View.AbstractOrnament;
import common.View.CircleOrnament;
import common.View.SquareOrnament;

import fringe.Model.Adapter;

@SuppressWarnings("serial")
public class VCanvas extends Canvas {// JPanel implements MouseListener,
                                     // MouseMotionListener {
    
    protected TreeMap<Double, Double> dataPoints;
    protected static AbstractOrnament[] ornaments = {new CircleOrnament(3), new SquareOrnament(3)};
    protected static Color[] colors = {Color.BLUE, Color.BLACK};
    protected double sigma = 1;
    
    public VCanvas(View v, Adapter a, TreeMap<Double, Double> g) {
        super(v, a, g);
        dataPoints = adapter.getVisibilityGraphDataPoints();
        System.out.println(dataPoints.size());
        // xAxisTitle = "angle [\u00b0]";
        xAxisTitle = "Angle (degrees)";
        yAxisTitle = "Power";
        graphTitle = "Fringe Function";
    }
    
    @Override
    public void drawPoint(Graphics2D g, double x, double y) {
        
        if (adapter.getVisibilityGraphDataPoints().containsKey(x) && adapter.getVisibilityGraphDataPoints().get(x) == y) {
            // System.out.println("drawing original");
            g.setColor(colors[0]);
            ornaments[1].draw(g, g2cx(x), g2cy(y));
            if (adapter.getRms().containsKey(x)) {
                System.out.println("found rms for " + x);
                drawRms(g, g2cx(x), g2cy(y - adapter.getRms().get(x) * view.getModel().getDisplayFactor() / 2), g2cy(y
                        + adapter.getRms().get(x) * view.getModel().getDisplayFactor() / 2));
            } else {
                System.out.println("no rms found for " + x);
            }
        } else {
            System.out.println("drawing changed");
            g.setColor(colors[1]);
            ornaments[0].draw(g, g2cx(x), g2cy(y));
        }
    }
    
    @Override
    public void drawDataSet(int count, Graphics2D g) {
        if (dataPoints.size() == 0) {
            return;
        }
        Set<Double> keys = dataPoints.keySet();
        Double previousKey = dataPoints.firstKey();
        System.out.println("RMS keys: " + adapter.getRms().keySet());
        for (Double key : keys) {
            /*
             * if (Math.abs(key) > 40) {
             * System.out.println("OVER 40");
             * continue;
             * }
             */
            g.setColor(Color.BLACK);
            g.drawLine(g2cx(previousKey), g2cy(dataPoints.get(previousKey)), g2cx(key), g2cy(dataPoints.get(key)));
            previousKey = key;
            drawPoint(g, key, dataPoints.get(key));
            /*
             * if (adapter.getRms().containsKey(key)) {
             * System.out.println("drawing rms");
             * drawRms(g, g2cx(key), g2cy(points.get(key) - adapter.getRms().get(key) * getSigma() / 2),
             * g2cy(points.get(key)
             * + adapter.getRms().get(key) * getSigma() / 2));
             * // System.out.println("RMS [" + key + "," + adapter.getRms().get(key));
             * }
             */
        }
        // System.out.println(dataPoints);
        // System.out.println(adapter.getVisibilityGraphDataPoints());
    }
    
    public void drawRms(Graphics2D g, int x, int y1, int y2) {
        g.setColor(Color.BLUE);
        g.drawLine(x, y1, x, y2);
        g.drawLine(x - 1, y1, x + 1, y1);
        g.drawLine(x - 1, y2, x + 1, y2);
    }
    
    public void update() {
        dataPoints = adapter.getVisibilityGraphDataPoints();
    }
    
    @Override
    public void update(Graphics g) {
        dataPoints = adapter.getVisibilityGraphDataPoints();
        paint(g);
    }
    
    @Override
    public void setCurrentPoint(Double currentPoint) {
        try {
            adapter.removeRmsPoint(currentPoint);
        } catch (NullPointerException e) {}
        this.currentPoint = currentPoint;
    }
}
