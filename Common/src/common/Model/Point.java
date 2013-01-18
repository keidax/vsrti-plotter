package common.Model;

import java.awt.geom.Point2D;

/**
 * A basic point class
 * 
 */
public class Point extends Point2D.Double implements Comparable<Point> {
    
    public Point(double x, double y) {
        setX(x);
        setY(y);
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    /**
     * compares x coordinates of objects MyPoint
     * 
     * @return 1 if this.x>input.x; 0 if this.x=input.x and -1 if this.x<input.x
     */
    @Override
    public int compareTo(Point o) {
        Point p = o;
        if (x > p.x) {
            return 1;
        } else if (x == p.x) {
            return 0;
        } else {
            return -1;
        }
    }
    
    @Override
    public String toString() {
        String s = "x: " + x + " y: " + y;
        return s;
    }
}
