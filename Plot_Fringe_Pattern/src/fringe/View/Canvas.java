package fringe.View;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
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

import common.View.SquareOrnament;

import fringe.Model.Adapter;

/**
 * Base class for graphs
 */

@SuppressWarnings("serial")
public abstract class Canvas extends JPanel implements MouseListener, MouseMotionListener {
    
    private TreeMap<Double, Double> points;
    public View view;
    public Adapter adapter;
    protected int lPad = 90, rPad = 30, tPad = 50, bPad = 60;
    /**
     * controls the spacing between marks on the x axis
     */
    protected int squareWidth = 60;
    protected int yLabelWidth = 10;
    protected int xLabelWidth = 30;
    protected double defaultY = 9;
    protected double defaultXLeft = -20;
    protected double defaultXRight = 20;
    protected int mCanx, mCany;
    protected Double currentPoint;
    protected Color[] colors = { Color.BLACK };
    protected VolatileImage volatileImg;
    protected String xAxisTitle = "x-axis";
    protected String yAxisTitle = "y-axis";
    protected String graphTitle = "Untitled";
    final JPopupMenu menu = new JPopupMenu();
    protected int mouseButton = 0;
    protected double maxX = 40;
    private JFileChooser fileChooser;
    // TODO find the right stroke thickness and font size
    private float stroke = 5;
    /**
     * determines the size of the axis labels and numbers
     */
    private int fontSize = 20;
    /**
     * determines the size of the title
     */
    private int titleSize = 30;
    
    private Canvas canvas;
    
    public Canvas(View v, Adapter a, TreeMap<Double, Double> g) {
        canvas = this;
        points = g;
        setAdapter(a);
        setView(v);
        addMouseListener(this);
        addMouseMotionListener(this);
        setSize(new Dimension(200, 50));
        setVisible(true);
        fileChooser = new JFileChooser();
        JMenuItem item = new JMenuItem("Save as JPEG");
        JMenuItem item2 = new JMenuItem("Save as EPS");
        item.addActionListener(new ActionListener() {
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
                // fileChooser.addChoosableFileFilter(new jpgSaveFilter());
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
                    JOptionPane.showMessageDialog(null, "cannot save image", "save error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        item2.addActionListener(new ActionListener() {
            
            // Allows the user to right click the graph and save it as a jpeg
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
                // fileChooser.addChoosableFileFilter(new jpgSaveFilter());
                if (fileChooser.showSaveDialog(canvas) == JFileChooser.APPROVE_OPTION) {
                    // ObjectOutputStream out;
                    try {
                        
                        File f = fileChooser.getSelectedFile();
                        if (!f.getName().trim().endsWith(".eps")) {
                            f = new File(f.getAbsolutePath() + ".eps");
                        }
                        // out = new ObjectOutputStream(new
                        // FileOutputStream(f));
                        EpsGraphics2D g = new EpsGraphics2D("Title", f, 0, 0, getWidth(), getHeight());
                        canvas.paint(g);
                        g.flush();
                        // System.out.println(g.toString());
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
                    JOptionPane.showMessageDialog(null, "cannot save image", "save error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        menu.add(item);
        menu.add(item2);
    }
    
    public TreeMap<Double, Double> getPoints() {
        return points;
    }
    
    public void setPoints(TreeMap<Double, Double> points) {
        this.points = points;
    }
    
    public void drawDataSet(int count, Graphics2D g) {
        if (points.size() == 0) {
            return;
        }
        Set<Double> keys = points.keySet();
        Double previousKey = points.firstKey();
        for (Double key : keys) {
            if (Math.abs(key) > 40) {
                continue;
            }
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(stroke));
            g.drawLine(g2cx(previousKey), g2cy(points.get(previousKey)), g2cx(key), g2cy(points.get(key)));
            previousKey = key;
            System.out.println("point at " + key + " - ");
            drawPoint(g, key, points.get(key));
        }
        
    }
    
    public void drawPoint(Graphics2D g, double x, double y) {
    }
    
    public int g2cx(double x) {
        double ratio = getRatioX();
        return (int) (getLeftShift() + (x - getMinX()) * ratio)
        // + getPlotWidth()/2;
        ;
    }
    
    /**
     * @param y
     *            the y coordinate of the graph, in units
     * @return the y coordinate of the canvas, in pixels
     */
    public int g2cy(double y) {
        double ratio = getRatioY();
        // height-(height-t-b)/2-bpad-y*ratio
        // (2*height-heihgt+t+b)/2 - b - y*ratio
        return (int) (getPlotHeight() + tPad - y * ratio);
        // return (int) (this.getPlotHeight() / 2 + tPad - y * ratio);
        // return (int)((this.getHeight()+tPad+bPad)/2-y*ratio-bPad);
    }
    
    public double c2gx(double x) {
        double ratio = getRatioX();
        
        return (x - getLeftShift()) / ratio + getMinX();
    }
    
    /**
     * @param toy
     *            the y coordinate of the canvas, in pixels
     * @return the y coordinate of the graph, in units
     */
    public double c2gy(double toy) {
        double ratio = getRatioY();
        // return (toy+bPad-getHeight())/-ratio;
        return (toy - getPlotHeight() - tPad) / -ratio;
        // return (getPlotHeight()-toy)/ratio;
    }
    
    public void drawXAxis(Graphics2D g) {
        double steps = squareWidth;
        g.setStroke(new BasicStroke(stroke));
        g.setFont(new Font(g.getFont().getFontName(), 0, fontSize));
        DecimalFormat df = new DecimalFormat("#.#");
        
        for (int i = 0; i < Math.round(getPlotWidth() / squareWidth + 0.5); i++) {// horizontal
            // draw vertical lines
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine((int) (getLeftShift() + i * steps), tPad, (int) (getLeftShift() + i * steps), getHeight() - bPad);
            // draw numbers at each vertical line
            g.setColor(Color.BLACK);
            String lString = df.format(c2gx(getLeftShift() + i * steps));
            
            FontMetrics fm = g.getFontMetrics();
            g.drawString(lString, (int) (getLeftShift() + i * steps - fm.stringWidth(lString) / 2), getHeight() - bPad + fm.getAscent()
                    + fm.getLeading() + 5);
        }
        // draw axis title
        g.drawString(" " + xAxisTitle + " ", getLeftShift() + getPlotWidth() / 2 - 50, getHeight() - 10);
        // draw horizontal axis
        g.drawLine(getLeftShift(), g2cy(0.0), getWidth() - rPad, g2cy(0.0));
    }
    
    public void drawYAxis(Graphics2D g) {
        drawVerticalLine(getLeftShift(), g);
        drawVerticalMetric(g);
        drawVerticalLabel(g);
    }
    
    /**
     * Draws the vertical line for the y axis
     * 
     * @param x
     *            the x value where the line will be drawn
     * @param g
     *            the current Graphics2D object
     */
    public void drawVerticalLine(int x, Graphics2D g) {
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(stroke));
        g.drawLine(x, tPad, x, getHeight() - bPad);// vertical
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i <= getPlotHeight() / squareWidth; i++) {// horizontal
            int markHeight = getHeight() - bPad - i * squareWidth;
            g.drawLine(x - 2, markHeight, x + 2, markHeight);
        }
    }
    
    public void drawVerticalMetric(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font(g.getFont().getFontName(), 0, fontSize));
        
        FontMetrics fm = g.getFontMetrics();
        DecimalFormat df = new DecimalFormat("0.00");
        for (int i = 0; i <= getPlotHeight() / squareWidth; i++) {// horizontal
            // String tempString="" + Math.round(c2gy(this.getHeight() - bPad - (i) * plotStep) * 10) / 10.0;
            String tempString = df.format(c2gy(getHeight() - bPad - i * squareWidth));
            // System.out.println(c2gy(this.getHeight() - bPad - (i) * squareWidth));
            // System.out.println(this.getHeight() - bPad - (i) * squareWidth);
            g.drawString(tempString, lPad - fm.stringWidth(tempString) - 5, getHeight() - bPad - i * squareWidth + fm.getAscent() / 2);
        }
    }
    
    /**
     * Draws the label for the y axis
     */
    public void drawVerticalLabel(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font(g.getFont().getFontName(), 0, fontSize));
        
        int translateDown = (getPlotHeight() + g.getFontMetrics().stringWidth(yAxisTitle)) / 2;
        
        g.translate(yLabelWidth, translateDown);
        g.rotate(-Math.PI / 2.0);
        
        g.drawString(yAxisTitle, 0, 10);
        
        g.rotate(Math.PI / 2.0);
        g.translate(-yLabelWidth, -translateDown);
    }
    
    public double countVerticalStep() {// NOT FINISHED
        int count = getPlotHeight() / squareWidth + 1;
        if (count % 2 == 1) {
            count++;
        }
        return (double) getPlotHeight() / count;
    }
    
    public double countHorizontalStep() {
        int count = getPlotWidth() / squareWidth + 1;
        return (double) getPlotWidth() / count;
    }
    
    public Double getMaxYPoint() {
        if (points == null || points.size() == 0) {
            return sinc(0);
        }
        double max = points.firstEntry().getValue();
        Set<Double> keys = points.keySet();
        for (Double key : keys) {
            if (points.get(key) > max) {
                max = points.get(key);
            }
        }
        if (max < 1) {
            return Math.max(sinc(0), 1.0);
        }
        return Math.max(sinc(0), max);
    }
    
    public static int countHorizontalMax(double max) {
        return (int) Math.pow(2, (int) Math.ceil(Math.log(max) / Math.log(2)));
    }
    
    public double getRatioX() {
        /*
         * if (points == null) { return (double) getPlotWidth() / (double)
         * countMax(defaultY); }
         */
        return getPlotWidth() / (getMaxX() - getMinX());
    }
    
    public double getRatioY() {
        return getPlotHeight() / getMaxY();
    }
    
    /**
     * @return The width of the plot section of the canvas, in pixels
     */
    protected int getPlotWidth() {
        return getWidth() - lPad - rPad;
    }
    
    /**
     * @return The height of the plot section of the canvas, in pixels
     */
    protected int getPlotHeight() {
        return getHeight() - tPad - bPad;
    }
    
    /**
     * @return The greatest y value in the data set. If no data set is currently
     *         displayed, returns 1.1 (if the beam pattern is showing) or the
     *         default y value.
     */
    public double getMaxY() {
        if (points == null || points.size() == 0) {
            if (view.showSinc && sinc(0) > 0) {
                return sinc(0.0) * 1.1;
            } else {
                return defaultY;
            }
        }
        
        double max = points.firstEntry().getValue();
        Set<Double> keys = points.keySet();
        for (Double key : keys) {
            if (points.get(key) > max) {
                max = points.get(key);
            }
        }
        if (max < 1) {
            return 1.0;
        }
        return max + max * .1;
    }
    
    public double getMaxX() {
        if (points == null || points.size() == 0) {
            return defaultXRight;
        }
        return points.lastKey();
    }
    
    public double getMinX() {
        if (points == null || points.size() == 0) {
            return defaultXLeft;
        }
        return points.firstKey();
    }
    
    public int getLeftShift() {
        return lPad;
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
    
    public int getLPad() {
        return lPad;
    }
    
    public void setLPad(int pad) {
        lPad = pad;
    }
    
    public int getRPad() {
        return rPad;
    }
    
    public void setRPad(int pad) {
        rPad = pad;
    }
    
    public int getTPad() {
        return tPad;
    }
    
    public void setTPad(int pad) {
        tPad = pad;
    }
    
    public int getBPad() {
        return bPad;
    }
    
    public void setBPad(int pad) {
        bPad = pad;
    }
    
    public int getSquareWidth() {
        return squareWidth;
    }
    
    public void setSquareWidth(int squareWidth) {
        this.squareWidth = squareWidth;
    }
    
    public int getYLabelWidth() {
        return yLabelWidth;
    }
    
    public void setYLabelWidth(int labelWidth) {
        yLabelWidth = labelWidth;
    }
    
    public int getXLabelWidth() {
        return xLabelWidth;
    }
    
    public void setXLabelWidth(int labelWidth) {
        xLabelWidth = labelWidth;
    }
    
    public double getDefaultY() {
        return defaultY;
    }
    
    public void setDefaultY(int defaultY) {
        this.defaultY = defaultY;
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
    
    public Color[] getColors() {
        return colors;
    }
    
    public void setColors(Color[] colors) {
        this.colors = colors;
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
        
        Set<Double> keys = points.keySet();
        for (Double key : keys) {
            if (new SquareOrnament().isInside(mCanx, mCany, g2cx(key), g2cy(points.get(key)))) {
                return key;
            }
        }
        return null;
    }
    
    protected Double getVerticallyPointOnGraph(int x, int y) {
        
        Set<Double> keys = points.keySet();
        for (Double key : keys) {
            if (new SquareOrnament().isInsideVertically(mCanx, mCany, g2cx(key), g2cy(points.get(key)))) {
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
        // this.getVision().getCare().set(this.getVision().getSoul().newMemento());
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
            // System.out.println("moving to ["+tox+","+toy+"]");
            adapter.moveVisibilityPoint(getCurrentPoint(), c2gy(toy));
            // getCurrentPoint().movePoint(getCurrentPoint(),
            // c2gx(tox),c2gy(toy,getCurrentPoint().getDataSet()));
        }
        // System.out.println("Current point is "+currentPoint);
    }
    
    /**
     * Records coordinates mouse was released and if there was no currentPoint,
     * then creates new point with particular coordinates
     */
    @Override
    public void mouseReleased(MouseEvent evt) {
        mouseButton = 0;
        // NOT FINISHED (just redo getCurrentDataSet to
        // getViewer().getCurrentDataSet
        mCanx = evt.getX();
        mCany = evt.getY();
        // System.out.println("released");
        if (currentPoint == null && mCanx >= getLeftShift() && mCanx <= getLeftShift() + getPlotWidth() && mCany >= tPad
                && mCany < getWidth() - bPad) {
            // getCurrentDataSet().addPoint(c2gx(mCanx),c2gy(mCany,getCurrentDataSet()));
            // update();
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
        
        // System.out.println("currentpoint="+currentPoint);
        if (getCurrentPoint() != null) {
            // double tox = Math.min(Math.max(getLeftShift(), mCanx), getLeftShift() + getPlotWidth());
            double toy = Math.min(Math.max(tPad, mCany), getHeight() - bPad);
            // System.out.println("moving to ["+tox+","+toy+"]");
            adapter.moveVisibilityPoint(getCurrentPoint(), c2gy(toy));
            // getCurrentPoint().movePoint(getCurrentPoint(),
            // c2gx(tox),c2gy(toy,getCurrentPoint().getDataSet()));
        }
    }
    
    @Override
    public void mouseMoved(MouseEvent evt) {
        DecimalFormat df = new DecimalFormat("#.##");
        mCanx = evt.getX();
        mCany = evt.getY();
        if (getPointOnGraph(mCanx, mCany) != null) {
            double p = getVerticallyPointOnGraph(mCanx, mCany);
            setToolTipText("[" + df.format(p) + "; " + df.format(points.get(p)) + "]");
        }
        if (getVerticallyPointOnGraph(mCanx, mCany) != null) {
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            
        } else {
            setCursor(Cursor.getDefaultCursor());
            setToolTipText("");
        }
        // this.drawCoor((Graphics2D)getGraphics());
    }
    
    @Override
    public void update(Graphics g) {
        paint(g);
    }
    
    @Override
    public void paint(Graphics g) {
        // create the hardware accelerated image.
        createBackBuffer();
        
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
    
    // main paint method
    protected void doPaint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        setBackground(Color.WHITE);
        
        drawXAxis(g2); // draw vertical lines
        g2.setColor(Color.BLACK);
        int i = 0;
        drawYAxis(g2); // draw vertical axis
        // draw axes
        g2.setColor(Color.BLACK);
        // g2.drawLine(this.getLeftShift(), g2cy(0.0), this.getLeftShift() +
        // this.getPlotWidth(), g2cy(0.0));//horizontal// draw horizontal axis
        g.setFont(new Font(g.getFont().getFontName(), 0, titleSize));
        g2.drawString(graphTitle, (getWidth() - g2.getFontMetrics().stringWidth(graphTitle)) / 2,
                (tPad + g2.getFontMetrics().getHeight() / 2) / 2);
        i = 0;
        g2.setColor(colors[0]);
        drawDataSet(i, g2);
        if (view.showSinc) {
            paintVis(g2);
        }
        i++;
    }
    
    /**
     * Draws Model
     * 
     * @param g
     */
    protected void paintVis(Graphics2D g) {
        g.setColor(Color.red);
        double last = -maxX;
        for (double i = -maxX + getPlotWidth() / 80; i < maxX; i += .2) {
            System.out.println("Drawing sinc at " + i + "," + sinc(i) + "]");
            g.drawLine(g2cx(i), g2cy(sinc(i)), g2cx(last), g2cy(sinc(last)));
            last = i;
        }
    }
    
    protected double sinc(double phi) {
        phi = phi * Math.PI / 180;
        return Math.sqrt(view.t1 * view.t1 + view.t2 * P(phi) * view.t2 * P(phi) + 2 * view.t1 * view.t2 * P(phi)
                * Math.cos(2 * Math.PI * view.getModel().getDeltaBaseline() * Math.sin(phi) / view.getModel().getLambda()));
        //
    }
    
    protected double P(double phi) {
        if (phi == 0.0) {
            return 1;
        }
        return Math.pow(Math.sin(Math.PI * view.getModel().getDiameter() * Math.sin(phi) / view.getModel().getLambda()), 2)
                / Math.pow(Math.PI * view.getModel().getDiameter() * Math.sin(phi) / view.getModel().getLambda(), 2);
    }
    
    protected void createBackBuffer() {
        GraphicsConfiguration gc = getGraphicsConfiguration();
        volatileImg = gc.createCompatibleVolatileImage(getWidth(), getHeight());
    }
}