package tift.View;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.Set;
import java.util.TreeMap;

import tift.Model.Adapter;

import common.View.SquareOrnament;

/**
 * 
 * @author Karel Durktoa and Adam Pere
 * 
 */
@SuppressWarnings("serial")
public class FFTCanvas extends Canvas {
    
    protected boolean amp;
    
    public FFTCanvas(View v, Adapter a, TreeMap<Double, Double> g, String yaxis, String title, boolean amplitude) {
        super(v, a, g, amplitude);
        xAxisTitle = "frequency (Hz)";
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
    public void drawDataSet(Graphics2D g) {
        g.setStroke(new BasicStroke(strokeSize / 2));
        if (getPoints().size() == 0) {
            return;
        }
        Set<Double> keys = getPoints().keySet();
        Double previousKey = getPoints().firstKey();
        for (Double key : keys) {
            if (key >= Double.parseDouble(View.viewer.fThetaMin.getText())) {
                g.setColor(Color.RED);
                int ornamentSize;
                new SquareOrnament(getOrnamentSize(key)).draw(g, g2cx(key - getMinX()), g2cy(getPoints().get(key)));
                g.setColor(Color.RED);
                g.drawLine(g2cx(previousKey - getMinX()), g2cy(getPoints().get(previousKey)), g2cx(key - getMinX()),
                        g2cy(getPoints().get(key)));
                previousKey = key;
                if (key > Double.parseDouble(View.viewer.fThetaMax.getText())
                        || key > 1 / (2 * Double.parseDouble(View.viewer.fDelta.getText()))) {
                    break;
                }
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
            if (key > Double.parseDouble(View.viewer.fThetaMin.getText())) {
                if (getPoints().get(key) > max) {
                    max = getPoints().get(key);
                }
                if (key > Double.parseDouble(View.viewer.fThetaMax.getText())
                        || key > 1 / (2 * Double.parseDouble(View.viewer.fDelta.getText()))) {
                    break;
                }
            }
        }
        
        if (max < 2.0) {
            return 2.0;
        }
        return max * 1.1;
    }
    
    @Override
    public double getMinYPoint() {
        if (getPoints().isEmpty()) {
            return -defaultY;
        }
        double min = getPoints().firstEntry().getValue();
        Set<Double> keys = getPoints().keySet();
        for (Double key : keys) {
            if (key > Double.parseDouble(View.viewer.fThetaMin.getText())) {
                if (getPoints().get(key) < min) {
                    min = getPoints().get(key);
                }
                if (key > Double.parseDouble(View.viewer.fThetaMax.getText())
                        || key > 1 / (2 * Double.parseDouble(View.viewer.fDelta.getText()))) {
                    break;
                }
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
        // frequency/2 are redundant
        if (getPoints() == null || getPoints().size() == 0) {
            return defaultY;
        }
        Set<Double> keys = getPoints().keySet();
        for (Double key : keys) {
            if (key > Double.parseDouble(View.viewer.fThetaMin.getText())) {
                if (key > Double.parseDouble(View.viewer.fThetaMax.getText())
                        || key > 1 / (2 * Double.parseDouble(View.viewer.fDelta.getText()))) {
                    return key;
                }
            }
        }
        return getPoints().lastKey();
    }
    
    /**
     * records where mouse was pressed and weather there is any point in less
     * distance then MyCanvas.r
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() != MouseEvent.BUTTON1) {
            return;
        }
        if (getVerticallyPointOnGraph(e.getX(), e.getY()) != null) {
            setCurrentPoint(getVerticallyPointOnGraph(e.getX(), e.getY()));
        } else {
            setCurrentPoint(null);
        }
        if (getCurrentPoint() != null) {
            double toy = Math.min(Math.max(tPad, e.getY()), getHeight() - bPad);
            if (amp) {
                adapter.moveImagePoint(getCurrentPoint(), c2gy(toy));
            } else {
                adapter.moveImagePoint2(getCurrentPoint(), c2gy(toy));
            }
        }
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        if (getVerticallyPointOnGraph(e.getX(), e.getY()) != null) {
            setCurrentPoint(getVerticallyPointOnGraph(e.getX(), e.getY()));
        } else {
            setCurrentPoint(null);
        }
        
        if (getCurrentPoint() != null) {
            double toy = Math.min(Math.max(tPad, e.getY()), getHeight() - bPad);
            if (amp) {
                adapter.moveImagePoint(getCurrentPoint(), c2gy(toy));
            } else {
                adapter.moveImagePoint2(getCurrentPoint(), c2gy(toy));
            }
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
}
