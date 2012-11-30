package fft.Viewer;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.Set;
import java.util.TreeMap;

import fft.Model.Adapter;

public class FFTCanvas extends Canvas { //JPanel implements MouseListener, MouseMotionListener {

    public FFTCanvas(Viewer v, Adapter a, TreeMap<Double, Double> g) {
        super(v, a, g);
        xAxis = "frequency (Hz)";
        yAxis = "Amplitude";
        graphTitle = "F(ν)";
    }

    @Override
    public void drawDataSet(int count, Graphics2D g) {
        if (getPoints().size() == 0) {
            return;
        }
        Set<Double> keys = this.getPoints().keySet();
        Double previousKey = this.getPoints().firstKey();
        for (Double key : keys) {
            new SquareOrnament().draw(g, g2cx(key), g2cy(this.getPoints().get(key)));
            g.drawLine(g2cx(previousKey),
                    g2cy(getPoints().get(previousKey)),
                    g2cx(key),
                    g2cy(getPoints().get(key)));
            previousKey = key;
            if (key > Double.parseDouble(Viewer.viewer.fThetaMax.getText())) {
                break;
            }
        }
    }

    @Override
    public Double getMaxYPoint() {
        if (getPoints().size() == 0) {
            return null;
        }
        double max = this.getPoints().firstEntry().getValue();
        Set<Double> keys = this.getPoints().keySet();
        for (Double key : keys) {
            if (this.getPoints().get(key) > max) {
                max = this.getPoints().get(key);
            }
            if (key > Double.parseDouble(Viewer.viewer.fThetaMax.getText())) {
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
        if (getPoints().size() == 0) {
            return (double) -Canvas.defaultY;
        }
        double min = this.getPoints().firstEntry().getValue();
        Set<Double> keys = this.getPoints().keySet();
        for (Double key : keys) {
            if (this.getPoints().get(key) < min) {
                min = this.getPoints().get(key);
            }
            if (key > Double.parseDouble(Viewer.viewer.fThetaMax.getText())) {
                break;
            }
        }
        return min;
    }

    @Override
    public double getMaxX() {
        //show only half of the points;
        if (getPoints() == null || getPoints().size() == 0) {
            return defaultY;
        }
        Set<Double> keys = getPoints().keySet();
        for (Double key : keys) {
            if (key >= Double.parseDouble(Viewer.viewer.fThetaMax.getText())) {
                return key;
            }
        }
        return this.getPoints().lastKey();//this.getPoints().ceilingKey(this.getPoints().lastKey()/2);
    }

    /**
     * records where mouse was pressed and weather there is any point in less distance then
     * MyCanvas.r
     */
    @Override
    public void mousePressed(MouseEvent evt) {
        //this.getVision().getCare().set(this.getVision().getSoul().newMemento());
        mCanx = evt.getX();
        mCany = evt.getY();
        if (getVerticallyPointOnGraph(mCanx, mCany) != null) {
            setCurrentPoint(getVerticallyPointOnGraph(mCanx, mCany));
        } else {
            setCurrentPoint(null);
        }

        if (getCurrentPoint() != null) {
            double toy = Math.min(Math.max(tPad, mCany), getHeight() - bPad);
            //System.out.println("moving to ["+tox+","+toy+"]");
            this.adapter.moveImagePoint(getCurrentPoint(), c2gy(toy));
            //getCurrentPoint().movePoint(getCurrentPoint(), c2gx(tox),c2gy(toy,getCurrentPoint().getDataSet()));
        }
        //System.out.println("Current point is "+currentPoint);
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
            double tox = Math.min(Math.max(getLeftShift(), mCanx), getLeftShift() + getPlotWidth());
            double toy = Math.min(Math.max(tPad, mCany), getHeight() - bPad);
            //System.out.println("moving to ["+tox+","+toy+"]");
            this.adapter.moveImagePoint(getCurrentPoint(), c2gy(toy));
        }
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }
}
