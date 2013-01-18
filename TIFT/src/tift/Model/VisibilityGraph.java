package tift.Model;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import common.Mathematics.Converter;
import common.Model.Complex;
import common.Model.FFT;
import common.Model.Point;

/**
 * This handles the Time Domain Graphs.
 * 
 * @author Karel Durktoa and Adam Pere
 * 
 */
public class VisibilityGraph extends Graph {
    
    public Complex[] compl;
    public File saveFile;
    public double lambda = 1;
    public SortedSet<Point> rawPoints, rawPoints2;
    public double halfBZero = 0;
    public TreeMap<Double, Double> rms = new TreeMap<Double, Double>(), gridedRms = new TreeMap<Double, Double>();
    public int numberOfPoints = 128;
    public boolean polar;
    
    /**
     * Constructor
     * 
     * @param m
     *            - Model
     */
    public VisibilityGraph(Model m) {
        super(m);
        rawPoints = new TreeSet<Point>();
        rawPoints2 = new TreeSet<Point>();
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
     * @param radio
     *            - Plot polar or rectangular form of complex numbers
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
        model.updateListeners();
        model.imageGraph.setPolar(radio);
    }
    
    public void setRms(TreeMap<Double, Double> rms) {
        this.rms = rms;
    }
    
    /**
     * @return The number of points in the graph. This is also the number of
     *         points used to evaluate the tift.
     */
    public int getNumberOfPoints() {
        return numberOfPoints;
    }
    
    /**
     * 
     * @param numberOfPoints
     *            - Add more points to the graph. Must be a power of 2.
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
     * @param num
     *            - The new array of Complex Numbers
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
        if (getRawPoints() != null && getRawPoints().size() > 0 && numberOfPoints < getRawPoints().last().getX() / getDeltaBaseline()) {
            numberOfPoints = (int) Math.ceil(getRawPoints().last().getX() / getDeltaBaseline());
        }
        if (getRawPoints().size() != 0) {
            for (double i = 0; i <= Math.max(numberOfPoints, getRawPoints().last().getX() / getDeltaBaseline()); i++) {
                getPoints().put(i * getDeltaBaseline() / getLambda(), 0.0);
                getPoints2().put(i * getDeltaBaseline() / getLambda(), 0.0);
                compl[(int) i] = new Complex(0, 0);
            }
        } else {
            for (double i = 0; i < numberOfPoints; i++) {
                getPoints().put(i * getDeltaBaseline() / getLambda(), 0.0);
                getPoints2().put(i * getDeltaBaseline() / getLambda(), 0.0);
                compl[(int) i] = new Complex(0, 0);
            }
        }
        
        if (!getRawPoints().isEmpty()) {
            Point[] pointss = getRawPoints().toArray(new Point[0]);
            ArrayList<Point> subp = new ArrayList<Point>();
            for (Point p : rawPoints) {
                addPoint(p.getX(), p.getY());
            }
            // if there're more points that would go into same grid, this cycle
            // will count their average as well as average
            // of rms and shows puts one average point
            for (int i = 0; i < pointss.length; i++) {
                if (!subp.isEmpty() && getGridX(pointss[i].getX()) != getGridX(subp.get(0).getX())) {
                    if (subp.size() == 1) {
                        addPoint(subp.get(0).getX(), subp.get(0).getY());
                    } else {
                        addPoint(subp.get(0).getX(), 0);
                    }
                    subp.clear();
                    subp.add(pointss[i]);
                } else {
                    subp.add(pointss[i]);
                }
            }
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
        
        rawPoints = new TreeSet<Point>();
        rawPoints2 = new TreeSet<Point>();
        rms = new TreeMap<Double, Double>();
        compl = new Complex[getPoints().keySet().toArray().length];
        reinicializePoints();
        
    }
    
    private String[] breakEquation(String equation) {
        equation = equation.replace(" ", "");
        int start = 0, end = 0, index = 0, num = equation.length();
        String[] La = new String[num]; // The reference array. This only changes
                                       // in the first for loop.
        
        for (int i = 0; i < num; i++) // This for loop breaks up the String
                                      // equation. Might make more sense for
                                      // this to be it's own method.
        {
            if (equation.charAt(i) == '*' || equation.charAt(i) == '/' || equation.charAt(i) == '(' || equation.charAt(i) == ')'
                    || equation.charAt(i) == '+' || equation.charAt(i) == '-' || equation.charAt(i) == '^') {
                end = i;
                if (start == end) {
                    La[index] = equation.charAt(i) + "";
                    start++;
                    index++;
                } else {
                    La[index] = equation.substring(start, end).replace('_', '-'); // Changes
                                                                                  // '_'
                                                                                  // to
                                                                                  // a
                                                                                  // negative
                                                                                  // sign
                    if (La[index].equals("e")) {
                        La[index] = Math.E + "";
                    } else if (La[index].equals("-e")) {
                        La[index] = "-" + Math.E + "";
                    } else if (La[index].equals("pi")) {
                        La[index] = Math.PI + ""; // changes "pi" to the double
                                                  // version of the mathematical
                                                  // constant pi.
                    } else if (La[index].equals("-pi")) {
                        La[index] = "-" + Math.PI + ""; // changes "pi" to the
                                                        // double version of the
                                                        // mathematical constant
                                                        // pi.
                    }
                    
                    index++;
                    La[index] = equation.charAt(i) + "";
                    index++;
                    start = end + 1;
                }
            }
        }
        return La;
    }
    
    /**
     * Takes an infix equation, breaks it up into an Array of type String by
     * operators. Evaluates the equation and plots the equation using the points
     * currently on the Time Domain graph. A minus sin is denoted by "_".
     * 
     * @param equation
     *            - The equation to be evaluated.
     */
    public void evaluate(String equation, String iEquation) {
        equation = equation.replace(" ", "");
        iEquation = iEquation.replace(" ", "");
        int start = 0, end = 0, index = 0, num = equation.length();
        int start2 = 0, end2 = 0, index2 = 0, num2 = iEquation.length();
        String[] M = new String[num];
        String[] La = new String[num]; // The reference array. This only changes in the first for loop.
        String[] M2 = new String[num2];
        String[] La2 = new String[num2]; // The reference array. This only changes in the first for loop.
        Object[] keys = getPoints().keySet().toArray();
        TreeMap<Double, Double> allPoints = getPoints();
        Converter con, con2;
        
        for (int i = 0; i < num; i++) // This for loop breaks up the String equation. Might make more sense for this to be it's own method.
        {
            char c = equation.charAt(i);
            if (c == '*' || c == '/' || c == '(' || c == ')' || c == '+' || c == '-' || c == '^') {
                end = i;
                if (start == end) {
                    La[index] = equation.charAt(i) + "";
                    start++;
                    index++;
                } else {
                    La[index] = equation.substring(start, end).replace('_', '-'); // Changes '_' to a negative sign
                    if (La[index].equals("e")) {
                        La[index] = Math.E + "";
                    } else if (La[index].equals("-e")) {
                        La[index] = "-" + Math.E + "";
                    } else if (La[index].equals("pi")) {
                        La[index] = Math.PI + ""; // changes "pi" to the double version of the mathematical constant pi.
                    } else if (La[index].equals("-pi")) {
                        La[index] = "-" + Math.PI + ""; // changes "pi" to the double version of the mathematical constant pi.
                    }
                    
                    index++;
                    La[index] = equation.charAt(i) + "";
                    index++;
                    start = end + 1;
                }
            }
        }
        
        for (int i = 0; i < num2; i++) // This for loop breaks up the String equation. Might make more sense for this to be it's own method.
        {
            if (iEquation.charAt(i) == '*' || iEquation.charAt(i) == '/' || iEquation.charAt(i) == '(' || iEquation.charAt(i) == ')'
                    || iEquation.charAt(i) == '+' || iEquation.charAt(i) == '-' || iEquation.charAt(i) == '^') {
                end2 = i;
                if (start2 == end2) {
                    La2[index2] = iEquation.charAt(i) + "";
                    start2++;
                    index2++;
                } else {
                    La2[index2] = iEquation.substring(start2, end2).replace('_', '-'); // Changes '_' to a negative sign
                    if (La2[index2].equals("e")) {
                        La2[index2] = Math.E + "";
                    } else if (La2[index2].equals("-e")) {
                        La2[index2] = "-" + Math.E + "";
                    } else if (La2[index2].equals("pi")) {
                        La2[index2] = Math.PI + ""; // changes "pi" to the double version of the mathematical constant pi.
                    } else if (La2[index2].equals("-pi")) {
                        La2[index2] = "-" + Math.PI + ""; // changes "pi" to the double version of the mathematical constant pi.
                    }
                    
                    index2++;
                    La2[index2] = iEquation.charAt(i) + "";
                    index2++;
                    start2 = end2 + 1;
                }
            }
        }
        
        if (start < num) {
            La[index] = equation.substring(start).replace('_', '-'); // Changes '_' to a negative sign
            if (La[index].equals("e")) {
                La[index] = Math.E + "";
            } else if (La[index].equals("-e")) {
                La[index] = "-" + Math.E + "";
            } else if (La[index].equals("pi")) {
                La[index] = Math.PI + ""; // changes "pi" to the double version of the mathematical constant pi.
            } else if (La[index].equals("-pi")) {
                La[index] = "-" + Math.PI + ""; // changes "pi" to the double version of the mathematical constant pi.
            }
        }
        
        if (start2 < num2) {
            La2[index2] = iEquation.substring(start).replace('_', '-'); // Changes '_' to a negative sign
            if (La2[index2].equals("e")) {
                La2[index2] = Math.E + "";
            } else if (La2[index2].equals("-e")) {
                La2[index2] = "-" + Math.E + "";
            } else if (La2[index2].equals("pi")) {
                La2[index2] = Math.PI + ""; // changes "pi" to the double
                                            // version of the mathematical
                                            // constant pi.
            } else if (La2[index2].equals("-pi")) {
                La2[index2] = "-" + Math.PI + ""; // changes "pi" to the double
                                                  // version of the mathematical
                                                  // constant pi.
            }
        }
        
        // Replaces the letter x with the x-value of the current point
        for (int i = 0; i < keys.length; i++) {
            for (int j = 0; j < num; j++) {
                M[j] = La[j];
                M2[j] = La2[j];
                if (La[j] != null && La[j].equals("-x")) {
                    M[j] = "-" + keys[i] + "";
                } else if (La[j] != null && La[j].contains("x")) {
                    M[j] = keys[i] + "";
                }
                if (La2[j] != null && La2[j].equals("-x")) {
                    M2[j] = "-" + keys[i] + "";
                } else if (La2[j] != null && La2[j].contains("x")) {
                    M2[j] = keys[i] + "";
                }
                
            }
            
            con = new Converter(M);
            con2 = new Converter(M2);
            Double ans = con.evaluate(); // evaluates the expression
            Double ans2 = con2.evaluate();
            System.out.println(ans + ": " + ans2);
            
            compl[i] = new Complex(ans, ans2); // creates the complex number then sets the points
            if (polar) {
                allPoints.put(Double.parseDouble(keys[i].toString()), compl[i].abs());
                getPoints2().put(Double.parseDouble(keys[i].toString()), compl[i].phase());
                
            } else {
                allPoints.put(Double.parseDouble(keys[i].toString()), compl[i].re());
                getPoints2().put(Double.parseDouble(keys[i].toString()), compl[i].im());
            }
            createImageGraph();
            model.updateListeners();
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
        getPoints().put(getGridX(x / getLambda()), y);// -this.halfBZero);
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
        if (rawPoints == null || rawPoints.size() < 2) {
            setExponent(5);
        }
        setExponent((int) Math.ceil(Math.log(rawPoints.size()) / Math.log(2)));
        
        // if (Graph.exponent < (int) Math.ceil(Math.log(getMaxRawX() /
        // Graph.getDeltaBaseline()) / Math.log(2))) {
        // Graph.exponent = (int) Math.ceil(Math.log(getMaxRawX() /
        // Graph.getDeltaBaseline()) / Math.log(2));
        // }
    }
    
    /**
     * 
     * @return max X value of raw points.
     */
    public double getMaxRawX() {
        if (getRawPoints().size() != 0) {
            return getRawPoints().last().getX();
        } else {
            return 0.0;
        }
    }
    
    /**
     * 
     * @return max Y value of raw points.
     */
    public double getMaxRawY() {
        if (getRawPoints() == null || getRawPoints().size() == 0) {
            return 0.0;
        }
        Point max = getRawPoints().first();
        for (Point p : getRawPoints()) {
            if (p.getY() > max.getY()) {
                max = p;
            }
        }
        return max.getY();
    }
    
    /**
     * 
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
        // check();
        Complex[] res = FFT.fft(compl);
        model.imageGraph.setComplex(res);
        for (int i = 0; i < res.length; i++) {
            if (polar) {
                model.imageGraph.getPoints().put(i * getLambda() / (getDeltaBaseline() * getPoints().size()), res[i].abs());
                model.imageGraph.getPoints2().put(i * getLambda() / (getDeltaBaseline() * getPoints().size()), res[i].phase());
            } else {
                model.imageGraph.getPoints().put(i * getLambda() / (getDeltaBaseline() * getPoints().size()), res[i].re());
                model.imageGraph.getPoints2().put(i * getLambda() / (getDeltaBaseline() * getPoints().size()), res[i].im());
            }
        }
    }
    
    /**
     * Deletes all raw points.
     */
    public void emptyRawPoints() {
        rawPoints.clear();
        rawPoints2.clear();
    }
    
    /**
     * Adds a raw point.
     * 
     * @param x
     * @param y
     */
    public void addRawPoint(double x, double y) {
        getRawPoints().add(new Point(x, y));
        recountExponent();
        countDeltaBaseline();
        deltaBaseline = countDeltaBaseline();
        addPoint(x, y);
    }
    
    /**
     * 
     * @return the save file
     */
    public File getSaveFile() {
        return saveFile;
    }
    
    /**
     * 
     * @param saveFile
     *            - File to be saved
     */
    public void setSaveFile(File saveFile) {
        this.saveFile = saveFile;
    }
    
    /**
     * 
     * @return Lambda
     */
    public double getLambda() {
        return lambda;
    }
    
    /**
     * 
     * @param lambda
     */
    public void setLambda(double lambda) {
        this.lambda = lambda;
        reinicializePoints();
    }
    
    /**
     * 
     * @return Sorted set of all raw points
     */
    public SortedSet<Point> getRawPoints() {
        return rawPoints;
    }
    
    /**
     * 
     * @param rawPoints
     *            - Sorted set of all new raw points.
     */
    public void setRawPoints(SortedSet<Point> rawPoints) {
        this.rawPoints = rawPoints;
    }
    
    /**
     * 
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
            gridedRms.put(getGridX(d / getLambda()), getRms().get(d));
        }
        // System.out.println("returning gridedrms size = "+gridedRms.size());
        return gridedRms;
    }
    
    /**
     * 
     * @param x
     *            - x value
     * @param rms
     */
    public void addRms(double x, double rms) {
        getRms().put(x, rms);
        gridedRms.clear();
    }
    
    /**
     * Move a raw point
     * 
     * @param p
     *            - Point
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
            // if(gridedRms==null)
            if (polar) {
                getPoints2().put(Double.parseDouble(keys[i].toString()), compl[i].phase());
                getPoints().put(Double.parseDouble(keys[i].toString()), compl[i].abs());
                model.imageGraph.getPoints().put(i * getLambda() / (getDeltaBaseline() * getPoints().size()), res[i].abs());
                model.imageGraph.getPoints2().put(i * getLambda() / (getDeltaBaseline() * getPoints().size()), res[i].phase());
            } else {
                getPoints2().put(Double.parseDouble(keys[i].toString()), compl[i].im());
                getPoints().put(Double.parseDouble(keys[i].toString()), compl[i].re());
                model.imageGraph.getPoints().put(i * getLambda() / (getDeltaBaseline() * getPoints().size()), res[i].re());
                model.imageGraph.getPoints2().put(i * getLambda() / (getDeltaBaseline() * getPoints().size()), res[i].im());
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
     * @param toy
     *            - new x value.
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
     * @param toy
     *            - new x value.
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
            if (toy != compl[n].re()) {
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
     * @param parseFile
     *            - file of all new points.
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
        s += "*lambda " + getLambda() + "\n";
        s += "*deltaBaseline " + getDeltaBaseline() + "\n";
        s += "*exponent " + getExponent() + "\n";
        s += "X_Y_Real_Imaginary" + "\n";
        Set<Double> keys = getPoints().keySet();
        int i = 0;
        for (Double key : keys) { // X Y RMS
            s += key * lambda + " " + compl[i].re() + " " + compl[i].im() + "\n";
            i++;
        }
        return s;
    }
    
    /**
     * 
     * @return Treemap<Double,Double> of data points from the real/magnitude
     *         graph.
     */
    public TreeMap<Double, Double> getDataPoints() {
        TreeMap<Double, Double> al = new TreeMap<Double, Double>();
        for (Point p : rawPoints) {
            // System.out.println("gridX("+p.getX()+"/"+getLambda()+")="+getGridX(p.getX()/getLambda()));
            al.put(getGridX(p.getX() / getLambda()), p.getY());
        }
        // System.out.println("DataPoints = "+al.size());
        return al;
    }
    
    /**
     * 
     * @return Treemap<Double,Double> of data points from the imaginary/phase
     *         graph.
     */
    public TreeMap<Double, Double> getDataPoints2() {
        TreeMap<Double, Double> al = new TreeMap<Double, Double>();
        for (Point p : rawPoints2) {
            // System.out.println("gridX("+p.getX()+"/"+getLambda()+")="+getGridX(p.getX()/getLambda()));
            al.put(getGridX(p.getX() / getLambda()), p.getY());
        }
        // System.out.println("DataPoints = "+al.size());
        return al;
    }
    
    /**
     * 
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
    
    /**
     * 
     * @return Delta Baseline.
     */
    private double countDeltaBaseline() {
        if (getRawPoints().size() < 2) {
            return 1.0;
        }
        double min = Double.MAX_VALUE;
        Point[] points = getRawPoints().toArray(new Point[0]);
        for (int i = 1; i < points.length; i++) {
            if (min > points[i].getX() - points[i - 1].getX()) {
                min = points[i].getX() - points[i - 1].getX();
            }
        }
        return min;
    }
}
