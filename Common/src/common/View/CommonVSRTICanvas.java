package common.View;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

@SuppressWarnings("serial")
public abstract class CommonVSRTICanvas extends CommonRootCanvas {
    
    /**
     * determines the size of the axis labels and numbers
     */
    protected int fontSize = 20;
    protected int strokeSize = 5;
    
    public CommonVSRTICanvas() {
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    
    public void drawXAxis(Graphics2D g) {
        g.setStroke(new BasicStroke(strokeSize));
        g.setFont(new Font(g.getFont().getFontName(), 0, fontSize));
        DecimalFormat df = new DecimalFormat("#.#");
        FontMetrics fm = g.getFontMetrics();
        
        // determine spacing for marks on x-axis
        double xSpacing = 1;
        double pixelsPerNumber = this.getPlotWidth() * 1.0 / (getMaxX() - getMinX());
        if (pixelsPerNumber > 200) {
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
        
        // draw axis title
        g.drawString(xAxisTitle, getLPad() + (getPlotWidth() - g.getFontMetrics().stringWidth(xAxisTitle)) / 2,
                getHeight() - 10);
        // draw horizontal axis
        g.drawLine(getLPad(), g2cy(0.0), getWidth() - rPad, g2cy(0.0));
        
        // draw vertical line at theta=0
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(g2cx(0), tPad, g2cx(0), getHeight() - bPad);
        
        for (double i = (int) ((int) (getMinX() / xSpacing) * xSpacing); i <= getMaxX(); i += xSpacing) {
            int xPosition = g2cx(i);
            int yPosition = getHeight() - bPad;
            
            // draw vertical marks
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(xPosition, yPosition + 2, xPosition, yPosition - 2);
            
            // draw labels for each mark
            g.setColor(Color.BLACK);
            String lString = df.format(i);
            g.drawString(lString, xPosition - fm.stringWidth(lString) / 2, yPosition + fm.getAscent() + 5);
        }
        
    }
    
    public void drawYAxis(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(strokeSize));
        g.setFont(new Font(g.getFont().getFontName(), 0, fontSize));
        g.drawLine(getLPad(), tPad, getLPad(), getHeight() - bPad);
        
        FontMetrics fm = g.getFontMetrics();
        DecimalFormat df = new DecimalFormat("#.#");
        
        // determine spacing for marks on y-axis
        double ySpacing = 1;
        double pixelsPerNumber = this.getPlotHeight() * 1.0 / (getMaxY() - getMinY());
        if (pixelsPerNumber > 200) {
            ySpacing = 0.2;
        } else if (pixelsPerNumber > 100) {
            ySpacing = 0.5;
        } else if (pixelsPerNumber > 35) {
            ySpacing = 1;
        } else if (pixelsPerNumber > 25) {
            ySpacing = 2;
        } else if (pixelsPerNumber > 8) {
            ySpacing = 5;
        } else if (pixelsPerNumber > 3.5) {
            ySpacing = 10;
        } else {
            ySpacing = 15;
        }
        
        for (double i = (int) ((int) (getMinY() / ySpacing) * ySpacing); i <= getMaxY(); i += ySpacing) {
            int xPosition = lPad;
            int yPosition = g2cy(i);
            // draw horixontal marks
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(xPosition - 2, yPosition, xPosition + 2, yPosition);
            // draw label for each mark
            g.setColor(Color.BLACK);
            String tempString = df.format(i);
            g.drawString(tempString, xPosition - fm.stringWidth(tempString) - 5, yPosition + fm.getAscent() / 2);
        }
        
        drawVerticalLabel(g);
    }
    
    /**
     * Draws the label for the y axis
     */
    public void drawVerticalLabel(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font(g.getFont().getFontName(), 0, fontSize));
        
        int translateDown = tPad + (getPlotHeight() + g.getFontMetrics().stringWidth(yAxisTitle)) / 2;
        
        g.translate(yLabelWidth, translateDown);
        g.rotate(-Math.PI / 2.0);
        
        g.drawString(yAxisTitle, 0, 10);
        
        g.rotate(Math.PI / 2.0);
        g.translate(-yLabelWidth, -translateDown);
    }
    
    public double getMinY() {
        return 0.0;
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
    
    @Override
    public void mouseMoved(MouseEvent e) {
        // System.out.println("moved!!!");
        DecimalFormat df = new DecimalFormat("#.##");
        if (getPointOnGraph(e.getX(), e.getY()) != null) {
            double p = getPointOnGraph(e.getX(), e.getY());
            this.setToolTipText("(" + df.format(p) + ", " + df.format(getPoints().get(p)) + ")");
            this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            
        } else {
            this.setToolTipText(null);
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
}
