package visibilities.Model;

import java.util.Vector;

public class Model {
    
    private VisibilityGraph visibilityGraph;
    // private ImageGraph imageGraph;
    private Vector<ModelListener> listeners;
    
    public Model() {
        listeners = new Vector<ModelListener>();
        // imageGraph = new ImageGraph(this);
        visibilityGraph = new VisibilityGraph(this);
    }
    
    public VisibilityGraph getVisibilityGraph() {
        return visibilityGraph;
    }
    
    public void setVisibilityGraph(VisibilityGraph visibilityGraph) {
        this.visibilityGraph = visibilityGraph;
    }
    
    /*
     * public ImageGraph getImageGraph() {
     * return imageGraph;
     * }
     */
    
    public void updateListeners() {
        // System.out.println("Model is updating all listeners");
        for (ModelListener ml : listeners) {
            ml.update();
        }
    }
    
    public Vector<ModelListener> getListeners() {
        return listeners;
    }
    
    public void setListeners(Vector<ModelListener> listeners) {
        this.listeners = listeners;
    }
    
    public void resetValuesToDefaults() {
        visibilityGraph.lambda = visibilityGraph.defaultLambda;
    }
}
