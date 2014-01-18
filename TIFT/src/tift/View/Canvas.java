package tift.View;

import common.View.CommonTIFTCanvas;
import common.View.SquareOrnament;
import tift.Model.Adapter;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Karel Durktoa and Adam Pere
 */
@SuppressWarnings("serial")
public abstract class Canvas extends CommonTIFTCanvas {

    public View view;
    public Adapter adapter;
    final boolean amp;
    Canvas canvas;

    public Canvas(View v, Adapter a, TreeMap<Double, Double> g, boolean amplitude) {
        super(a, g);
        setAdapter(a);
        setView(v);

        amp = amplitude;

    }

    // GETTERS AND SETTERS
    public Adapter getAdapter() {
        return adapter;
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    @Override
    public double getMaxYPoint() {
        if (getPoints() == null || getPoints().size() == 0) {
            return defaultY;
        }
        double max = getPoints().firstEntry().getValue();
        for (Map.Entry<Double, Double> entry : getPoints().entrySet()) {
            if (max < entry.getValue())
                max = entry.getValue();
        }
        if (max < defaultY) {
            return defaultY;
        }
        return max * 1.1;
    }

    @Override
    public double getMinYPoint() {
        if (getPoints() == null || getPoints().size() == 0) {
            return -defaultY;
        }
        double min = getPoints().firstEntry().getValue();
        for (Map.Entry<Double, Double> entry : getPoints().entrySet()) {
            if (min > entry.getValue())
                min = entry.getValue();
        }
        if (min > -defaultY) {
            return -defaultY;
        }
        return min * 1.1;
    }

    @Override
    public void drawDataSet(Graphics2D g) {
        if (getPoints() == null || getPoints().size() == 0) {
            return;
        }
        g.setStroke(new BasicStroke(strokeSize / 2));
        g.setColor(Color.RED);
        Set<Double> keys = getPoints().keySet();
        Double previousKey = getPoints().firstKey();
        for (Double key : keys) {
            if (key >= getMinX() && key <= getMaxX()) {
                new SquareOrnament(getOrnamentSize(key)).draw(g, g2cx(key), g2cy(getPoints().get(key)));
                g.drawLine(g2cx(previousKey), g2cy(getPoints().get(previousKey)), g2cx(key), g2cy(getPoints().get(key)));
            }
            previousKey = key;
        }
    }

    /**
     * records where mouse was pressed and whether there is any point in less
     * distance then MyCanvas.r
     */
    @Override
    public void mousePressed(MouseEvent e) {
        mouseButton = e.getButton();
        if (e.getButton() != MouseEvent.BUTTON1) {
            return;
        }

        if (getVerticallyPointOnGraph(e.getX(), e.getY()) != null) {
            setCurrentPoint(getVerticallyPointOnGraph(e.getX(), e.getY()));
        } else {
            setCurrentPoint(null);
        }

        if (getCurrentPoint() != null) {
            double toy = Math.min(Math.max(tPad, e.getY()), getHeight() - bPad);
            if (amp) {
                adapter.moveVisibilityPoint(getCurrentPoint(), c2gy(toy));
            } else {
                adapter.moveVisibilityPoint2(getCurrentPoint(), c2gy(toy));
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (mouseButton != MouseEvent.BUTTON1) {
            return;
        }
        if (getVerticallyPointOnGraph(e.getX(), e.getY()) != null) {
            setCurrentPoint(getVerticallyPointOnGraph(e.getX(), e.getY()));
        } else {
            setCurrentPoint(null);
        }

        if (getCurrentPoint() != null) {
            double toy = Math.min(Math.max(tPad, e.getY()), getHeight() - bPad);
            if (amp) {
                adapter.moveVisibilityPoint(getCurrentPoint(), c2gy(toy));
            } else {
                adapter.moveVisibilityPoint2(getCurrentPoint(), c2gy(toy));
            }
        }
    }
}
