package tift.Model;

import common.Model.Complex;
import common.Model.FFT;

/**
 * This handles the Frequency Domain Graphs
 *
 * @author Karel Durktoa and Adam Pere
 */
public class ImageGraph extends Graph {

    public Complex[] compl;
    public boolean polar;

    /**
     * Non-Default Constructor
     *
     * @param m - model instance
     */
    public ImageGraph(Model m) {
        super(m);
        polar = true;

    }

    /**
     * Set the array of Complex Numbers to a new array of Complex Numbers.
     *
     * @param numbers - new array of Complex Numbers
     */
    public void setComplex(Complex[] numbers) {
        compl = numbers;
    }

//    /**
//     * Set the instance variable polar to true or false. Polar determines
//     * whether or not the graph is plotting magnitude/phase (polar form of
//     * complex numbers) or real and imaginary numbers (rectangular form of
//     * complex numbers)
//     *
//     * @param radio - Plot polar or rectangular form of complex numbers
//     */
//    public void setPolar(boolean radio) {
//        polar = radio;
//        Object[] keys = getPoints().keySet().toArray();
//
//        for (int i = 0; i < keys.length; i++) {
//            if (polar) {
//                getPoints().put(Double.parseDouble(keys[i].toString()), compl[i].abs());
//                getPoints2().put(Double.parseDouble(keys[i].toString()), compl[i].phase());
//            } else {
//
//                getPoints().put(Double.parseDouble(keys[i].toString()), compl[i].re());
//                getPoints2().put(Double.parseDouble(keys[i].toString()), compl[i].im());
//            }
//        }
//        model.updateListeners();
//    }

    /**
     * This handles the creating of the Visibility Graph (Time Domain Graphs).
     * It does so by taking the ifft of the array of Complex Numbers and setting
     * the points in the Visibility Graph accordingly.
     */
    public void createVisibilityGraph() {
        Complex[] res = FFT.fft(compl);
        model.visibilityGraph.setComplex(res);

        model.visibilityGraph.getPoints().clear();
        model.visibilityGraph.getPoints2().clear();

        for (int i = 0; i < res.length; i++) {
            if (polar) {
                model.visibilityGraph.getPoints().put(i * getDeltaBaseline(), res[i].abs());
                model.visibilityGraph.getPoints2().put(i * getDeltaBaseline(), res[i].phase());
            } else {
                model.visibilityGraph.getPoints().put(i * getDeltaBaseline(), res[i].re());
                model.visibilityGraph.getPoints2().put(i * getDeltaBaseline(), res[i].im());
            }
        }
    }

    /**
     * Updates the model listeners
     */
    public void update() {
        model.updateListeners();
    }

    /**
     * Changes the y-value of a point. This is called when a point is clicked or
     * dragged on the graph. This handles the real/magnitude graphs in the
     * Frequency Domain.
     *
     * @param currentPoint x value of point
     * @param toy          new y value.
     */
    public void movePoint(double currentPoint, double toy) {
        int n = convertXPointToIndex(currentPoint);
        if (polar) {
            if (toy != compl[n].abs()) {
                compl[n] = new Complex(toy * Math.cos(compl[n].phase()), toy * Math.sin(compl[n].phase()));
                getPoints2().put(currentPoint, compl[n].phase());
                getPoints().put(currentPoint, compl[n].abs());
            }
        } else {
            if (toy != compl[n].re()) {
                compl[n] = new Complex(toy, compl[n].im());
                getPoints2().put(currentPoint, compl[n].im());
                getPoints().put(currentPoint, compl[n].re());
            }
        }
        createVisibilityGraph();
        update();
    }

    /**
     * Changes the x-value of a point. This is called when a point is clicked or
     * dragged on the graph. This handles the imaginary/phase graphs in the
     * Frequency Domain.
     *
     * @param currentPoint
     * @param toy          - new x value.
     */
    public void movePoint2(double currentPoint, double toy) {
        int n = convertXPointToIndex(currentPoint);
        if (polar) {
            if (toy != compl[n].phase()) {
                compl[n] = new Complex(compl[n].abs() * Math.cos(toy), compl[n].abs() * Math.sin(toy));
                getPoints2().put(currentPoint, compl[n].phase());
                getPoints().put(currentPoint, compl[n].abs());
            }

        } else {
            if (toy != compl[n].re()) {
                compl[n] = new Complex(compl[n].re(), toy);
                getPoints2().put(currentPoint, compl[n].im());
                getPoints().put(currentPoint, compl[n].re());
            }
        }
        createVisibilityGraph();
        update();
    }

    /**
     * @param point the x-point on the graph, either positive or negative
     * @return the corresponding index in the array of FFT values, as a positive
     */
    private int convertXPointToIndex(double point) {
        System.out.println("movePoint: currentPoint = " + point);
        int n = 0; //= (int) (currentPoint * getDeltaBaseline() * getPoints().size());
        if (point > 0) {
            n = (int) Math.ceil(point * getDeltaBaseline() * getPoints().size());
        } else if (point < 0) {
            n = (int) Math.floor(point * getDeltaBaseline() * getPoints().size()) + getPoints().size() + 1;

        }

        System.out.println("n as double = " + point * getDeltaBaseline() * getPoints().size());
        System.out.println("n = " + n);
        return n;
    }
}
