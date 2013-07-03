package beam.View;

import java.awt.BasicStroke;
import java.awt.Color;
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
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Set;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.sourceforge.jlibeps.epsgraphics.EpsGraphics2D;

import common.View.CommonVSRTICanvas;
import common.View.ViewUtilities;

/**
 * Base class for graphs
 */

@SuppressWarnings("serial")
public abstract class Canvas extends CommonVSRTICanvas implements MouseListener {
    
    public View view;
    
    /**
     * controls the spacing between marks on the x axis
     */
    protected int squareWidth = 60;
    protected int yLabelWidth = 10;
    protected double defaultY = 25;
    protected double defaultXLeft = -45;
    protected double defaultXRight = 45;
    protected int mCanx, mCany;
    protected Double currentPoint;
    protected Color[] colors = {Color.BLACK};
    protected VolatileImage volatileImg;
    final JPopupMenu menu = new JPopupMenu();
    protected int mouseButton = 0;
    protected double maxX = 40;
    private JFileChooser fileChooser;
    
    /**
     * determines the size of the title
     */
    private int titleSize = 30;
    
    private Canvas canvas;
    
    public Canvas(View v, TreeMap<Double, Double> g) {
        canvas = this;
        points = g;
        setView(v);
        
        lPad = 90;
        rPad = 30;
        tPad = 50;
        bPad = 60;
        
        addMouseListener(this);
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
                        paintForSave(image.createGraphics());
                        ImageIO.write(image, "jpeg", f);
                        out.close();
                    } catch (IOException ex) {}
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
                        canvas.paintForSave(g);
                        g.flush();
                        // System.out.println(g.toString());
                        g.close();
                        // f.close();
                        
                        // BufferedImage image = new BufferedImage(getWidth(),
                        // getHeight(), BufferedImage.TYPE_INT_RGB);
                        // paint(image.createGraphics());
                        // ImageIO.write(image, "eps", f);
                        // out.close();
                    } catch (IOException ex) {}
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
            g.setStroke(new BasicStroke(strokeSize));
            g.drawLine(g2cx(previousKey), g2cy(points.get(previousKey)), g2cx(key), g2cy(points.get(key)));
            previousKey = key;
            System.out.println("point at " + key + " - ");
            drawPoint(g, key, points.get(key));
        }
        
    }
    
    public void drawPoint(Graphics2D g, double x, double y) {}
    
    public double countVerticalStep() {// NOT FINISHED
        /*
         * int count = this.getPlotHeight() / squareWidth + 1; if (count % 2 ==
         * 1) { count++; } return (double) this.getPlotHeight() / count;
         */
        return squareWidth;
    }
    
    /*
     * probably unnecessary public double countVerticalLabelStep() { double
     * count = this.getPlotHeight() / squareWidth + 1;//30 double max; if
     * (points == null || getMaxY() == null) { max = countMax(defaultY);//50 }
     * else { max = countMaxVertical(getMaxY());//50 } return (double) max /
     * (double) count; }
     */
    
    /*
     * probably unnecessary public double countHorizontalLabelStep() {// NOT
     * FINISHED double count = this.getPlotWidth() / squareWidth; double max =
     * getMaxX()-getMinX(); return max / count; }
     */
    
    /*
     * probably unnecessary public int countMax(double max) { int i = 0; boolean
     * end = false; while (!end) { if (max / (steps[i % steps.length] * (int)
     * Math.pow(10, i / steps.length)) < 1) { end = true; } i++; } i--;
     * //System.
     * out.println("countMax("+max+") = "+steps[i%steps.length]*(int)Math
     * .pow(10,i/steps.length)); //return
     * (int)Math.pow(2,(int)(Math.ceil(Math.log(max)/Math.log(2)))); return
     * (int) Math.pow(2, (int) (Math.ceil(Math.log(max /
     * adapter.getDeltaBaseline()) / Math.log(2)))); }
     */
    
    /*
     * probably unnecessary public int countMaxVertical(double max) { return
     * (int)(max*1.1); // int i = 0; // boolean end = false; // while (!end) {
     * // if (max / (steps[i % steps.length] * (int) Math.pow(10, i /
     * steps.length)) < 1) { // end = true; // } // i++; // } // i -= 2; //
     * //System
     * .out.println("countMaxVertical is "+steps[i%steps.length]*(int)Math
     * .pow(10,i/steps.length)); // return steps[i % steps.length] * (int)
     * Math.pow(10, i / steps.length); }
     */
    
    public static int countHorizontalMax(double max) {
        return (int) Math.pow(2, (int) Math.ceil(Math.log(max) / Math.log(2)));
    }
    
    /**
     * @return The greatest y value in the data set. If no data set is currently
     *         displayed, returns 1.1 (if the beam pattern is showing) or the
     *         default y value.
     */
    @Override
    public double getMaxY() {
        if (points == null || points.size() == 0) {
            if (view.isBeamPatternVisible()) {
                return bessel(0.0) * view.getModel().getPeakValue() * 1.2;
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
    
    @Override
    public double getMaxX() {
        if (points == null || points.size() == 0) {
            return defaultXRight;
        }
        return points.lastKey();
    }
    
    @Override
    public double getMinX() {
        if (points == null || points.size() == 0) {
            return defaultXLeft;
        }
        return points.firstKey();
    }
    
    // GETTERS AND SETTERS
    
    public View getView() {
        return view;
    }
    
    public void setView(View view) {
        this.view = view;
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
    
    public double getDefaultY() {
        return defaultY;
    }
    
    public void setDefaultY(double defaultY) {
        this.defaultY = defaultY;
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
    public void mouseEntered(MouseEvent e) {}
    
    @Override
    public void mouseExited(MouseEvent e) {}
    
    @Override
    public void mousePressed(MouseEvent evt) {}
    
    @Override
    public void mouseReleased(MouseEvent evt) {}
    
    @Override
    public void update(Graphics g) {
        paint(g);
    }
    
    public void paintForSave(Graphics g) {
        // create the hardware accelerated image.
        createBackBuffer();
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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
    
    @Override
    public void paint(Graphics g) {
        // create the hardware accelerated image.
        createBackBuffer();
        
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
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
            ((Graphics2D) offscreenGraphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
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
        g.setFont(new Font(g.getFont().getFontName(), 0, titleSize));
        g2.drawString(graphTitle, (getWidth() - g2.getFontMetrics().stringWidth(graphTitle)) / 2, (tPad + g2
                .getFontMetrics().getHeight() / 2) / 2);
        i = 0;
        g2.setColor(colors[0]);
        drawDataSet(i, g2);
        if (view.isBeamPatternVisible()) {
            paintPattern(g2);
        }
        i++;
        
    }
    
    protected void paintPattern(Graphics2D g) {
        System.out.println("painting model");
        g.setColor(Color.red);
        double previous = getMinX();
        /*
         * for (Double p : points.keySet()) {
         * // System.out.println("drawing from " + previous + " to " + p + ", bessel = " + bessel(p));
         * g.drawLine(g2cx(p), g2cy(bessel(p) * points.get(p)), g2cx(previous), g2cy(bessel(previous) *
         * points.get(previous)));
         * previous = p;
         * }
         */
        
        g.setColor(Color.blue);
        previous = getMinX();
        for (double i = getMinX(); i <= getMaxX(); i += .5) {
            g.drawLine(g2cx(i), g2cy(bessel(i) * view.getModel().getPeakValue()), g2cx(previous), g2cy(bessel(previous)
                    * view.getModel().getPeakValue()));
            previous = i;
        }
        System.out.println("Min x: " + getMinX() + "\nMax x: " + getMaxX() + "\nmax y: " + getMaxY() + "\npoints(0): "
                + points.get(0.0));
    }
    
    /**
     * @param theta
     *            the angle (in degrees)
     * @return
     */
    protected double bessel(double theta) {
        // System.out.println("bessel call: " + theta);
        if (theta == 0) {
            return 1;
        }
        double x = besselX(theta);
        // System.out.println("bessel x = " + x);
        return Math.pow(2 * ViewUtilities.besselJ(x) / x, 2);
    }
    
    /**
     * 
     * @param theta
     *            the angle (in degrees)
     * @return x where x equals pi*D*theta(in radians)/lambda
     */
    private double besselX(double theta) {
        // System.out.println("view d = " + view.getD() + "view lambda = " + view.getLambda());
        return Math.PI * view.getModel().getDiameter() * theta * Math.PI / 180 / view.getModel().getLambda();
    }
    
    protected void createBackBuffer() {
        GraphicsConfiguration gc = getGraphicsConfiguration();
        volatileImg = gc.createCompatibleVolatileImage(getWidth(), getHeight());
    }
}