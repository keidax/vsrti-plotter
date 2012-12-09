package fft.Model;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import fft.Mathematics.Converter;

public class VisibilityGraph extends Graph {


    public File saveFile;
    public double lambda = 1;
    public SortedSet<Point> rawPoints;
    public double halfBZero = 0;
    public TreeMap<Double, Double> rms = new TreeMap<Double, Double>(), gridedRms = new TreeMap<Double, Double>();
    //public ArrayList<InputFile> inputFiles;
    public int numberOfPoints = 150;

    public VisibilityGraph(Model m) {
        super(m);
        rawPoints = new TreeSet<Point>();
        rms = new TreeMap<Double, Double>();
        reinicializePoints();
    }

    public void setRms(TreeMap<Double, Double> rms) {
        this.rms = rms;
    }

    public int getNumberOfPoints() {
        return numberOfPoints;
    }

    public void setNumberOfPoints(int numberOfPoints) {
        this.numberOfPoints = numberOfPoints;
        this.reinicializePoints();
    }

    public void setGridedRms(TreeMap<Double, Double> gridedRms) {
        this.gridedRms = gridedRms;
    }
/**
 * Set's all the points back to their original position. If a file was opened, this sets all the points back to their position
 * when the file was opened. If a file was never opened, it sets the y-value of all points to 0.
 */
    public void reinicializePoints() {
        getPoints().clear();
        gridedRms.clear();
        if(getRawPoints()!=null && getRawPoints().size()>0 &&numberOfPoints<getRawPoints().last().getX()/getDeltaBaseline())
            numberOfPoints=(int) Math.ceil(getRawPoints().last().getX()/getDeltaBaseline());
        if(getRawPoints().size()!=0)
            for (double i = 0; i <= Math.max(numberOfPoints,getRawPoints().last().getX()/getDeltaBaseline()); i++){
                this.getPoints().put(i * getDeltaBaseline() / getLambda(), 0.0);
            }
        else
            for (double i = 0; i < numberOfPoints; i++){
                this.getPoints().put(i * getDeltaBaseline() / getLambda(), 0.0);
            }
        double maxkey = getPoints().lastKey();
        if (!getRawPoints().isEmpty()) {
            Point[] pointss = getRawPoints().toArray(new Point[0]);
            ArrayList<Point> subp = new ArrayList<Point>();
            for (Point p : rawPoints) {
                addPoint(p.getX(), p.getY());
            }
            //if there're more points that would go into same grid, this cycle will count their average as well as average
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
        this.createImageGraph();
        update();
    }
    /**
     * Calls reinicialize points:
     * Set's all the points back to their original position. If a file was opened, this sets all the points back to their position
     * when the file was opened. If a file was never opened, it sets the y-value of all points to 0.
     */
    public void reset() {
        reinicializePoints();
    }
    /**
     * Sets the y-value of all points to 0.
     */
    public void fullReset(){
    	
        rawPoints = new TreeSet<Point>();
        rms = new TreeMap<Double, Double>();
        reinicializePoints();
         
    }
    
    /**
     * Takes an infix equation, breaks it up into an Array of type String by operators,
     * Evaluates the equation and plots the equation using the points currently on the Time Domain graph.
     * @param equation - The equation to be evaluated.
     */
    public void evaluate(String equation)
    {
    	equation = equation.replace(" ", "");	
    	int start = 0, end = 0, index = 0, num = equation.length();
		String[] M = new String[num];
		String[] La = new String[num];
		Object[] keys = this.getPoints().keySet().toArray();
		TreeMap<Double,Double> allPoints = this.getPoints();
		Converter con;
		
		for(int i = 0; i<num; i++) //This for loop breaks up the String equation. Might make more sense for this to be it's own method.
		{
			if(equation.charAt(i) == '*' || equation.charAt(i) == '/' || equation.charAt(i) == '(' || equation.charAt(i) == ')' || equation.charAt(i) == '+' || equation.charAt(i) == '-' ||  equation.charAt(i) == '^' )
			{
				end = i;
				if(start == end)
				{					
					La[index] = equation.charAt(i) + "";
					start++;
					index++;					
				}
				else
				{
					La[index] = equation.substring(start,end).replace('_', '-');
					
					if(La[index].equals("e"))
						La[index] = Math.E + "";
					else if(La[index].equals("-e"))
						La[index] = "-" + Math.E + "";
					else if(La[index].equals("pi"))
						La[index] = Math.PI + "";
					else if(La[index].equals("-pi"))
						La[index] = "-" + Math.PI + "";
	
					index++;
					La[index]= equation.charAt(i) + "";
					index++;
					start = end+1;
				}	
			}
		}
		
		if(start < num)
			La[index] = equation.substring(start).replace('_', '-');
		
		for(int i = 0; i<keys.length; i++)
		{
			for(int j = 0; j<num; j++)
			{
				M[j] = La[j];
				if(La[j] != null && La[j].contains("-x"))
 					M[j] = "-" + keys[i] + "";
				else if(La[j] != null && La[j].contains("x"))
 					M[j] = keys[i] + "";	
			}
					
			con = new Converter(M);
			Double ans = con.evaluate();
			allPoints.put(Double.parseDouble(keys[i].toString()), ans);
			this.createImageGraph();
			this.update();			
		}	
    }
    
    
    public void addPoint(double x, double y) {
        //System.out.println("Adding point " + x + " into grid(x)=" + getGridX(x));
        this.getPoints().put(getGridX(x / getLambda()), y);//-this.halfBZero);
        //this.createImageGraph();
    }

    public void removePoint(double x) {
        this.getPoints().put(getGridX(x), 0.0);
        this.createImageGraph();
    }

    public void recountExponent() {
        if (rawPoints == null || rawPoints.size() < 2) {
            setExponent(5);
        }
        setExponent((int) Math.ceil(Math.log(this.rawPoints.size()) / Math.log(2)));

//        if (Graph.exponent < (int) Math.ceil(Math.log(getMaxRawX() / Graph.getDeltaBaseline()) / Math.log(2))) {
//            Graph.exponent = (int) Math.ceil(Math.log(getMaxRawX() / Graph.getDeltaBaseline()) / Math.log(2));
//        }
    }

    public double getMaxRawX() {
        if (this.getRawPoints().size() != 0) {
            return this.getRawPoints().last().getX();
        } else {
            return 0.0;
        }
    }

    public double getMaxRawY() {
        if (this.getRawPoints() == null || this.getRawPoints().size() == 0) {
            return 0.0;
        }
        Point max = this.getRawPoints().first();
        for (Point p : this.getRawPoints()) {
            if (p.getY() > max.getY()) {
                max = p;
            }
        }
        return max.getY();
    }

    public double getGridX(double x) {
        if (this.getPoints().floorKey(x) != null) {
            return this.getPoints().floorKey(x);
        } else {
            return this.getPoints().ceilingKey(x);
        }
        //return Math.floor(new Double(x)/Graph.getDeltaBaseline());
    }
//////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
    public void createImageGraph() {

        Object[] keys = this.getPoints().keySet().toArray();
        double[][] data = new double[3][keys.length];
        for (int i = 0; i < keys.length; i++) {
            //if(gridedRms==null)
            if (getGridedRms() == null || !getGridedRms().containsKey(keys[i])) {
                data[0][i] = data[1][i] = data[2][i] = getPoints().get(keys[i]);
            } else {
                data[0][i] = getPoints().get(keys[i]) + getGridedRms().get(keys[i]) / 2;
                data[1][i] = getPoints().get(keys[i]);
                data[2][i] = getPoints().get(keys[i]) - getGridedRms().get(keys[i]) / 2;
            }
        }
        Dct1d dct = new Dct1d(data[0].length);

        double[][] res = new double[3][];
        for (int i = 0; i < 3; i++) {
            res[i] = dct.DCT(data[0]);
        }

        model.imageGraph.getPoints().clear();
        for (int i = 0; i < res[0].length; i++) {
            double a = 0;
            for (int j = 0; j < 3; j++) {
                a += res[j][i];
            }
            a /= 3;
            model.imageGraph.getPoints().put(i * this.getLambda() / (getDeltaBaseline() * getPoints().size() *2), Math.signum(a) * Math.sqrt(Math.abs(a)));
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////
 ////////////////////////////////////////////////////////////////////////////////////////
    
//////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
    public void createImageGraphTEST() {

        Object[] keys = this.getPoints().keySet().toArray();
        double[][] data = new double[3][keys.length];
        for (int i = 0; i < keys.length; i++) {
            //if(gridedRms==null)
            if (getGridedRms() == null || !getGridedRms().containsKey(keys[i])) {
                data[0][i] = data[1][i] = data[2][i] = getPoints().get(keys[i]);
            } else {
                data[0][i] = getPoints().get(keys[i]) + getGridedRms().get(keys[i]) / 2;
                data[1][i] = getPoints().get(keys[i]);
                data[2][i] = getPoints().get(keys[i]) - getGridedRms().get(keys[i]) / 2;
            }
        }
        Dct1d dct = new Dct1d(data[0].length);

        double[][] res = new double[3][];
        for (int i = 0; i < 3; i++) {
            res[i] = dct.DCT(data[0]);
        }

        model.imageGraph.getPoints().clear();
        for (int i = 0; i < res[0].length; i++) {
            double a = 0;
            for (int j = 0; j < 3; j++) {
                a += res[j][i];
            }
            a /= 3;
            model.imageGraph.getPoints().put(i * this.getLambda() / (getDeltaBaseline() * getPoints().size() *2), Math.signum(a) * Math.sqrt(Math.abs(a)));
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////
 ////////////////////////////////////////////////////////////////////////////////////////

    public void emptyRawPoints() {
        rawPoints.clear();
    }

    public void addRawPoint(double x, double y) {
        this.getRawPoints().add(new Point(x, y));
        this.recountExponent();
        this.countDeltaBaseline();
        deltaBaseline = this.countDeltaBaseline();
        addPoint(x, y);
    }

    public File getSaveFile() {
        return saveFile;
    }

    public void setSaveFile(File saveFile) {
        this.saveFile = saveFile;
    }

    public double getLambda() {
        return lambda;
    }

    public void setLambda(double lambda) {
        this.lambda = lambda;
        this.reinicializePoints();
    }

    public SortedSet<Point> getRawPoints() {
        return rawPoints;
    }

    public void setRawPoints(SortedSet<Point> rawPoints) {
        this.rawPoints = rawPoints;
    }

    public TreeMap<Double, Double> getGridedRms() {
        if (gridedRms != null && !gridedRms.isEmpty()) {
            return gridedRms;
        }
        if (getRms() == null || getRms().isEmpty()) {
            return new TreeMap<Double, Double>();
        }
        gridedRms.clear();// = new TreeMap<Double,Double>();
        Set<Double> keys = getRms().keySet();
        //System.out.println("rms size " + keys.size());
        for (Double d : keys) {
            //System.out.println(d/getLambda() +" -> " + getGridX(d/getLambda()));
            gridedRms.put(getGridX(d / getLambda()), getRms().get(d));
        }
        //System.out.println("returning gridedrms size = "+gridedRms.size());
        return gridedRms;
    }

    public void addRms(double x, double rms) {
        getRms().put(x, rms);
        gridedRms.clear();
    }

    public void moveRawPoint(Point p, double x, double y) {
        p.setX(x);
        p.setY(y);
    }

    public void update() {
        model.updateListeners();
    }

    public void movePoint(double currentPoint, double toy) {
        this.getPoints().put(currentPoint, toy);
        this.createImageGraph();
        System.out.println("Point was moved and called updating");
        update();
    }

    public void importPoints(TreeMap<Double, Double> parseFile) {
        this.getPoints().clear();
        this.getPoints().putAll(parseFile);
        this.getRawPoints().clear();
        Set<Double> keys = parseFile.keySet();
        
        System.out.println("*************" +keys+ "***************");
        
        for (Double key : keys) {
            getRawPoints().add(new Point(key, parseFile.get(key)));
        }
        System.out.println("Importing raw data, size" + getRawPoints().size() + ", ");
        this.recountExponent();
        this.setDeltaBaseline(this.countDeltaBaseline());
        this.createImageGraph();
        //System.out.println("ImportedPoint and call updating");
        update();
        //System.out.println("points imported "+getPoints().size());
    }

    @Override
    public String toString() {
        String s = "";
        s += "*lambda " + this.getLambda() + "\n";
        s += "*deltaBaseline " + getDeltaBaseline() + "\n";
        s += "*exponent " + getExponent() + "\n";
        s += "X_Y_RMS";
        Set<Double> keys = this.getPoints().keySet();
        for (Double key : keys) { // X Y RMS
            s += key * this.lambda + " " + getPoints().get(key) + " " + (getGridedRms().containsKey(key) ? getGridedRms().get(key) : 0.0) + "\n";
        }
        s += "\n\nANGLE_INTENSITY";
        keys = model.imageGraph.points.keySet();
        for (double key : keys) {
            s += key * this.lambda + " " + getPoints().get(key) + "\n";
        }
        //System.out.println(s);
        return s;
    }

    public TreeMap<Double, Double> getDataPoints() {
        TreeMap<Double, Double> al = new TreeMap<Double, Double>();
        for (Point p : rawPoints) {
            //System.out.println("gridX("+p.getX()+"/"+getLambda()+")="+getGridX(p.getX()/getLambda()));
            al.put(getGridX(p.getX() / getLambda()), p.getY());
        }
        //System.out.println("DataPoints = "+al.size());
        return al;
    }

    public TreeMap<Double, Double> getRms() {
        return rms;
    }

    public void importRms(TreeMap<Double, Double> parseData) {
        this.getRms().clear();
        //gridedRms.clear();
        getRms().putAll(parseData);
        gridedRms.putAll(parseData);
        update();
    }

    public void removeGridedRmsPoint(double x) {
        getGridedRms().remove(x);
    }

    void removeRms(double i) {
        this.getRms().remove(i);
    }

    private double countDeltaBaseline() {
        if (getRawPoints().size() < 2) {
            return 1.0;
        }
        double min = Double.MAX_VALUE;
        Point[] points = this.getRawPoints().toArray(new Point[0]);
        for (int i = 1; i < points.length; i++) {
            if (min > points[i].getX() - points[i - 1].getX()) {
                min = points[i].getX() - points[i - 1].getX();
            }
        }
        return min;
    }
}
