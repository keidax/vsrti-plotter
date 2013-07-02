package tift.View;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Set;
import java.util.TreeMap;

import tift.Model.Adapter;

import common.View.CircleOrnament;
import common.View.SquareOrnament;

/**
 * 
 * @author Karel Durktoa and Adam Pere
 * 
 */
@SuppressWarnings("serial")
public class VCanvas extends Canvas {
    
    protected static Color[] colors = {Color.BLUE, Color.BLACK};
    protected int sigma = 1;
    final boolean amp;
    
    public VCanvas(View v, Adapter a, TreeMap<Double, Double> g, String yaxis, String title, boolean amplitude) {
        super(v, a, g, amplitude);
        xAxisTitle = "time (s)";
        yAxisTitle = yaxis;
        graphTitle = title;
        amp = amplitude;
    }
    
    public void setYAxis(String axis) {
        yAxisTitle = axis;
    }
    
    public void setTitle(String t) {
        graphTitle = t;
    }
    
    @Override
    public void drawPoint(Graphics2D g, double x, double y) {
        if (getPoints().containsKey(x) && getPoints().get(x) == y) {
            g.setColor(colors[1]);
            new SquareOrnament(getOrnamentSize(x)).draw(g, g2cx(x), g2cy(y));
            if (adapter.getRms().containsKey(x)) {
                drawRms(g, g2cx(x), g2cy(y - adapter.getRms().get(x) * getSigma() / 2), g2cy(y
                        + adapter.getRms().get(x) * getSigma() / 2));
            }
        } else {
            g.setColor(colors[1]);
            new CircleOrnament(getOrnamentSize(x)).draw(g, g2cx(x), g2cy(y));
        }
    }
    
    @Override
    public void drawDataSet(Graphics2D g) {
        g.setStroke(new BasicStroke(strokeSize / 2));
        if (getPoints().size() == 0) {
            return;
        }
        Set<Double> keys = getPoints().keySet();
        Double previousKey = getPoints().firstKey();
        for (Double key : keys) {
            if (key >= getMinX()) {
                g.setColor(Color.RED);
                new SquareOrnament(getOrnamentSize(key)).draw(g, g2cx(key), g2cy(getPoints().get(key)));
                g.setColor(Color.RED);
                g.drawLine(g2cx(previousKey), g2cy(getPoints().get(previousKey)), g2cx(key), g2cy(getPoints().get(key)));
                previousKey = key;
            }
            
            if (key > Double.parseDouble(View.viewer.fMaxTime.getText())) {
                break;
            }
        }
    }
    
    @Override
    public double getMaxYPoint() {
        if (getPoints().size() == 0) {
            return 0;
        }
        double max = getPoints().firstEntry().getValue();
        Set<Double> keys = getPoints().keySet();
        for (Double key : keys) {
            if (getPoints().get(key) > max) {
                max = getPoints().get(key);
            }
            if (key > Double.parseDouble(View.viewer.fMaxTime.getText())) {
                break;
            }
        }
        if (max < 2.0) {
            return 2.0;
        }
        return max * 1.1;
    }
    
    @Override
    public double getMinYPoint() {
        if (getPoints().size() == 0) {
            return -defaultY;
        }
        double min = getPoints().firstEntry().getValue();
        Set<Double> keys = getPoints().keySet();
        for (Double key : keys) {
            if (getPoints().get(key) < min) {
                min = getPoints().get(key);
            }
            if (key > Double.parseDouble(View.viewer.fMaxTime.getText())) {
                break;
            }
        }
        if (min > -defaultY) {
            return -defaultY;
        }
        return min * 1.1;
    }
    
    @Override
    public double getMaxX() {
        // show only half of the points because any points after nyquist
        // frequency/2 are useless
        if (getPoints() == null || getPoints().size() == 0) {
            return defaultY;
        }
        Set<Double> keys = getPoints().keySet();
        for (Double key : keys) {
            if (key > Double.parseDouble(View.viewer.fMaxTime.getText())) {
                return key;
            }
            
        }
        return getPoints().lastKey();
    }
    
    public void drawRms(Graphics2D g, int x, int y1, int y2) {
        g.drawLine(x, y1, x, y2);
        g.drawLine(x - 1, y1, x + 1, y1);
        g.drawLine(x - 1, y2, x + 1, y2);
    }
    
    public void update() {
        if (amp) {
            setPoints(adapter.getVisibilityGraphDataPoints());
        } else {
            setPoints(adapter.getVisibilityGraphDataPoints2());
        }
    }
    
    @Override
    public void update(Graphics g) {
        if (amp) {
            setPoints(adapter.getVisibilityGraphDataPoints());
        } else {
            setPoints(adapter.getVisibilityGraphDataPoints2());
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
