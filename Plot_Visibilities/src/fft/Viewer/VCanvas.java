package fft.Viewer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.TreeMap;

import fft.Model.Adapter;

@SuppressWarnings("serial")
public class VCanvas extends Canvas {//JPanel implements MouseListener, MouseMotionListener {

    protected TreeMap<Double, Double> dataPoints;
    protected static AbstractOrnament[] ornaments = {new CircleOrnament(), new SquareOrnament()};
    protected static Color[] colors = {Color.BLUE, Color.BLACK};
    protected int sigma = 1;
    protected float RMSStroke = (float)2;

    public VCanvas(Viewer v, Adapter a, TreeMap<Double, Double> g) {
        super(v, a, g);
        dataPoints = this.adapter.getVisibilityGraphDataPoints();
        System.out.println(dataPoints.size());
        xAxisTitle = "Baseline/λ";
        yAxisTitle = "Visibility";
        graphTitle = "Visibility";
    }

    @Override
    public void drawPoint(Graphics2D g, double x, double y) {
        //System.out.println("RMS = "+adapter.getRms().size());
        if (adapter.getVisibilityGraphDataPoints().containsKey(x)
                && adapter.getVisibilityGraphDataPoints().get(x) == y) {
            g.setColor(colors[0]);
            ornaments[1].draw(g, g2cx(x), g2cy(y));
            if (adapter.getRms().containsKey(x)) {
            	//System.out.println("** " + adapter.getRms().get(x)*getSigma()/2);
                //System.out.println("g2cy(y + Sigma*rms) "+g2cy(y+getSigma()*adapter.getRms().get(x)));
                drawRms(g, g2cx(x), g2cy(y - adapter.getRms().get(x) * getSigma() / 2), g2cy(y + adapter.getRms().get(x) * getSigma() / 2));
                //System.out.println("RMS ["+x+","+adapter.getRms().get(x));
                //System.out.println("rms = "+adapter.getRms().get(x));
            }
        } else {
            g.setColor(colors[1]);
            ornaments[0].draw(g, g2cx(x), g2cy(y));
        }
    }
    
    public void drawRms(Graphics2D g, int x, int y1, int y2) {
    	g.setColor(Color.BLUE);
    	Stroke oldStroke=g.getStroke();
    	g.setStroke(new BasicStroke(RMSStroke));
        //height*=10;
        //System.out.println("draw:["+x+","+y1+"-"+y2+"]");
        g.drawLine(x, y1, x, y2);
        g.drawLine(x - 1, y1, x + 1, y1);
        g.drawLine(x - 1, y2, x + 1, y2);
        g.setStroke(oldStroke);
    }

    public void update() {
        dataPoints = this.adapter.getVisibilityGraphDataPoints();
    }

    @Override
    public void update(Graphics g) {
        dataPoints = adapter.getVisibilityGraphDataPoints();
        paint(g);
    }

    public int getSigma() {
        return this.sigma;
    }

    public void setSigma(int i) {
        sigma = i;
    }

    @Override
    public void setCurrentPoint(Double currentPoint) {
        try {
            adapter.removeRmsPoint(currentPoint);
        } catch (NullPointerException e) {
        }
        this.currentPoint = currentPoint;
    }
}
