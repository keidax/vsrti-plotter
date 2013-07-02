package common.View;

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
import javax.swing.JPopupMenu;

import org.sourceforge.jlibeps.epsgraphics.EpsGraphics2D;

import common.Model.CommonTIFTAdapter;

@SuppressWarnings("serial")
public abstract class CommonTIFTCanvas extends CommonRootCanvas implements MouseListener, MouseMotionListener {
    
    protected int[] steps = {1, 2, 5};
    protected int squareWidth; // width of vertical line placement
    protected int xLabelWidth;
    protected int defaultY = 2;
    protected int defaultX = 1;
    
    protected int mCanx, mCany;
    protected Double currentPoint;
    protected static Color[] colors = {Color.BLACK};
    protected VolatileImage volatileImg;
    
    protected final JPopupMenu menu = new JPopupMenu();
    protected int mouseButton = 0;
    protected JFileChooser fileChooser;
    
    protected int strokeSize = 4;
    protected int fontSize = 20;
    
    protected CommonTIFTAdapter commonAdapter;
    
    public CommonTIFTCanvas(CommonTIFTAdapter a, TreeMap<Double, Double> g) {
        lPad = 80;
        rPad = 30;
        tPad = 40;
        bPad = 60;
        squareWidth = 50;
        xLabelWidth = 30;
        setPoints(g);
        commonAdapter = a;
        addMouseListener(this);
        addMouseMotionListener(this);
        fileChooser = new JFileChooser();
        setSize(new Dimension(200, 50));
        setVisible(true);
        
        JMenuItem item = new JMenuItem("Save as JPEG");
        JMenuItem item2 = new JMenuItem("Save as EPS");
        
        item.addActionListener(new ActionListener() {
            
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
                if (fileChooser.showSaveDialog(CommonTIFTCanvas.this) == JFileChooser.APPROVE_OPTION) {
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
                if (fileChooser.showSaveDialog(CommonTIFTCanvas.this) == JFileChooser.APPROVE_OPTION) {
                    // ObjectOutputStream out;
                    try {
                        
                        File f = fileChooser.getSelectedFile();
                        if (!f.getName().trim().endsWith(".eps")) {
                            f = new File(f.getAbsolutePath() + ".eps");
                        }
                        // out = new ObjectOutputStream(new
                        // FileOutputStream(f));
                        EpsGraphics2D g = new EpsGraphics2D("Title", f, 0, 0, getWidth(), getHeight());
                        CommonTIFTCanvas.this.paint(g);
                        g.flush();
                        System.out.println(g.toString());
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
    
    /**
     * Draw the points
     * 
     * @param count
     * @param g
     */
    public void drawDataSet(Graphics2D g) {
        if (getPoints().size() == 0) {
            return;
        }
        Set<Double> keys = getPoints().keySet();
        Double previousKey = getPoints().firstKey();
        for (Double key : keys) {
            g.setColor(Color.BLACK);
            g.drawLine(g2cx(previousKey), g2cy(getPoints().get(previousKey)), g2cx(key), g2cy(getPoints().get(key)));
            previousKey = key;
            drawPoint(g, key, getPoints().get(key));
        }
        
    }
    
    /**
     * Draw a single point
     * 
     * @param g
     * @param x
     * @param y
     */
    public void drawPoint(Graphics2D g, double x, double y) {}
    
    public double getMaxY() {
        if (getPoints() == null || getPoints().isEmpty()) {
            return defaultY;
        }
        Double maxy = getMaxYPoint();
        return maxy;
    }
    
    public double getMaxX() {
        if (getPoints() == null || getPoints().size() == 0) {
            return defaultX;
        }
        return getPoints().lastKey();
    }
    
    public double getMinX() {
        return 0.0;
        
    }
    
    public double getMinY() {
        if (getPoints().isEmpty() || getPoints().size() == 0) {
            return -defaultY;
        } else {
            return getMinYPoint();
        }
    }
    
    /**
     * Horizontal label step
     * 
     * @return
     */
    public double countHorizontalLabelStep() {
        int count = getPlotWidth() / squareWidth + 1;
        double max = getMaxX() - getMinX();
        return max / count;
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
    
    public double countVerticalStep() {// NOT FINISHED
        int count = getPlotHeight() / squareWidth + 1;
        if (count % 2 == 1) {
            count++;
        }
        return (double) getPlotHeight() / count;
    }
    
    public double countVerticalLabelStep() {// NOT FINISHED
        int count = getPlotHeight() / squareWidth + 1;// 30
        int max;
        if (getPoints() == null || getPoints().size() == 0) {
            max = countMax(defaultY);// 50
        } else {
            max = countMaxVertical(Math.max(Math.abs(getMaxYPoint()), Math.abs(getMinYPoint())));// 50
        }
        return (double) max / (double) count;
    }
    
    public double getMaxYRange() {
        if (getMaxYPoint() - getMinYPoint() < 2) {
            return 2.0;
        } else {
            return getMaxYPoint() - getMinYPoint();
        }
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
     * 
     * @param points
     */
    public void setPoints(TreeMap<Double, Double> points) {
        this.points = points;
    }
    
    public double getMaxYPoint() {
        if (getPoints().size() == 0) {
            return 0;
        }
        double max = getPoints().firstEntry().getValue();
        Set<Double> keys = getPoints().keySet();
        for (Double key : keys) {
            if (getPoints().get(key) > max) {
                max = getPoints().get(key);
            }
        }
        if (max < 1) {
            return 1.0;
        }
        return max * 1.1;
    }
    
    public double getMinYPoint() {
        if (getPoints().isEmpty()) {
            return -defaultY;
        }
        double min = getPoints().firstEntry().getValue();
        Set<Double> keys = getPoints().keySet();
        for (Double key : keys) {
            if (getPoints().get(key) < min) {
                min = getPoints().get(key);
            }
        }
        
        if (min > -defaultY) {
            return -defaultY;
        }
        return min * 1.1;
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
        return (int) Math.pow(2, (int) Math.ceil(Math.log(max / commonAdapter.getDeltaBaseline()) / Math.log(2)));
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
    
    public int[] getSteps() {
        return steps;
    }
    
    public void setSteps(int[] steps) {
        this.steps = steps;
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
    
    public int getDefaultY() {
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
            if (new SquareOrnament().isInside(mCanx, mCany, g2cx(key), g2cy(getPoints().get(key)))) {
                return key;
            }
        }
        return null;
    }
    
    protected Double getVerticallyPointOnGraph(int x, int y) {
        
        Set<Double> keys = getPoints().keySet();
        for (Double key : keys) {
            if (new SquareOrnament().isInsideVertically(mCanx, mCany, g2cx(key), g2cy(getPoints().get(key)))) {
                return key;
            }
        }
        return null;
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
        // This loop will continually render to (and produce if necessary)
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
    
    protected void createBackBuffer() {
        GraphicsConfiguration gc = getGraphicsConfiguration();
        volatileImg = gc.createCompatibleVolatileImage(getWidth(), getHeight());
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
        
        if (currentPoint == null && mCanx >= getLPad() && mCanx <= getLPad() + getPlotWidth() && mCany >= tPad
                && mCany < getWidth() - bPad) {}
    }
    
    @Override
    public void mouseMoved(MouseEvent evt) {
        mCanx = evt.getX();
        mCany = evt.getY();
        DecimalFormat df = new DecimalFormat("#.##");
        if (getPointOnGraph(mCanx, mCany) != null) {
            double p = getVerticallyPointOnGraph(mCanx, mCany);
            setToolTipText("[" + df.format(p) + "; " + df.format(getPoints().get(p)) + "]");
        }
        if (getVerticallyPointOnGraph(mCanx, mCany) != null) {
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            
        } else {
            setCursor(Cursor.getDefaultCursor());
            setToolTipText("");
        }
    }
    
    protected void doPaint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        setBackground(Color.WHITE);
        
        int i = 0;
        drawYAxis(i++, g2);
        drawXAxis(g2);
        g2.setColor(Color.BLACK);
        g2.drawString(graphTitle, (getWidth() - g2.getFontMetrics().stringWidth(graphTitle)) / 2, (tPad + g2
                .getFontMetrics().getHeight() / 2) / 2);
        g2.setColor(colors[0]);
        drawDataSet(g2);
    }
    
    /**
     * Draws the x-axis
     * 
     * @param g
     */
    public void drawXAxis(Graphics2D g) {
        g.setFont(new Font(g.getFont().getFontName(), 0, fontSize));
        g.setStroke(new BasicStroke(strokeSize));
        double steps = countHorizontalStep();
        double lstep = countHorizontalLabelStep();
        DecimalFormat df = new DecimalFormat("#.##");
        FontMetrics fm = g.getFontMetrics();
        
        for (int i = 0; i < (getPlotWidth() - 1) / steps + 1; i++) {// horizontal
            int xPosition = (int) (getLPad() + i * steps);
            int yPosition = g2cy(0.0);
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(xPosition, yPosition + 2, xPosition, yPosition - 2);
            
            g.setColor(Color.BLACK);
            String labelString = "" + df.format(Math.round(i * lstep * 100) / 100.0 + getMinX());
            yPosition = getHeight() - bPad + fm.getAscent() + 5;
            g.drawString(labelString, xPosition - fm.stringWidth(labelString) / 2, yPosition);
        }
        
        g.drawLine(getLPad(), g2cy(0.0), getLPad() + getPlotWidth(), g2cy(0.0));
        g.drawString(xAxisTitle, getLPad() + (getPlotWidth() - g.getFontMetrics().stringWidth(xAxisTitle)) / 2,
                getHeight() - 10);
        
        System.out.println("g2cy(0) = " + g2cy(0.0));
        
    }
    
    /**
     * Draws the y-axis
     * 
     * @param count
     * @param g
     */
    public void drawYAxis(int count, Graphics2D g) {
        g.setStroke(new BasicStroke(strokeSize));
        g.setFont(new Font(g.getFont().getFontName(), 0, fontSize));
        g.setColor(Color.BLACK);
        drawVerticalLine(countVerticalStep(), g);
        g.setColor(colors[count % colors.length]);
        drawVerticalMetric(countVerticalStep(), g);
        drawVerticalLabel(g);
    }
    
    /**
     * Draws the label for the y axis
     */
    public void drawVerticalLabel(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font(g.getFont().getFontName(), 0, fontSize));
        
        int translateDown = tPad + (getPlotHeight() + g.getFontMetrics().stringWidth(yAxisTitle)) / 2;
        
        g.translate(yLabelWidth, translateDown);
        g.rotate(-Math.PI / 2.0);
        
        g.drawString(yAxisTitle, 0, 10);
        
        g.rotate(Math.PI / 2.0);
        g.translate(-yLabelWidth, -translateDown);
    }
    
    /**
     * Draws vertical lines
     * 
     * @param x
     * @param steps
     * @param g
     */
    public void drawVerticalLine(double steps, Graphics2D g) {
        g.drawLine(lPad, tPad, lPad, getHeight() - bPad);// vertical
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < (getPlotHeight() - 1) / steps + 1; i++) {// horizontal
            g.drawLine(lPad - 2, (int) (getHeight() - bPad - i * steps), lPad + 2, (int) (getHeight() - bPad - i
                    * steps));
        }
    }
    
    /**
     * Draws vertical metric
     * 
     * @param strStep
     * @param plotStep
     * @param g
     */
    public void drawVerticalMetric(double plotStep, Graphics2D g) {
        FontMetrics fm = g.getFontMetrics();
        int x = 0;
        int y = 0;
        String label = "";
        g.setFont(new Font(g.getFont().getFontName(), 0, fontSize));
        for (int i = 0; i < ((getPlotHeight() - 1) / plotStep + 1) / 2; i++) {// horizontal
            label = "" + Math.round(c2gy(getPlotHeight() / 2 + tPad - i * plotStep) * 10) / 10.0;
            x = lPad - fm.stringWidth(label) - 5;
            y = (int) (getPlotHeight() / 2 + tPad - i * plotStep) + fm.getAscent() / 2;
            g.drawString(label, x, y);
        }
        for (int i = 1; i < ((getPlotHeight() - 1) / plotStep + 1) / 2; i++) {// horizontal
            label = "" + Math.round(c2gy(getPlotHeight() / 2 + tPad + i * plotStep) * 10) / 10.0;
            x = lPad - fm.stringWidth(label) - 5;
            y = (int) (getPlotHeight() / 2 + tPad + i * plotStep) + fm.getAscent() / 2;
            g.drawString(label, x, y);
        }
    }
    
}
