package fft.View;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Set;
import java.util.TreeMap;

import fft.Model.Adapter;

public class VCanvas extends Canvas {//JPanel implements MouseListener, MouseMotionListener {

    protected TreeMap<Double, Double> dataPoints;
    protected static AbstractOrnament[] ornaments = {new CircleOrnament(), new SquareOrnament()};
    protected static Color[] colors = {Color.BLUE, Color.BLACK};
    protected int sigma = 1;

    public VCanvas(View v, Adapter a, TreeMap<Double, Double> g) {
        super(v, a, g);
        dataPoints = this.adapter.getVisibilityGraphDataPoints();
        System.out.println(dataPoints.size());
        xAxisTitle = "angle [degrees]";
        yAxisTitle = "power";
        graphTitle = "Fringe Function";
    }

    @Override
    public void drawPoint(Graphics2D g, double x, double y) {
        System.out.println("point at ["+x+","+y+"]");
        if (adapter.getVisibilityGraphDataPoints().containsKey(x)
                && adapter.getVisibilityGraphDataPoints().get(x) == y) {
            g.setColor(colors[0]);
            ornaments[1].draw(g, g2cx(x), g2cy(y));
//            System.out.println("RMS SIZE IS "+adapter.getRms().size()+" "+adapter.getRms().firstKey()+ " "+adapter.getRms().firstEntry());
            if (adapter.getRms().containsKey(x)) {
                drawRms(g, g2cx(x), g2cy(y - adapter.getRms().get(x) * getSigma() / 2), g2cy(y + adapter.getRms().get(x) * getSigma() / 2));
                System.out.println("RMS ["+x+","+adapter.getRms().get(x));
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
            if(Math.abs(key)>40)
                continue;
            g.setColor(Color.BLACK);
            g.drawLine(g2cx(previousKey),
                    g2cy(getPoints().get(previousKey)),
                    g2cx(key),
                    g2cy(getPoints().get(key)));
            previousKey = key;
            drawPoint(g, key, getPoints().get(key));
            System.out.println(key + " : " + adapter.getRms().get(key));
            if (adapter.getRms().containsKey(key)) { 
                drawRms(g, g2cx(key), g2cy(getPoints().get(key) - adapter.getRms().get(key) * getSigma() / 2), g2cy(getPoints().get(key) + adapter.getRms().get(key) * getSigma() / 2));
                System.out.println("RMS ["+key+","+adapter.getRms().get(key));
            }
        }
    }

    public void drawRms(Graphics2D g, int x, int y1, int y2) {
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
