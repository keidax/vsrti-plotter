package tift2.View;

import common.View.SquareOrnament;
import tift2.Model.Adapter;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Set;
import java.util.TreeMap;

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
            if (key > adapter.getMaxFrequency()) {
                break;
            }
        }
    }

    //@Override
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
            if (key > adapter.getMaxFrequency()) {
                break;
            }
        }
        if (max < 2.0) {
            return 2.0;
        }
        return max * 1.1;
    }

    //@Override
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
            if (key > adapter.getMaxFrequency()) {
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
            if (key >= adapter.getMaxFrequency()) {
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
    public void mousePressed(MouseEvent e) {
        // this.getVision().getCare().set(this.getVision().getSoul().newMemento());
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
            // System.out.println("moving to ["+tox+","+toy+"]");
            adapter.moveImagePoint(getCurrentPoint(), c2gy(toy));
            // getCurrentPoint().movePoint(getCurrentPoint(),
            // c2gx(tox),c2gy(toy,getCurrentPoint().getDataSet()));
        }
        // System.out.println("Current point is "+currentPoint);
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        if (getVerticallyPointOnGraph(e.getX(), e.getY()) != null) {
            setCurrentPoint(getVerticallyPointOnGraph(e.getX(), e.getY()));
        } else {
            setCurrentPoint(null);
        }
        
        if (getCurrentPoint() != null) {
            // double tox = Math.min(Math.max(getLeftShift(), mCanx), getLeftShift() + getPlotWidth());
            double toy = Math.min(Math.max(tPad, e.getY()), getHeight() - bPad);
            adapter.moveImagePoint(getCurrentPoint(), c2gy(toy));
        }
    }
    
    @Override
    public void update(Graphics g) {
        paint(g);
    }
}
