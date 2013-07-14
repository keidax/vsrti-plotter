package tift2.View;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.Set;
import java.util.TreeMap;

import tift2.Model.Adapter;

import common.View.SquareOrnament;

@SuppressWarnings("serial")
public class FFTCanvas extends Canvas { // JPanel implements MouseListener,
                                        // MouseMotionListener {
    
    public FFTCanvas(View v, Adapter a, TreeMap<Double, Double> g) {
        super(v, a, g);
        xAxisTitle = "frequency (Hz)";
        yAxisTitle = "Amplitude";
        graphTitle = "F(\ud835\udf08)";
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
            g.setColor(Color.RED);
            new SquareOrnament(getOrnamentSize(key)).draw(g, g2cx(key), g2cy(getPoints().get(key)));
            g.setColor(Color.RED);
            g.drawLine(g2cx(previousKey), g2cy(getPoints().get(previousKey)), g2cx(key), g2cy(getPoints().get(key)));
            previousKey = key;
            if (key > Double.parseDouble(View.view.fThetaMax.getText())) {
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
            if (key > Double.parseDouble(View.view.fThetaMax.getText())) {
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
            return (double) -defaultY;
        }
        double min = getPoints().firstEntry().getValue();
        Set<Double> keys = getPoints().keySet();
        for (Double key : keys) {
            if (getPoints().get(key) < min) {
                min = getPoints().get(key);
            }
            if (key > Double.parseDouble(View.view.fThetaMax.getText())) {
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
        // show only half of the points;
        if (getPoints() == null || getPoints().size() == 0) {
            return defaultY;
        }
        Set<Double> keys = getPoints().keySet();
        for (Double key : keys) {
            if (key >= Double.parseDouble(View.view.fThetaMax.getText())) {
                return key;
            }
        }
        return getPoints().lastKey();// this.getPoints().ceilingKey(this.getPoints().lastKey()/2);
    }
    
    /**
     * records where mouse was pressed and weather there is any point in less
     * distance then MyCanvas.r
     */
    @Override
    public void mousePressed(MouseEvent evt) {
        // this.getVision().getCare().set(this.getVision().getSoul().newMemento());
        if (evt.getButton() != MouseEvent.BUTTON1) {
            return;
        }
        mCanx = evt.getX();
        mCany = evt.getY();
        if (getVerticallyPointOnGraph(mCanx, mCany) != null) {
            setCurrentPoint(getVerticallyPointOnGraph(mCanx, mCany));
        } else {
            setCurrentPoint(null);
        }
        
        if (getCurrentPoint() != null) {
            double toy = Math.min(Math.max(tPad, mCany), getHeight() - bPad);
            // System.out.println("moving to ["+tox+","+toy+"]");
            adapter.moveImagePoint(getCurrentPoint(), c2gy(toy));
            // getCurrentPoint().movePoint(getCurrentPoint(),
            // c2gx(tox),c2gy(toy,getCurrentPoint().getDataSet()));
        }
        // System.out.println("Current point is "+currentPoint);
    }
    
    @Override
    public void mouseDragged(MouseEvent evt) {
        mCanx = evt.getX();
        mCany = evt.getY();
        if (getVerticallyPointOnGraph(mCanx, mCany) != null) {
            setCurrentPoint(getVerticallyPointOnGraph(mCanx, mCany));
        } else {
            setCurrentPoint(null);
        }
        
        if (getCurrentPoint() != null) {
            // double tox = Math.min(Math.max(getLeftShift(), mCanx), getLeftShift() + getPlotWidth());
            double toy = Math.min(Math.max(tPad, mCany), getHeight() - bPad);
            adapter.moveImagePoint(getCurrentPoint(), c2gy(toy));
        }
    }
    
    @Override
    public void update(Graphics g) {
        paint(g);
    }
}
