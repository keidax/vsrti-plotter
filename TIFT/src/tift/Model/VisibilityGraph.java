package tift.Model;

import common.Mathematics.PostfixEvaluator;
import common.Model.Complex;
import common.Model.FFT;

import java.util.HashMap;
import java.util.Map;

/**
 * This handles the Time Domain Graphs.
 *
 * @author Karel Durktoa
 * @author Adam Pere
 * @author Gabriel Holodak
 */
public class VisibilityGraph extends Graph {

    private Complex[] complex;
    private int numberOfPoints = 128;
    private boolean polar;

    /**
     * Constructor
     *
     * @param m - Model
     */
    public VisibilityGraph(Model m) {
        super(m);
        complex = new Complex[numberOfPoints];
        polar = true;
        reinitializePoints();
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
        Object[] keys = getPoints().keySet().toArray();
        for (int i = 0; i < keys.length; i++) {
            if (polar) {
                getPoints().put(Double.parseDouble(keys[i].toString()), complex[i].abs());
                getPoints2().put(Double.parseDouble(keys[i].toString()), complex[i].phase());
            } else {
                getPoints().put(Double.parseDouble(keys[i].toString()), complex[i].re());
                getPoints2().put(Double.parseDouble(keys[i].toString()), complex[i].im());
            }
        }
        createImageGraph();
        model.updateListeners();
    }

    /**
     * @return The number of points in the graph. This is also the number of
     * points used to evaluate the tift.
     */
    public int getNumberOfPoints() {
        return numberOfPoints;
    }

    /**
     * @param numberOfPoints - Add more points to the graph. Must be a power of 2.
     */
    public void setNumberOfPoints(int numberOfPoints) {
        this.numberOfPoints = numberOfPoints;
        reinitializePoints();
    }

    /**
     * Set the array of Complex Numbers to a new array of complex numbers. This
     * array will be used to determine point values.
     *
     * @param num - The new array of Complex Numbers
     */
    public void setComplex(Complex[] num) {
        complex = num;
    }

    /**
     * Set's all the points back to their original position. If a file was
     * opened, this sets all the points back to their position when the file was
     * opened. If a file was never opened, it sets the y-value of all points to
     * 0.
     */
    public void reinitializePoints() {
        getPoints().clear();
        getPoints2().clear();
        if (complex.length != numberOfPoints) {
            complex = new Complex[numberOfPoints];
        }

        for (int i = 0; i < numberOfPoints; i++) {
            getPoints().put(i * getDeltaBaseline(), 0.0);
            getPoints2().put(i * getDeltaBaseline(), 0.0);
            complex[i] = new Complex(0, 0);
        }
        createImageGraph();
        model.updateListeners();
    }

    /**
     * Calls reinitialize points: Set's all the points back to their original
     * position. If a file was opened, this sets all the points back to their
     * position when the file was opened. If a file was never opened, it sets
     * the y-value of all points to 0.
     */
    public void reset() {
        reinitializePoints();
    }

    /**
     * Sets the y-value of all points to 0 no matter what.
     */
    public void fullReset() {
        complex = new Complex[getPoints().size()];
        reinitializePoints();
    }

    /**
     * Takes an infix equation, breaks it up into an Array of type String by
     * operators. Evaluates the equation and plots the equation using the points
     * currently on the Time Domain graph. A minus sin is denoted by "_".
     *
     * @param equation - The equation to be evaluated.
     */
    public void evaluate(String equation, String iEquation) {
        Map<String, Double> map = new HashMap<String, Double>();
        PostfixEvaluator eval1 = new PostfixEvaluator(equation, map), eval2 = new PostfixEvaluator(iEquation, map);
        Object[] keys = getPoints().keySet().toArray();
        // Replaces the letter t with the x-value of the current point
        for (int i = 0; i < keys.length; i++) {

            double intendedKey = (Double) keys[i];
            double lowerKey = getPoints().floorKey(intendedKey), higherKey = getPoints().ceilingKey(intendedKey);
            intendedKey = (intendedKey - lowerKey < higherKey - intendedKey ? lowerKey : higherKey);

            map.put("t", intendedKey);
            Double ans = eval1.evaluate();
            Double ans2 = eval2.evaluate();
            complex[i] = new Complex(ans, ans2);
            if (polar) {
                getPoints().put(intendedKey, complex[i].abs());
                getPoints2().put(intendedKey, complex[i].phase());
            } else {
                getPoints().put(intendedKey, complex[i].re());
                getPoints2().put(intendedKey, complex[i].im());
            }
        }
    }

    /**
     * Handles the creation of the Image Graph. It does so by evaluating the tift
     * of the Time Domain's array of complex numbers and setting each point in
     * the image graph (the graphs in the Frequency Domain).
     */
    public void createImageGraph() {
        Complex[] res = FFT.fft(complex);
        model.imageGraph.setComplex(res);

        model.imageGraph.getPoints().clear();
        model.imageGraph.getPoints2().clear();

        for (int i = 0; i < res.length / 2; i++) {
            if (polar) {
                model.imageGraph.getPoints().put(i / (getDeltaBaseline() * getPoints().size()), res[i].abs());
                model.imageGraph.getPoints2().put(i / (getDeltaBaseline() * getPoints().size()), res[i].phase());
            } else {
                model.imageGraph.getPoints().put(i / (getDeltaBaseline() * getPoints().size()), res[i].re());
                model.imageGraph.getPoints2().put(i / (getDeltaBaseline() * getPoints().size()), res[i].im());
            }
        }
        for (int i = res.length / 2 + 1; i < res.length; i++) {
            if (polar) {
                model.imageGraph.getPoints().put((i - res.length) / (getDeltaBaseline() * getPoints().size()), res[i].abs());
                model.imageGraph.getPoints2().put((i - res.length) / (getDeltaBaseline() * getPoints().size()), res[i].phase());
            } else {
                model.imageGraph.getPoints().put((i - res.length) / (getDeltaBaseline() * getPoints().size()), res[i].re());
                model.imageGraph.getPoints2().put((i - res.length) / (getDeltaBaseline() * getPoints().size()), res[i].im());
            }
        }
    }

    /**
     * Updates all graphs. If polar changes, it will change the graphs from
     * rectangular to polar.
     */
    public void update() {
        Object[] keys = getPoints().keySet().toArray();
        Complex[] res = FFT.fft(complex);
        model.imageGraph.setComplex(res);
        for (int i = 0; i < keys.length; i++) {
            if (polar) {
                getPoints2().put(Double.parseDouble(keys[i].toString()), complex[i].phase());
                getPoints().put(Double.parseDouble(keys[i].toString()), complex[i].abs());
                model.imageGraph.getPoints().put(i / (getDeltaBaseline() * getPoints().size()), res[i].abs());
                model.imageGraph.getPoints2().put(i / (getDeltaBaseline() * getPoints().size()), res[i].phase());
            } else {
                getPoints2().put(Double.parseDouble(keys[i].toString()), complex[i].im());
                getPoints().put(Double.parseDouble(keys[i].toString()), complex[i].re());
                model.imageGraph.getPoints().put(i / (getDeltaBaseline() * getPoints().size()), res[i].re());
                model.imageGraph.getPoints2().put(i / (getDeltaBaseline() * getPoints().size()), res[i].im());
            }
        }
        model.updateListeners();
    }

    /**
     * Changes the y-value of a point. This is called when a point is clicked or
     * dragged on the graph. This handles the real/magnitude graphs in the Time
     * Domain.
     *
     * @param currentPoint the x-value of the point to be changed
     * @param toy the new y value.
     */
    public void movePoint(double currentPoint, double toy) {
        int n = (int) (currentPoint / getDeltaBaseline());
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
        createImageGraph();
        model.updateListeners();
    }

    /**
     * Changes the y-value of a point. This is called when a point is clicked or
     * dragged on the graph. This handles the imaginary/phase graphs in the Time
     * Domain.
     *
     * @param currentPoint the x-value of the point to be changed
     * @param toy the new y value.
     */
    public void movePoint2(double currentPoint, double toy) {
        int n = (int) (currentPoint / getDeltaBaseline());
        if (polar) {
            if (toy != complex[n].phase()) {
                complex[n] = new Complex(complex[n].abs() * Math.cos(toy), complex[n].abs() * Math.sin(toy));
                getPoints2().put(currentPoint, complex[n].phase());
                getPoints().put(currentPoint, complex[n].abs());
            }

        } else {
            if (toy != complex[n].im()) {
                complex[n] = new Complex(complex[n].re(), toy);
                getPoints2().put(currentPoint, complex[n].im());
                getPoints().put(currentPoint, complex[n].re());
            }
        }
        createImageGraph();
        model.updateListeners();
    }
}
