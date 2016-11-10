package common.View;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.VolatileImage;
import java.text.DecimalFormat;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.*;

@SuppressWarnings("serial")
public abstract class CommonRootCanvas extends JPanel implements MouseListener, MouseMotionListener {

    protected TreeMap<Double, Double> points;

    protected int lCanvasPadding, rCanvasPadding, tCanvasPadding, bCanvasPadding;

    protected String xAxisTitle = "x-axis";
    protected String yAxisTitle = "y-axis";
    protected String graphTitle = "Untitled";

    protected int yLabelWidth = 10;
    protected int strokeSize = 4;

    protected VolatileImage volatileImg;

    /**
     * determines the size of the axis labels and numbers
     */
    protected int fontSize = 20;
    protected Font normalFont;
    protected final JPopupMenu menu = new JPopupMenu();

    public CommonRootCanvas() {

    }

    public String getGraphTitle() {
        return graphTitle;
    }

    public void setGraphTitle(String graphTitle) {
        this.graphTitle = graphTitle;
    }

    /**
     * @param x the x coordinate of the graph, in units
     * @return the x coordinate of the canvas, in pixels
     */
    public int g2cx(double x) {
        return (int) (lCanvasPadding + (x - getMinX()) * getRatioX());
    }

    /**
     * @param y the y coordinate of the graph, in units
     * @return the y coordinate of the canvas, in pixels
     */
    public int g2cy(double y) {
        return (int) (getPlotHeight() + tCanvasPadding - (y - getMinY()) * getRatioY());
    }

    /**
     * @param x the x coordinate of the canvas, in pixels
     * @return the x coordinate of the graph, in units
     */
    public double c2gx(double x) {
        return (x - lCanvasPadding) / getRatioX() + getMinX();
    }

    /**
     * @param y the y coordinate of the canvas, in pixels
     * @return the y coordinate of the graph, in units
     */
    public double c2gy(double y) {
        return (y - getPlotHeight() - tCanvasPadding) / -getRatioY() + getMinY();
    }

    protected Font getNormalFont() {
        if (normalFont == null) {
            normalFont = new Font(Font.SANS_SERIF, Font.PLAIN, fontSize);
        }
        return normalFont;
    }

    protected Font getTitleFont(){
        return getNormalFont();
    }

    /**
     * @return The width of the plot section of the canvas, in pixels
     */
    protected int getPlotWidth() {
        return getWidth() - lCanvasPadding - rCanvasPadding;
    }

    /**
     * @return The height of the plot section of the canvas, in pixels
     */
    protected int getPlotHeight() {
        return getHeight() - tCanvasPadding - bCanvasPadding;
    }

    protected abstract double getMinX();

    protected abstract double getMinY();

    protected abstract double getMaxX();

    protected abstract double getMaxY();

    public String getXAxisTitle() {
        return xAxisTitle;
    }

    public void setXAxisTitle(String xAxisTitle) {
        this.xAxisTitle = xAxisTitle;
    }

    public String getYAxisTitle() {
        return yAxisTitle;
    }

    public void setYAxisTitle(String yAxisTitle) {
        this.yAxisTitle = yAxisTitle;
    }

    public double getXSpacing() {
        // determine spacing for marks on x-axis
        double xSpacing = 1;
        //TODO make this able to scale arbitrarily
        double pixelsPerNumber = this.getPlotWidth() * 1.0 / (getMaxX() - getMinX());
        if (pixelsPerNumber > 350) {
            xSpacing = 0.1;
        } else if (pixelsPerNumber > 200) {
            xSpacing = 0.2;
        } else if (pixelsPerNumber > 100) {
            xSpacing = 0.5;
        } else if (pixelsPerNumber > 35) {
            xSpacing = 1;
        } else if (pixelsPerNumber > 25) {
            xSpacing = 2;
        } else if (pixelsPerNumber > 8) {
            xSpacing = 5;
        } else if (pixelsPerNumber > 3.5) {
            xSpacing = 10;
        } else {
            xSpacing = 15;
        }
        return xSpacing;
    }

    public double getYSpacing(){
        // determine spacing for marks on y-axis
        double ySpacing = 1;
        double pixelsPerNumber = this.getPlotHeight() * 1.0 / (getMaxY() - getMinY());
        if (pixelsPerNumber > 200) {
            ySpacing = 0.2;
        } else if (pixelsPerNumber > 100) {
            ySpacing = 0.5;
        } else if (pixelsPerNumber > 35) {
            ySpacing = 1;
        } else if (pixelsPerNumber > 25) {
            ySpacing = 2;
        } else if (pixelsPerNumber > 8) {
            ySpacing = 5;
        } else if (pixelsPerNumber > 3.5) {
            ySpacing = 10;
        } else {
            ySpacing = 15;
        }
        return ySpacing;
    }

    /**
     * Draws the y-axis of the graph
     *
     * @param g the Graphics2D object to use
     */
    public void drawYAxis(Graphics2D g) {
        g.setStroke(new BasicStroke(strokeSize));
        g.setFont(getNormalFont());

        g.setColor(Color.BLACK);
        g.drawLine(lCanvasPadding, tCanvasPadding, lCanvasPadding, getHeight() - bCanvasPadding);
        drawYAxisMetric(g);
        drawYAxisTitle(g);
    }

    /**
     * Draws vertical metric - the labels and markings on the y-axis
     */
    public void drawYAxisMetric(Graphics2D g) {
        FontMetrics fm = g.getFontMetrics();
        DecimalFormat df = new DecimalFormat("0.0");

        double ySpacing = getYSpacing();
        for(double i = (int) ((int)(getMinY() / ySpacing) * ySpacing); i <= getMaxY(); i+= ySpacing){
            int xPos = lCanvasPadding;
            int yPos = g2cy(i);
            // draw horixontal marks
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(xPos - 2, yPos, xPos + 2, yPos);
            // draw label for each mark
            g.setColor(Color.BLACK);
            String tempString = df.format(i);
            g.drawString(tempString, xPos - fm.stringWidth(tempString) - 5, yPos + fm.getAscent() / 2);
        }
    }

    /**
     * Draws the title for the y axis
     */
    public void drawYAxisTitle(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font(g.getFont().getFontName(), 0, fontSize));

        int translateDown = tCanvasPadding + (getPlotHeight() + g.getFontMetrics().stringWidth(yAxisTitle)) / 2;

        g.translate(yLabelWidth, translateDown);
        g.rotate(-Math.PI / 2.0);

        g.drawString(yAxisTitle, 0, 10);

        g.rotate(Math.PI / 2.0);
        g.translate(-yLabelWidth, -translateDown);
    }

    /**
     * Draws the x-axis
     */
    public void drawXAxis(Graphics2D g) {
        g.setStroke(new BasicStroke(strokeSize));
        g.setFont(getNormalFont());

        // draw horizontal axis
        g.setColor(Color.BLACK);
        g.drawLine(lCanvasPadding, g2cy(0.0), getWidth() - rCanvasPadding, g2cy(0.0));
        drawXAxisMetric(g);
        drawXAxisTitle(g);
    }

    public void drawXAxisMetric(Graphics2D g){
        DecimalFormat df = new DecimalFormat("#.#");
        FontMetrics fm = g.getFontMetrics();
        double xSpacing = getXSpacing();
        for (double i = (int) ((int) (getMinX() / xSpacing) * xSpacing); i <= getMaxX(); i += xSpacing) {
            int xPosition = g2cx(i);
            int yPosition = getHeight() - bCanvasPadding;

            // draw vertical marks
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(xPosition, yPosition + 2, xPosition, yPosition - 2);

            // draw labels for each mark
            g.setColor(Color.BLACK);
            String lString = df.format(i);
            g.drawString(lString, xPosition - fm.stringWidth(lString) / 2, yPosition + fm.getAscent() + 5);
        }
    }

    public void drawXAxisTitle(Graphics2D g){
        g.drawString(xAxisTitle, lCanvasPadding + (getPlotWidth() - g.getFontMetrics().stringWidth(xAxisTitle)) / 2,
                getHeight() - 10);
    }

    protected void drawGraphTitle(Graphics2D g){
        g.setColor(Color.BLACK);
        g.setFont(getTitleFont());
        g.drawString(graphTitle, (getWidth() - g.getFontMetrics().stringWidth(graphTitle)) / 2,
                (tCanvasPadding + g.getFontMetrics().getHeight() / 2) / 2);
    }

    public int getLeftCanvasPadding() {
        return lCanvasPadding;
    }

    public void setLeftCanvasPadding(int pad) {
        lCanvasPadding = pad;
    }

    public int getRightCanvasPadding() {
        return rCanvasPadding;
    }

    public void setRightCanvasPadding(int pad) {
        rCanvasPadding = pad;
    }

    public int getTopCanvasPadding() {
        return tCanvasPadding;
    }

    public void setTopCanvasPadding(int pad) {
        tCanvasPadding = pad;
    }

    public int getBottomCanvasPadding() {
        return bCanvasPadding;
    }

    public void setBottomCanvasPadding(int pad) {
        bCanvasPadding = pad;
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
            if (new SquareOrnament(getOrnamentSize(key)).isInside(x, y, g2cx(key), g2cy(getPoints().get(key)))) {
                return key;
            }
        }
        return null;
    }

    protected int getOrnamentSize(double x) {
        return 3;
    }

    public double getRatioY() {
        return getPlotHeight() / (getMaxY() - getMinY());
    }

    public double getRatioX() {
        return getPlotWidth() / (getMaxX() - getMinX());
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            menu.show(this, e.getX(), e.getY());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public void paint(Graphics g) {
        // create the hardware accelerated image.
        createBackBuffer();

        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Main rendering loop. Volatile images may lose their contents.
        // This loop will continually render to (and produce if neccessary)
        // volatile images until the rendering was completed successfully.
        do {

            // Validate the volatile image for the graphics configuration of this
            // component. If the volatile image doesn't apply for this graphics configuration
            // (in other words, the hardware acceleration doesn't apply for the new device)
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
            doPaint((Graphics2D) offscreenGraphics); // call core paint method.

            // paint back buffer to main graphics
            g.drawImage(volatileImg, 0, 0, this);
            // Test if content is lost
        } while (volatileImg.contentsLost());
    }

    protected void createBackBuffer() {
        GraphicsConfiguration gc = getGraphicsConfiguration();
        volatileImg = gc.createCompatibleVolatileImage(getWidth(), getHeight());
    }

    protected void doPaint(Graphics2D g) {
        long start = System.currentTimeMillis();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        setBackground(Color.WHITE);

        drawXAxis(g);
        drawYAxis(g);
        drawGraphTitle(g);
        drawDataSet(g);

        long end = System.currentTimeMillis();
        System.err.println("drawing time: " + (end - start));
    }

    abstract protected void drawDataSet(Graphics2D g);
}
