package visibilities.View;

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

import visibilities.Model.Adapter;

import common.View.CommonVSRTICanvas;
import common.View.ViewUtilities;

/**
 * Base class for graphs
 */

@SuppressWarnings("serial")
public abstract class Canvas extends CommonVSRTICanvas {
    
    public TreeMap<Double, Double> points;
    public View view;
    public Adapter adapter;
    
    protected int squareWidth = 60;
    protected int xLabelWidth = 30;
    protected double defaultY = 9;
    protected double defaultXLeft = 0;
    protected double defaultXRight = 25;
    protected int mCanx, mCany;
    protected Double currentPoint;
    protected Color[] colors = {Color.BLACK};
    protected VolatileImage volatileImg;
    final JPopupMenu menu = new JPopupMenu();
    private JFileChooser fileChooser;
    
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
        
        lPad = 90;
        rPad = 30;
        tPad = 50;
        bPad = 60;
        
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
                    
                    try {
                        
                        File f = fileChooser.getSelectedFile();
                        if (!f.getName().trim().endsWith(".eps")) {
                            f = new File(f.getAbsolutePath() + ".eps");
                        }
                        // out = new ObjectOutputStream(new
                        // FileOutputStream(f));
                        
                        EpsGraphics2D g = new EpsGraphics2D("Title", f, 0, 0, getWidth(), getHeight());
                        g.setAccurateTextMode(false);
                        
                        canvas.paintForSave(g);
                        g.flush();
                        g.close();
                        
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
    
    // public void paintComponent(Graphics g){
    // super.paintComponent(g);
    // Graphics2D g2 = (Graphics2D)g;
    //
    // g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
    // RenderingHints.VALUE_ANTIALIAS_ON);
    // setBackground(Color.WHITE);
    //
    // drawXAxis(g2); //draw vertical lines
    // g2.setColor(Color.BLACK);
    // int i=0;
    // drawYAxis(i++,g2); //draw vertical axis
    // //draw axes
    // g2.setColor(Color.BLACK);
    // g2.drawLine(this.getLPad(), this.getHeight()-Canvas.bPad,
    // this.getLPad()+this.getPlotWidth(),
    // this.getHeight()-Canvas.bPad);//horizontal// draw horizontal axis
    //
    // i=0;
    // g2.setColor(colors[0]);
    // drawDataSet(i,g2);
    // i++;
    //
    // }
    
    public void drawDataSet(Graphics2D g) {
        if (points.size() == 0) {
            return;
        }
        Set<Double> keys = points.keySet();
        Double previousKey = points.firstKey();
        for (Double key : keys) {
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(stroke));
            g.drawLine(g2cx(previousKey), g2cy(points.get(previousKey)), g2cx(key), g2cy(points.get(key)));
            previousKey = key;
            drawPoint(g, key, points.get(key));
        }
        
    }
    
    public void drawPoint(Graphics2D g, double x, double y) {}
    
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
    
    /*
     * unnecessary public double countVerticalLabelStep() {// NOT FINISHED int
     * count = this.getPlotHeight() / squareWidth + 1;//30 int max; if
     * (points == null || points.size()==0) { if(view.showVis){ max =
     * (int)(visModel(100)*1.1); //return sinc(0.0)*1.1; } else max =
     * countMax(defaultY);//50 } else { max =
     * countMaxVertical(Math.max(Math.abs(getMaxYPoint()),
     * Math.abs(getMinYPoint())));//50 } return (double) max / (double) count; }
     */
    
    /*
     * public Double getMinYPoint() { if (points.size() == 0) { return
     * (double) -Canvas.defaultY; } double min =
     * this.points.firstEntry().getValue(); Set<Double> keys =
     * this.points.keySet(); for (Double key : keys) { if
     * (this.points.get(key) < min) { min = this.points.get(key); } }
     * return min; }
     */
    
    /*
     * public double countHorizontalLabelStep() {// NOT FINISHED int count =
     * this.getPlotWidth() / squareWidth + 1;//30 //int
     * max=countMax(this.getMaxX());//50 double max = this.getMaxX();//50 return
     * max / count; }
     */
    
    /*
     * public int countMax(double max) { int i = 0; boolean end = false; while
     * (!end) { if (max / (steps[i % steps.length] * (int) Math.pow(10, i /
     * steps.length)) < 1) { end = true; } i++; } i--; //return
     * (int)Math.pow(2,(int)(Math.ceil(Math.log(max)/Math.log(2)))); return
     * (int) Math.pow(2, (int) (Math.ceil(Math.log(max /
     * adapter.getDeltaBaseline()) / Math.log(2)))); }
     */
    
    /*
     * public int countMaxVertical(double max) { int i = 0; boolean end = false;
     * while (!end) { if (max / (steps[i % steps.length] * (int) Math.pow(10, i
     * / steps.length)) < 1) { end = true; } i++; } i -= 2; return steps[i %
     * steps.length] * (int) Math.pow(10, i / steps.length); }
     */
    
    public static int countHorizontalMax(double max) {
        return (int) Math.pow(2, (int) Math.ceil(Math.log(max) / Math.log(2)));
    }
    
    /**
     * @return The greatest y value in the data set. If no data set is currently
     *         displayed, returns 1.1 (if the beam pattern is showing) or the
     *         default y value.
     */
    public double getMaxY() {
        if (points == null || points.size() == 0) {
            if (view.showVis && visModel(0) > 0) {
                return visModel(0.0) * 1.1;
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
        drawYAxis(g2); // draw vertical axis
        // draw axes
        g2.setColor(Color.BLACK);
        // g2.drawLine(this.getLPad(), g2cy(0.0), this.getLPad() +
        // this.getPlotWidth(), g2cy(0.0));//horizontal// draw horizontal axis
        g.setFont(new Font(g.getFont().getFontName(), 0, titleSize));
        g2.drawString(graphTitle, (getWidth() - g2.getFontMetrics().stringWidth(graphTitle)) / 2, (tPad + g2
                .getFontMetrics().getHeight() / 2) / 2);
        g2.setColor(colors[0]);
        drawDataSet(g2);
        if (view.showVis) {
            paintVis(g2);
        }
    }
    
    /**
     * Draws Model
     * 
     * @param g
     */
    protected void paintVis(Graphics2D g) {
        g.setColor(Color.red);
        double last = getMinX();
        // System.out.println(visModel(last));
        for (double i = getMinX(); i <= getMaxX(); i += .1) {
            // g.drawLine(g2cx(i),
            // g2cy(visModel(i)*(points.containsKey(0.0)?points.get(0.0):1)),
            // g2cx(last),
            // g2cy(visModel(last)*(points.containsKey(0.0)?points.get(0.0):1)));
            g.drawLine(g2cx(i), g2cy(visModel(i)), g2cx(last), g2cy(visModel(last)));
            last = i;
        }
    }
    
    /**
     * Calculates Model
     * 
     * @param phi
     * @return
     */
    protected double visModelOld(double blam) {
        
        double X1 = 2 * Math.PI * blam * view.getPhi1();
        double X2 = 2 * Math.PI * blam * view.getPhi2();
        
        double tA = view.T1 * Math.sqrt(2 / Math.pow(X1, 2) * (1 - Math.cos(X1)));
        double tB = view.T2 * Math.sqrt(2 / Math.pow(X2, 2) * (1 - Math.cos(X2)));
        
        if (blam == 0) {
            return 1;
        }
        
        return Math.sqrt(Math.pow(tA, 2) + Math.pow(tB, 2) + 2 * tA * tB
                * Math.cos(2 * Math.PI * blam * Math.sin(view.theta)));
        //
    }
    
    protected double visModel(double blam) {
        
        double X1, X2;
        
        double tA = view.T1;
        
        if (blam == 0 || view.getPhi1() == 0) {
            tA *= 1;
        } else if (view.isShape1Rect()) {
            X1 = 2 * Math.PI * blam * view.getPhi1();
            tA *= Math.sqrt(2 / Math.pow(X1, 2) * (1 - Math.cos(X1)));
        } else {
            X1 = Math.PI * blam * view.getPhi1();
            tA *= 2 * ViewUtilities.besselJ(X1) / X1;
        }
        
        double tB = view.T2;
        
        if (blam == 0 || view.getPhi2() == 0) {
            tB *= 1;
        } else if (view.isShape2Rect()) {
            X2 = 2 * Math.PI * blam * view.getPhi2();
            tB *= Math.sqrt(2 / Math.pow(X2, 2) * (1 - Math.cos(X2)));
        } else {
            X2 = Math.PI * blam * view.getPhi2();
            tB *= 2 * ViewUtilities.besselJ(X2) / X2;
        }
        return Math.sqrt(Math.pow(tA, 2) + Math.pow(tB, 2) + 2 * tA * tB
                * Math.cos(2 * Math.PI * blam * Math.sin(view.theta)));
    }
    
    protected void createBackBuffer() {
        GraphicsConfiguration gc = getGraphicsConfiguration();
        volatileImg = gc.createCompatibleVolatileImage(getWidth(), getHeight());
    }
}