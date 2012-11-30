package fft.Model;

public class ImageGraph extends Graph {

  

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
        model.visibilityGraph.getPoints().clear();
        for (int i = 0; i < res.length; i++) {
            model.visibilityGraph.getPoints().put(i * getDeltaBaseline() / model.getVisibilityGraph().getLambda(), res[i]);
            
        }

        /*System.out.println(this.getPoints().size());
        Complex[] complex = new Complex[(int) Math.pow(2,Graph.getExponent())];

        Object[] keys = this.getPoints().keySet().toArray();
        for(int i=0;i<keys.length;i++){
        complex[i]=new Complex(getPoints().get((Double)keys[i]),compl[i].im());
        }
        Complex[] fftComplex = FFT.ifft(complex);
        //TreeMap<Double,Double> newMap = new TreeMap<Double,Double>();
        model.visibilityGraph.getPoints().clear();
        for(int i=0;i<fftComplex.length;i++){
        model.visibilityGraph.getPoints().put(
        (double)i*Graph.getDeltaBaseline()/model.getVisibilityGraph().getLambda(),
        fftComplex[i].re());
        //System.out.print(b)
        }
        model.visibilityGraph.compl=fftComplex;*/
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
