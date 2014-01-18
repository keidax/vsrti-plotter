package tift.View;

import tift.Model.Adapter;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.TreeMap;

/**
 * @author Karel Durktoa
 * @author Adam Pere
 * @author Gabriel Holodak
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
    public double getMinX() {
        return adapter.getMinFrequency();
    }

    @Override
    public double getMaxX() {
        return adapter.getMaxFrequency();
    }


    @Override
    public void drawDataSet(Graphics2D g) {
        if (getMinX() < 0 && getMaxX() > 0) {
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(g2cx(0), tPad, g2cx(0), getHeight() - bPad);
        }
        super.drawDataSet(g);
    }

    /**
     * records where mouse was pressed and weather there is any point in less
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
            System.out.println("ypoint = " + c2gy(toy));
            if (amp) {
                adapter.moveImagePoint(getCurrentPoint(), c2gy(toy));
            } else {
                adapter.moveImagePoint2(getCurrentPoint(), c2gy(toy));
            }
        }
    }
}
