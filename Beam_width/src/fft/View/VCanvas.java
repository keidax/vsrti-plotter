package fft.View;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Set;
import java.util.TreeMap;

/**
 * Specific implementation of Beam graph
 * 
 */

@SuppressWarnings("serial")
public class VCanvas extends Canvas {// JPanel implements MouseListener,
                                     // MouseMotionListener {

    // protected TreeMap<Double, Double> dataPoints;
    protected TreeMap<Double, Double> rmsPoints;
    protected AbstractOrnament[] ornaments = { new CircleOrnament(),
            new SquareOrnament() };
    protected Color[] colors = { Color.BLUE, Color.BLACK };
    protected int sigma = 1;
    
    public VCanvas(View v, TreeMap<Double, Double> g) {
        super(v, g);
        // dataPoints = this.adapter.getVisibilityGraphDataPoints();
        rmsPoints = new TreeMap<Double, Double>();
        // dataPoints = new TreeMap<Double, Double>();
        // System.out.println(dataPoints.size());
        xAxisTitle = "Angle (degrees)";
        yAxisTitle = "Power";
        graphTitle = "Beam";
    }
    
    @Override
    public void drawPoint(Graphics2D g, double x, double y) {
        // System.out.println("point at ["+x+","+y+"]");
        // System.out.println("RMS = "+rmsPoints.size());
        /*
         * if (dataPoints.containsKey(x) && dataPoints.get(x) == y) {
         */// TODO not sure what the point of dataPoints is...
        g.setColor(colors[0]);
        ornaments[1].draw(g, g2cx(x), g2cy(y));
        System.out.println("***" + x);
        if (rmsPoints.containsKey(x)) {
            // System.out.println("g2cy(y + Sigma*rms) "+g2cy(y+getSigma()*rmsPoints.get(x)));
            drawRms(g, g2cx(x), g2cy(y - rmsPoints.get(x) * getSigma() / 2),
                    g2cy(y + rmsPoints.get(x) * getSigma() / 2));
            // System.out.println("RMS ["+x+","+rmsPoints.get(x));
            // System.out.println("rms = "+rmsPoints.get(x));
        }
        /*
         * } else { g.setColor(colors[1]); ornaments[0].draw(g, g2cx(x),
         * g2cy(y)); }
         */
    }
    
    @Override
    public void drawDataSet(int count, Graphics2D g) {
        if (points.size() == 0) {
            return;
        }
        Set<Double> keys = points.keySet();
        Double previousKey = points.firstKey();
        for (Double key : keys) {
            g.setColor(Color.BLACK);
            g.drawLine(g2cx(previousKey), g2cy(points.get(previousKey)),
                    g2cx(key), g2cy(points.get(key)));
            previousKey = key;
            drawPoint(g, key, points.get(key));
            System.out.println("** " + key);
            if (rmsPoints.containsKey(key)) {
                // System.out.println("g2cy(y + Sigma*rms) "+g2cy(y+getSigma()*rmsPoints.get(x)));
                drawRms(g, g2cx(key), g2cy(points.get(key) - rmsPoints.get(key)
                        * getSigma() / 2),
                        g2cy(points.get(key) + rmsPoints.get(key) * getSigma()
                                / 2));
                // new SquareOrnament().draw(g,
                // g2cx(key),g2cy(this.points.get(key)));
                // System.out.println("Drawing point at ["+key+","+this.points.get(key)+"]");
                // System.out.println("Draw a point ["+p.getX()+","+p.getY()+"] at ["+g2cx(p.getX())+","+g2cy(p.getY(),ds)+"]");
            }
        }
        
    }
    
    public void drawRms(Graphics2D g, int x, int y1, int y2) {
        // height*=10;
        // System.out.println("draw:["+x+","+y1+"-"+y2+"]");
        g.drawLine(x, y1, x, y2);
        g.drawLine(x - 1, y1, x + 1, y1);
        g.drawLine(x - 1, y2, x + 1, y2);
    }
    
    public void update(TreeMap<Double, Double> points,
            TreeMap<Double, Double> rmsPoints) {
        this.points = points;
        // this.dataPoints = dataPoints;
        this.rmsPoints = rmsPoints;
    }
    
    @Override
    public void update(Graphics g) { // TODO is this necessary?...
    // dataPoints = adapter.getVisibilityGraphDataPoints();
        paint(g);
    }
    
    public int getSigma() {
        return sigma;
    }
    
    public void setSigma(int i) {
        sigma = i;
    }
    
    @Override
    public void setCurrentPoint(Double currentPoint) {
        try {
            view.removeRmsPoint(currentPoint); // TODO find out if RMS should
                                               // disappear like this
        } catch (NullPointerException e) {
        }
        this.currentPoint = currentPoint;
    }
}
