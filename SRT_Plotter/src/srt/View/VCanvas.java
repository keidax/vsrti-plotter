package srt.View;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Set;
import java.util.TreeMap;

import common.View.*;
import org.sourceforge.jlibeps.epsgraphics.EpsGraphics2D;
import srt.Model.Adapter;

import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
public class VCanvas extends CommonRootCanvas {

    public View view;
    public Adapter adapter;
    protected int squareWidth = 30;
    protected static int defaultX = 9;
    protected static int defaultY = 9;
    protected JFileChooser fileChooser;

    
    protected TreeMap<Double, Double> dataPoints;
    protected static AbstractOrnament[] ornaments = {new CircleOrnament(3), new SquareOrnament(3)};
    protected static Color[] colors = {Color.BLUE, Color.BLACK};
    protected int sigma = 1;

    public enum PlotMode {
        PLOT_CHANNELS, PLOT_FREQUENCIES, PLOT_VELOCITIES, PLOT_AVERAGE_TA, PLOT_BEAM_WIDTH
    }

    private PlotMode currentPlotMode;

    public PlotMode getPlotMode() {
        return currentPlotMode;
    }

    public void setPlotMode(PlotMode currentPlotMode) {
        this.currentPlotMode = currentPlotMode;
        switch (currentPlotMode){
            case PLOT_AVERAGE_TA:
                setXAxisTitle("Data Block");
                setYAxisTitle("Antenna Temperature (K)");
                setGraphTitle("Averge TA vs. Order");
                break;
            case PLOT_CHANNELS:
                setXAxisTitle("Channels");
                setYAxisTitle("Antenna Temperature (K)");
                setGraphTitle("Antenna Temperature vs. Channel");
                break;
            case PLOT_FREQUENCIES:
                setXAxisTitle("Frequency (MHz)");
                setYAxisTitle("Antenna Temperature (K)");
                setGraphTitle("Spectrum: Antenna Temperature vs. Frequency");
                break;
            case PLOT_VELOCITIES:
                setXAxisTitle("Velocity (km/s)");
                setYAxisTitle("Antenna Temperature (K)");
                setGraphTitle("Antenna Temperature vs. Velocity");
                break;
            case PLOT_BEAM_WIDTH:
                setXAxisTitle("Angle (degrees)");
                setYAxisTitle("Antenna Temperature (K)");
                setGraphTitle("Beam Width: Average Antenna Temperature vs. Angle");
                break;
        }
    }
    
    public VCanvas(View v, Adapter a, TreeMap<Double, Double> g) {
        setPoints(g);
        setAdapter(a);
        setView(v);
        addMouseListener(this);
        addMouseMotionListener(this);
        fileChooser = new JFileChooser();
        setSize(new Dimension(200, 50));
        setVisible(true);

        // lCanvasPadding = (old) lPad + yLabelWidth
        lCanvasPadding = 100;
        rCanvasPadding = 30;
        tCanvasPadding = 30;
        bCanvasPadding = 60;

        yLabelWidth = 10;

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

                    @Override
                    public String getDescription() {
                        return "Only JPEG";
                    }
                });
                if (fileChooser.showSaveDialog(VCanvas.this) == JFileChooser.APPROVE_OPTION) {
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
                if (fileChooser.showSaveDialog(VCanvas.this) == JFileChooser.APPROVE_OPTION) {
                    // ObjectOutputStream out;
                    try {

                        File f = fileChooser.getSelectedFile();
                        if (!f.getName().trim().endsWith(".eps")) {
                            f = new File(f.getAbsolutePath() + ".eps");
                        }
                        // out = new ObjectOutputStream(new
                        // FileOutputStream(f));
                        EpsGraphics2D g = new EpsGraphics2D("Title", f, 0, 0, getWidth(), getHeight());
                        VCanvas.this.paint(g);
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

        dataPoints = adapter.getVisibilityGraphDataPoints();
        setPlotMode(PlotMode.PLOT_CHANNELS);
    }
    
    /**
     * Draws an individual point as a black circle
     */
    public void drawPoint(Graphics2D g, double x, double y) {
        if (adapter.getVisibilityGraphDataPoints().containsKey(x) && adapter.getVisibilityGraphDataPoints().get(x) == y) {
            g.setColor(Color.BLACK);
            ornaments[1].draw(g, g2cx(x), g2cy(y));
            if (adapter.getRms().containsKey(x)) {
                drawRms(g, g2cx(x), g2cy(y - adapter.getRms().get(x) * sigma / 2), g2cy(y
                        + adapter.getRms().get(x) * sigma / 2));
            }
        } else {
            g.setColor(Color.BLACK);
            ornaments[0].draw(g, g2cx(x), g2cy(y));
        }
    }
    
    /**
     * Draws an individual point as a red circle or a red triangle
     */
    public void drawPoint(Graphics2D g, double x, double y, boolean b) {
        
        g.setColor(Color.RED);
        TriangleOrnament t = new TriangleOrnament();
        if (b == true) {
            ornaments[1].draw(g, g2cx(x), g2cy(y));
        } else {
            t.draw(g, g2cx(x), g2cy(y));
        }
    }
    
    /**
     * Draws RMS values
     */
    public void drawRms(Graphics2D g, int x, int y1, int y2) {
        g.drawLine(x, y1, x, y2);
        g.drawLine(x - 1, y1, x + 1, y1);
        g.drawLine(x - 1, y2, x + 1, y2);
    }

       /**
     * Draws all points to the inputed graphics
     */
    @Override
    protected void drawDataSet(Graphics2D g) {
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
     * Draws the x-axis
     */
    /*public void drawXAxis(Graphics2D g) {
        double steps = countHorizontalStep();
        double lstep = countHorizontalLabelStep();
        g.setFont(new Font(g.getFont().getFontName(), 0, 11));
        DecimalFormat df = new DecimalFormat("#.##");

        if (currentPlotMode == PlotMode.PLOT_FREQUENCIES) {
            g.drawString(View.getView(this).currentDataBlock.fStart + "+", 0, getHeight() - 17);
        } else if(currentPlotMode == PlotMode.PLOT_VELOCITIES) {
            g.drawString(ViewUtilities.frequencyToVelocity(View.getView(this).currentDataBlock.fStart) + "+", 0, getHeight() - 17);
        }

        for (int i = 0; i < (getPlotWidth() - 1) / steps + 1; i++) {
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine((int) (lCanvasPadding + i * steps), tCanvasPadding, (int) (lCanvasPadding + i * steps), getHeight() - bCanvasPadding);
            g.setColor(Color.BLACK);

            if (currentPlotMode == PlotMode.PLOT_BEAM_WIDTH) {
                g.drawString("" + df.format(i * lstep * 100 / 100.0 + View.getView(this).min), (int) (lCanvasPadding + i
                        * steps - xLabelWidth / 4), getHeight() - 17);
            } else {
                g.drawString("" + df.format(i * lstep * 100 / 100.0),
                        (int) (lCanvasPadding + i * steps - xLabelWidth / 4), getHeight() - 17);
            }

        }
        drawXAxisTitle(g);
    }*/

    @Override
    public void drawXAxisMetric(Graphics2D g){
        DecimalFormat df = new DecimalFormat("#.#");
        FontMetrics fm = g.getFontMetrics();

        // Certain modes have a constant offset value for the x-axis, so we draw this number to the left of the origin
        if (currentPlotMode == PlotMode.PLOT_FREQUENCIES) {
            double offsetNumber = View.getView(this).currentDataBlock.fStart;
            String offsetString = df.format(offsetNumber) + "+";
            g.drawString(offsetString, lCanvasPadding - fm.stringWidth(offsetString),
                    getHeight() - bCanvasPadding + fm.getAscent() + 5);
        }

        double xSpacing = getXSpacing();
        for (double i = (int) (getMinX() - (getMinX() % xSpacing) + xSpacing); i <= getMaxX(); i += xSpacing) {
            int xPosition = g2cx(i);
            int yPosition = getHeight() - bCanvasPadding;

            // draw vertical marks
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(xPosition, tCanvasPadding, xPosition, yPosition - 2);

            // draw labels for each mark
            g.setColor(Color.BLACK);
            String lString = df.format(i);
            g.drawString(lString, xPosition - fm.stringWidth(lString) / 2, yPosition + fm.getAscent() + 5);
        }
    }

    /**
     * Returns the x-value of the point with the largest y-value
     */
    public Double getMaxYPoint() {
        if (getPoints() == null || getPoints().size() == 0) {
            return defaultY + 0.0;
        }
        double max = getPoints().firstEntry().getValue();
        Set<Double> keys = getPoints().keySet();
        for (Double key : keys) {
            if (getPoints().get(key) > max) {
                max = getPoints().get(key);
            }
        }
        if (max < 1) {
            return defaultY + 0.0;
        }
        return 1.1 * max;
    }

    public Double getMinYPoint(){
        if(getPoints() == null || getPoints().size() == 0) {
            return 0.0;
        }

        double min = Collections.min(getPoints().values());
        if(min > 0){
            return 0.0;
        } else {
            return min;
        }
    }

    public double getMaxY() {
        if (getPoints().isEmpty()) {
            return defaultY;
        }
        return getMaxYPoint();
    }

    public double getMinY() {
        if (getPoints().isEmpty()) {
            return 0;
        }
        return getMinYPoint() * 1.1;
    }

    protected double getMaxX() {
        if (getPoints() == null || getPoints().size() == 0) {
            return defaultX;
        }
        return getPoints().lastKey();
    }

    protected double getMinX() {
        if (getPoints() == null || getPoints().size() == 0) {
            return 0.0;
        }
        return Math.floor(getPoints().firstKey());
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
     * @param x int
     * @param y int
     * @return MyPoint
     */
    protected Double getPointOnGraph(int x, int y) {

        Set<Double> keys = getPoints().keySet();
        for (Double key : keys) {
            if (new SquareOrnament(3).isInside(x, y, g2cx(key), g2cy(getPoints().get(key)))) {
                return key;
            }
        }
        return null;
    }

    protected Double getVerticallyPointOnGraph(int x, int y) {

        Set<Double> keys = getPoints().keySet();
        for (Double key : keys) {
            if (new SquareOrnament(3).isInsideVertically(x, y, g2cx(key), g2cy(getPoints().get(key)))) {
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
        int mouseX = evt.getX();
        int mouseY = evt.getY();
        double p = 0;

        if (View.getView(this).choosingDelete && getPointOnGraph(mouseX, mouseY) != null
                && evt.getButton() != MouseEvent.BUTTON2) {
            p = getPointOnGraph(mouseX, mouseY);
            if (View.getView(this).deleting[(int) p - 1] != -1) {
                drawPoint((Graphics2D) getGraphics(), p, getPoints().get(p), false);
                View.getView(this).deleting[(int) p - 1] = -1;

            } else if (View.getView(this).deleting[(int) p - 1] == -1) {
                drawPoint((Graphics2D) getGraphics(), p, getPoints().get(p));
                View.getView(this).deleting[(int) p - 1] = 0;

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

        int mouseX = evt.getX();
        int mouseY = evt.getY();
        double p = 0;
        if (evt.getButton() != MouseEvent.BUTTON2 && getVerticallyPointOnGraph(mouseX, mouseY) != null
                && View.getView(this).choosingDelete) {
            p = getVerticallyPointOnGraph(mouseX, mouseY);

            if (View.getView(this).deleting[(int) p - 1] != -1) {
                drawPoint((Graphics2D) getGraphics(), p, getPoints().get(p), false);
                View.getView(this).deleting[(int) p - 1] = -1;
            }
        }
        if (evt.getButton() == MouseEvent.BUTTON2 && getVerticallyPointOnGraph(mouseX, mouseY) != null
                && View.getView(this).choosingDelete) {
            p = getVerticallyPointOnGraph(mouseX, mouseY);

            if (View.getView(this).deleting[(int) p - 1] == -1) {
                drawPoint((Graphics2D) getGraphics(), p, getPoints().get(p));
                View.getView(this).deleting[(int) p - 1] = 0;
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent evt) {
        DecimalFormat df = new DecimalFormat("#.##");
        int mouseX = evt.getX();
        int mouseY = evt.getY();
        double min = 0;
        if (currentPlotMode == PlotMode.PLOT_FREQUENCIES && View.getView(this).currentDataBlock != null) {
            min = View.getView(this).currentDataBlock.fStart;
            df = new DecimalFormat("#.###");
        }
        if (currentPlotMode == PlotMode.PLOT_AVERAGE_TA && getPointOnGraph(mouseX, mouseY) != null) {
            double p = getVerticallyPointOnGraph(mouseX, mouseY);
            setToolTipText("[" + df.format(p + min) + "; " + df.format(getPoints().get(p)) + "]  Data Block: "
                    + View.getView(this).taPlotted[(int) p] + ")");
        } else if (currentPlotMode == PlotMode.PLOT_BEAM_WIDTH && getPointOnGraph(mouseX, mouseY) != null) {
            double p = getVerticallyPointOnGraph(mouseX, mouseY);
            setToolTipText("[" + df.format(p + View.getView(this).min) + "; " + df.format(getPoints().get(p)) + "]");
        } else if (getPointOnGraph(mouseX, mouseY) != null) {
            double p = getVerticallyPointOnGraph(mouseX, mouseY);
            setToolTipText("[" + df.format(p + min) + "; " + df.format(getPoints().get(p)) + "]");
        }
        if (getVerticallyPointOnGraph(mouseX, mouseY) != null) {
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

        } else {
            setCursor(Cursor.getDefaultCursor());
            setToolTipText("");
        }
    }
}
