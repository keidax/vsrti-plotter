package fft.Viewer;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.Set;
import java.util.TreeMap;

import fft.Model.Adapter;

/**
 * 
 * @author Karel Durktoa and Adam Pere
 *
 */
public class FFTCanvas extends Canvas {

	protected boolean amp;
	protected TreeMap<Double, Double> dataPoints;
	
    public FFTCanvas(Viewer v, Adapter a, TreeMap<Double, Double> g, String yaxis, String title, boolean amplitude) {
        super(v, a, g, amplitude);
        xAxis = "frequency (Hz)";
        yAxis = yaxis;
        graphTitle = title;
        amp = amplitude;
        dataPoints = g;
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
    public void drawDataSet(int count, Graphics2D g) {
        if (dataPoints.size() == 0) {
            return;
        }
        Set<Double> keys = this.dataPoints.keySet();
        Double previousKey = this.dataPoints.firstKey();
        for (Double key : keys) {
           if(key >= Double.parseDouble(Viewer.viewer.fThetaMin.getText()))
        	{
        	   new SquareOrnament().draw(g, g2cx(key-getMinX()), g2cy(this.dataPoints.get(key)));
        	   g.drawLine(g2cx(previousKey-getMinX()),
                    g2cy(dataPoints.get(previousKey)),
                    g2cx(key-getMinX()),
                    g2cy(dataPoints.get(key)));
            previousKey = key;
            if (key > Double.parseDouble(Viewer.viewer.fThetaMax.getText()) || key > 1/(2*Double.parseDouble(Viewer.viewer.fDelta.getText())))  {
                break;
            }
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
            if(key > Double.parseDouble(Viewer.viewer.fThetaMin.getText()) )
        	{
            	if (this.dataPoints.get(key) > max) 
            	{
            		max = this.dataPoints.get(key);
            	}
            if (key > Double.parseDouble(Viewer.viewer.fThetaMax.getText()) || key > 1/(2*Double.parseDouble(Viewer.viewer.fDelta.getText()))) {
                break;
            }
        }}

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
        	if(key > Double.parseDouble(Viewer.viewer.fThetaMin.getText()) )
            {if (this.dataPoints.get(key) < min) {
                min = this.dataPoints.get(key);
            }
            if (key > Double.parseDouble(Viewer.viewer.fThetaMax.getText()) || key > 1/(2*Double.parseDouble(Viewer.viewer.fDelta.getText())))  {
                break;
            }
        }}
        return min;
    }

    @Override
    public double getMaxX() {
        //show only half of the points because any points after nyquist frequency/2 are redundant
        if (dataPoints == null || dataPoints.size() == 0) {
            return defaultY;
        }
        Set<Double> keys = dataPoints.keySet();
        for (Double key : keys) {
        	if(key > Double.parseDouble(Viewer.viewer.fThetaMin.getText()) )
        	{
            if (key > Double.parseDouble(Viewer.viewer.fThetaMax.getText()) || key > 1/(2*Double.parseDouble(Viewer.viewer.fDelta.getText())))  {
                return key;
            }
        	}
        }
        return this.dataPoints.lastKey();
    }
    
   

    /**
     * records where mouse was pressed and weather there is any point in less distance then
     * MyCanvas.r
     */
    @Override
    public void mousePressed(MouseEvent evt) {
        mCanx =evt.getX();
        mCany = evt.getY();
        if (getVerticallyPointOnGraph(mCanx, mCany) != null) {
            setCurrentPoint(getVerticallyPointOnGraph(mCanx, mCany));
        } else {
            setCurrentPoint(null);
        }
        if (getCurrentPoint() != null) {
            double toy = Math.min(Math.max(tPad, mCany), getHeight() - bPad);
           if(amp)
        	   this.adapter.moveImagePoint(getCurrentPoint(), c2gy(toy));
           else
        	   this.adapter.moveImagePoint2(getCurrentPoint(), c2gy(toy));
        }
    }

    @Override
    public void mouseDragged(MouseEvent evt) {
        mCanx = evt.getX();
        mCany = evt.getY();
        if (getVerticallyPointOnGraph(mCanx, mCany) != null) {
            setCurrentPoint(getVerticallyPointOnGraph(mCanx, mCany));
        } else {
            setCurrentPoint(null);
        }

        if (getCurrentPoint() != null) {
            double toy = Math.min(Math.max(tPad, mCany), getHeight() - bPad);
            if(amp)
            	this.adapter.moveImagePoint(getCurrentPoint(), c2gy(toy));
            else
            	this.adapter.moveImagePoint2(getCurrentPoint(), c2gy(toy));
        }
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
}
