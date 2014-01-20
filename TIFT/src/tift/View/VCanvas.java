package tift.View;

import tift.Model.Adapter;

import java.awt.event.MouseEvent;
import java.util.TreeMap;

/**
 * @author Karel Durktoa
 * @author Adam Pere
 * @author Gabriel Holodak
 */
@SuppressWarnings("serial")
public class VCanvas extends Canvas {

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
    public double getMinX() {
        return adapter.getMinTime();
    }

    @Override
    public double getMaxX() {
        return adapter.getMaxTime();
    }

    @Override
    public void setCurrentPoint(Double currentPoint) {
        this.currentPoint = currentPoint;
    }


    /**
     * records where mouse was pressed and whether there is any point in less
     * distance then MyCanvas.r
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            handleMouseMoveEvent(e);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        handleMouseMoveEvent(e);
    }

    private void handleMouseMoveEvent(MouseEvent e) {
        if (getVerticallyPointOnGraph(e.getX(), e.getY()) != null) {
            setCurrentPoint(getVerticallyPointOnGraph(e.getX(), e.getY()));
        } else {
            setCurrentPoint(null);
        }

        if (getCurrentPoint() != null) {
            double toy = Math.min(Math.max(tPad, e.getY()), getHeight() - bPad);
            if (amp) {
                adapter.moveVisibilityPoint(getCurrentPoint(), c2gy(toy));
            } else {
                adapter.moveVisibilityPoint2(getCurrentPoint(), c2gy(toy));
            }
        }
    }
}
