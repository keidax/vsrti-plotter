package fft.View;

import java.awt.Graphics2D;

public class CircleOrnament extends AbstractOrnament {
    
    public static int radius = 2;
    public static int detectRadius = radius + 1;
    
    @Override
    public void draw(Graphics2D g, int x, int y) {
        g.fillArc(x - radius, y - radius, radius * 2 + 1, radius * 2 + 1, 0,
                380);
    }
    
    @Override
    public boolean isInside(int x, int y, int cx, int cy) {
        if (Math.sqrt(Math.pow(x - cx, 2) + Math.pow(y - cy, 2)) <= detectRadius) {
            return true;
        } else {
            return false;
        }
    }
}
