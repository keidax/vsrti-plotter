package srt.View;

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

import srt.Model.Adapter;

import common.View.SquareOrnament;


public abstract class Canvas extends JPanel implements MouseListener,
        MouseMotionListener {
    
    public TreeMap<Double, Double> points;
    public View view;
    public Adapter adapter;
    protected static int lPad = 20, rPad = 30, tPad = 30, bPad = 40;
    protected static int[] steps = { 1, 2, 5 };
    protected static int squareWidth = 30;
    protected static int yLabelWidth = 40;
    protected static int xLabelWidth = 30;
    protected static int defaultY = 9;
    protected int mCanx, mCany;
    protected Double currentPoint;
    protected static Color[] colors = { Color.BLACK };
    protected VolatileImage volatileImg;
    final JPopupMenu menu = new JPopupMenu();
    protected String xAxis = "x-axis";
    protected String yAxis = "y-axis";
    protected String graphTitle = "Untitled";
    protected Canvas canvas;
    protected double maxX = 40;
    protected JFileChooser fileChooser;
    
    public Canvas(View v, Adapter a, TreeMap<Double, Double> g) {
        canvas = this;
        setPoints(g);
        setAdapter(a);
        setView(v);
        addMouseListener(this);
        addMouseMotionListener(this);
        fileChooser = new JFileChooser();
        setSize(new Dimension(200, 50));
        setVisible(true);
        
        JMenuItem item = new JMenuItem("Save as JPEG");
        JMenuItem item2 = new JMenuItem("Save as EPS");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser
                        .setFileFilter(new javax.swing.filechooser.FileFilter() {
                            
                            @Override
                            public boolean accept(File f) {
                                
                                if (f.isDirectory()) {
                                    return true;
                                }
                                String s = f.getName();
                                int i = s.lastIndexOf('.');
                                
                                if (i > 0 && i < s.length() - 1) {
                                    String extension =
                                            s.substring(i + 1).toLowerCase();
                                    if ("jpeg".equals(extension)
                                            || "jpg".equals(extension)) {
                                        return true;
                                    } else {
                                        return false;
                                    }
                                }
                                return false;
                            }
                            
                            @Override
                            public String getDescription() {
                                return "Only JPEG";
                            }
                        });
                if (fileChooser.showSaveDialog(canvas) == JFileChooser.APPROVE_OPTION) {
                    ObjectOutputStream out;
                    try {
                        
                        File f = fileChooser.getSelectedFile();
                        if (!(f.getName().trim().endsWith(".jpg") || f
                                .getName().trim().endsWith(".jpeg"))) {
                            f = new File(f.getAbsolutePath() + ".jpeg");
                        }
                        out = new ObjectOutputStream(new FileOutputStream(f));
                        BufferedImage image =
                                new BufferedImage(getWidth(), getHeight(),
                                        BufferedImage.TYPE_INT_RGB);
                        paint(image.createGraphics());
                        ImageIO.write(image, "jpeg", f);
                        out.close();
                    } catch (IOException ex) {
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "cannot save image",
                            "save error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        item2.addActionListener(new ActionListener() {
            
            // Allows the user to right click the graph and save it as a jpeg
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser
                        .setFileFilter(new javax.swing.filechooser.FileFilter() {
                            
                            @Override
                            public boolean accept(File f) {
                                
                                if (f.isDirectory()) {
                                    return true;
                                }
                                String s = f.getName();
                                int i = s.lastIndexOf('.');
                                
                                if (i > 0 && i < s.length() - 1) {
                                    String extension =
                                            s.substring(i + 1).toLowerCase();
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
                // fileChooser.addChoosableFileFilter(new jpgSaveFilter());
                if (fileChooser.showSaveDialog(canvas) == JFileChooser.APPROVE_OPTION) {
                    ObjectOutputStream out;
                    try {
                        
                        File f = fileChooser.getSelectedFile();
                        if (!f.getName().trim().endsWith(".eps")) {
                            f = new File(f.getAbsolutePath() + ".eps");
                        }
                        // out = new ObjectOutputStream(new
                        // FileOutputStream(f));
                        EpsGraphics2D g =
                                new EpsGraphics2D("Title", f, 0, 0, getWidth(),
                                        getHeight());
                        canvas.paint(g);
                        g.flush();
                        System.out.println(g.toString());
                        g.close();
                        // f.close();
                        
                        // BufferedImage image = new BufferedImage(getWidth(),
                        // getHeight(), BufferedImage.TYPE_INT_RGB);
                        // paint(image.createGraphics());
                        // ImageIO.write(image, "eps", f);
                        // out.close();
                    } catch (IOException ex) {
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "cannot save image",
                            "save error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        menu.add(item);
        menu.add(item2);
        
    }
    
    /**
     * 
     * @return tree map of points
     */
    public TreeMap<Double, Double> getPoints() {
        return points;
    }
    
    /**
     * Sets the points
     * 
     * @param points
     */
    public void setPoints(TreeMap<Double, Double> points) {
        this.points = points;
    }
    
    /**
     * Draws all points to the inputed graphics
     * 
     * @param count
     * @param g
     */
    public void drawDataSet(int count, Graphics2D g) {
        if (getPoints().size() == 0) {
            return;
        }
        Set<Double> keys = getPoints().keySet();
        Double previousKey = getPoints().firstKey();
        for (Double key : keys) {
            g.setColor(Color.BLACK);
            g.drawLine(g2cx(previousKey), g2cy(getPoints().get(previousKey)),
                    g2cx(key), g2cy(getPoints().get(key)));
            previousKey = key;
            drawPoint(g, key, getPoints().get(key));
        }
        
    }
    
    public void drawPoint(Graphics2D g, double x, double y) {
    }
    
    public void drawPoint(Graphics2D g, double x, double y, boolean b) {
    }
    
    public int g2cx(double x) {
        double ratio = getRatioX();
        return (int) (x * ratio) + getLeftShift();
    }
    
    public int g2cy(double y) {
        double ratio = getRatioY();
        return (int) (getPlotHeight() - y * ratio);
    }
    
    public double c2gx(int x) {
        double ratio = getRatioX();
        return (x - getLeftShift()) / ratio;
    }
    
    public double c2gy(double toy) {
        double ratio = getRatioY();
        return (toy - getPlotHeight()) / -ratio;
    }
    
    /**
     * Draws the x-axis
     * 
     * @param g
     */
    public void drawXAxis(Graphics2D g) {
        double steps = countHorizontalStep();
        double lstep = countHorizontalLabelStep();
        g.setFont(new Font(g.getFont().getFontName(), 0, 11));
        DecimalFormat df = new DecimalFormat("#.##");
        double min = 0;
        if (xAxis.equals("Frequency")) {
            min = View.viewer.currentNode.fStart;
        }
        for (int i = 0; i < (getPlotWidth() - 1) / steps + 1; i++) {
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine((int) (getLeftShift() + i * steps), tPad,
                    (int) (getLeftShift() + i * steps), getHeight() - bPad);
            g.setColor(Color.BLACK);
            if (xAxis.equals("Frequency (MHz)") && i == 0) {
                g.drawString(View.viewer.currentNode.fStart + "+", 0,
                        getHeight() - 17);
            }
            if (graphTitle
                    .equals("Beam Width: Average Antenna Temperature vs. Angle")) {
                g.drawString(
                        ""
                                + df.format(i * lstep * 100 / 100.0
                                        + View.viewer.min),
                        (int) (getLeftShift() + i * steps - xLabelWidth / 4),
                        getHeight() - 17);
            } else {
                g.drawString("" + df.format(i * lstep * 100 / 100.0),
                        (int) (getLeftShift() + i * steps - xLabelWidth / 4),
                        getHeight() - 17);
            }
            
        }
        g.drawString(" " + xAxis + " ", getLeftShift() + getPlotWidth() / 2,
                getHeight() - 2);
    }
    
    /**
     * Draws the y-axis
     * 
     * @param count
     * @param g
     */
    public void drawYAxis(int count, Graphics2D g) {
        g.setColor(Color.BLACK);
        drawVerticalLine(lPad + (count / 2 + 1) * yLabelWidth, countVerticalStep(), g);
        g.setColor(colors[count % colors.length]);
        drawVerticalMetric(lPad, countVerticalLabelStep(), countVerticalStep(),
                g);
        drawVerticalLabel(count / 2 * yLabelWidth + 5, " " + yAxis, g);
    }
    
    /**
     * Draws vertical lines
     * 
     * @param x
     * @param steps
     * @param g
     */
    public void drawVerticalLine(int x, double steps, Graphics2D g) {
        g.drawLine(x, Canvas.tPad, x, getHeight() - Canvas.bPad);// vertical
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < (getPlotHeight() - 1) / steps + 1; i++) {// horizontal
            g.drawLine(x - 2, (int) (getHeight() - Canvas.bPad - i * steps),
                    x + 2, (int) (getHeight() - Canvas.bPad - i * steps));
        }
    }
    
    /**
     * Draws vertical metric
     * 
     * @param x
     * @param strStep
     * @param plotStep
     * @param g
     */
    public void drawVerticalMetric(int x, double strStep, double plotStep,
            Graphics2D g) {
        g.setFont(new Font(g.getFont().getFontName(), 0, 11));
        for (int i = 0; i < ((getPlotHeight() - 1) / plotStep + 1) / 2; i++) {
            g.drawString(
                    ""
                            + Math.round(c2gy((getHeight() - Canvas.bPad - tPad)
                                    / 2 + tPad - i * plotStep) * 10) / 10.0, x,
                    (int) ((getHeight() - Canvas.bPad - tPad) / 2 + tPad - i
                            * plotStep));
        }
        for (int i = 1; i < ((getPlotHeight() - 1) / plotStep + 1) / 2; i++) {// horizontal
            g.drawString(
                    ""
                            + Math.round(c2gy((getHeight() - Canvas.bPad - tPad)
                                    / 2 + tPad + i * plotStep) * 10) / 10.0,
                    x - 5,
                    (int) ((getHeight() - Canvas.bPad - tPad) / 2 + tPad + i
                            * plotStep));
        }
    }
    
    /**
     * Draws the vertical label
     * 
     * @param x
     * @param title
     * @param g
     */
    public void drawVerticalLabel(int x, String title, Graphics2D g) {
        g.translate(x, getPlotHeight() / 2);
        g.rotate(-Math.PI / 2.0);
        g.drawString(title, 0, 10);
        g.rotate(Math.PI / 2.0);
        g.translate(-x, -getPlotHeight() / 2);
    }
    
    /**
     * Counts the vertical step
     * 
     * @return
     */
    public double countVerticalStep() {
        int count = getPlotHeight() / squareWidth + 1;
        if (count % 2 == 1) {
            count++;
        }
        return (double) getPlotHeight() / count;
    }
    
    /**
     * Counts the horizontal step
     * 
     * @return
     */
    public double countHorizontalStep() {
        int count = getPlotWidth() / squareWidth + 1;
        return (double) getPlotWidth() / count;
    }
    
    /**
     * Vertical Label step
     * 
     * @return
     */
    public double countVerticalLabelStep() {
        int count = getPlotHeight() / squareWidth + 1;
        int max;
        if (getPoints() == null || getMaxYPoint() == null) {
            max = countMax(defaultY);// 50
        } else {
            max =
                    countMaxVertical(Math.max(Math.abs(getMaxYPoint()),
                            Math.abs(getMinYPoint())));// 50
        }
        return (double) max / (double) count;
    }
    
    /**
     * Returns the x-value of the point with the largest y-value
     * 
     * @return
     */
    public Double getMaxYPoint() {
        if (getPoints() == null || getPoints().size() == 0) {
            return Canvas.defaultY + 0.0;
        }
        double max = getPoints().firstEntry().getValue();
        Set<Double> keys = getPoints().keySet();
        for (Double key : keys) {
            if (getPoints().get(key) > max) {
                max = getPoints().get(key);
            }
        }
        if (max < 1) {
            return Canvas.defaultY + 0.0;
        }
        return max + max * .1;
    }
    
    /**
     * Returns the x-value of the point with the smallest y-value
     * 
     * @return
     */
    public Double getMinYPoint() {
        if (getPoints().size() == 0) {
            return (double) -Canvas.defaultY;
        }
        double min = getPoints().firstEntry().getValue();
        Set<Double> keys = getPoints().keySet();
        for (Double key : keys) {
            if (getPoints().get(key) < min) {
                min = getPoints().get(key);
            }
        }
        return min;
    }
    
    /**
     * Returns max y (x-value) - min y (x-value)
     * 
     * @return
     */
    public double getMaxYRange() {
        if (getMaxYPoint() - getMinYPoint() < 2) {
            return 2.0;
        } else {
            return getMaxYPoint() - getMinYPoint();
        }
    }
    
    /**
     * Horizontal label step
     * 
     * @return
     */
    public double countHorizontalLabelStep() {// NOT FINISHED
        int count = getPlotWidth() / squareWidth + 1;
        double max = getMaxX();
        return max / count;
    }
    
    public int countMax(double max) {
        int i = 0;
        boolean end = false;
        while (!end) {
            if (max
                    / (steps[i % steps.length] * (int) Math.pow(10, i
                            / steps.length)) < 1) {
                end = true;
            }
            i++;
        }
        i--;
        return (int) Math.pow(
                2,
                (int) Math.ceil(Math.log(max / adapter.getDeltaBaseline())
                        / Math.log(2)));
    }
    
    public int countMaxVertical(double max) {
        int i = 0;
        boolean end = false;
        while (!end) {
            if (max
                    / (steps[i % steps.length] * (int) Math.pow(10, i
                            / steps.length)) < 1) {
                end = true;
            }
            i++;
        }
        i -= 2;
        return steps[i % steps.length] * (int) Math.pow(10, i / steps.length);
    }
    
    public static int countHorizontalMax(double max) {
        return (int) Math.pow(2, (int) Math.ceil(Math.log(max) / Math.log(2)));
    }
    
    public double getRatioX() {
        if (getPoints() == null) {
            return (double) getPlotWidth() / (double) countMax(defaultY);
        }
        return getPlotWidth() / getMaxX();
    }
    
    public double getRatioY() {
        if (getPoints() == null || getMaxYPoint() == null) {
            return getPlotHeight() / 1.0;
        } else {
            return getPlotHeight() / getMaxY();
        }
    }
    
    protected int getPlotWidth() {
        return getWidth() - yLabelWidth - lPad - rPad;
    }
    
    protected int getPlotHeight() {
        return getHeight() - Canvas.tPad - Canvas.bPad;
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
            maxX = defaultY;
            return defaultY;
        }
        maxX = getPoints().lastKey();
        if (xAxis.equals("Frequency (MHz)")) {
            return getPoints().lastKey();
        } else {
            return Math.ceil(maxX / (getPlotWidth() / squareWidth + 1))
                    * (getPlotWidth() / squareWidth + 1);
            // return this.getPoints().lastKey();
        }
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
    
    public View getView() {
        return view;
    }
    
    public void setView(View view) {
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
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            menu.show(this, e.getX(), e.getY());
        }
    }
    
    @Override
    public void mouseEntered(MouseEvent arg0) {
        // Do nothing
    }
    
    @Override
    public void mouseExited(MouseEvent arg0) {
        // Do nothing
    }
    
    /**
     * Returns the object MyPoint which is in closer then MyCanvas.r to
     * coordinates [x,y]
     * 
     * @param x
     *            int
     * @param y
     *            int
     * @return MyPoint
     */
    protected Double getPointOnGraph(int x, int y) {
        
        Set<Double> keys = getPoints().keySet();
        for (Double key : keys) {
            if (new SquareOrnament().isInside(mCanx, mCany, g2cx(key),
                    g2cy(getPoints().get(key)))) {
                return key;
            }
        }
        return null;
    }
    
    protected Double getVerticallyPointOnGraph(int x, int y) {
        
        Set<Double> keys = getPoints().keySet();
        for (Double key : keys) {
            if (new SquareOrnament().isInsideVertically(mCanx, mCany,
                    g2cx(key), g2cy(getPoints().get(key)))) {
                return key;
            }
            
        }
        return null;
    }
    
    /**
     * records where mouse was pressed and whether there is any point in less
     * distance then MyCanvas.r
     */
    @Override
    public void mousePressed(MouseEvent evt) {
        mCanx = evt.getX();
        mCany = evt.getY();
        double p = 0;
        
        if (View.viewer.choosingDelete && getPointOnGraph(mCanx, mCany) != null
                && evt.getButton() != MouseEvent.BUTTON2) {
            p = getPointOnGraph(mCanx, mCany);
            if (View.viewer.deleting[(int) p - 1] != -1) {
                drawPoint((Graphics2D) getGraphics(), p, getPoints().get(p),
                        false);
                View.viewer.deleting[(int) p - 1] = -1;
                
            } else if (View.viewer.deleting[(int) p - 1] == -1) {
                drawPoint((Graphics2D) getGraphics(), p, getPoints().get(p));
                View.viewer.deleting[(int) p - 1] = 0;
                
            }
        }
    }
    
    /**
     * Records coordinates mouse was released and if there was no currentPoint,
     * then creates new point with particular coordinates
     */
    @Override
    public void mouseReleased(MouseEvent evt) {
        
    }
    
    @Override
    public void mouseDragged(MouseEvent evt) {
        
        mCanx = evt.getX();
        mCany = evt.getY();
        double p = 0;
        if (evt.getButton() != MouseEvent.BUTTON2
                && getVerticallyPointOnGraph(mCanx, mCany) != null
                && View.viewer.choosingDelete) {
            p = getVerticallyPointOnGraph(mCanx, mCany);
            
            if (View.viewer.deleting[(int) p - 1] != -1) {
                drawPoint((Graphics2D) getGraphics(), p, getPoints().get(p),
                        false);
                View.viewer.deleting[(int) p - 1] = -1;
            }
        }
        if (evt.getButton() == MouseEvent.BUTTON2
                && getVerticallyPointOnGraph(mCanx, mCany) != null
                && View.viewer.choosingDelete) {
            p = getVerticallyPointOnGraph(mCanx, mCany);
            
            if (View.viewer.deleting[(int) p - 1] == -1) {
                drawPoint((Graphics2D) getGraphics(), p, getPoints().get(p));
                View.viewer.deleting[(int) p - 1] = 0;
            }
        }
    }
    
    @Override
    public void mouseMoved(MouseEvent evt) {
        DecimalFormat df = new DecimalFormat("#.##");
        mCanx = evt.getX();
        mCany = evt.getY();
        double min = 0;
        if (xAxis.equals("Frequency") && View.viewer.currentNode != null) {
            min = View.viewer.currentNode.fStart;
            df = new DecimalFormat("#.###");
        }
        if (View.viewer.vGraph.graphTitle.equals("Averge TA vs. Order")
                && getPointOnGraph(mCanx, mCany) != null) {
            double p = getVerticallyPointOnGraph(mCanx, mCany);
            setToolTipText("[" + df.format(p + min) + "; "
                    + df.format(getPoints().get(p)) + "]  Data Block: "
                    + View.viewer.taPlotted[(int) p] + ")");
        } else if (graphTitle
                .equals("Beam Width: Average Antenna Temperature vs. Angle")
                && getPointOnGraph(mCanx, mCany) != null) {
            double p = getVerticallyPointOnGraph(mCanx, mCany);
            setToolTipText("[" + df.format(p + View.viewer.min) + "; "
                    + df.format(getPoints().get(p)) + "]");
        } else if (getPointOnGraph(mCanx, mCany) != null) {
            double p = getVerticallyPointOnGraph(mCanx, mCany);
            setToolTipText("[" + df.format(p + min) + "; "
                    + df.format(getPoints().get(p)) + "]");
        }
        if (getVerticallyPointOnGraph(mCanx, mCany) != null) {
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            
        } else {
            setCursor(Cursor.getDefaultCursor());
            setToolTipText("");
        }
    }
    
    @Override
    public void update(Graphics g) {
        paint(g);
    }
    
    @Override
    public void paint(Graphics g) {
        // create the hardware accelerated image.
        createBackBuffer();
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        // Main rendering loop. Volatile images may lose their contents.
        // This loop will continually render to (and produce if neccessary)
        // volatile images
        // until the rendering was completed successfully.
        do {
            
            // Validate the volatile image for the graphics configuration of
            // this
            // component. If the volatile image doesn't apply for this graphics
            // configuration
            // (in other words, the hardware acceleration doesn't apply for the
            // new device)
            // then we need to re-create it.
            GraphicsConfiguration gc = getGraphicsConfiguration();
            int valCode = volatileImg.validate(gc);
            
            // This means the device doesn't match up to this hardware
            // accelerated image.
            if (valCode == VolatileImage.IMAGE_INCOMPATIBLE) {
                createBackBuffer(); // recreate the hardware accelerated image.
            }
            
            Graphics offscreenGraphics = volatileImg.getGraphics();
            doPaint(offscreenGraphics); // call core paint method.
            
            // paint back buffer to main graphics
            g.drawImage(volatileImg, 0, 0, this);
            // Test if content is lost
        } while (volatileImg.contentsLost());
    }
    
    protected void doPaint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        setBackground(Color.WHITE);
        
        drawXAxis(g2); // draw vertical lines
        g2.setColor(Color.BLACK);
        int i = 0;
        drawYAxis(i++, g2); // draw vertical axis
        // draw axes
        g2.setColor(Color.BLACK);
        g2.drawLine(getLeftShift(), g2cy(0.0), getLeftShift() + getPlotWidth(),
                g2cy(0.0));// horizontal// draw horizontal axis
        g2.drawString(graphTitle, getWidth() / 2 - 80, tPad / 2);
        i = 0;
        g2.setColor(colors[0]);
        drawDataSet(i, g2);
        i++;
    }
    
    // //// D
    
    protected void paintSinc(Graphics2D g) {
        g.setColor(Color.red);
        double last = -maxX;
        for (double i = -maxX + getPlotWidth() / 80; i < maxX; i += .5) {
            g.drawLine(g2cx(i), g2cy(sinc(i)
                    * (points.containsKey(0.0) ? points.get(0.0) : 1)),
                    g2cx(last), g2cy(sinc(last)
                            * (points.containsKey(0.0) ? points.get(0.0) : 1)));
            last = i;
        }
    }
    
    /**
     * Calculates Model
     * 
     * @param phi
     * @return
     */
    protected double sinc(double phi) {
        if (phi == 0) {
            return 1;
        }
        return 100
                * Math.pow(
                        Math.sin(Math.PI * view.getD()
                                * Math.sin(phi * Math.PI / 180)
                                / adapter.getLambda()), 2)
                / Math.pow(
                        Math.PI * view.getD() * Math.sin(phi * Math.PI / 180)
                                / adapter.getLambda(), 2);
        
    }
    
    protected void createBackBuffer() {
        GraphicsConfiguration gc = getGraphicsConfiguration();
        volatileImg = gc.createCompatibleVolatileImage(getWidth(), getHeight());
    }
    
}
