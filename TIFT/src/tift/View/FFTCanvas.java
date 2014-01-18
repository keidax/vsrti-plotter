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
}
