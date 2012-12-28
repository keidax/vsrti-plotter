// Seems to actually be the canvas. Controls mouse over point value displays.
package fft.View;

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
import java.util.Set;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import fft.Model.Adapter;

public abstract class Canvas1 extends JPanel implements MouseListener,
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
    protected String xAxis = "x-axis";
    protected String yAxis = "y-axis";
    protected String graphTitle = "Untitled";
    final JPopupMenu menu = new JPopupMenu();
    protected int mouseButton = 0;
    Canvas1 canvas;
    
    public Canvas1(View v, Adapter a, TreeMap<Double, Double> g) {
        canvas = this;
        setPoints(g);
        setAdapter(a);
        setView(v);
        addMouseListener(this);
        addMouseMotionListener(this);
        setSize(new Dimension(200, 50));
        setVisible(true);
        
        JMenuItem item = new JMenuItem("Save as JPEG");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
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
        
        menu.add(item);
    }
    
    public TreeMap<Double, Double> getPoints() {
        return points;
    }
    
    public void setPoints(TreeMap<Double, Double> points) {
        this.points = points;
    }
    
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
    
    public int g2cx(double x) {
        double ratio = getRatioX();
        return (int) (x * ratio) + getLeftShift();
    }
    
    public int g2cy(double y) {
        double ratio = getRatioY();
        // height-(height-t-b)/2-bpad-y*ratio
        // (2*height-heihgt+t+b)/2 - b - y*ratio
        return (int) (getPlotHeight() / 2 + tPad - y * ratio);
        // return (int)((this.getHeight()+tPad+bPad)/2-y*ratio-bPad);
    }
    
    public double c2gx(int x) {
        double ratio = getRatioX();
        return (x - getLeftShift()) / ratio;
    }
    
    public double c2gy(double toy) {
        double ratio = getRatioY();
        // return (toy+bPad-getHeight())/-ratio;
        return (getPlotHeight() / 2 + tPad - toy) / ratio;
        // return ((getHeight()+tPad+bPad)/2-toy-bPad)/ratio;
        // return (this.getHeight()-bPad-tPad)/2+tPad-toy*ratio;
        // return ((this.getHeight()-bPad-tPad)/2-toy+tPad)/ratio;
    }
    
    public void drawXAxis(Graphics2D g) {
        int count = 1;// this.getGraph().getDataSets().size();
        // g.drawLine(yLabelWidth+(yLabelWidth/2), (this.getHeight()-bPad)/2,
        // this.getWidth()-yLabelWidth-rPad, (this.getHeight()-bPad)/2);
        double steps = countHorizontalStep();
        double lstep = countHorizontalLabelStep();
        g.setFont(new Font(g.getFont().getFontName(), 0, 11));
        for (int i = 0; i < (getPlotWidth() - 1) / steps + 1; i++) {// horizontal
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine((int) (getLeftShift() + i * steps), tPad,
                    (int) (getLeftShift() + i * steps), getHeight() - bPad);
            g.setColor(Color.BLACK);
            g.drawString(
                    ""
                            + Math.round((i - getPlotWidth() / steps / 2)
                                    * lstep * 100) / 100.0,
                    (int) (getLeftShift() + i * steps - xLabelWidth / 4),
                    getHeight() - 17);
        }
        g.drawString(" " + xAxis + " ", getLeftShift() + getPlotWidth() / 2,
                getHeight() - 2);
    }
    
    public void drawYAxis(int count, Graphics2D g) {
        g.setColor(Color.BLACK);
        drawVerticalLine(lPad + (count / 2 + 1) * yLabelWidth,
                countVerticalStep(), g);
        g.setColor(colors[count % colors.length]);
        drawVerticalMetric(lPad, countVerticalLabelStep(), countVerticalStep(),
                g);
        drawVerticalLabel(count / 2 * yLabelWidth + 5, " " + yAxis, g);
        // new SquareOrnament().draw(g, lPad+(((int)(count/2))*yLabelWidth)+23,
        // 10);
    }
    
    public void drawVerticalLine(int x, double steps, Graphics2D g) {
        g.drawLine(x, Canvas1.tPad, x, getHeight() - Canvas1.bPad);// vertical
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < (getPlotHeight() - 1) / steps + 1; i++) {// horizontal
            g.drawLine(x - 2, (int) (getHeight() - Canvas1.bPad - i * steps),
                    x + 2, (int) (getHeight() - Canvas1.bPad - i * steps));
        }
    }
    
    public void drawVerticalMetric(int x, double strStep, double plotStep,
            Graphics2D g) {
        g.setFont(new Font(g.getFont().getFontName(), 0, 11));
        // for(int i=0;i<(this.getPlotHeight()-1)/2*plotStep+1;i++){//horizontal
        for (int i = 0; i < ((getPlotHeight() - 1) / plotStep + 1) / 2; i++) {// horizontal
            // g.drawString(""+Math.round(i*strStep*10)/10.0,x,
            // (int)((this.getHeight()-FFTCanvas.bPad-tPad)/2+tPad-(i)*plotStep));
            g.drawString(
                    ""
                            + Math.round(c2gy((getHeight() - Canvas1.bPad - tPad)
                                    / 2 + tPad - i * plotStep) * 10) / 10.0, x,
                    (int) ((getHeight() - Canvas1.bPad - tPad) / 2 + tPad - i
                            * plotStep));
            // g.drawString(""+Math.round(i*strStep*10)/10.0,x,
            // g2cy((i*plotStep)));
            // g.drawString(""+Math.round((i*strStep)*10)/10.0,x,
            // (int)(g2cy(i)*plotStep));
        }
        for (int i = 1; i < ((getPlotHeight() - 1) / plotStep + 1) / 2; i++) {// horizontal
            g.drawString(
                    ""
                            + Math.round(c2gy((getHeight() - Canvas1.bPad - tPad)
                                    / 2 + tPad + i * plotStep) * 10) / 10.0,
                    x - 5, (int) ((getHeight() - Canvas1.bPad - tPad) / 2
                            + tPad + i * plotStep));
        }
        // g.drawString(""+Math.round(-i*strStep*10)/10.0,x,
        // (int)((this.getHeight()-FFTCanvas.bPad-tPad)/2+tPad-(-i)*plotStep));}
    }
    
    public void drawVerticalLabel(int x, String title, Graphics2D g) {
        g.translate(x, getPlotHeight() / 2);
        g.rotate(-Math.PI / 2.0);
        g.drawString(title, 0, 10);
        g.rotate(Math.PI / 2.0);
        g.translate(-x, -getPlotHeight() / 2);
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
    
    public double countVerticalLabelStep() {// NOT FINISHED
        int count = getPlotHeight() / squareWidth + 1;// 30
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
    
    public Double getMaxYPoint() {
        if (getPoints().size() == 0) {
            return null;
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
        return max;
    }
    
    public Double getMinYPoint() {
        if (getPoints().size() == 0) {
            return (double) -Canvas1.defaultY;
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
    
    public double getMaxYRange() {
        if (getMaxYPoint() - getMinYPoint() < 2) {
            return 2.0;
        } else {
            return getMaxYPoint() - getMinYPoint();
        }
    }
    
    public double countHorizontalLabelStep() {// NOT FINISHED
        int count = getPlotWidth() / squareWidth + 1;// 30
        // int max=countMax(this.getMaxX());//50
        double max = getMaxX();// 50
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
        // System.out.println("countMax("+max+") = "+steps[i%steps.length]*(int)Math.pow(10,i/steps.length));
        // return (int)Math.pow(2,(int)(Math.ceil(Math.log(max)/Math.log(2))));
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
        // System.out.println("countMaxVertical is "+steps[i%steps.length]*(int)Math.pow(10,i/steps.length));
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
            return (double) getPlotHeight() / (double) countMax(defaultY);
        } else {
            return getPlotHeight()
                    / (2 * Math.max(Math.abs(getMaxY()),
                            Math.abs(getMinYPoint())) * 1.1);
        }
    }
    
    protected int getPlotWidth() {
        return getWidth() - yLabelWidth - lPad - rPad;
    }
    
    protected int getPlotHeight() {
        return getHeight() - Canvas1.tPad - Canvas1.bPad;
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
        return getPoints().lastKey();
    }
    
    public int getLeftShift() {
        return lPad + Canvas1.yLabelWidth;
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
        Canvas1.steps = steps;
    }
    
    public static int getSquareWidth() {
        return squareWidth;
    }
    
    public static void setSquareWidth(int squareWidth) {
        Canvas1.squareWidth = squareWidth;
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
        Canvas1.defaultY = defaultY;
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
     * records where mouse was pressed and weather there is any point in less
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
        if (currentPoint == null && mCanx >= getLeftShift()
                && mCanx <= getLeftShift() + getPlotWidth() && mCany >= tPad
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
        System.out.println("Button" + evt.getButton());
        if (evt.getButton() != MouseEvent.BUTTON2) {
            if (getVerticallyPointOnGraph(mCanx, mCany) != null) {
                setCurrentPoint(getVerticallyPointOnGraph(mCanx, mCany));
            } else {
                setCurrentPoint(null);
            }
        }
        
        // System.out.println("currentpoint="+currentPoint);
        if (getCurrentPoint() != null) {
            double tox =
                    Math.min(Math.max(getLeftShift(), mCanx), getLeftShift()
                            + getPlotWidth());
            double toy = Math.min(Math.max(tPad, mCany), getHeight() - bPad);
            // System.out.println("moving to ["+tox+","+toy+"]");
            adapter.moveVisibilityPoint(getCurrentPoint(), c2gy(toy));
            // getCurrentPoint().movePoint(getCurrentPoint(),
            // c2gx(tox),c2gy(toy,getCurrentPoint().getDataSet()));
        }
    }
    
    @Override
    public void mouseMoved(MouseEvent evt) {
        mCanx = evt.getX();
        mCany = evt.getY();
        if (getPointOnGraph(mCanx, mCany) != null) {
            double p = getVerticallyPointOnGraph(mCanx, mCany);
            setToolTipText("[" + p + ";" + getPoints().get(p) + "]");
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
