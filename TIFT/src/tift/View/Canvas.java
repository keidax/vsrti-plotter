package tift.View;

import java.awt.event.MouseEvent;
import java.util.TreeMap;

import tift.Model.Adapter;

import common.View.CommonTIFTCanvas;

/**
 * 
 * @author Karel Durktoa and Adam Pere
 * 
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
    
    /**
     * records where mouse was pressed and whether there is any point in less
     * distance then MyCanvas.r
     */
    @Override
    public void mousePressed(MouseEvent evt) {
        mouseButton = evt.getButton();
        if (evt.getButton() != MouseEvent.BUTTON1) {
            return;
        }
        mCanx = evt.getX();
        mCany = evt.getY();
        if (evt.getButton() == MouseEvent.BUTTON2) {
            if (getPointOnGraph(mCanx, mCany) != null) {
                setCurrentPoint(getPointOnGraph(mCanx, mCany));
            } else {
                setCurrentPoint(null);
            }
        } else {
            if (getVerticallyPointOnGraph(mCanx, mCany) != null) {
                setCurrentPoint(getVerticallyPointOnGraph(mCanx, mCany));
            } else {
                setCurrentPoint(null);
            }
        }
        if (getCurrentPoint() != null) {
            double toy = Math.min(Math.max(tPad, mCany), getHeight() - bPad);
            if (amp) {
                adapter.moveVisibilityPoint(getCurrentPoint(), c2gy(toy));
            } else {
                adapter.moveVisibilityPoint2(getCurrentPoint(), c2gy(toy));
            }
        }
    }
    
    @Override
    public void mouseDragged(MouseEvent evt) {
        if (mouseButton != MouseEvent.BUTTON1) {
            return;
        }
        mCanx = evt.getX();
        mCany = evt.getY();
        if (evt.getButton() != MouseEvent.BUTTON2) {
            if (getVerticallyPointOnGraph(mCanx, mCany) != null) {
                setCurrentPoint(getVerticallyPointOnGraph(mCanx, mCany));
            } else {
                setCurrentPoint(null);
            }
        }
        
        if (getCurrentPoint() != null) {
            double toy = Math.min(Math.max(tPad, mCany), getHeight() - bPad);
            if (amp) {
                adapter.moveVisibilityPoint(getCurrentPoint(), c2gy(toy));
            } else {
                adapter.moveVisibilityPoint2(getCurrentPoint(), c2gy(toy));
            }
        }
    }
}
