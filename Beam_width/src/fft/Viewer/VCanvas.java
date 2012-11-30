package fft.Viewer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Set;
import java.util.TreeMap;

import fft.Model.Adapter;

@SuppressWarnings("serial")
public class VCanvas extends Canvas {//JPanel implements MouseListener, MouseMotionListener {

    protected TreeMap<Double, Double> dataPoints;
    protected AbstractOrnament[] ornaments = {new CircleOrnament(), new SquareOrnament()};
    protected Color[] colors = {Color.BLUE, Color.BLACK};
    protected int sigma = 1;

    public VCanvas(Viewer v, Adapter a, TreeMap<Double, Double> g) {
        super(v, a, g);
        dataPoints = this.adapter.getVisibilityGraphDataPoints();
        System.out.println(dataPoints.size());
        xAxisTitle = "Angle (degrees)";
        yAxisTitle = "Power";
        graphTitle = "Beam";
    }

    @Override
    public void drawPoint(Graphics2D g, double x, double y) {
        //System.out.println("point at ["+x+","+y+"]");
        //System.out.println("RMS = "+adapter.getRms().size());
        if (adapter.getVisibilityGraphDataPoints().containsKey(x)
                && adapter.getVisibilityGraphDataPoints().get(x) == y) {
            g.setColor(colors[0]);
            ornaments[1].draw(g, g2cx(x), g2cy(y));
            System.out.println("***" + x);
            if (adapter.getRms().containsKey(x)) {
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
    
    @Override
	public void drawDataSet(int count, Graphics2D g) {
        if (getPoints().size() == 0) {
            return;
        }
        Set<Double> keys = this.getPoints().keySet();
        Double previousKey = getPoints().firstKey();
        for (Double key : keys) {
            g.setColor(Color.BLACK);
            g.drawLine(g2cx(previousKey),
                    g2cy(getPoints().get(previousKey)),
                    g2cx(key),
                    g2cy(getPoints().get(key)));
            previousKey = key;
            drawPoint(g, key, getPoints().get(key));
            System.out.println("** " + key);
            if (adapter.getRms().containsKey(key)) {
                //System.out.println("g2cy(y + Sigma*rms) "+g2cy(y+getSigma()*adapter.getRms().get(x)));
                drawRms(g, g2cx(key), g2cy(getPoints().get(key) - adapter.getRms().get(key) * getSigma() / 2), g2cy(getPoints().get(key) + adapter.getRms().get(key) * getSigma() / 2));
            //new SquareOrnament().draw(g, g2cx(key),g2cy(this.getPoints().get(key)));
            //System.out.println("Drawing point at ["+key+","+this.getPoints().get(key)+"]");
            //System.out.println("Draw a point ["+p.getX()+","+p.getY()+"] at ["+g2cx(p.getX())+","+g2cy(p.getY(),ds)+"]");
        }}

    }

    public void drawRms(Graphics2D g, int x, int y1, int y2) {
        //height*=10;
        //System.out.println("draw:["+x+","+y1+"-"+y2+"]");
        g.drawLine(x, y1, x, y2);
        g.drawLine(x - 1, y1, x + 1, y1);
        g.drawLine(x - 1, y2, x + 1, y2);
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
