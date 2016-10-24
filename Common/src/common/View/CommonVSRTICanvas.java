package common.View;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

@SuppressWarnings("serial")
public abstract class CommonVSRTICanvas extends CommonRootCanvas {


    public CommonVSRTICanvas() {
        addMouseListener(this);
        addMouseMotionListener(this);
        strokeSize = 5;
    }

    /**
     * Draw a vertical line at theta=0
     * @param g the Graphics2D object
     */
    protected void drawThetaLine(Graphics2D g) {
        g.setStroke(new BasicStroke(strokeSize));
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(g2cx(0), tCanvasPadding, g2cx(0), getHeight() - bCanvasPadding);
    }
    
    public double getMinY() {
        return 0.0;
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
