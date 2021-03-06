package common.View;

import java.awt.Graphics2D;

public class CircleOrnament extends AbstractOrnament {
    
    public int radius = 3;
    public int detectRadius = radius + 1;
    
    public CircleOrnament(int radius) {
        super();
        this.radius = radius;
    }
    
    @Override
    public void draw(Graphics2D g, int x, int y) {
        g.fillArc(x - radius, y - radius, radius * 2 + 1, radius * 2 + 1, 0, 380);
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
