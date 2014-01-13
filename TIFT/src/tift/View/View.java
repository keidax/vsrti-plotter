package tift.View;

import tift.Model.Adapter;
import tift.Model.ModelListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Karel Durktoa
 * @author Adam Pere
 * @author Gabriel Holodak
 */
public class View extends JFrame implements ModelListener {

    public Adapter adapter;
    public VCanvas vGraph, vGraph2; // real/magnitude graph and imaginary/phase graph in time domain
    public FFTCanvas iGraph, iGraph2; // real/magnitude graph and imaginary/phase graph in frequency
    // domain
    public JButton exit;
    final JPopupMenu menu = new JPopupMenu();
    public String link = "http://www1.union.edu/marrj/radioastro/ToolforInteractiveFourierTransforms.html";
    public String equation;
    public String iEquation;
    public JLabel lEquation;
    public boolean radio;
    public File f;
    private JFileChooser jfc;

    public View(Adapter a, String title) {
        super(title);
        setAdapter(a);
        jfc = new JFileChooser();
        vGraph = new VCanvas(this, getAdapter(), getAdapter().getVisiblityGraphPoints(), "Magnitude", "f(t) - Magnitude", true);
        vGraph2 = new VCanvas(this, getAdapter(), getAdapter().getVisiblityGraphPoints2(), "Phase", "f(t) - Phase", false);
        // "\ud835\udf08" is unicode for mathematical italic small nu
        iGraph = new FFTCanvas(this, getAdapter(), getAdapter().getImageGraphPoints(), "Magnitude", "F(\ud835\udf08) - Magnitude", true);
        iGraph2 = new FFTCanvas(this, getAdapter(), getAdapter().getImageGraphPoints2(), "Phase", "F(\ud835\udf08) - Phase", false);
        radio = true;
        //vGraph.setSize(300, 100);
        //vGraph2.setSize(300, 100);
        f = null;

        JPanel mainPanel, sidePanel;
        JLabel lDelta, lMaxF, lBlank;

        final JTextField fDelta, fLambda, fThetaMax, fSigma, fNumber, fThetaMin, fMinTime, fMaxTime;

        equation = "";
        iEquation = "";

        // BUTTONS

        final JButton bSave, bOpen, bExit, bImage, bReset, bAbout, bFullReset, bInstruction, bEquation, bRadio;
        bOpen = new JButton("Open file");
        bSave = new JButton("Save file");
        bExit = new JButton("Exit");
        bImage = new JButton("Show grayscale image");
        bReset = new JButton("Reset");
        bFullReset = new JButton("Full Reset");
        bAbout = new JButton("About");
        bInstruction = new JButton("Instructions");
        bEquation = new JButton("Enter Equation");
        bRadio = new JButton("Show Rectangular");
        lDelta = new JLabel("<HTML><P><b>f(t) Input Paremeters:</b></P><P></P><P>" + '\u0394'
                + "t: </P><P></P><P># of points:</P></HTML>");
        lMaxF = new JLabel("<HTML><P><b>Display Options:</b></P><P></P><P>max time:</P><P></P><P>max frequency:</P></HTML>");
        lBlank = new JLabel("<HTML><P>  </P> <P> </P><P>  </P> <P> </P><P>  </P> </HTML>");
        lBlank.setMaximumSize(new Dimension(110, 100));
        lMaxF.setMaximumSize(new Dimension(110, 160));
        lDelta.setMaximumSize(new Dimension(110, 100));

        lEquation = new JLabel("Equation: ");

        // Text Boxes
        fDelta = new JTextField(adapter.getDeltaBaseline() + ""); // Delta t
        fNumber = new JTextField(adapter.getNumberOfPoints() + ""); // NumberToken of
        // Points
        fLambda = new JTextField(adapter.getLambda() + ""); // Lambda
        fThetaMax = new JTextField(adapter.getLambda() / adapter.getDeltaBaseline() / 2 + ""); // Max
        // Frequency to be displayed
        fThetaMin = new JTextField("0.00"); // Min Frequency to be displayed
        fMinTime = new JTextField("0.00"); // Min Frequency to be displayed
        fMaxTime = new JTextField(adapter.getDeltaBaseline() * adapter.getNumberOfPoints() + ""); // Max
        // Time to be displayed
        fSigma = new JTextField(getVGraph().getSigma() + "");
        fDelta.setToolTipText("<HTML><P WIDTH='300px'>\u0394t =  x-axis step-size. (Numbers only, do not include "
                + "units)<BR/>");
        fThetaMax.setToolTipText("<HTML><P WIDTH='300px'>max frequency = largest frequency displayed in frequency domain.</P></HTML>");
        fThetaMin.setToolTipText("<HTML><P WIDTH='300px'>min frequency = smallest frequency displayed in frequency domain.</P></HTML>");
        fMaxTime.setToolTipText("<HTML><P WIDTH='300px'>max time = largest time displayed in time domain.</P></HTML>");
        fMinTime.setToolTipText("<HTML><P WIDTH='300px'>min time = smallest time displayed in time domain.</P></HTML>");
        fNumber.setToolTipText("<HTML><P WIDTH = '300px'>number of points graphed.</P></HTML>");

        // Setting size of text boxes
        fSigma.setMaximumSize(new Dimension(50, 20));
        fSigma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int sigma = Integer.parseInt(fSigma.getText());
                    getVGraph().setSigma(sigma);
                    getVGraph().update();
                    update();
                } catch (NumberFormatException e1) {
                }
            }
        });
        fDelta.setMaximumSize(new Dimension(50, 20));
        fDelta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double delta = Double.parseDouble(fDelta.getText());
                    lEquation.setText("Equation: ");
                    adapter.setDeltaBaseline(delta);
                    fMaxTime.setText(adapter.getDeltaBaseline() * adapter.getNumberOfPoints() + "");
                } catch (NumberFormatException e1) {
                }
            }
        });
        fLambda.setMaximumSize(new Dimension(50, 20));
        fLambda.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double lambda = Double.parseDouble(fLambda.getText());
                    adapter.setLambda(lambda);
                    fMaxTime.setText(adapter.getDeltaBaseline() * adapter.getNumberOfPoints() + "");
                } catch (NumberFormatException e1) {
                }
            }
        });
        fThetaMax.setMaximumSize(new Dimension(50, 20));
        fThetaMax.setMinimumSize(new Dimension(50, 20));
        fThetaMax.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double thetaMax = Double.parseDouble(fThetaMax.getText());
                    update();
                } catch (NumberFormatException e1) {
                }
            }
        });
        fThetaMin.setMaximumSize(new Dimension(50, 20));
        fThetaMin.setMinimumSize(new Dimension(50, 20));
        fThetaMin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double thetaMin = Double.parseDouble(fThetaMin.getText());
                    update();
                } catch (NumberFormatException e1) {
                }
            }
        });
        fMaxTime.setMaximumSize(new Dimension(50, 20));
        fMaxTime.setMinimumSize(new Dimension(50, 20));
        fMaxTime.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double maxTime = Double.parseDouble(fMaxTime.getText());
                    update();
                } catch (NumberFormatException e1) {
                }
            }
        });
        fMinTime.setMaximumSize(new Dimension(50, 20));
        fMinTime.setMinimumSize(new Dimension(50, 20));
        fMinTime.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double minTime = Double.parseDouble(fMinTime.getText());
                    update();
                } catch (NumberFormatException e1) {
                }
            }
        });
        fNumber.setMaximumSize(new Dimension(50, 20));
        fNumber.setMinimumSize(new Dimension(50, 20));
        fNumber.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    lEquation.setText("Equation: ");
                    int x = Integer.parseInt(fNumber.getText().trim());
                    x = (int) (Math.log(x) / Math.log(2));
                    adapter.setNumberOfPoints((int) Math.pow(2, x));
                    fMaxTime.setText(adapter.getDeltaBaseline() * adapter.getNumberOfPoints() + "");
                } catch (NumberFormatException e1) {
                }
            }
        });

        // Setting up the GUI layout
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        sidePanel = new JPanel();
        sidePanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));

        getContentPane().add(mainPanel);
        getContentPane().add(sidePanel);

        mainPanel.setPreferredSize(new Dimension(600, 500));
        mainPanel.setMinimumSize(new Dimension(300, 300));
        mainPanel.add(lEquation);
        mainPanel.add(vGraph);
        mainPanel.add(vGraph2);
        mainPanel.add(Box.createRigidArea(new Dimension(5, 5)));
        mainPanel.add(iGraph);
        mainPanel.add(iGraph2);


        sidePanel.setMaximumSize(new Dimension(100, 500));
        sidePanel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        sidePanel.add(new Label("f(t) Input Parameters"), c);

        c.gridy++;
        c.gridx = 0;
        c.gridwidth = 1;
        sidePanel.add(new Label("delta t:"), c);
        c.gridx = 1;
        sidePanel.add(fDelta, c);

        c.gridy++;
        c.gridx = 0;
        sidePanel.add(new Label("# of points:"), c);
        c.gridx = 1;
        sidePanel.add(fNumber, c);

        c.gridy++;
        c.gridx = 0;
        c.gridwidth = 2;
        sidePanel.add(new Label("Display Options"), c);

        c.gridy++;
        c.gridx = 0;
        c.gridwidth = 1;
        sidePanel.add(new Label("Max time:"), c);
        c.gridx = 1;
        sidePanel.add(fMaxTime, c);

        c.gridy++;
        c.gridx = 0;
        sidePanel.add(new Label("Max frequency:"), c);
        c.gridx = 1;
        sidePanel.add(fThetaMax, c);

        c.gridy++;
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)), c);

        c.gridy++;
        c.gridx = 0;
        sidePanel.add(bOpen, c);
        c.gridx = 1;
        sidePanel.add(bSave, c);

        c.gridy++;
        c.gridx = 0;
        c.gridwidth = 2;
        sidePanel.add(bEquation, c);

        c.gridy++;
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)), c);

        c.gridy++;
        c.gridx = 0;
        c.gridwidth = 2;
        sidePanel.add(bRadio, c);

        c.gridy++;
        c.gridx = 0;
        c.gridwidth = 1;
        sidePanel.add(bReset, c);
        c.gridx = 1;
        sidePanel.add(bFullReset, c);

        c.gridy++;
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)), c);

        c.gridy++;
        c.gridx = 0;
        c.gridwidth = 2;
        sidePanel.add(bInstruction, c);

        c.gridy++;
        c.gridx = 0;
        c.gridwidth = 1;
        sidePanel.add(bAbout, c);
        c.gridx = 1;
        sidePanel.add(bExit, c);

        // BUTTONS FUNCTIONS
        bOpen.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                jfc.showOpenDialog(View.this);
                f = jfc.getSelectedFile();

                if (f == null || !f.canRead()) {
                    return;
                }
                Double tm[][] = parseFile(f);
                adapter.fullReset();
                if (tm != null) {
                    ;
                    adapter.importVisibilityGraphPoints(tm);
                }
                lEquation.setText("Equation: ");
            }
        });

        bSave.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                jfc.showSaveDialog(View.this);
                File f = jfc.getSelectedFile();
                if (f == null) {
                    return;
                }
                writeIntoFile(f, adapter.exportVisibilityGraphPoints());
            }

            private void writeIntoFile(File f, String exportVisibilityGraphPoints) {

                BufferedWriter out;

                try {

                    out = new BufferedWriter(new FileWriter(f));
                    out.write(exportVisibilityGraphPoints);
                    out.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        bExit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                dispose();
            }
        });

        bImage.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                double a = 255 / getIGraph().getMaxY();
                double b = 0;
                ImageFrame frame;
                if (getIGraph().getPoints().ceilingKey(Double.parseDouble(fThetaMax.getText())) == null) {
                    frame =
                            new ImageFrame("Grayscale image", a, b, getIGraph().getPoints().subMap(
                                    0.0,
                                    true,
                                    getIGraph().getPoints().floorKey(
                                            Double.parseDouble(fThetaMax.getText())), true));
                } else {
                    frame =
                            new ImageFrame("Grayscale image", a, b, getIGraph().getPoints().subMap(
                                    0.0,
                                    true,
                                    getIGraph().getPoints().ceilingKey(
                                            Double.parseDouble(fThetaMax.getText())), true));
                }
                // frame.displayData();
                frame.setVisible(true);
                // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(getIGraph().getWidth(), getIGraph().getHeight());
                // frame.pack();
            }
        });

        // Pressing reset will set all points to the state of the last opened
        // file or the last entered equation. Opened files take precedence over
        // equations.
        bReset.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {

                if (f != null && f.canRead()) {
                    Double tm[][] = parseFile(f);
                    adapter.fullReset();
                    if (tm != null) {
                        ;
                        adapter.importVisibilityGraphPoints(tm);
                    }
                    lEquation.setText("Equation: ");
                } else {
                    if (!equation.equals("")) {
                        adapter.evaluate(equation, iEquation);
                        lEquation.setText("Equation: " + equation + " " + iEquation + "i");
                    } else {
                        adapter.fullReset();
                    }

                }

            }
        });
        bFullReset.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                adapter.fullReset();
                lEquation.setText("Equation: ");
            }
        });

        bRadio.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (radio) {
                    bRadio.setText("Show Polar");
                    vGraph.setYAxis("");
                    vGraph2.setYAxis("");

                    vGraph.setTitle("f(t) - Real");
                    vGraph2.setTitle("f(t) - Imaginary");

                    iGraph.setYAxis("");
                    iGraph2.setYAxis("");

                    iGraph.setTitle("F(ν) - Real");
                    iGraph2.setTitle("F(ν) - Imaginary");
                } else {
                    bRadio.setText("Show Rectangular");
                    vGraph.setYAxis("Magnitude");
                    vGraph2.setYAxis("Phase");

                    vGraph.setTitle("f(t) - Magnitude");
                    vGraph2.setTitle("f(t) - Phase");

                    iGraph.setYAxis("Magnitude");
                    iGraph2.setYAxis("Phase");

                    iGraph.setTitle("F(ν) - Magnitude");
                    iGraph2.setTitle("F(ν) - Phase");

                }
                radio = !radio;
                adapter.setPolar(radio);

            }
        });

        bEquation.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                final JFrame jf = new JFrame("TIFT - Equation");
                JButton enter = new JButton("Enter");
                final JTextField eqn, eqn2;
                JPanel row1 = new JPanel();
                row1.setLayout(new BoxLayout(row1, BoxLayout.Y_AXIS));
                row1.add(Box.createRigidArea(new Dimension(5, 5)));

                if (!equation.equals("")) {
                    eqn = new JTextField(equation);
                    eqn2 = new JTextField(iEquation);

                } else {
                    eqn = new JTextField();
                    eqn2 = new JTextField();
                }
                // eqn.setMaximumSize(new Dimension(700,100));
                JLabel jl = new JLabel("<html>Acceptable Inputs:<br><br>"
                        + "Use t as the independent variable, e as Euler's number, and pi as pi.<br>"
                        + "*, ^, /, +, -, (, and ) are valid operators.<br>"
                        + "sin(_), cos(_), tan(_), log(_), ln(_), exp(_), delta(_) and u(_) (unit step) are also valid.<br>"
                        + "Angles are in radians.<br><br>" + "</html>");
                jf.getContentPane().setLayout(new FlowLayout());
                jf.getContentPane().add(row1);
                row1.add(jl);
                row1.add(new JLabel("Real part of the equation:"));
                row1.add(eqn);
                row1.add(new JLabel("Imaginary part of the equation:"));
                row1.add(eqn2);
                row1.add(enter);

                jf.setMinimumSize(new Dimension(400, 275));
                jf.pack();
                jf.setLocationRelativeTo(null);
                jf.setVisible(true);

                ActionListener setEquationListener = new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        equation = eqn.getText().toLowerCase().replace(" ", "");
                        iEquation = eqn2.getText().toLowerCase().replace(" ", "");
                        try {
                            adapter.evaluate(equation, iEquation);
                            lEquation.setText("Equation: " + equation + " + i*" + iEquation);
                            jf.setVisible(false);
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(jf, "Error evaluating equation: " + e.getMessage());
                            System.out.println(e.getMessage());
                        }
                    }
                };
                enter.addActionListener(setEquationListener);
                eqn.addActionListener(setEquationListener);
                eqn2.addActionListener(setEquationListener);
            }
        });

        bAbout.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFrame jf = new JFrame("TIFT");
                JLabel jl = new JLabel(common.View.ViewUtilities.TIFT_ABOUT_TEXT);
                jf.getContentPane().setLayout(new FlowLayout());
                jf.getContentPane().add(jl);
                jf.pack();
                jf.setLocationRelativeTo(null);
                jf.setVisible(true);
            }
        });

        bInstruction.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    java.awt.Desktop.getDesktop().browse(new URI(link));
                } catch (IOException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                } catch (URISyntaxException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        getVGraph().addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        this.setSize(800, 600);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    public Double[][] parseFile(File f) {
        Double xValue[] = new Double[128];
        Double real[] = new Double[128];
        Double imag[] = new Double[128];
        try {

            FileInputStream fstream = new FileInputStream(f);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            boolean vis = false;
            int count = 0;
            while ((strLine = br.readLine()) != null) {
                if (strLine.trim().startsWith(";")) {
                    continue;
                }
                if (strLine.trim().startsWith("*lambda")) {
                    adapter.setLambda(Double.parseDouble(strLine.split(" ")[1]));
                } else if (strLine.trim().startsWith("*deltaBaseline")) {
                    adapter.setDeltaBaseline(Double.parseDouble(strLine.split(" ")[1]));
                } else if (strLine.trim().startsWith("*numberOfPoints")) {
                    adapter.setNumberOfPoints(Integer.parseInt(strLine.split(" ")[1]));
                } else if (strLine.trim().startsWith("X_Y_Real_Imaginary")) {
                    vis = true;
                } else if (vis) {
                    if (strLine.trim().equals("")) {
                        continue;
                    }
                    try {
                        String[] s = strLine.split(" ");

                        xValue[count] = Double.parseDouble(s[0]);
                        real[count] = Double.parseDouble(s[1]);
                        imag[count] = Double.parseDouble(s[2]);
                        count++;
                    } catch (NumberFormatException e) {
                    }
                } else {
                    continue;
                }

            }
            in.close();
        } catch (Exception e) {// Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }

        Double returning[][] = new Double[3][128];
        returning[0] = xValue;
        returning[1] = real;
        returning[2] = imag;
        return returning;
    }

    public Adapter getAdapter() {
        return adapter;
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
    }

    public VCanvas getVGraph() {
        return vGraph;
    }

    public VCanvas getVGraph2() {
        return vGraph2;
    }

    public void setVGraph(VCanvas graph) {
        vGraph = graph;
    }

    public void setVGraph2(VCanvas graph) {
        vGraph2 = graph;
    }

    public FFTCanvas getIGraph() {
        return iGraph;
    }

    public void setIGraph(FFTCanvas graph) {
        iGraph = graph;
    }

    public FFTCanvas getIGraph2() {
        return iGraph2;
    }

    public void setIGraph2(FFTCanvas graph) {
        iGraph2 = graph;
    }

    public JButton getExit() {
        return exit;
    }

    public void setExit(JButton exit) {
        this.exit = exit;
    }

    @Override
    public void update() {
        //fDelta.setText(adapter.getDeltaBaseline() + "");
        //fLambda.setText(adapter.getLambda() + "");
        //fNumber.setText(adapter.getNumberOfPoints() + "");
        // fMaxA.setText(this.adapter.getMaxAmp() + "");
        // fThetaMax.setText(adapter.getLambda()/this.adapter.getDeltaBaseline()+
        // "");
        //fSigma.setText(getVGraph().getSigma() + "");

        repaint();
    }

    public void go() {
        getAdapter().getModel().getListeners().add(this);
    }
}
