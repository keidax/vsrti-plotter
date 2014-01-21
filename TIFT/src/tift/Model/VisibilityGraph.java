package tift.Model;

import common.Mathematics.PostfixEvaluator;
import common.Model.Complex;
import common.Model.FFT;
import common.Model.Point;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * This handles the Time Domain Graphs.
 *
 * @author Karel Durktoa
 * @author Adam Pere
 * @author Gabriel Holodak
 */
public class VisibilityGraph extends Graph {

    public Complex[] compl;
    public File saveFile;
    public TreeMap<Double, Double> rms = new TreeMap<Double, Double>(), gridedRms = new TreeMap<Double, Double>();
    public int numberOfPoints = 128;
    public boolean polar;

    /**
     * Constructor
     *
     * @param m - Model
     */
    public VisibilityGraph(Model m) {
        super(m);
        rms = new TreeMap<Double, Double>();
        compl = new Complex[numberOfPoints];
        polar = true;
        reinicializePoints();
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
                getPoints().put(Double.parseDouble(keys[i].toString()), compl[i].abs());
                getPoints2().put(Double.parseDouble(keys[i].toString()), compl[i].phase());
            } else {
                getPoints().put(Double.parseDouble(keys[i].toString()), compl[i].re());
                getPoints2().put(Double.parseDouble(keys[i].toString()), compl[i].im());
            }
        }
        createImageGraph();
        model.updateListeners();
    }

    public void setRms(TreeMap<Double, Double> rms) {
        this.rms = rms;
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
        reinicializePoints();
    }

    public void setGridedRms(TreeMap<Double, Double> gridedRms) {
        this.gridedRms = gridedRms;
    }

    /**
     * Set the array of Complex Numbers to a new array of complex numbers. This
     * array will be used to determine point values.
     *
     * @param num - The new array of Complex Numbers
     */
    public void setComplex(Complex[] num) {
        compl = num;
    }

    /**
     * Set's all the points back to their original position. If a file was
     * opened, this sets all the points back to their position when the file was
     * opened. If a file was never opened, it sets the y-value of all points to
     * 0.
     */
    public void reinicializePoints() {
        getPoints().clear();
        getPoints2().clear();
        gridedRms.clear();
        if (compl.length != numberOfPoints) {
            compl = new Complex[numberOfPoints];
        }

        for (int i = 0; i < numberOfPoints; i++) {
            getPoints().put(i * getDeltaBaseline(), 0.0);
            getPoints2().put(i * getDeltaBaseline(), 0.0);
            compl[i] = new Complex(0, 0);
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
        reinicializePoints();
    }

    /**
     * Sets the y-value of all points to 0 no matter what.
     */
    public void fullReset() {
        rms = new TreeMap<Double, Double>();
        compl = new Complex[getPoints().size()];
        reinicializePoints();
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
            compl[i] = new Complex(ans, ans2);
            if (polar) {
                getPoints().put(intendedKey, compl[i].abs());
                getPoints2().put(intendedKey, compl[i].phase());
            } else {
                getPoints().put(intendedKey, compl[i].re());
                getPoints2().put(intendedKey, compl[i].im());
            }
        }
    }

    /**
     * Adds a point to the real/magnitude graph
     *
     * @param x
     * @param y
     */
    public void addPoint(double x, double y) {
        // System.out.println("Adding point " + x + " into grid(x)=" +
        // getGridX(x));
        getPoints().put(getGridX(x), y);// -this.halfBZero);
        // this.createImageGraph();
    }

    /**
     * Removes a point
     *
     * @param x
     */
    public void removePoint(double x) {
        getPoints().put(getGridX(x), 0.0);
        createImageGraph();
    }

    /**
     * Recounts the exponent
     */
    public void recountExponent() {
        if (points == null || points.size() < 2) {
            setExponent(5);
        }
        setExponent((int) Math.ceil(Math.log(points.size()) / Math.log(2)));

        // if (Graph.exponent < (int) Math.ceil(Math.log(getMaxRawX() /
        // Graph.getDeltaBaseline()) / Math.log(2))) {
        // Graph.exponent = (int) Math.ceil(Math.log(getMaxRawX() /
        // Graph.getDeltaBaseline()) / Math.log(2));
        // }
    }

    /**
     * @param x
     * @return
     */
    public double getGridX(double x) {
        if (getPoints().floorKey(x) != null) {
            return getPoints().floorKey(x);
        } else {
            return getPoints().ceilingKey(x);
        }
        // return Math.floor(new Double(x)/Graph.getDeltaBaseline());
    }

    /**
     * Handles the creation of the Image Graph. It does so by evaluating the tift
     * of the Time Domain's array of complex numbers and setting each point in
     * the image graph (the graphs in the Frequency Domain).
     */
    public void createImageGraph() {
        Complex[] res = FFT.fft(compl);
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
     * @return the save file
     */
    public File getSaveFile() {
        return saveFile;
    }

    /**
     * @param saveFile - File to be saved
     */
    public void setSaveFile(File saveFile) {
        this.saveFile = saveFile;
    }

    /**
     * @return The Grided RMS
     */
    public TreeMap<Double, Double> getGridedRms() {
        if (gridedRms != null && !gridedRms.isEmpty()) {
            return gridedRms;
        }
        if (getRms() == null || getRms().isEmpty()) {
            return new TreeMap<Double, Double>();
        }
        gridedRms.clear();// = new TreeMap<Double,Double>();
        Set<Double> keys = getRms().keySet();
        // System.out.println("rms size " + keys.size());
        for (Double d : keys) {
            // System.out.println(d/getLambda() +" -> " +
            // getGridX(d/getLambda()));
            gridedRms.put(getGridX(d), getRms().get(d));
        }
        // System.out.println("returning gridedrms size = "+gridedRms.size());
        return gridedRms;
    }

    /**
     * @param x   - x value
     * @param rms
     */
    public void addRms(double x, double rms) {
        getRms().put(x, rms);
        gridedRms.clear();
    }

    /**
     * Move a raw point
     *
     * @param p - Point
     * @param x
     * @param y
     */
    public void moveRawPoint(Point p, double x, double y) {
        p.setX(x);
        p.setY(y);
    }

    /**
     * Updates all graphs. If polar changes, it will change the graphs from
     * rectangular to polar.
     */
    public void update() {
        Object[] keys = getPoints().keySet().toArray();
        Complex[] res = FFT.fft(compl);
        model.imageGraph.setComplex(res);
        for (int i = 0; i < keys.length; i++) {
            if (polar) {
                getPoints2().put(Double.parseDouble(keys[i].toString()), compl[i].phase());
                getPoints().put(Double.parseDouble(keys[i].toString()), compl[i].abs());
                model.imageGraph.getPoints().put(i / (getDeltaBaseline() * getPoints().size()), res[i].abs());
                model.imageGraph.getPoints2().put(i / (getDeltaBaseline() * getPoints().size()), res[i].phase());
            } else {
                getPoints2().put(Double.parseDouble(keys[i].toString()), compl[i].im());
                getPoints().put(Double.parseDouble(keys[i].toString()), compl[i].re());
                model.imageGraph.getPoints().put(i / (getDeltaBaseline() * getPoints().size()), res[i].re());
                model.imageGraph.getPoints2().put(i / (getDeltaBaseline() * getPoints().size()), res[i].im());
            }
        }
        model.updateListeners();
    }

    /**
     * Changes the x-value of a point. This is called when a point is clicked or
     * dragged on the graph. This handles the real/magnitude graphs in the Time
     * Domain.
     *
     * @param currentPoint
     * @param toy          - new x value.
     */
    public void movePoint(double currentPoint, double toy) {
        int n = (int) (currentPoint / getDeltaBaseline());
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
        createImageGraph();
        model.updateListeners();
    }

    /**
     * Changes the x-value of a point. This is called when a point is clicked or
     * dragged on the graph. This handles the imaginary/phase graphs in the Time
     * Domain.
     *
     * @param currentPoint
     * @param toy          - new x value.
     */
    public void movePoint2(double currentPoint, double toy) {
        int n = (int) (currentPoint / getDeltaBaseline());
        if (polar) {
            if (toy != compl[n].phase()) {
                compl[n] = new Complex(compl[n].abs() * Math.cos(toy), compl[n].abs() * Math.sin(toy));
                getPoints2().put(currentPoint, compl[n].phase());
                getPoints().put(currentPoint, compl[n].abs());
            }

        } else {
            if (toy != compl[n].im()) {
                compl[n] = new Complex(compl[n].re(), toy);
                getPoints2().put(currentPoint, compl[n].im());
                getPoints().put(currentPoint, compl[n].re());
            }
        }
        createImageGraph();
        model.updateListeners();
    }

    /**
     * Replaces all current points with the points in the file.
     *
     * @param parseFile - file of all new points.
     */
    public void importPoints(Double[][] parseFile) {
        getPoints().clear();
        getPoints2().clear();

        compl = new Complex[parseFile[0].length];

        for (int i = 0; i < compl.length; i++) {
            compl[i] = new Complex(parseFile[1][i], parseFile[2][i]);

            if (polar) {
                getPoints().put(parseFile[0][i], compl[i].abs());
                getPoints2().put(parseFile[0][i], compl[i].phase());
            } else {
                getPoints().put(parseFile[0][i], compl[i].re());
                getPoints2().put(parseFile[0][i], compl[i].im());
            }
        }
        System.out.println("Importing raw data, size" + getPoints().size() + ", ");
        // this.recountExponent();
        // this.setDeltaBaseline(this.countDeltaBaseline());
        update();
        // System.out.println("ImportedPoint and call updating");
        model.updateListeners();
        // System.out.println("points imported "+getPoints().size());
    }

    /**
     * Prints out point values.
     */
    @Override
    public String toString() {
        String s = "";
        s += "*deltaBaseline " + getDeltaBaseline() + "\n";
        s += "*exponent " + getExponent() + "\n";
        s += "X_Y_Real_Imaginary" + "\n";
        Set<Double> keys = getPoints().keySet();
        int i = 0;
        for (Double key : keys) { // X Y RMS
            s += key + " " + compl[i].re() + " " + compl[i].im() + "\n";
            i++;
        }
        return s;
    }

    /**
     * @return Treemap<Double,Double> of RMS.
     */
    public TreeMap<Double, Double> getRms() {
        return rms;
    }

    public void importRms(TreeMap<Double, Double> parseData) {
        getRms().clear();
        // gridedRms.clear();
        getRms().putAll(parseData);
        gridedRms.putAll(parseData);
        update();
    }

    /**
     * Removes grided RMS point
     *
     * @param x
     */
    public void removeGridedRmsPoint(double x) {
        getGridedRms().remove(x);
    }

    /**
     * Removes an RMS point
     *
     * @param i
     */
    void removeRms(double i) {
        getRms().remove(i);
    }
}
