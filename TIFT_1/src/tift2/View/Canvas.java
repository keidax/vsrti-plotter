package tift2.View;

import common.View.CommonTIFTCanvas;
import tift2.Model.Adapter;

import java.awt.event.MouseEvent;
import java.util.TreeMap;

/**
 * 
 * @author Karel Durktoa and Adam Pere
 * 
 */
@SuppressWarnings("serial")
public abstract class Canvas extends CommonTIFTCanvas {
    
    public View view;
    public Adapter adapter;
    
    public Canvas(View v, Adapter a, TreeMap<Double, Double> g) {
        
        super(a, g);
        setAdapter(a);
        setView(v);
        
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
    
    /**
     * records where mouse was pressed and whether there is any point in less
     * distance then MyCanvas.r
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() != MouseEvent.BUTTON1) {
            return;
        }
        if (e.getButton() == MouseEvent.BUTTON2) {
            if (getPointOnGraph(e.getX(), e.getY()) != null) {
                setCurrentPoint(getPointOnGraph(e.getX(), e.getY()));
            } else {
                setCurrentPoint(null);
            }
        } else {
            if (getVerticallyPointOnGraph(e.getX(), e.getY()) != null) {
                setCurrentPoint(getVerticallyPointOnGraph(e.getX(), e.getY()));
            } else {
                setCurrentPoint(null);
            }
        }
        if (getCurrentPoint() != null) {
            double toy = Math.min(Math.max(tCanvasPadding, e.getY()), getHeight() - bCanvasPadding);
            adapter.moveVisibilityPoint(getCurrentPoint(), c2gy(toy));
        }
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {

        if (e.getButton() != MouseEvent.BUTTON2) {
            if (getVerticallyPointOnGraph(e.getX(), e.getY()) != null) {
                setCurrentPoint(getVerticallyPointOnGraph(e.getX(), e.getY()));
            } else {
                setCurrentPoint(null);
            }
        }
        
        if (getCurrentPoint() != null) {
            double toy = Math.min(Math.max(tCanvasPadding, e.getY()), getHeight() - bCanvasPadding);
            adapter.moveVisibilityPoint(getCurrentPoint(), c2gy(toy));
        }
    }
}
