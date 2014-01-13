package tift2.View;

import tift2.Model.Adapter;
import tift2.Model.ModelListener;

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
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class View extends JFrame implements ModelListener {
    public Adapter adapter;
    public VCanvas vGraph;
    public FFTCanvas iGraph;
    public String link = "http://www1.union.edu/marrj/radioastro/ToolforInteractiveFourierTransforms.html";
    public String equation;
    File f;
    private JFileChooser jfc;

    public View(Adapter a, String title) {
        super(title);
        jfc = new JFileChooser();
        setAdapter(a);
        f = null;
        vGraph = new VCanvas(this, getAdapter(), getAdapter().getVisiblityGraphPoints());
        iGraph = new FFTCanvas(this, getAdapter(), getAdapter().getImageGraphPoints());

        //vGraph.setSize(300, 100);

        JPanel mainPanel, sidePanel;
        equation = "";

        // BUTTONS

        final JButton bSave, bOpen, bExit, bReset, bAbout, bFullReset, bInstruction, bEquation;
        bOpen = new JButton("Open file");
        bSave = new JButton("Save file");
        bExit = new JButton("Exit");
        bReset = new JButton("Reset");
        bFullReset = new JButton("Full Reset");
        bAbout = new JButton("About");
        bInstruction = new JButton("Instructions");
        bEquation = new JButton("Enter Equation");


        final JLabel lEquation = new JLabel("Equation: ");

        // FIELDS

        final JTextField fDelta, fThetaMax, fNumber, fMaxTime;

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        sidePanel = new JPanel();
        sidePanel.setBorder(new EmptyBorder(5, 5, 5, 5));


        getContentPane().add(mainPanel);
        getContentPane().add(sidePanel);
        //mainPanel.setPreferredSize(new Dimension(600, 500));
        //mainPanel.setMinimumSize(new Dimension(300, 300));
        mainPanel.add(lEquation);
        mainPanel.add(vGraph);
        mainPanel.add(Box.createRigidArea(new Dimension(5, 5)));
        mainPanel.add(iGraph);

        sidePanel.setMaximumSize(new Dimension(100, 400));
        sidePanel.setLayout(new GridBagLayout());


        fDelta = new JTextField(adapter.getDeltaBaseline() + "");
        fNumber = new JTextField(adapter.getNumberOfPoints() + "");
        fMaxTime = new JTextField(adapter.getDeltaBaseline() * adapter.getNumberOfPoints() + ""); // Max Time to be
        // displayed
        fThetaMax = new JTextField(adapter.getLambda() / adapter.getDeltaBaseline() + "");
        fDelta.setToolTipText("<HTML><P WIDTH='300px'>\u0394t =  x-axis step-size. (Numbers only, do not include "
                + "units)<BR/>");
        fThetaMax
                .setToolTipText("<HTML><P WIDTH='300px'>max frequency = largest frequency displayed in spectrum window.</P></HTML>");
        fMaxTime.setToolTipText("<HTML><P WIDTH='300px'>max time = largest time displayed in time domain.</P></HTML>");

//        fDelta.setMaximumSize(new Dimension(50, 20));
//        fThetaMax.setMaximumSize(new Dimension(50, 35));
//        fMaxTime.setMaximumSize(new Dimension(50, 20));
//        fMaxTime.setMinimumSize(new Dimension(50, 20));
//        fNumber.setMaximumSize(new Dimension(50, 20));
//        fNumber.setMinimumSize(new Dimension(50, 20));

        fMaxTime.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double maxTime = Double.parseDouble(fMaxTime.getText());
                    adapter.setMaxTime(maxTime);
                    update();
                } catch (NumberFormatException e1) {
                }
            }
        });
        fDelta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double delta = Double.parseDouble(fDelta.getText());
                    adapter.setDeltaBaseline(delta);
                    fMaxTime.setText(adapter.getDeltaBaseline() * adapter.getNumberOfPoints() + "");
                } catch (NumberFormatException e1) {
                }
            }
        });

        fThetaMax.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double maxFrequency = Double.parseDouble(fThetaMax.getText());
                    adapter.setMaxFrequency(maxFrequency);
                    update();
                } catch (NumberFormatException e1) {
                }
            }
        });

        fNumber.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int numberOfPoints = Integer.parseInt(fNumber.getText());
                    adapter.setNumberOfPoints(numberOfPoints);
                    fMaxTime.setText(adapter.getDeltaBaseline() * adapter.getNumberOfPoints() + "");
                } catch (NumberFormatException e1) {
                }
            }
        });

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
                TreeMap<Double, Double>[] tm = parseFile(f);
                adapter.fullReset();
                if (tm != null) {
                    adapter.importVisibilityGraphPoints(tm[0]);
                    adapter.importVisibilityGraphRms(tm[1]);// view.parseFile(f));
                    adapter.importImageGraphPoints(tm[2]);
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

        bReset.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (f != null && f.canRead()) {
                    TreeMap<Double, Double>[] tm = parseFile(f);
                    adapter.fullReset();
                    if (tm != null) {
                        adapter.importVisibilityGraphPoints(tm[0]);
                        adapter.importVisibilityGraphRms(tm[1]);// view.parseFile(f));
                        adapter.importImageGraphPoints(tm[2]);
                    }
                    lEquation.setText("Equation: ");
                } else {
                    if (!equation.equals("")) {
                        adapter.evaluate(equation);
                        lEquation.setText("Equation: " + equation);
                    } else {
                        adapter.fullReset();
                    }

                    // graphEquation();
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

        bEquation.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                final JFrame jf = new JFrame("TIFT - Equation");
                JButton enter = new JButton("Enter");
                final JTextField eqn;
                JPanel row1 = new JPanel();
                row1.setLayout(new BoxLayout(row1, BoxLayout.Y_AXIS));
                row1.add(Box.createRigidArea(new Dimension(5, 5)));

                if (!equation.equals("Equation: ")) {
                    eqn = new JTextField(equation);
                } else {
                    eqn = new JTextField("Enter Equation Here");
                }
                // eqn.setMaximumSize(new Dimension(700,100));
                JLabel jl =
                        new JLabel(
                                "<html>Acceptable Inputs:<br><br>"
                                        + "Use t as the independent variable, e as Euler's number, and pi as pi.<br>"
                                        + "*, ^, /, +, -, (, and ) are valid operators.<br>"
                                        + "sin(_), cos(_), tan(_), log(_), ln(_), exp(_), delta(_) and u(_) (unit step) are also valid.<br>"
                                        + "Angles are in radians.<br><br>" + "</html>");
                jf.getContentPane().setLayout(new FlowLayout());
                jf.getContentPane().add(row1);
                row1.add(jl);
                row1.add(eqn);
                row1.add(enter);

                jf.setMinimumSize(new Dimension(400, 250));
                jf.pack();
                jf.setLocationRelativeTo(null);
                jf.setVisible(true);

                ActionListener setEquationListener = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        equation = eqn.getText().toLowerCase().replace(" ", "");
                        try {
                            adapter.evaluate(equation);
                            lEquation.setText("Equation: " + equation);
                            jf.setVisible(false);
//                            graphEquation();
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(jf, "Error evaluating equation: " + e.getMessage());
                            //System.out.println("toString = " + e.toString() + "\nmessage = " + e.getMessage() + "\ncause = " + e.getCause());
                        }
                    }
                };

                enter.addActionListener(setEquationListener);
                eqn.addActionListener(setEquationListener);
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

    public TreeMap<Double, Double>[] parseFile(File f) {
        TreeMap<Double, Double> ret = new TreeMap<Double, Double>();
        TreeMap<Double, Double> rms = new TreeMap<Double, Double>();
        TreeMap<Double, Double> retim = new TreeMap<Double, Double>();
        try {

            FileInputStream fstream = new FileInputStream(f);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            boolean vis = false;
            boolean im = false;
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
                } else if (strLine.trim().startsWith("X_Y_RMS")) {
                    im = false;
                    vis = true;
                } else if (strLine.trim().startsWith("ANGLE_INTENSITY")) {
                    vis = false;
                    im = true;
                } else if (vis) {
                    if (strLine.trim().equals("")) {
                        continue;
                    }
                    try {
                        String[] s = strLine.split(" ");
                        ret.put(Double.parseDouble(s[0]), Double.parseDouble(s[1]));
                        if (s.length > 2 && !s[2].trim().equals("null")) {
                            rms.put(Double.parseDouble(s[0]), Double.parseDouble(s[2]));
                        }
                    } catch (NumberFormatException e) {
                    }
                } else if (im) {
                    if (strLine.trim().equals("")) {
                        continue;
                    }
                    try {
                        String[] s = strLine.split(" ");
                        retim.put(Double.parseDouble(s[0]), Double.parseDouble(s[1]));
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
        TreeMap<Double, Double>[] back = new TreeMap[3];
        back[0] = ret;
        back[1] = rms;
        back[2] = retim;
        return back;
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

    @Override
    public void update() {
        repaint();
    }

//    public void graphEquation() {
//        adapter.graphEquation(equation);
//    }

    public void go() {
        getAdapter().getModel().getListeners().add(this);
    }
}
