package common.View;

import common.Model.CommonTIFTAdapter;
import org.sourceforge.jlibeps.epsgraphics.EpsGraphics2D;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

@SuppressWarnings("serial")
public abstract class CommonTIFTCanvas extends CommonRootCanvas {

    protected int defaultY = 2;
    protected int defaultX = 1;

    protected Double currentPoint;
    protected VolatileImage volatileImg;

    protected JFileChooser fileChooser;

    protected Font titleFont;

    protected CommonTIFTAdapter commonAdapter;

    private boolean antialiasing = true;

    //experimental speed optimizations
    private double cachedMinY, cachedMaxY;
    private boolean needsRecaching = true;
    private HashMap<Double, Integer> mapG2CY = new HashMap<Double, Integer>();

    public CommonTIFTCanvas(CommonTIFTAdapter a, TreeMap<Double, Double> g) {
        lCanvasPadding = 80;
        rCanvasPadding = 30;
        tCanvasPadding = 40;
        bCanvasPadding = 60;
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
                        System.out.println("g = " + g.toString());
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

    /**
     * Draw the points
     *
     * @param g the Graphics2D object to use
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

    public boolean getAntialiasing() {
        return antialiasing;
    }

    public void setAntialiasing(boolean antialiasing) {
        this.antialiasing = antialiasing;
    }

    @Override
    protected int getOrnamentSize(double x) {
        if (x <= getMinX()) {
            return 5;
        } else {
            return 3;
        }
    }

    /**
     * Draw a single point
     *
     * @param g the Graphics2D object to use
     * @param x
     * @param y
     */
    public void drawPoint(Graphics2D g, double x, double y) {
    }

    public double getMaxY() {
        if (!needsRecaching) {
            //System.out.print('+');
            return cachedMaxY;
        }

        if (getPoints() == null || getPoints().isEmpty()) {
            return defaultY;
        }

        double max = getPoints().firstEntry().getValue();
        Set<Double> keys = getPoints().keySet();
        for (Double key : keys) {
            if (getPoints().get(key) > max) {
                max = getPoints().get(key);
            }
        }
        if (max < 1) {
            return defaultY;
        }
        return max * 1.1;
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

    @Override
    public int g2cy(double y) {
        if (mapG2CY.containsKey(y)) {
            return mapG2CY.get(y);
        }
        int val = super.g2cy(y);
        mapG2CY.put(y, val);
        return val;
    }

    public double getMinY() {

        if (!needsRecaching) {
            //System.out.print('-');
            return cachedMinY;
        }

        if (getPoints() == null || getPoints().isEmpty()) {
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

    public Double getCurrentPoint() {
        return currentPoint;
    }

    public void setCurrentPoint(Double currentPoint) {
        this.currentPoint = currentPoint;
    }

    protected Double getVerticallyPointOnGraph(int cx, int cy) {
        double x = c2gx(cx);
        // don't allow points outside of graph area
        if (x < getMinX() || x > getMaxX() || cy < tCanvasPadding || cy > getHeight() - bCanvasPadding) {
            return null;
        }
        double floorX, ceilX;
        // catch if x is less than least key or greater than largest key
        try {
            floorX = getPoints().floorKey(x);
        } catch (NullPointerException e) {
            floorX = getPoints().firstKey();
        }
        try {
            ceilX = getPoints().ceilingKey(x);
        } catch (NullPointerException e) {
            ceilX = getPoints().lastKey();
        }
        return (x - floorX < ceilX - x) ? floorX : ceilX;
    }

    @Override
    public void paint(Graphics g) {
        // create the hardware accelerated image.
        createBackBuffer();

        // Main rendering loop. Volatile images may lose their contents.
        // This loop will continually render to (and produce if necessary)
        // volatile images until the rendering was completed successfully.
        do {

            // Validate the volatile image for the graphics configuration of this
            // component. If the volatile image doesn't apply for this graphics configuration
            // (in other words, the hardware acceleration doesn't apply for the new device)
            // then we need to re-create it.
            GraphicsConfiguration gc = getGraphicsConfiguration();

//            System.out.println("page flipping: " + gc.getBufferCapabilities().isPageFlipping()
//            + "\nmulti buffer: " + gc.getBufferCapabilities().isMultiBufferAvailable()
//            + "\naccelerated: " + gc.getImageCapabilities().isAccelerated()
//            + "\ntrue volatile: "+ gc.getImageCapabilities().isTrueVolatile());

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

    /**
     * Records coordinates mouse was released and if there was no currentPoint,
     * then creates new point with particular coordinates
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        // NOT FINISHED (just redo getCurrentDataSet to
        // getViewer().getCurrentDataSet

        if (currentPoint == null && e.getX() >= getLeftCanvasPadding() && e.getX() <= getLeftCanvasPadding() + getPlotWidth() && e.getY() >= tCanvasPadding
                && e.getY() < getWidth() - bCanvasPadding) {
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        DecimalFormat df = new DecimalFormat("#.##");
        if (getPointOnGraph(e.getX(), e.getY()) != null) {
            double p = getVerticallyPointOnGraph(e.getX(), e.getY());
            setToolTipText("(" + df.format(p) + ", " + df.format(getPoints().get(p)) + ")");
        }
        if (getVerticallyPointOnGraph(e.getX(), e.getY()) != null) {
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

        } else {
            setCursor(Cursor.getDefaultCursor());
            setToolTipText(null);
        }
    }

    protected void doPaint(Graphics g) {
        mapG2CY.clear();
        needsRecaching = true;
        cachedMinY = getMinY();
        cachedMaxY = getMaxY();
        needsRecaching = false;

        long start = System.currentTimeMillis();

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                antialiasing ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
        setBackground(Color.WHITE);

        drawYAxis(g2);
        drawXAxis(g2);
        g2.setColor(Color.BLACK);
        g2.setFont(getTitleFont());
        g2.drawString(graphTitle, (getWidth() - g2.getFontMetrics().stringWidth(graphTitle)) / 2, (tCanvasPadding + g2.getFontMetrics().getHeight() / 2) / 2);
        g2.setColor(Color.BLACK);
        drawDataSet(g2);
        long end = System.currentTimeMillis();
        System.out.println("drawing time: " + (end - start));
    }

    private Font getTitleFont() {
        if (titleFont == null) {
            try {
                titleFont = Font.createFont(Font.TRUETYPE_FONT, CommonTIFTCanvas.class.getClassLoader().getResourceAsStream("FreeSerif-min.ttf"));
                titleFont = titleFont.deriveFont((float) (fontSize * 1.2));
            } catch (FontFormatException e) {
                System.err.println("Unable to load font, reverting to default. Some characters may not display correctly.");
            } catch (IOException e) {
                System.err.println("Unable to load font, reverting to default. Some characters may not display correctly.");
            }
        }
        return titleFont;
    }

}
