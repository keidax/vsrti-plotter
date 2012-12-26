package fft.View;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;

public interface ViewListener {
	public void setLambda(double lambda);
	public void setDeltaBaseline(double delta);
	public void reset();
	public void fullReset();
	public void importVisibilityGraphPoints(TreeMap<Double, Double> parseFile);
    public void importVisibilityGraphRms(TreeMap<Double, Double> parseData);
	public void setNoise(double noise);
	public void setExponent(int tempExponent);
	public void writeSaveFile(File f);
	public void moveVisibilityPoint(double currentPoint, double toy);
	public void removeRmsPoint(double x);
}
