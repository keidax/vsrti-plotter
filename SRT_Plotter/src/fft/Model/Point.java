package fft.Model;

import java.awt.geom.Point2D;

public class Point extends Point2D.Double implements Comparable<Point> {

    public Graph graph;
    private static final long serialVersionUID = 1L;
 
    public Point(Graph g, double x, double y) {
        graph = g;
        this.setX(x);
        this.setY(y);
        
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    /**
     * compares x coordinates of objects MyPoint
     * @return 1 if this.x>input.x; 0 if this.x=input.x and -1 if this.x<input.x
     */
    @Override
	public int compareTo(Point o) {
        Point p = o;
        if (this.x > p.x) {
            return 1;
        } else if (this.x == p.x) {
            return 0;
        } else {
            return -1;
        }
    }
    @Override
	public String toString()
    {
    	String s = "x: " + this.x + " y: " + this.y;
    	return s;
    }
}
