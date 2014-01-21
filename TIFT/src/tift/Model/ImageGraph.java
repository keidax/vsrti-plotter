package tift.Model;

import common.Model.Complex;
import common.Model.FFT;

/**
 * This handles the Frequency Domain Graphs
 *
 * @author Karel Durktoa
 * @author Adam Pere
 * @author Gabriel Holodak
 */
public class ImageGraph extends Graph {

    private Complex[] complex;
    private boolean polar;

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
        complex = numbers;
    }

    /**
     * Set the instance variable polar to true or false. Polar determines
     * whether or not the graph is plotting magnitude/phase (polar form of
     * complex numbers) or real and imaginary numbers (rectangular form of
     * complex numbers)
     *
     * @param radio - Plot polar or rectangular form of complex numbers
     */
    public void setPolar(boolean radio) {
        polar = radio;
    }

    /**
     * This handles the creating of the Visibility Graph (Time Domain Graphs).
     * It does so by taking the inverse fft of the array of Complex Numbers and setting
     * the points in the Visibility Graph accordingly.
     */
    public void createVisibilityGraph() {
        Complex[] res = FFT.fft(complex);
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
     * @param currentPoint the x-value of the point to be changed
     * @param toy the new y value.
     */
    public void movePoint(double currentPoint, double toy) {
        int n = convertXPointToIndex(currentPoint);
        if (polar) {
            if (toy != complex[n].abs()) {
                complex[n] = new Complex(toy * Math.cos(complex[n].phase()), toy * Math.sin(complex[n].phase()));
                getPoints2().put(currentPoint, complex[n].phase());
                getPoints().put(currentPoint, complex[n].abs());
            }
        } else {
            if (toy != complex[n].re()) {
                complex[n] = new Complex(toy, complex[n].im());
                getPoints2().put(currentPoint, complex[n].im());
                getPoints().put(currentPoint, complex[n].re());
            }
        }
        createVisibilityGraph();
        update();
    }

    /**
     * Changes the y-value of a point. This is called when a point is clicked or
     * dragged on the graph. This handles the imaginary/phase graphs in the
     * Frequency Domain.
     *
     * @param currentPoint the x-value of the point to be changed
     * @param toy the new y value.
     */
    public void movePoint2(double currentPoint, double toy) {
        int n = convertXPointToIndex(currentPoint);
        if (polar) {
            if (toy != complex[n].phase()) {
                complex[n] = new Complex(complex[n].abs() * Math.cos(toy), complex[n].abs() * Math.sin(toy));
                getPoints2().put(currentPoint, complex[n].phase());
                getPoints().put(currentPoint, complex[n].abs());
            }

        } else {
            if (toy != complex[n].re()) {
                complex[n] = new Complex(complex[n].re(), toy);
                getPoints2().put(currentPoint, complex[n].im());
                getPoints().put(currentPoint, complex[n].re());
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
        int n = 0; //= (int) (currentPoint * getDeltaBaseline() * getPoints().size());
        if (point > 0) {
            n = (int) Math.ceil(point * getDeltaBaseline() * getPoints().size());
        } else if (point < 0) {
            n = (int) Math.floor(point * getDeltaBaseline() * getPoints().size()) + getPoints().size() + 1;

        }
        return n;
    }
}
