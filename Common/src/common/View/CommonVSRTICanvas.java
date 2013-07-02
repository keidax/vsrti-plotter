package common.View;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.text.DecimalFormat;

@SuppressWarnings("serial")
public abstract class CommonVSRTICanvas extends CommonRootCanvas {
    
    protected float stroke = 5;
    /**
     * determines the size of the axis labels and numbers
     */
    protected int fontSize = 20;
    
    public void drawXAxis(Graphics2D g) {
        g.setStroke(new BasicStroke(stroke));
        g.setFont(new Font(g.getFont().getFontName(), 0, fontSize));
        DecimalFormat df = new DecimalFormat("#.#");
        FontMetrics fm = g.getFontMetrics();
        
        // determine spacing for marks on x-axis
        int xSpacing = 1;
        double pixelsPerNumber = this.getPlotWidth() * 1.0 / (getMaxX() - getMinX());
        if (pixelsPerNumber > 50) {
            xSpacing = 1;
        } else if (pixelsPerNumber <= 50 && pixelsPerNumber > 30) {
            xSpacing = 2;
        } else if (pixelsPerNumber <= 30 && pixelsPerNumber > 7.5) {
            xSpacing = 5;
        } else if (pixelsPerNumber <= 7.5 && pixelsPerNumber > 3.5) {
            xSpacing = 10;
        } else if (pixelsPerNumber <= 3.5) {
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
        
        for (int i = (int) (getMinX() / xSpacing) * xSpacing; i <= getMaxX(); i += xSpacing) {
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
        g.setStroke(new BasicStroke(stroke));
        g.setFont(new Font(g.getFont().getFontName(), 0, fontSize));
        g.drawLine(getLPad(), tPad, getLPad(), getHeight() - bPad);
        
        FontMetrics fm = g.getFontMetrics();
        DecimalFormat df = new DecimalFormat("#.#");
        
        // determine spacing for marks on y-axis
        double ySpacing = 1;
        double pixelsPerNumber = this.getPlotHeight() * 1.0 / getMaxY();
        System.out.println("pixels / number " + pixelsPerNumber);
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
        
        for (double i = 0; i <= getMaxY(); i += ySpacing) {
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
    
}
