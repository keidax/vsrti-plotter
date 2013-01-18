package common.Model;

import java.util.TreeMap;

/**
 * Interface for views on the model
 * 
 */

public interface ModelListener {
    
    public void updateView(TreeMap<Double, Double> points, TreeMap<Double, Double> rmsPoints);
}
