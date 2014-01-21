package tift.View;

import tift.Model.Adapter;
import tift.Model.ModelListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
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

    public final Adapter adapter;
    public VCanvas vGraph, vGraph2; // real/magnitude graph and imaginary/phase graph in time domain
    public FFTCanvas iGraph, iGraph2; // real/magnitude graph and imaginary/phase graph in frequency
    // domain
    final JPopupMenu menu = new JPopupMenu();
    public String link = "http://www1.union.edu/marrj/radioastro/ToolforInteractiveFourierTransforms.html";
    public String equation = "0";
    public String iEquation = "0";
    public JLabel lEquation;
    public boolean radio;

    private final JTextField fDelta, fNumber, fMinFrequency, fMaxFrequency, fMinTime, fMaxTime;


    public View(final Adapter adapter, String title) {
        super(title);
        this.adapter = adapter;
        vGraph = new VCanvas(this, adapter, adapter.getVisibilityGraphPoints(), "Magnitude", "f(t) - Magnitude", true);
        vGraph2 = new VCanvas(this, adapter, adapter.getVisibilityGraphPoints2(), "Phase", "f(t) - Phase", false);
        // "\ud835\udf08" is unicode for mathematical italic small nu
        iGraph = new FFTCanvas(this, adapter, adapter.getImageGraphPoints(), "Magnitude", "F(\ud835\udf08) - Magnitude", true);
        iGraph2 = new FFTCanvas(this, adapter, adapter.getImageGraphPoints2(), "Phase", "F(\ud835\udf08) - Phase", false);
        radio = true;

        JPanel mainPanel, sidePanel;


        equation = "";
        iEquation = "";

        // BUTTONS

        final JButton bExit, bReset, bAbout, bFullReset, bInstruction, bEquation, bRadio, bUpdate;
        bExit = new JButton("Exit");
        bReset = new JButton("Reset");
        bFullReset = new JButton("Full Reset");
        bAbout = new JButton("About");
        bInstruction = new JButton("Instructions");
        bEquation = new JButton("Enter Equation");
        bRadio = new JButton("Show Rectangular");
        bUpdate = new JButton("Update");

        lEquation = new JLabel("Equation: ");
        final JCheckBox cbAntialiasing = new JCheckBox();
        cbAntialiasing.setSelected(vGraph.getAntialiasing());

        // Text Boxes
        fDelta = new JTextField(adapter.getDeltaBaseline() + "");
        fNumber = new JTextField(adapter.getNumberOfPoints() + "");
        fMaxFrequency = new JTextField(adapter.getMaxFrequency() + "");
        fMinFrequency = new JTextField(adapter.getMinFrequency() + "");
        fMinTime = new JTextField(adapter.getMinTime() + "");
        fMaxTime = new JTextField(adapter.getMaxTime() + "");

        fDelta.setToolTipText("<HTML><P WIDTH='300px'>\u0394t =  x-axis step-size. (Numbers only, do not include units)<BR/>");
        fMaxFrequency.setToolTipText("<HTML><P WIDTH='300px'>max frequency = largest frequency displayed in frequency domain.</P></HTML>");
        fMinFrequency.setToolTipText("<HTML><P WIDTH='300px'>min frequency = smallest frequency displayed in frequency domain.</P></HTML>");
        fMaxTime.setToolTipText("<HTML><P WIDTH='300px'>max time = largest time displayed in time domain.</P></HTML>");
        fMinTime.setToolTipText("<HTML><P WIDTH='300px'>min time = smallest time displayed in time domain.</P></HTML>");
        fNumber.setToolTipText("<HTML><P WIDTH = '300px'>number of points graphed.</P></HTML>");

        fDelta.setMaximumSize(new Dimension(50, 20));
        ActionListener fDeltaActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double delta = Double.parseDouble(fDelta.getText());
                    if (delta != adapter.getDeltaBaseline()) {
                        lEquation.setText("Equation: ");
                        adapter.setDeltaBaseline(delta);
                        adapter.resetFrequencyRange();
                        adapter.resetTimeRange();
                        update();
                    }
                } catch (NumberFormatException e1) {
                }
            }
        };
        fDelta.addActionListener(fDeltaActionListener);

        fNumber.setMaximumSize(new Dimension(50, 20));
        ActionListener fNumberActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    lEquation.setText("Equation: ");
                    int x = Integer.parseInt(fNumber.getText().trim());
                    if (x != adapter.getNumberOfPoints()) {
                        //TODO why casting to a power of 2?
                        x = (int) (Math.log(x) / Math.log(2));
                        adapter.setNumberOfPoints((int) Math.pow(2, x));
                        adapter.resetFrequencyRange();
                        adapter.resetTimeRange();
                        update();
                    }
                } catch (NumberFormatException e1) {
                }
            }
        };
        fNumber.addActionListener(fNumberActionListener);

        fMaxFrequency.setMaximumSize(new Dimension(50, 20));
        fMaxFrequency.setMinimumSize(new Dimension(50, 20));
        ActionListener fMaxFrequencyActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double maxFrequency = Double.parseDouble(fMaxFrequency.getText());
                    if (maxFrequency != adapter.getMaxFrequency()) {
                        adapter.setMaxFrequency(maxFrequency);
                        update();
                    }
                } catch (NumberFormatException e1) {
                }
            }
        };
        fMaxFrequency.addActionListener(fMaxFrequencyActionListener);

        fMinFrequency.setMaximumSize(new Dimension(50, 20));
        fMinFrequency.setMinimumSize(new Dimension(50, 20));
        ActionListener fMinFrequencyActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double minFrequency = Double.parseDouble(fMinFrequency.getText());
                    if (minFrequency != adapter.getMinFrequency()) {
                        adapter.setMinFrequency(minFrequency);
                        update();
                    }
                } catch (NumberFormatException e1) {
                }
            }
        };
        fMinFrequency.addActionListener(fMinFrequencyActionListener);

        fMaxTime.setMaximumSize(new Dimension(50, 20));
        fMaxTime.setMinimumSize(new Dimension(50, 20));
        ActionListener fMaxTimeActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double maxTime = Double.parseDouble(fMaxTime.getText());
                    if (maxTime != adapter.getMaxTime()) {
                        adapter.setMaxTime(maxTime);
                        update();
                    }
                } catch (NumberFormatException e1) {
                }
            }
        };
        fMaxTime.addActionListener(fMaxTimeActionListener);

        fMinTime.setMaximumSize(new Dimension(50, 20));
        fMinTime.setMinimumSize(new Dimension(50, 20));
        ActionListener fMinTimeActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double minTime = Double.parseDouble(fMinTime.getText());
                    if (minTime != adapter.getMinTime()) {
                        adapter.setMinTime(minTime);
                        update();
                    }
                } catch (NumberFormatException e1) {
                }
            }
        };
        fMinTime.addActionListener(fMinTimeActionListener);

        bUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double delta = Double.parseDouble(fDelta.getText());
                    int x = Integer.parseInt(fNumber.getText().trim());
                    if (delta != adapter.getDeltaBaseline() || x != adapter.getNumberOfPoints()) {
                        x = (int) (Math.log(x) / Math.log(2));
                        adapter.setNumberOfPoints((int) Math.pow(2, x));
                        adapter.setDeltaBaseline(delta);
                        adapter.resetFrequencyRange();
                        adapter.resetTimeRange();
                        update();
                        //TODO do something with equation here?
                    } else {
                        double minTime = Double.parseDouble(fMinTime.getText());
                        double maxTime = Double.parseDouble(fMaxTime.getText());
                        double minFrequency = Double.parseDouble(fMinFrequency.getText());
                        double maxFrequency = Double.parseDouble(fMaxFrequency.getText());
                        adapter.setMinTime(minTime);
                        adapter.setMaxTime(maxTime);
                        adapter.setMinFrequency(minFrequency);
                        adapter.setMaxFrequency(maxFrequency);
                        update();
                    }
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
        sidePanel.add(new Label("Min time:"), c);
        c.gridx = 1;
        sidePanel.add(fMinTime, c);

        c.gridy++;
        c.gridx = 0;
        c.gridwidth = 1;
        sidePanel.add(new Label("Max time:"), c);
        c.gridx = 1;
        sidePanel.add(fMaxTime, c);

        c.gridy++;
        c.gridx = 0;
        sidePanel.add(new Label("Min frequency:"), c);
        c.gridx = 1;
        sidePanel.add(fMinFrequency, c);

        c.gridy++;
        c.gridx = 0;
        sidePanel.add(new Label("Max frequency:"), c);
        c.gridx = 1;
        sidePanel.add(fMaxFrequency, c);

        c.gridy++;
        c.gridx = 0;
        c.gridwidth = 2;
        sidePanel.add(new Label("Testing options"), c);

        c.gridy++;
        c.gridx = 0;
        c.gridwidth = 1;
        sidePanel.add(new Label("Antialiasing:"), c);
        c.gridx = 1;
        sidePanel.add(cbAntialiasing, c);

        c.gridy++;
        c.gridx = 0;
        c.gridwidth = 2;
        sidePanel.add(bUpdate, c);

        c.gridy++;
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)), c);

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
        bExit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                dispose();
            }
        });

        // Pressing reset will set all points to the state of the last opened
        // file or the last entered equation. Opened files take precedence over
        // equations.
        bReset.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                    if (!(equation.equals("") || iEquation.equals(""))) {
                        try {
                            adapter.evaluate(equation, iEquation);
                            lEquation.setText("Equation: " + equation + " " + iEquation + "i");
                        } catch (Exception e1) {
                            adapter.reset();
                        }
                    } else {
                        adapter.reset();
                    }
            }
        });
        bFullReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                adapter.fullReset();
                equation = "0";
                iEquation = "0";
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
//                        try {
                        adapter.evaluate(equation, iEquation);
                        lEquation.setText("Equation: " + equation + " + i*" + iEquation);
                        jf.setVisible(false);
//                        } catch (Exception e) {
//                            JOptionPane.showMessageDialog(jf, "Error evaluating equation: " + e.getMessage());
//                            System.out.println(e.getMessage());
//                        }
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

        cbAntialiasing.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean checked = cbAntialiasing.isSelected();
                vGraph.setAntialiasing(checked);
                vGraph2.setAntialiasing(checked);
                iGraph.setAntialiasing(checked);
                iGraph2.setAntialiasing(checked);
            }
        });

        this.setSize(800, 600);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    public Adapter getAdapter() {
        return adapter;
    }

    public FFTCanvas getIGraph() {
        return iGraph;
    }

    @Override
    public void update() {
        fDelta.setText(adapter.getDeltaBaseline() + "");
        fNumber.setText(adapter.getNumberOfPoints() + "");
        fMaxFrequency.setText(adapter.getMaxFrequency() + "");
        fMinFrequency.setText(adapter.getMinFrequency() + "");
        fMaxTime.setText(adapter.getMaxTime() + "");
        fMinTime.setText(adapter.getMinTime() + "");
        repaint();
    }

    public void go() {
        getAdapter().getModel().getListeners().add(this);
    }
}
