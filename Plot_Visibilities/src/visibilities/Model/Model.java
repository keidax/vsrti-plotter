package visibilities.Model;

import java.util.Vector;

public class Model {
    
    private VisibilityGraph visibilityGraph;
    private Vector<ModelListener> listeners;
    
    public Model() {
        listeners = new Vector<ModelListener>();
        visibilityGraph = new VisibilityGraph(this);
    }
    
    public VisibilityGraph getVisibilityGraph() {
        return visibilityGraph;
    }
    
    public void setVisibilityGraph(VisibilityGraph visibilityGraph) {
        this.visibilityGraph = visibilityGraph;
    }
    
    public void updateListeners() {
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
