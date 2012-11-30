package fft.Viewer;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.Set;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.sourceforge.jlibeps.epsgraphics.EpsGraphics2D;

import fft.Model.Adapter;

/**
 * 
 * @author Karel Durktoa and Adam Pere
 *
 */
public abstract class Canvas extends JPanel implements MouseListener, MouseMotionListener {

    public TreeMap<Double, Double> points;
    public Viewer view;
    public Adapter adapter;
    protected static int lPad = 20, rPad = 30, tPad = 30, bPad = 40; //Margins
    protected static int[] steps = {1, 2, 5};
    protected static int squareWidth = 30; //width of vertical line placement
    protected static int yLabelWidth = 40;
    protected static int xLabelWidth = 30;
    protected static int defaultY = 9;
    protected int mCanx, mCany;
    protected Double currentPoint;
    protected static Color[] colors = {Color.BLACK};
    protected VolatileImage volatileImg;
    protected String xAxis = "x-axis";
    protected String yAxis = "y-axis";
    protected String graphTitle = "Untitled";
    final JPopupMenu menu = new JPopupMenu();
    protected int mouseButton = 0;
    private JFileChooser fileChooser;
    Canvas canvas;
    final boolean amp;

    public Canvas(Viewer v, Adapter a, TreeMap<Double, Double> g, boolean amplitude) {
        canvas = this;
        setPoints(g);
        setAdapter(a);
        this.setView(v);
        addMouseListener(this);
        addMouseMotionListener(this);
        fileChooser = new JFileChooser();
        setSize(new Dimension(200, 50));
        this.setVisible(true);
        this.amp = amplitude;
        JMenuItem item = new JMenuItem("Save as JPEG");
        JMenuItem item2 = new JMenuItem("Save as EPS");
        
        item.addActionListener(new ActionListener() {
        	
        	//Allows the user to right click the graph and save it as a jpeg
            @Override
			public void actionPerformed(ActionEvent e) {
                fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {

                    @Override
					public boolean accept(File f) {

                        if (f.isDirectory()) {
                            return true;
                        }
                        String s = f.getName();
                        int i = s.lastIndexOf('.');

                        if (i > 0 && i < s.length() - 1) {
                            String extension = s.substring(i + 1).toLowerCase();
                            if ("jpeg".equals(extension) || "jpg".equals(extension)) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                        return false;
                    }
                    // The description of this filter

                    @Override
					public String getDescription() {
                        return "Only JPEG";
                    }
                });
                //fileChooser.addChoosableFileFilter(new jpgSaveFilter());
                if (fileChooser.showSaveDialog(canvas) == JFileChooser.APPROVE_OPTION) {
                    ObjectOutputStream out;
                    try {
                        File f = fileChooser.getSelectedFile();
                        if (!(f.getName().trim().endsWith(".jpg") || f.getName().trim().endsWith(".jpeg"))) {
                            f = new File(f.getAbsolutePath() + ".jpeg");
                        }
                        out = new ObjectOutputStream(new FileOutputStream(f));
                        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
                        paint(image.createGraphics());
                        ImageIO.write(image, "jpeg", f);
                        out.close();
                    } catch (IOException ex) {
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "cannot save image", "save error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        item2.addActionListener(new ActionListener() {
        	
        	//Allows the user to right click the graph and save it as a jpeg
            @Override
			public void actionPerformed(ActionEvent e) {
                fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {

                    @Override
					public boolean accept(File f) {

                        if (f.isDirectory()) {
                            return true;
                        }
                        String s = f.getName();
                        int i = s.lastIndexOf('.');

                        if (i > 0 && i < s.length() - 1) {
                            String extension = s.substring(i + 1).toLowerCase();
                            if ("eps".equals(extension)) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                        return false;
                    }
                    // The description of this filter

                    @Override
					public String getDescription() {
                        return "Only EPS";
                    }
                });
                //fileChooser.addChoosableFileFilter(new jpgSaveFilter());
                if (fileChooser.showSaveDialog(canvas) == JFileChooser.APPROVE_OPTION) {
                    ObjectOutputStream out;
                    try {

                        File f = fileChooser.getSelectedFile();
                        if (!(f.getName().trim().endsWith(".eps"))) {
                            f = new File(f.getAbsolutePath() + ".eps");
                        }
                        //out = new ObjectOutputStream(new FileOutputStream(f));
                        EpsGraphics2D g = new EpsGraphics2D("Title", f, 0, 0, getWidth(), getHeight());
                        canvas.paint(g);
                        g.flush(); 
                        System.out.println(g.toString());
                        g.close();
                       // f.close();
                       
                        //BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
                       // paint(image.createGraphics());
                       // ImageIO.write(image, "eps", f);
                       // out.close();
                    } catch (IOException ex) {
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "cannot save image", "save error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        menu.add(item);
        menu.add(item2);
    }

    /**
     * 
     * @return The points on the graph
     */
    public TreeMap<Double, Double> getPoints() {
        return points;
    }
    
    /**
     * Set the points on the graph
     * @param points
     */
    public void setPoints(TreeMap<Double, Double> points) {
        this.points = points;
    }
    
    /**
     * Draw the points
     * @param count
     * @param g
     */
    public void drawDataSet(int count, Graphics2D g) {
        if (getPoints().size() == 0) {
            return;
        }
        Set<Double> keys = this.getPoints().keySet();
        Double previousKey = getPoints().firstKey();
        for (Double key : keys) {
            g.setColor(Color.BLACK);
            g.drawLine(g2cx(previousKey),
                    g2cy(getPoints().get(previousKey)),
                    g2cx(key),
                    g2cy(getPoints().get(key)));
            previousKey = key;
            drawPoint(g, key, getPoints().get(key));
        }

    }
    
    /**
     * Draw a single point
     * @param g
     * @param x
     * @param y
     */
    public void drawPoint(Graphics2D g, double x, double y) {
    }

    public int g2cx(double x) {
        double ratio = this.getRatioX();
        return (int) ((x * ratio) + getLeftShift());
    }

    public int g2cy(double y) {
        double ratio = this.getRatioY();
//	    	height-(height-t-b)/2-bpad-y*ratio
//	    	(2*height-heihgt+t+b)/2 - b - y*ratio
        return (int) (this.getPlotHeight() / 2 + tPad - y * ratio);
        //return (int)((this.getHeight()+tPad+bPad)/2-y*ratio-bPad);
    }

    public double c2gx(int x) {
        double ratio = this.getRatioX() ;
        return (x - getLeftShift()) / ratio;
    }

    public double c2gy(double toy) {
        double ratio = this.getRatioY();
        return (this.getPlotHeight() / 2 + tPad - toy) / ratio;
    }

    public void drawXAxis(Graphics2D g) {
        double steps = this.countHorizontalStep();
        double lstep = this.countHorizontalLabelStep();
        g.setFont(new Font(g.getFont().getFontName(), 0, 11));
        DecimalFormat df = new DecimalFormat("#.##");
        for (int i = 0; i < (this.getPlotWidth() - 1) / steps + 1; i++) {//horizontal
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine((int) (this.getLeftShift() + (i) * steps), tPad, (int) (this.getLeftShift() + (i) * steps), this.getHeight() - bPad);
            g.setColor(Color.BLACK);
            g.drawString("" + df.format(Math.round((i) * lstep * 100) / 100.0 +getMinX()), (int) (this.getLeftShift() + (i) * steps - xLabelWidth / 4), this.getHeight() - 17);
        }
        g.drawString(" " + xAxis + " ", getLeftShift() + getPlotWidth() / 2, this.getHeight() - 2);
    }

    public void drawYAxis(int count, Graphics2D g) {
        g.setColor(Color.BLACK);
        drawVerticalLine(lPad + ((count / 2 + 1) * yLabelWidth), countVerticalStep(), g);
        g.setColor(colors[count % colors.length]);
        drawVerticalMetric(lPad, countVerticalLabelStep(), countVerticalStep(), g);
        drawVerticalLabel(((count / 2) * yLabelWidth) + 5, " " + yAxis, g);
    }

    public void drawVerticalLine(int x, double steps, Graphics2D g) {
        g.drawLine(x, Canvas.tPad, x, this.getHeight() - Canvas.bPad);//vertical
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < (this.getPlotHeight() - 1) / steps + 1; i++) {//horizontal
            g.drawLine(x - 2, (int) (this.getHeight() - Canvas.bPad - (i) * steps), x + 2, (int) (this.getHeight() - Canvas.bPad - (i) * steps));
        }
    }

    public void drawVerticalMetric(int x, double strStep, double plotStep, Graphics2D g) {
        g.setFont(new Font(g.getFont().getFontName(), 0, 10));
        for (int i = 0; i < ((this.getPlotHeight() - 1) / plotStep + 1) / 2; i++) {//horizontal
            g.drawString("" + Math.round(c2gy((this.getHeight() - Canvas.bPad - tPad) / 2 + tPad - (i) * plotStep) * 10) / 10.0, x, (int) ((this.getHeight() - Canvas.bPad - tPad) / 2 + tPad - (i) * plotStep));
        }
        for (int i = 1; i < ((this.getPlotHeight() - 1) / plotStep + 1) / 2; i++) {//horizontal
            g.drawString("" + Math.round(c2gy((this.getHeight() - Canvas.bPad - tPad) / 2 + tPad + (i) * plotStep) * 10) / 10.0, x - 5, (int) ((this.getHeight() - Canvas.bPad - tPad) / 2 + tPad + (i) * plotStep));
        }
    }

    public void drawVerticalLabel(int x, String title, Graphics2D g) {
        g.translate(x, getPlotHeight() / 2);
        g.rotate(-Math.PI / 2.0);
        g.drawString(title, 0, 10);
        g.rotate(Math.PI / 2.0);
        g.translate(-x, -getPlotHeight() / 2);
    }

    public double countVerticalStep() {// NOT FINISHED
        int count = this.getPlotHeight() / squareWidth + 1;
        if (count % 2 == 1) {
            count++;
        }
        return (double) this.getPlotHeight() / count;
    }

    public double countHorizontalStep() {
        int count = this.getPlotWidth() / squareWidth + 1;
        return (double) this.getPlotWidth() / count;
    }

    public double countVerticalLabelStep() {// NOT FINISHED
        int count = this.getPlotHeight() / squareWidth + 1;//30
        int max;
        if (getPoints() == null || getMaxYPoint() == null) {
            max = countMax(defaultY);//50
        } else {
            max = countMaxVertical(Math.max(Math.abs(getMaxYPoint()), Math.abs(getMinYPoint())));//50
        }
        return (double) max / (double) count;
    }

    public Double getMaxYPoint() {
        if (getPoints().size() == 0) {
            return null;
        }
        double max = this.getPoints().firstEntry().getValue();
        Set<Double> keys = this.getPoints().keySet();
        for (Double key : keys) {
            if (this.getPoints().get(key) > max) {
                max = this.getPoints().get(key);
            }
        }
        if (max < 1) {
            return 1.0;
        }
        return max;
    }

    public Double getMinYPoint() {
        if (getPoints().size() == 0) {
            return (double) -Canvas.defaultY;
        }
        double min = this.getPoints().firstEntry().getValue();
        Set<Double> keys = this.getPoints().keySet();
        for (Double key : keys) {
            if (this.getPoints().get(key) < min) {
                min = this.getPoints().get(key);
            }
        }
        return min;
    }

    public double getMaxYRange() {
        if (getMaxYPoint() - getMinYPoint() < 2) {
            return 2.0;
        } else {
            return getMaxYPoint() - getMinYPoint();
        }
    }

    public double countHorizontalLabelStep() {// NOT FINISHED
        int count = this.getPlotWidth() / squareWidth + 1;//30
        double max = this.getMaxX() - this.getMinX();//50
        return max / count;
    }

    public int countMax(double max) {
        int i = 0;
        boolean end = false;
        while (!end) {
            if (max / (steps[i % steps.length] * (int) Math.pow(10, i / steps.length)) < 1) {
                end = true;
            }
            i++;
        }
        i--;
        return (int) Math.pow(2, (int) (Math.ceil(Math.log(max / adapter.getDeltaBaseline()) / Math.log(2))));
    }

    public int countMaxVertical(double max) {
        int i = 0;
        boolean end = false;
        while (!end) {
            if (max / (steps[i % steps.length] * (int) Math.pow(10, i / steps.length)) < 1) {
                end = true;
            }
            i++;
        }
        i -= 2;
        return steps[i % steps.length] * (int) Math.pow(10, i / steps.length);
    }

    public static int countHorizontalMax(double max) {
        return (int) Math.pow(2, (int) (Math.ceil(Math.log(max) / Math.log(2))));
    }
    
    public double getMinX()
    {
    	return 0.0; 
    			
    }
    public double getRatioX() {
        if (getPoints() == null) {
            return (double) getPlotWidth() / ((double) countMax(defaultY));
        }
        return (getPlotWidth()) / (getMaxX()-getMinX());
    }

    public double getRatioY() {
        if (getPoints() == null || getMaxYPoint() == null) {
            return (double) getPlotHeight() / (double) countMax(defaultY);
        } else {
            return getPlotHeight() / (2 * Math.max(Math.abs(getMaxY()), Math.abs(getMinYPoint())) * 1.1);
        }
    }

    protected int getPlotWidth() {
        return this.getWidth() - yLabelWidth - lPad - rPad;
    }

    protected int getPlotHeight() {
        return this.getHeight() - Canvas.tPad - Canvas.bPad;
    }

    public double getMaxY() {
        if (getPoints().isEmpty()) {
            return defaultY;
        }
        Double maxy = getMaxYPoint();
        return maxy;
    }

    public double getMaxX() {
        if (getPoints() == null || getPoints().size() == 0) {
            return defaultY;
        }
        return this.getPoints().lastKey();
    }

    public int getLeftShift() {
        return lPad + Canvas.yLabelWidth;
    }

    // GETTERS AND SETTERS
    public Adapter getAdapter() {
        return adapter;
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
    }

    public Viewer getView() {
        return view;
    }

    public void setView(Viewer view) {
        this.view = view;
    }

    public static int getLPad() {
        return lPad;
    }

    public static void setLPad(int pad) {
        lPad = pad;
    }

    public static int getRPad() {
        return rPad;
    }

    public static void setRPad(int pad) {
        rPad = pad;
    }

    public static int getTPad() {
        return tPad;
    }

    public static void setTPad(int pad) {
        tPad = pad;
    }

    public static int getBPad() {
        return bPad;
    }

    public static void setBPad(int pad) {
        bPad = pad;
    }

    public static int[] getSteps() {
        return steps;
    }

    public static void setSteps(int[] steps) {
        Canvas.steps = steps;
    }

    public static int getSquareWidth() {
        return squareWidth;
    }

    public static void setSquareWidth(int squareWidth) {
        Canvas.squareWidth = squareWidth;
    }

    public static int getYLabelWidth() {
        return yLabelWidth;
    }

    public static void setYLabelWidth(int labelWidth) {
        yLabelWidth = labelWidth;
    }

    public static int getXLabelWidth() {
        return xLabelWidth;
    }

    public static void setXLabelWidth(int labelWidth) {
        xLabelWidth = labelWidth;
    }

    public static int getDefaultY() {
        return defaultY;
    }

    public static void setDefaultY(int defaultY) {
        Canvas.defaultY = defaultY;
    }

    public int getMCanx() {
        return mCanx;
    }

    public void setMCanx(int canx) {
        mCanx = canx;
    }

    public int getMCany() {
        return mCany;
    }

    public void setMCany(int cany) {
        mCany = cany;
    }

    public Double getCurrentPoint() {
        return currentPoint;
    }

    public void setCurrentPoint(Double currentPoint) {
        this.currentPoint = currentPoint;
    }

    public static Color[] getColors() {
        return colors;
    }

    public static void setColors(Color[] colors) {
        VCanvas.colors = colors;
    }

    /**
     * Returns the object MyPoint which is in closer then MyCanvas.r to coordinates [x,y]
     * @param x int
     * @param y int
     * @return MyPoint
     */
    protected Double getPointOnGraph(int x, int y) {

        Set<Double> keys = this.getPoints().keySet();
        for (Double key : keys) {
            if ((new SquareOrnament()).isInside(mCanx, mCany, g2cx(key), g2cy(getPoints().get(key)))) {
                return key;
            }
        }
        return null;
    }

    protected Double getVerticallyPointOnGraph(int x, int y) {

        Set<Double> keys = this.getPoints().keySet();
        for (Double key : keys) {
            if ((new SquareOrnament()).isInsideVertically(mCanx, mCany, g2cx(key), g2cy(getPoints().get(key)))) {
                return key;
            }
        }
        return null;
    }

    /**
     * records where mouse was pressed and whether there is any point in less distance then
     * MyCanvas.r
     */
    @Override
	public void mousePressed(MouseEvent evt) {
        mouseButton = evt.getButton();
        if (evt.getButton() != MouseEvent.BUTTON1) {
            return;
        }
        mCanx = evt.getX();
        mCany = evt.getY();
        if (evt.getButton() == MouseEvent.BUTTON2) {
            if (getPointOnGraph(mCanx, mCany) != null) {
                setCurrentPoint(getPointOnGraph(mCanx, mCany));
            } else {
                setCurrentPoint(null);
            }
        } else {
            if (getVerticallyPointOnGraph(mCanx, mCany) != null) {
                setCurrentPoint(getVerticallyPointOnGraph(mCanx, mCany));
            } else {
                setCurrentPoint(null);
            }
        }
        if (getCurrentPoint() != null) {
            double toy = Math.min(Math.max(tPad, mCany), getHeight() - bPad);
            if(amp)
            	this.adapter.moveVisibilityPoint(getCurrentPoint(), c2gy(toy));
            else
            	this.adapter.moveVisibilityPoint2(getCurrentPoint(), c2gy(toy));
        }
    }

    @Override
	public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            menu.show(this, e.getX(), e.getY());
        }
    }

    @Override
	public void mouseEntered(MouseEvent e) {
    }

    @Override
	public void mouseExited(MouseEvent e) {
    }

    /**
     * Records coordinates mouse was released and if there was no currentPoint, then creates new point
     * with particular coordinates
     */
    @Override
	public void mouseReleased(MouseEvent evt) {
        mouseButton = 0;
        // NOT FINISHED (just redo getCurrentDataSet to getViewer().getCurrentDataSet
        mCanx = evt.getX();
        mCany = evt.getY();

        if (currentPoint == null && mCanx >= getLeftShift() && mCanx <= getLeftShift() + getPlotWidth() && mCany >= tPad && mCany < getWidth() - bPad) {
        }
    }

    @Override
    public void mouseDragged(MouseEvent evt) {
        if (mouseButton != MouseEvent.BUTTON1) {
            return;
        }
        mCanx = evt.getX();
        mCany = evt.getY();
        if (evt.getButton() != MouseEvent.BUTTON2) {
            if (getVerticallyPointOnGraph(mCanx, mCany) != null) {
                setCurrentPoint(getVerticallyPointOnGraph(mCanx, mCany));
            } else {
                setCurrentPoint(null);
            }
        }

        if (getCurrentPoint() != null) {
            double toy = Math.min(Math.max(tPad, mCany), getHeight() - bPad);
            if(amp)
            	this.adapter.moveVisibilityPoint(getCurrentPoint(), c2gy(toy));
            else
            	this.adapter.moveVisibilityPoint2(getCurrentPoint(), c2gy(toy));
        }
    }

    @Override
    public void mouseMoved(MouseEvent evt) {
        mCanx = evt.getX();
        mCany = evt.getY();
        DecimalFormat df = new DecimalFormat("#.##");
        if (getPointOnGraph(mCanx, mCany) != null) {
            double p = getVerticallyPointOnGraph(mCanx, mCany);
            this.setToolTipText("[" + df.format(p) + "; " + df.format(this.getPoints().get(p)) + "]");
        }
        if (getVerticallyPointOnGraph(mCanx, mCany) != null) {
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

        } else {
            setCursor(Cursor.getDefaultCursor());
            this.setToolTipText("");
        }
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public void paint(Graphics g) {
    	
    	((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        // create the hardware accelerated image.
        createBackBuffer();

        // Main rendering loop. Volatile images may lose their contents.
        // This loop will continually render to (and produce if necessary) volatile images
        // until the rendering was completed successfully.
        do {

            // Validate the volatile image for the graphics configuration of this
            // component. If the volatile image doesn't apply for this graphics configuration
            // (in other words, the hardware acceleration doesn't apply for the new device)
            // then we need to re-create it.
            GraphicsConfiguration gc = this.getGraphicsConfiguration();
            int valCode = volatileImg.validate(gc);

            // This means the device doesn't match up to this hardware accelerated image.
            if (valCode == VolatileImage.IMAGE_INCOMPATIBLE) {
                createBackBuffer(); // recreate the hardware accelerated image.
            }

            Graphics offscreenGraphics = volatileImg.getGraphics();
            ((Graphics2D) offscreenGraphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            doPaint(offscreenGraphics); // call core paint method.

            // paint back buffer to main graphics
            g.drawImage(volatileImg, 0, 0, this);
            // Test if content is lost
        } while (volatileImg.contentsLost());
    }

    protected void doPaint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        setBackground(Color.WHITE);
        drawXAxis(g2); //draw vertical lines
        g2.setColor(Color.BLACK);
        int i = 0;
        drawYAxis(i++, g2); //draw vertical axis
        //draw axes
        g2.setColor(Color.BLACK);
        g2.drawLine(this.getLeftShift(), g2cy(0.0), this.getLeftShift() + this.getPlotWidth(), g2cy(0.0));//horizontal// draw horizontal axis
        g2.drawString(graphTitle, getWidth() / 2 - 10, tPad / 2);
        i = 0;
        g2.setColor(colors[0]);
        drawDataSet(i, g2);
        i++;
    }

    protected void createBackBuffer() {
        GraphicsConfiguration gc = getGraphicsConfiguration();
        volatileImg = gc.createCompatibleVolatileImage(getWidth(), getHeight());
    }
}
