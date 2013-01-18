package common.View;

import java.awt.Graphics2D;

public abstract class AbstractOrnament {
    
    public abstract boolean isInside(int x, int y, int cx, int cy);
    
    public abstract void draw(Graphics2D g, int x, int y);
}
