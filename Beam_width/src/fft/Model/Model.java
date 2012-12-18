package fft.Model;

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
        //System.out.println("Model is updating all listeners");
        for (ModelListener ml : listeners) {
            ml.update(visibilityGraph.getPoints(), visibilityGraph.getGridedRms());
        }
    }

    public Vector<ModelListener> getListeners() {
        return listeners;
    }

    public void setListeners(Vector<ModelListener> listeners) {
        this.listeners = listeners;
    }
    
    
    public void addListener(ModelListener listener){
    	listeners.add(listener);
    }
}
