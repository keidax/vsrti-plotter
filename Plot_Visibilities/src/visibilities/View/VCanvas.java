package visibilities.View;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Set;
import java.util.TreeMap;

import common.View.*;
import org.sourceforge.jlibeps.epsgraphics.EpsGraphics2D;
import visibilities.Model.Adapter;

import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
public class VCanvas extends CommonVSRTICanvas {
    public View view;
    public Adapter adapter;

    protected static AbstractOrnament[] ornaments = {new CircleOrnament(3), new SquareOrnament(3)};
    protected static Color[] colors = {Color.BLUE, Color.BLACK};
    protected final double defaultSigma = 1;
    protected double sigma = defaultSigma;
    
    protected float RMSStroke = 2;

    protected double defaultY = 9;
    protected double defaultXLeft = 0;
    protected double defaultXRight = 25;
    protected Double currentPoint;
    private JFileChooser fileChooser;
    /**
     * determines the size of the title
     */
    private int titleSize = 30;


    public VCanvas(View v, Adapter a, TreeMap<Double, Double> g) {
        points = g;
        setAdapter(a);
        setView(v);

        lCanvasPadding = 90;
        rCanvasPadding = 30;
        tCanvasPadding = 50;
        bCanvasPadding = 60;

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

                    try {

                        File f = fileChooser.getSelectedFile();
                        if (!f.getName().trim().endsWith(".eps")) {
                            f = new File(f.getAbsolutePath() + ".eps");
                        }
                        // out = new ObjectOutputStream(new
                        // FileOutputStream(f));

                        EpsGraphics2D g = new EpsGraphics2D("Title", f, 0, 0, getWidth(), getHeight());
                        g.setAccurateTextMode(false);

                        paint(g);
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


        xAxisTitle = "Baseline/λ";
        yAxisTitle = "Visibility";
        graphTitle = "Visibility";
    }

    public void drawPoint(Graphics2D g, double x, double y) {
        // System.out.println("RMS = "+adapter.getRms().size());
        if (adapter.getVisibilityGraphDataPoints().containsKey(x) && adapter.getVisibilityGraphDataPoints().get(x) == y) {
            g.setColor(colors[0]);
            ornaments[1].draw(g, g2cx(x), g2cy(y));
            if (adapter.getRms().containsKey(x)) {
                // System.out.println("** " +
                // adapter.getRms().get(x)*getSigma()/2);
                // System.out.println("g2cy(y + Sigma*rms) "+g2cy(y+getSigma()*adapter.getRms().get(x)));
                drawRms(g, g2cx(x), g2cy(y - adapter.getRms().get(x) * getSigma() / 2), g2cy(y
                        + adapter.getRms().get(x) * getSigma() / 2));
                // System.out.println("RMS ["+x+","+adapter.getRms().get(x));
                // System.out.println("rms = "+adapter.getRms().get(x));
            }
        } else {
            g.setColor(colors[1]);
            ornaments[0].draw(g, g2cx(x), g2cy(y));
        }
    }
    
    public void drawRms(Graphics2D g, int x, int y1, int y2) {
        g.setColor(Color.BLUE);
        Stroke oldStroke = g.getStroke();
        g.setStroke(new BasicStroke(RMSStroke));
        // height*=10;
        // System.out.println("draw:["+x+","+y1+"-"+y2+"]");
        g.drawLine(x, y1, x, y2);
        g.drawLine(x - 1, y1, x + 1, y1);
        g.drawLine(x - 1, y2, x + 1, y2);
        g.setStroke(oldStroke);
    }

    @Override
    protected void drawDataSet(Graphics2D g) {
        if (adapter.getVisibilityGraphDataPoints().size() == 0) {
            return;
        }
        Set<Double> keys = adapter.getVisibilityGraphDataPoints().keySet();
        Double previousKey = adapter.getVisibilityGraphDataPoints().firstKey();
        for (Double key : keys) {
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(strokeSize));
            g.drawLine(g2cx(previousKey), g2cy(adapter.getVisibilityGraphDataPoints().get(previousKey)), g2cx(key), g2cy(adapter.getVisibilityGraphDataPoints().get(key)));
            previousKey = key;
            drawPoint(g, key, adapter.getVisibilityGraphDataPoints().get(key));
        }

    }

    // main paint method
    @Override
    protected void doPaint(Graphics2D g) {
        super.doPaint(g);
        if (view.showVis) {
            paintVis(g);
        }
    }

    @Override
    protected Font getTitleFont(){
        return super.getTitleFont().deriveFont((float)titleSize);
    }

    /**
     * @return The greatest y value in the data set. If no data set is currently
     *         displayed, returns 1.1 (if the beam pattern is showing) or the
     *         default y value.
     */
    public double getMaxY() {
        if (adapter.getVisibilityGraphDataPoints() == null || adapter.getVisibilityGraphDataPoints().size() == 0) {
            if (view.showVis && visModel(0) > 0) {
                return visModel(0.0) * 1.1;
            } else {
                return defaultY;
            }
        }

        double max = adapter.getVisibilityGraphDataPoints().firstEntry().getValue();
        Set<Double> keys = adapter.getVisibilityGraphDataPoints().keySet();
        for (Double key : keys) {
            if (adapter.getVisibilityGraphDataPoints().get(key) > max) {
                max = adapter.getVisibilityGraphDataPoints().get(key);
            }
        }
        if (max < 1) {
            return 1.0;
        }
        return max + max * .1;
    }

    public double getMaxX() {
        if (adapter.getVisibilityGraphDataPoints() == null || adapter.getVisibilityGraphDataPoints().size() == 0) {
            return defaultXRight;
        }
        return adapter.getVisibilityGraphDataPoints().lastKey();
    }

    public double getMinX() {
        if (adapter.getVisibilityGraphDataPoints() == null || adapter.getVisibilityGraphDataPoints().size() == 0) {
            return defaultXLeft;
        }
        return adapter.getVisibilityGraphDataPoints().firstKey();
    }
    
    public double getSigma() {
        return sigma;
    }
    
    public void setSigma(double i) {
        sigma = i;
    }
    
    public void resetToDefaults() {
        sigma = defaultSigma;
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

    /**
     * Draws Model
     */
    protected void paintVis(Graphics2D g) {
        g.setColor(Color.red);
        double last = getMinX();
        // System.out.println(visModel(last));
        for (double i = getMinX(); i <= getMaxX(); i += .1) {
            g.drawLine(g2cx(i), g2cy(visModel(i)), g2cx(last), g2cy(visModel(last)));
            last = i;
        }
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
}
