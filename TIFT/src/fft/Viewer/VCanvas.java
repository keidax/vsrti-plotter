package fft.Viewer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Set;
import java.util.TreeMap;

import fft.Model.Adapter;

/**
 * 
 * @author Karel Durktoa and Adam Pere
 *
 */
public class VCanvas extends Canvas {

    protected TreeMap<Double, Double> dataPoints;
    protected static AbstractOrnament[] ornaments = {new CircleOrnament(), new SquareOrnament()};
    protected static Color[] colors = {Color.BLUE, Color.BLACK};
    protected int sigma = 1;
    final boolean amp;

    public VCanvas(Viewer v, Adapter a, TreeMap<Double, Double> g, String yaxis, String title, boolean amplitude) {
        super(v, a, g, amplitude);
        dataPoints = g;
        System.out.println(dataPoints.size());
        xAxis = "time (s)";
        yAxis = yaxis;
        graphTitle = title;
        amp = amplitude;
    }
    
    public void setYAxis(String axis)
    {
    	yAxis = axis;
    }
    
    public void setTitle(String t)
    {
    	graphTitle = t;
    }
    @Override
    public void drawPoint(Graphics2D g, double x, double y) {
    	
        if (dataPoints.containsKey(x) && dataPoints.get(x) == y) {
            g.setColor(colors[1]);
            ornaments[1].draw(g, g2cx(x), g2cy(y));
            if (adapter.getRms().containsKey(x)) {
                drawRms(g, g2cx(x), g2cy(y - adapter.getRms().get(x) * getSigma() / 2), g2cy(y + adapter.getRms().get(x) * getSigma() / 2));
            }
        } else {
            g.setColor(colors[1]);
            ornaments[0].draw(g, g2cx(x), g2cy(y));
        }
    }
    
    @Override
    public void drawDataSet(int count, Graphics2D g) {
        if (dataPoints.size() == 0) {
            return;
        }
        Set<Double> keys = this.dataPoints.keySet();
        Double previousKey = this.dataPoints.firstKey();
        for (Double key : keys) {
        	if(key>getMinX())
           {
        		new SquareOrnament().draw(g, g2cx(key), g2cy(this.dataPoints.get(key)));
        		g.drawLine(g2cx(previousKey), g2cy(dataPoints.get(previousKey)), g2cx(key), g2cy(dataPoints.get(key)));
        		previousKey = key;
           }
            
            if (key > Double.parseDouble(Viewer.viewer.fMaxTime.getText()))  {
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
            if (key > Double.parseDouble(Viewer.viewer.fMaxTime.getText())) {
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
            if (key > Double.parseDouble(Viewer.viewer.fMaxTime.getText()))  {
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
            if (key > Double.parseDouble(Viewer.viewer.fMaxTime.getText()))  {
                return key;
            }
        	
        }
        return this.dataPoints.lastKey();
    }


    public void drawRms(Graphics2D g, int x, int y1, int y2) {
        g.drawLine(x, y1, x, y2);
        g.drawLine(x - 1, y1, x + 1, y1);
        g.drawLine(x - 1, y2, x + 1, y2);
    }

    public void update() {
    	if(amp)
    	{
    		dataPoints = this.adapter.getVisibilityGraphDataPoints();
    	}
    	else
    	{
    		dataPoints = this.adapter.getVisibilityGraphDataPoints2();
    	}
    }

    @Override
    public void update(Graphics g) {
        if(amp)
        {
        	dataPoints = adapter.getVisibilityGraphDataPoints();
        }
        else
        {
        	dataPoints = this.adapter.getVisibilityGraphDataPoints2();
        }
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
