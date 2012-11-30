package fft.Model;

public class ImageGraph extends Graph {

    public Complex[] compl;

    public ImageGraph(Model m) {
        super(m);
        for (int i = 0; i < 20; i++) {
            getPoints().put((double) i, Math.sin((double) i / 2));
        }
    }

    public void createVisibilityGraph() {

        Object[] keys = this.getPoints().keySet().toArray();
        double[] data = new double[keys.length];
        for (int i = 0; i < data.length; i++) {
            data[i] = Math.signum(getPoints().get(keys[i])) * Math.pow(getPoints().get(keys[i]), 2);
        }
        Dct1d dct = new Dct1d(data.length);
        double[] res = dct.iDCT(data);
        model.getVisibilityGraph().getPoints().clear();
        for (int i = 0; i < res.length; i++) {
            model.getVisibilityGraph().getPoints().put(
                    i * getDeltaBaseline() / model.getVisibilityGraph().getLambda(), res[i]);
        }
    }

    public void update() {
        model.updateListeners();
    }

    public void movePoint(double currentPoint, double toy) {
        this.getPoints().put(currentPoint, toy);
        this.createVisibilityGraph();
        update();
    }
}
