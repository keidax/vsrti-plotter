package fft.View;

import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;

/**
 * 
 * @author Karel Durktoa
 *
 */
public class TriangleOrnament extends AbstractOrnament {

    public static int radius = 6;

    @Override
    public void draw(Graphics2D g, int x, int y) {
        double[] xs = new double[3];
        xs[0] = x;
        xs[1] = (x - Math.sqrt(Math.pow(radius, 2) - Math.pow(radius / 2, 2)));
        xs[2] = (x + Math.sqrt(Math.pow(radius, 2) - Math.pow(radius / 2, 2)));
        double[] ys = new double[3];
        ys[0] = y - radius;
        ys[1] = y + radius / 2;
        ys[2] = y + radius / 2;
        GeneralPath polygon = new GeneralPath(Path2D.WIND_EVEN_ODD, xs.length);
        polygon.moveTo(xs[0], ys[0]);
        for (int index = 1; index < xs.length; index++) {
            polygon.lineTo(xs[index], ys[index]);
        }
        ;
        polygon.closePath();
        g.fill(polygon);
    }

    @Override
    public boolean isInside(int x, int y, int cx, int cy) {
        // NOT CORRECT YET!!!
        if (Math.sqrt(Math.pow(x - cx, 2) + Math.pow(y - cy, 2)) <= radius) {
            return true;
        } else {
            return false;
        }
    }
}
