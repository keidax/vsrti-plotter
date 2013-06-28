package common.View;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class CommonCanvas extends JPanel {
    
    protected int lPad, rPad, tPad, bPad;
    
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
    
    protected abstract double getMaxX();
    
    protected abstract double getMinX();
    
    protected abstract double getMaxY();
    
    public double getRatioX() {
        return getPlotWidth() / (getMaxX() - getMinX());
    }
    
    public double getRatioY() {
        return getPlotHeight() / getMaxY();
    }
    
    public int g2cx(double x) {
        return (int) (getLPad() + (x - getMinX()) * getRatioX());
    }
    
    /**
     * @param y
     *            the y coordinate of the graph, in units
     * @return the y coordinate of the canvas, in pixels
     */
    public int g2cy(double y) {
        return (int) (getPlotHeight() + tPad - y * getRatioY());
    }
    
    public double c2gx(double x) {
        return (x - getLPad()) / getRatioX() + getMinX();
    }
    
    /**
     * @param toy
     *            the y coordinate of the canvas, in pixels
     * @return the y coordinate of the graph, in units
     */
    public double c2gy(double toy) {
        return (toy - getPlotHeight() - tPad) / -getRatioY();
    }
    
    public int getLPad() {
        return lPad;
    }
    
    public void setLPad(int pad) {
        lPad = pad;
    }
    
    public int getRPad() {
        return rPad;
    }
    
    public void setRPad(int pad) {
        rPad = pad;
    }
    
    public int getTPad() {
        return tPad;
    }
    
    public void setTPad(int pad) {
        tPad = pad;
    }
    
    public int getBPad() {
        return bPad;
    }
    
    public void setBPad(int pad) {
        bPad = pad;
    }
    
}
