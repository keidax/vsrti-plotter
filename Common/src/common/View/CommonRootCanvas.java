package common.View;

import java.util.TreeMap;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class CommonRootCanvas extends JPanel {
    
    public TreeMap<Double, Double> points;
    
    protected int lPad, rPad, tPad, bPad;
    
    protected String xAxisTitle = "x-axis";
    protected String yAxisTitle = "y-axis";
    protected String graphTitle = "Untitled";
    
    protected int yLabelWidth = 10;
    
    public int g2cx(double x) {
        return (int) (lPad + (x - getMinX()) * getRatioX());
    }
    
    /**
     * @param y
     *            the y coordinate of the graph, in units
     * @return the y coordinate of the canvas, in pixels
     */
    public int g2cy(double y) {
        return (int) (getPlotHeight() + tPad - (y - getMinY()) * getRatioY());
    }
    
    public double c2gx(double x) {
        return (x - lPad) / getRatioX() + getMinX();
    }
    
    /**
     * @param toy
     *            the y coordinate of the canvas, in pixels
     * @return the y coordinate of the graph, in units
     */
    public double c2gy(double toy) {
        return (toy - getPlotHeight() - tPad) / -getRatioY() + getMinY();
    }
    
    /**
     * @return The width of the plot section of the canvas, in pixels
     */
    protected int getPlotWidth() {
        return getWidth() - lPad - rPad;
    }
    
    /**
     * @return The height of the plot section of the canvas, in pixels
     */
    protected int getPlotHeight() {
        return getHeight() - tPad - bPad;
    }
    
    public abstract double getMinX();
    
    public abstract double getMinY();
    
    protected abstract double getMaxX();
    
    protected abstract double getMaxY();
    
    public double getRatioY() {
        return getPlotHeight() / (getMaxY() - getMinY());
    }
    
    public double getRatioX() {
        return getPlotWidth() / (getMaxX() - getMinX());
    }
    
}
