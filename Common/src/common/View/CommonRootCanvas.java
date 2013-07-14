package common.View;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class CommonRootCanvas extends JPanel implements MouseListener, MouseMotionListener {
    
    protected TreeMap<Double, Double> points;
    
    protected int lPad, rPad, tPad, bPad;
    
    protected String xAxisTitle = "x-axis";
    protected String yAxisTitle = "y-axis";
    protected String graphTitle = "Untitled";
    
    protected int yLabelWidth = 10;
    
    public CommonRootCanvas() {
        
    }
    
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
    
    /**
     * 
     * @return The points on the graph
     */
    public TreeMap<Double, Double> getPoints() {
        return points;
    }
    
    /**
     * Set the points on the graph
     * 
     * @param points
     */
    public void setPoints(TreeMap<Double, Double> points) {
        this.points = points;
    }
    
    /**
     * Returns the object MyPoint which is in closer then MyCanvas.r to
     * coordinates [x,y]
     * 
     * @param x
     *            int
     * @param y
     *            int
     * @return MyPoint
     */
    protected Double getPointOnGraph(int x, int y) {
        
        Set<Double> keys = getPoints().keySet();
        for (Double key : keys) {
            if (new SquareOrnament(getOrnamentSize(key)).isInside(x, y, g2cx(key), g2cy(getPoints().get(key)))) {
                return key;
            }
        }
        return null;
    }
    
    protected int getOrnamentSize(double x) {
        return 3;
    }
    
    public double getRatioY() {
        return getPlotHeight() / (getMaxY() - getMinY());
    }
    
    public double getRatioX() {
        return getPlotWidth() / (getMaxX() - getMinX());
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {}
    
    @Override
    public void mouseExited(MouseEvent e) {}
    
    @Override
    public void mouseClicked(MouseEvent e) {}
    
    @Override
    public void mousePressed(MouseEvent e) {}
    
    @Override
    public void mouseReleased(MouseEvent e) {}
    
    @Override
    public void mouseMoved(MouseEvent e) {}
    
    @Override
    public void mouseDragged(MouseEvent e) {}
    
}
