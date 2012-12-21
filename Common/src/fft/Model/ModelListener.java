package fft.Model;

import java.util.TreeMap;

/**
 * Interface for views on the model
 *
 */

public interface ModelListener {

    public void update(TreeMap<Double, Double> points, TreeMap<Double, Double> rmsPoints);
}
