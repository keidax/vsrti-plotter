package fft.Model;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

import fft.Viewer.InputFile;

public class PFPAdapter extends Adapter {
	
    public PFPAdapter(Model m) {
        super(m);
    }

    public TreeMap<Double, Double> getRms() {
        return getModel().getVisibilityGraph().getRms();
    }

    public void moveVisibilityPoint(double currentPoint, double toy) {
        this.getModel().getVisibilityGraph().movePoint(currentPoint, toy);
    }

    public void moveImagePoint(Double currentPoint, double toy) {
        this.getModel().getImageGraph().movePoint(currentPoint, toy);
    }

    public void importVisibilityGraphPoints(TreeMap<Double, Double> parseFile) {
        this.getModel().getVisibilityGraph().importPoints(parseFile);
    }

    public void importVisibilityGraphRms(TreeMap<Double, Double> parseData) {
        getModel().getVisibilityGraph().importRms(parseData);
    }

    public String exportVisibilityGraphPoints() {
        return this.getModel().getVisibilityGraph().toString();
    }

    public void removeRmsPoint(double x) {
        getModel().getVisibilityGraph().removeGridedRmsPoint(x);
    }
}
