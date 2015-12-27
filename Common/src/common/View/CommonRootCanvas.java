package common.View;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.*;

@SuppressWarnings("serial")
public abstract class CommonRootCanvas extends JPanel implements MouseListener, MouseMotionListener {
    
    protected TreeMap<Double, Double> points;
    
    protected int lPad, rPad, tPad, bPad;
    
    protected String xAxisTitle = "x-axis";
    protected String yAxisTitle = "y-axis";
    protected String graphTitle = "Untitled";
    
    protected int yLabelWidth = 10;

    /**
     * determines the size of the axis labels and numbers
     */
    protected int fontSize = 20;
    protected Font normalFont;
    protected final JPopupMenu menu = new JPopupMenu();
    
    public CommonRootCanvas() {
        
    }
    
    public String getGraphTitle() {
        return graphTitle;
    }
    
    public void setGraphTitle(String graphTitle) {
        this.graphTitle = graphTitle;
    }
    
    public int g2cx(double x) {
        return (int) (lPad + (x - getMinX()) * getRatioX());
    }

    protected Font getNormalFont() {
        if (normalFont == null) {
            normalFont = new Font(Font.SANS_SERIF, Font.PLAIN, fontSize);
        }
        return normalFont;
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

    public double getXSpacing() {
        // determine spacing for marks on x-axis
        double xSpacing = 1;
        //TODO make this able to scale arbitrarily
        double pixelsPerNumber = this.getPlotWidth() * 1.0 / (getMaxX() - getMinX());
        if (pixelsPerNumber > 350) {
            xSpacing = 0.1;
        } else if (pixelsPerNumber > 200) {
            xSpacing = 0.2;
        } else if (pixelsPerNumber > 100) {
            xSpacing = 0.5;
        } else if (pixelsPerNumber > 35) {
            xSpacing = 1;
        } else if (pixelsPerNumber > 25) {
            xSpacing = 2;
        } else if (pixelsPerNumber > 8) {
            xSpacing = 5;
        } else if (pixelsPerNumber > 3.5) {
            xSpacing = 10;
        } else {
            xSpacing = 15;
        }
        return xSpacing;
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
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            menu.show(this, e.getX(), e.getY());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}
    
    @Override
    public void mouseReleased(MouseEvent e) {}
    
    @Override
    public void mouseMoved(MouseEvent e) {}
    
    @Override
    public void mouseDragged(MouseEvent e) {}
    
}
