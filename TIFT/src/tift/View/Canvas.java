package tift.View;

import common.View.CommonTIFTCanvas;
import common.View.SquareOrnament;
import tift.Model.Adapter;

import java.awt.*;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Karel Durktoa
 * @author Adam Pere
 * @author Gabriel Holodak
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
    public void drawDataSet(Graphics2D g) {
        if (getPoints() == null || getPoints().size() == 0) {
            return;
        }
        g.setStroke(new BasicStroke(strokeSize / 2));
        g.setColor(Color.RED);
        Set<Double> keys = getPoints().keySet();
        Double previousKey = getPoints().firstKey();
        int a = g2cx(getPoints().firstKey()), b = g2cy(getPoints().firstEntry().getValue()), c, d;
        for (Double key : keys) {
            if (key >= getMinX() && key <= getMaxX()) {
                new SquareOrnament(getOrnamentSize(key)).draw(g, g2cx(key), g2cy(getPoints().get(key)));
                c = g2cx(key);
                d = g2cy(getPoints().get(key));
                if (previousKey >= getMinX()) {
                    g.drawLine(a, b, c, d);
                }
                // remember these values, so we don't need to do the calculations twice
                a = c;
                b = d;
                previousKey = key;
            }
        }
    }
}







