package tift.View;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Set;
import java.util.TreeMap;

import tift.Model.Adapter;

import common.View.AbstractOrnament;
import common.View.CircleOrnament;
import common.View.SquareOrnament;

/**
 * 
 * @author Karel Durktoa and Adam Pere
 * 
 */
@SuppressWarnings("serial")
public class VCanvas extends Canvas {
    
    protected TreeMap<Double, Double> dataPoints;
    protected static AbstractOrnament[] ornaments = {new CircleOrnament(), new SquareOrnament()};
    protected static Color[] colors = {Color.BLUE, Color.BLACK};
    protected int sigma = 1;
    final boolean amp;
    
    public VCanvas(View v, Adapter a, TreeMap<Double, Double> g, String yaxis, String title, boolean amplitude) {
        super(v, a, g, amplitude);
        dataPoints = g;
        System.out.println(dataPoints.size());
        xAxis = "time (s)";
        yAxis = yaxis;
        graphTitle = title;
        amp = amplitude;
    }
    
    public void setYAxis(String axis) {
        yAxis = axis;
    }
    
    public void setTitle(String t) {
        graphTitle = t;
    }
    
    @Override
    public void drawPoint(Graphics2D g, double x, double y) {
        
        if (dataPoints.containsKey(x) && dataPoints.get(x) == y) {
            g.setColor(colors[1]);
            ornaments[1].draw(g, g2cx(x), g2cy(y));
            if (adapter.getRms().containsKey(x)) {
                drawRms(g, g2cx(x), g2cy(y - adapter.getRms().get(x) * getSigma() / 2), g2cy(y
                        + adapter.getRms().get(x) * getSigma() / 2));
            }
        } else {
            g.setColor(colors[1]);
            ornaments[0].draw(g, g2cx(x), g2cy(y));
        }
    }
    
    @Override
    public void drawDataSet(Graphics2D g) {
        g.setStroke(new BasicStroke(strokeSize / 2));
        if (dataPoints.size() == 0) {
            return;
        }
        Set<Double> keys = dataPoints.keySet();
        Double previousKey = dataPoints.firstKey();
        for (Double key : keys) {
            if (key >= getMinX()) {
                g.setColor(Color.RED);
                new SquareOrnament().draw(g, g2cx(key), g2cy(dataPoints.get(key)));
                g.setColor(Color.BLACK);
                g.drawLine(g2cx(previousKey), g2cy(dataPoints.get(previousKey)), g2cx(key), g2cy(dataPoints.get(key)));
                previousKey = key;
            }
            
            if (key > Double.parseDouble(View.viewer.fMaxTime.getText())) {
                break;
            }
        }
    }
    
    @Override
    public Double getMaxYPoint() {
        if (dataPoints.size() == 0) {
            return null;
        }
        double max = dataPoints.firstEntry().getValue();
        Set<Double> keys = dataPoints.keySet();
        for (Double key : keys) {
            if (dataPoints.get(key) > max) {
                max = dataPoints.get(key);
            }
            if (key > Double.parseDouble(View.viewer.fMaxTime.getText())) {
                break;
            }
        }
        if (max < 2.0) {
            return 2.0;
        }
        return max;
    }
    
    @Override
    public Double getMinYPoint() {
        if (dataPoints.size() == 0) {
            return (double) -defaultY;
        }
        double min = dataPoints.firstEntry().getValue();
        Set<Double> keys = dataPoints.keySet();
        for (Double key : keys) {
            if (dataPoints.get(key) < min) {
                min = dataPoints.get(key);
            }
            if (key > Double.parseDouble(View.viewer.fMaxTime.getText())) {
                break;
            }
        }
        return min;
    }
    
    @Override
    public double getMaxX() {
        // show only half of the points because any points after nyquist
        // frequency/2 are useless
        if (dataPoints == null || dataPoints.size() == 0) {
            return defaultY;
        }
        Set<Double> keys = dataPoints.keySet();
        for (Double key : keys) {
            if (key > Double.parseDouble(View.viewer.fMaxTime.getText())) {
                return key;
            }
            
        }
        return dataPoints.lastKey();
    }
    
    public void drawRms(Graphics2D g, int x, int y1, int y2) {
        g.drawLine(x, y1, x, y2);
        g.drawLine(x - 1, y1, x + 1, y1);
        g.drawLine(x - 1, y2, x + 1, y2);
    }
    
    public void update() {
        if (amp) {
            dataPoints = adapter.getVisibilityGraphDataPoints();
        } else {
            dataPoints = adapter.getVisibilityGraphDataPoints2();
        }
    }
    
    @Override
    public void update(Graphics g) {
        if (amp) {
            dataPoints = adapter.getVisibilityGraphDataPoints();
        } else {
            dataPoints = adapter.getVisibilityGraphDataPoints2();
        }
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
        } catch (NullPointerException e) {}
        this.currentPoint = currentPoint;
    }
}
