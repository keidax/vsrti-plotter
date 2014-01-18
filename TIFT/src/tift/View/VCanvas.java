package tift.View;

import tift.Model.Adapter;

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
        try {
            adapter.removeRmsPoint(currentPoint);
        } catch (NullPointerException e) {
            System.out.println("NullPointerException in VCanvas.setCurrentPoint");
        }
        this.currentPoint = currentPoint;
    }
}
