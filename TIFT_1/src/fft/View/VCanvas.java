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
        dataPoints = this.adapter.getVisiblityGraphPoints();
        xAxis = "time (s)";
        yAxis = "Amplitude";
        graphTitle = "f(t)";
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
            if (key > Double.parseDouble(View.view.fMaxTime.getText()))  {
                break;
            }
        }

    }
    
    @Override
    public Double getMaxYPoint() {
        if (dataPoints.size() == 0) {
            return null;
        }
        double max = this.dataPoints.firstEntry().getValue();
        Set<Double> keys = this.dataPoints.keySet();
        for (Double key : keys) {
            if (this.dataPoints.get(key) > max) {
                max = this.dataPoints.get(key);
            }
            if (key > Double.parseDouble(View.view.fMaxTime.getText())) {
                break;
            }
        }
        if (max < 2.0) {
            return 2.0;
        }
        return max;
    }
    
    @Override
    public Double getMinYPoint() {
        if (dataPoints.size() == 0) {
            return (double) -Canvas.defaultY;
        }
        double min = this.dataPoints.firstEntry().getValue();
        Set<Double> keys = this.dataPoints.keySet();
        for (Double key : keys) {
        	if (this.dataPoints.get(key) < min) {
                min = this.dataPoints.get(key);
            }
            if (key > Double.parseDouble(View.view.fMaxTime.getText()))  {
                break;
            }
        }
        return min;
    }
    
    @Override
    public double getMaxX() {
        //show only half of the points because any points after nyquist frequency/2 are useless
        if (dataPoints == null || dataPoints.size() == 0) {
            return defaultY;
        }
        Set<Double> keys = dataPoints.keySet();
        for (Double key : keys) {
            if (key > Double.parseDouble(View.view.fMaxTime.getText()))  {
                return key;
            }
        	
        }
        return this.dataPoints.lastKey();
    }

    @Override
    public void drawPoint(Graphics2D g, double x, double y) {

        //System.out.println("RMS = "+adapter.getRms().size());
        if (adapter.getVisibilityGraphDataPoints().containsKey(x)
                && adapter.getVisibilityGraphDataPoints().get(x) == y) {
            g.setColor(colors[0]);
            ornaments[1].draw(g, g2cx(x), g2cy(y));
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

    public void drawRms(Graphics2D g, int x, int y1, int y2) {
        //height*=10;
        //System.out.println("draw:["+x+","+y1+"-"+y2+"]");
        g.drawLine(x, y1, x, y2);
        g.drawLine(x - 1, y1, x + 1, y1);
        g.drawLine(x - 1, y2, x + 1, y2);
    }

    public void update() {
    	dataPoints = this.adapter.getVisiblityGraphPoints();
    }

    @Override
    public void update(Graphics g) {
    	dataPoints = this.adapter.getVisiblityGraphPoints();
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
