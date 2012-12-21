package fft.View;

import java.awt.Graphics2D;

public class SquareOrnament extends AbstractOrnament {

    public static int side = 5;
    public static int detectSide = side + 1;

    @Override
    public void draw(Graphics2D g, int x, int y) {
        g.fillRect(x - side / 2, y - side / 2, side, side);
    }

    @Override
    public boolean isInside(int x, int y, int cx, int cy) {
        if (Math.abs(x - cx) <= detectSide / 2 && Math.abs(y - cy) <= detectSide / 2) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isInsideVertically(int x, int y, int cx, int cy) {
        if (Math.abs(x - cx) <= detectSide / 2) {
            return true;
        } else {
            return false;
        }
    }

	@Override
	public void setRadius(int radius) {
		side = radius;
		
	}

	@Override
	public int getRadius() {
		// TODO Auto-generated method stub
		return side;
	}
}
