package common.Model;

import java.io.File;
import java.util.List;
import java.util.TreeMap;

import common.View.InputFile;


public interface ModelInterface {
    
    /**
     * Add a ModelListener (so, a view) to be notified of changes in this model
     * 
     * @param listener
     *            the ModelListener to be added
     */
    public void addListener(ModelListener listener);
    
    /**
     * Tells the model to update all its listeners
     */
    public void update();
    
    /**
     * Reload points from their source
     */
    public void reinitialize();
    
    public TreeMap<Double, Double> getPoints();
    
    /**
     * Import data points from a file
     * 
     * @param parseFile
     */
    public void importPoints(TreeMap<Double, Double> parseFile);
    
    public TreeMap<Double, Double> getRms();
    
    /**
     * Import RMS data from a file
     * 
     * @param parseData
     */
    public void importRms(TreeMap<Double, Double> parseData);
    
    public int getExponent();
    
    public void setExponent(int e);
    
    public double getNoise();
    
    public void setNoise(double noise);
    
    public String getSaveFilename();
    
    public void setSaveFile(File f);
    
    public double getLambda();
    
    public void setLambda(double l);
    
    public double getDeltaBaseline();
    
    public void setDeltaBaseline(double d);
    
    /**
     * Import a set of InputFiles, and discard previous sources
     * 
     * @param f
     */
    public void setRawPoints(List<InputFile> f);
    
    /**
     * Clear data sources
     */
    public void emptyRawPoints();
    
    /**
     * Remove the RMS for a specific point
     * 
     * @param x
     */
    public void removeRmsPoint(double x);
    
    /**
     * Return the points, in a String representation. Useful for making a save
     * file.
     * 
     * @return a single String containing the model's data
     */
    public String exportPoints();
    
    public void movePoint(double currentPoint, double toy);
}
