package tift2.View;

import tift2.Model.Adapter;
import tift2.Model.ModelListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class View extends JFrame implements ModelListener, ActionListener {

    private static final long serialVersionUID = 1L;
    public Adapter adapter;
    public VCanvas vGraph;
    public FFTCanvas iGraph;
    public JButton exit;
    // public Model model;
    public JTextField fDelta, fLambda, fThetaMax, fSigma, fNumber, fMaxTime;
    public JButton bSave, bOpen, bExit, bReset, bAbout, bFullReset, bInstruction, bEquation;
    public static View view;
    final JPopupMenu menu = new JPopupMenu();
    public String link = "http://www1.union.edu/marrj/radioastro/ToolforInteractiveFourierTransforms.html";
    public String equation;
    public JLabel lEquation;
    File f;
    private JFileChooser jfc;

    public View(Adapter a, String title) {
        super(title);
        View.view = this;
        jfc = new JFileChooser();
        // model = m;
        setAdapter(a);
        f = null;
        vGraph = new VCanvas(this, getAdapter(), getAdapter().getVisiblityGraphPoints());
        iGraph = new FFTCanvas(this, getAdapter(), getAdapter().getImageGraphPoints());

        vGraph.setSize(300, 100);

        JPanel row1, row1col1, row1col2, row1col2col2, jBlank, jblank, labels, jDelta, jLambda, jExponent, jButtons, jButtons2, jButtons3, jButtons4, jButton5, jThetaMax, jSigma, jLabels, jFields;
        JLabel lDelta, lLambda, lExponent, lThetaMax, lSigma;
        equation = "";
        // BUTTONS
        bOpen = new JButton("Open file");
        bSave = new JButton("Save file");
        bExit = new JButton("Exit");
        bReset = new JButton("Reset");
        bFullReset = new JButton("Full Reset");
        bAbout = new JButton("About");
        bInstruction = new JButton("Instructions");
        bEquation = new JButton("Enter Equation");
        lDelta = new JLabel("<HTML><P><B>f(t) Input Parameters:</P><P>" + '\u0394'
                + "t: </P><P># of points:</P><P> </P><P><B>Display Options:</B></P><P>max time: </P><P>max frequency: </P></HTML>");
        lDelta.setMaximumSize(new Dimension(105, 250));
        fDelta = new JTextField(adapter.getDeltaBaseline() + "");
        lEquation = new JLabel("Equation: ");

        fNumber = new JTextField(adapter.getNumberOfPoints() + "");
        fMaxTime = new JTextField(adapter.getDeltaBaseline() * adapter.getNumberOfPoints() + ""); // Max Time to be
        // displayed
        fLambda = new JTextField(adapter.getLambda() + "");
        fThetaMax = new JTextField(adapter.getLambda() / adapter.getDeltaBaseline() + "");
        fSigma = new JTextField(getVGraph().getSigma() + "");
        fDelta.setToolTipText("<HTML><P WIDTH='300px'>\u0394t =  x-axis step-size. (Numbers only, do not include " + "units)<BR/>");
        fThetaMax.setToolTipText("<HTML><P WIDTH='300px'>max frequency = largest frequency displayed in spectrum window.</P></HTML>");
        fMaxTime.setToolTipText("<HTML><P WIDTH='300px'>max time = largest time displayed in time domain.</P></HTML>");
        fDelta.addMouseListener(new MouseAdapter() {

            final int defaultTimeout = ToolTipManager.sharedInstance().getInitialDelay();

            @Override
            public void mouseEntered(MouseEvent e) {
                ToolTipManager.sharedInstance().setInitialDelay(0);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ToolTipManager.sharedInstance().setInitialDelay(defaultTimeout);
            }
        });
        fThetaMax.addMouseListener(new MouseAdapter() {

            final int defaultTimeout = ToolTipManager.sharedInstance().getInitialDelay();

            @Override
            public void mouseEntered(MouseEvent e) {
                ToolTipManager.sharedInstance().setInitialDelay(0);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ToolTipManager.sharedInstance().setInitialDelay(defaultTimeout);
            }
        });
        fSigma.addMouseListener(new MouseAdapter() {

            final int defaultTimeout = ToolTipManager.sharedInstance().getInitialDelay();

            @Override
            public void mouseEntered(MouseEvent e) {
                ToolTipManager.sharedInstance().setInitialDelay(0);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ToolTipManager.sharedInstance().setInitialDelay(defaultTimeout);
            }
        });
        fNumber.addMouseListener(new MouseAdapter() {

            final int defaultTimeout = ToolTipManager.sharedInstance().getInitialDelay();

            @Override
            public void mouseEntered(MouseEvent e) {
                ToolTipManager.sharedInstance().setInitialDelay(0);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ToolTipManager.sharedInstance().setInitialDelay(defaultTimeout);
            }
        });
        fLambda.addMouseListener(new MouseAdapter() {

            final int defaultTimeout = ToolTipManager.sharedInstance().getInitialDelay();

            @Override
            public void mouseEntered(MouseEvent e) {
                ToolTipManager.sharedInstance().setInitialDelay(0);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ToolTipManager.sharedInstance().setInitialDelay(defaultTimeout);
            }
        });

        fSigma.setMaximumSize(new Dimension(50, 20));
        // fSigma.setMinimumSize(new Dimension(50,20));
        fSigma.addActionListener(this);
        fDelta.setMaximumSize(new Dimension(50, 20));
        fMaxTime.setMaximumSize(new Dimension(50, 20));
        fMaxTime.setMinimumSize(new Dimension(50, 20));
        fMaxTime.addActionListener(this);
        fDelta.addActionListener(this);
        fLambda.setMaximumSize(new Dimension(50, 20));
        fLambda.addActionListener(this);
        fThetaMax.setMaximumSize(new Dimension(50, 35));
        fThetaMax.addActionListener(this);
        fNumber.setMaximumSize(new Dimension(50, 20));
        fNumber.setMinimumSize(new Dimension(50, 20));
        fNumber.addActionListener(this);
        JScrollPane jScroll;
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        row1 = new JPanel();
        row1.setLayout(new BoxLayout(row1, BoxLayout.X_AXIS));
        row1col1 = new JPanel();
        row1col1.setBorder(new EmptyBorder(5, 5, 5, 5));
        row1col1.setLayout(new BoxLayout(row1col1, BoxLayout.Y_AXIS));
        row1col2 = new JPanel();
        row1col2.setBorder(new EmptyBorder(5, 5, 5, 5));
        row1col2.setLayout(new BoxLayout(row1col2, BoxLayout.Y_AXIS));
        row1col2col2 = new JPanel();
        row1col2col2.setLayout(new BoxLayout(row1col2col2, BoxLayout.Y_AXIS));
        labels = new JPanel();
        labels.setLayout(new GridLayout());

        getContentPane().add(row1);
        row1.add(row1col1);
        row1.add(row1col2);
        row1col1.setPreferredSize(new Dimension(600, 500));
        row1col1.setMinimumSize(new Dimension(300, 300));
        row1col1.add(lEquation);
        row1col1.add(vGraph);
        row1col1.add(Box.createRigidArea(new Dimension(5, 5)));

        row1col1.add(iGraph);
        row1col2.setMaximumSize(new Dimension(100, 375));

        row1col2.add(row1col2col2);

        // row1col2col2.add(new JTable());

        labels.setLayout(new BoxLayout(labels, BoxLayout.X_AXIS));

        jDelta = new JPanel();
        jLambda = new JPanel();
        jExponent = new JPanel();
        jButtons = new JPanel();
        jButtons2 = new JPanel();
        jButtons3 = new JPanel();
        jButtons4 = new JPanel();
        jButton5 = new JPanel();
        jThetaMax = new JPanel();
        jBlank = new JPanel();
        jblank = new JPanel();
        jSigma = new JPanel();
        jLabels = new JPanel();
        jFields = new JPanel();
        labels.setSize(100, 200);

        labels.add(jLabels);
        labels.add(jFields);

        row1col2col2.add(labels);

        row1col2col2.add(jButtons);
        row1col2col2.add(jButtons2);

        row1col2col2.add(jButtons3);

        row1col2col2.add(jblank);
        row1col2col2.add(jBlank);

        row1col2col2.add(jButtons4);
        row1col2col2.add(jButton5);

        jDelta.setLayout(new BoxLayout(jDelta, BoxLayout.X_AXIS));
        jLambda.setLayout(new BoxLayout(jLambda, BoxLayout.X_AXIS));
        jExponent.setLayout(new BoxLayout(jExponent, BoxLayout.X_AXIS));
        jThetaMax.setLayout(new BoxLayout(jThetaMax, BoxLayout.X_AXIS));
        jButtons.setLayout(new BoxLayout(jButtons, BoxLayout.X_AXIS));
        jButtons2.setLayout(new BoxLayout(jButtons2, BoxLayout.X_AXIS));
        jButtons3.setLayout(new BoxLayout(jButtons3, BoxLayout.X_AXIS));
        jButtons4.setLayout(new BoxLayout(jButtons4, BoxLayout.X_AXIS));
        jLabels.setLayout(new BoxLayout(jLabels, BoxLayout.Y_AXIS));
        jFields.setLayout(new BoxLayout(jFields, BoxLayout.Y_AXIS));
        jButtons.add(bOpen);
        jButtons.add(bSave);
        jButtons2.add(bEquation);
        jButtons3.add(bReset);
        jButtons3.add(bFullReset);
        jButtons4.add(bInstruction);
        jButton5.add(bAbout);

        jButton5.add(bExit);

        jLabels.add(lDelta);
        jFields.add(Box.createRigidArea(new Dimension(10, 50)));
        jFields.add(fDelta);
        jFields.add(fNumber);
        jFields.add(Box.createRigidArea(new Dimension(10, 35)));
        jFields.add(fMaxTime);
        jFields.add(fThetaMax);
        jFields.add(Box.createRigidArea(new Dimension(10, 20)));

        // BUTTONS FUNCTIONS
        bOpen.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                jfc.showOpenDialog(View.view);
                f = jfc.getSelectedFile();

                if (f == null || !f.canRead()) {
                    return;
                }
                TreeMap<Double, Double>[] tm = view.parseFile(f);
                adapter.fullReset();
                if (tm != null) {
                    view.adapter.importVisibilityGraphPoints(tm[0]);
                    view.adapter.importVisibilityGraphRms(tm[1]);// view.parseFile(f));
                    view.adapter.importImageGraphPoints(tm[2]);
                }
                view.lEquation.setText("Equation: ");
            }
        });

        bSave.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                jfc.showSaveDialog(View.view);
                File f = jfc.getSelectedFile();
                if (f == null) {
                    return;
                }
                writeIntoFile(f, view.adapter.exportVisibilityGraphPoints());
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
                    TreeMap<Double, Double>[] tm = view.parseFile(f);
                    adapter.fullReset();
                    if (tm != null) {
                        view.adapter.importVisibilityGraphPoints(tm[0]);
                        view.adapter.importVisibilityGraphRms(tm[1]);// view.parseFile(f));
                        view.adapter.importImageGraphPoints(tm[2]);
                    }
                    view.lEquation.setText("Equation: ");
                } else {
                    if (!equation.equals("")) {
                        adapter.evaluate(equation);
                        view.lEquation.setText("Equation: " + equation);
                    } else {
                        view.adapter.fullReset();
                    }

                    // graphEquation();
                }
            }
        });

        bFullReset.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                view.adapter.fullReset();
                view.lEquation.setText("Equation: ");
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
                JLabel jl = new JLabel("<html>Acceptable Inputs:<br><br>"
                        + "Use 'x' as the independent variable, e as Euler's NumberToken, and pi as pi.<br>"
                        + "use _ to represent negative numbers.<br>" + "*, ^, /, +, -, (, and ) are valid operators.<br>"
                        + "sin(_), cos(_), tan(_), log(_), ln(_), delta(_) and u(_) (unit step) are also valid.<br>"
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

                enter.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        equation = eqn.getText().toLowerCase().replace(" ", "");
                        try {
                            adapter.evaluate(equation);
                            view.lEquation.setText("Equation: " + equation);
                            jf.setVisible(false);
                            graphEquation();
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(jf, "Equation Error. Try adding parentheses.");
                        }
                    }

                });

                eqn.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        equation = eqn.getText().toLowerCase().replace(" ", "");
                        try {
                            adapter.evaluate(equation);
                            view.lEquation.setText("Equation: " + equation);
                            jf.setVisible(false);
                            graphEquation();
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(jf, "Equation Error. Try adding parentheses.");
                        }
                    }

                });

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

    public void paintComponent() {
        fLambda.setText(adapter.getLambda() + "");
        fDelta.setText(adapter.getDeltaBaseline() + "");
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
                    view.adapter.setLambda(Double.parseDouble(strLine.split(" ")[1]));
                    view.fLambda.setText(strLine.split(" ")[1]);
                } else if (strLine.trim().startsWith("*deltaBaseline")) {
                    view.adapter.setDeltaBaseline(Double.parseDouble(strLine.split(" ")[1]));
                    view.fDelta.setText(strLine.split(" ")[1]);
                } else if (strLine.trim().startsWith("*numberOfPoints")) {
                    view.adapter.setNumberOfPoints(Integer.parseInt(strLine.split(" ")[1]));
                    view.fNumber.setText(strLine.split(" ")[1]);
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

    public void setVGraph(VCanvas graph) {
        vGraph = graph;
    }

    public FFTCanvas getIGraph() {
        return iGraph;
    }

    public void setIGraph(FFTCanvas graph) {
        iGraph = graph;
    }

    public JButton getExit() {
        return exit;
    }

    public void setExit(JButton exit) {
        this.exit = exit;
    }

    @Override
    public void update() {
        fDelta.setText(adapter.getDeltaBaseline() + "");
        fLambda.setText(adapter.getLambda() + "");
        fNumber.setText(adapter.getNumberOfPoints() + "");
        // fThetaMax = new
        // JTextField(adapter.getLambda()/this.adapter.getDeltaBaseline()+ "");
        fSigma.setText(getVGraph().getSigma() + "");

        repaint();
    }

    public void graphEquation() {
        adapter.graphEquation(equation);
    }

    public void go() {
        getAdapter().getModel().getListeners().add(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(fDelta)) {
            try {
                Double.parseDouble(fDelta.getText());
                view.lEquation.setText("");
                adapter.setDeltaBaseline(Double.parseDouble(fDelta.getText()));
                fMaxTime.setText(adapter.getDeltaBaseline() * adapter.getNumberOfPoints() + "");
            } catch (NumberFormatException e1) {
            }
        } else if (e.getSource().equals(fLambda)) {
            try {
                Double.parseDouble(fLambda.getText());
                adapter.setLambda(Double.parseDouble(fLambda.getText()));
                fMaxTime.setText(adapter.getDeltaBaseline() * adapter.getNumberOfPoints() + "");
            } catch (NumberFormatException e1) {
            }
        } else if (e.getSource().equals(fNumber)) {
            try {
                view.lEquation.setText("");
                Integer.parseInt(fNumber.getText().trim());
                adapter.setNumberOfPoints(Integer.parseInt(fNumber.getText().trim()));
                fMaxTime.setText(adapter.getDeltaBaseline() * adapter.getNumberOfPoints() + "");
            } catch (NumberFormatException e1) {
            }
        } else if (e.getSource().equals(fThetaMax)) {
            try {
                Double.parseDouble(fThetaMax.getText());
                update();
            } catch (NumberFormatException e1) {
            }
        } else if (e.getSource().equals(fSigma)) {
            try {
                Integer.parseInt(fSigma.getText());
                getVGraph().setSigma(Integer.parseInt(fSigma.getText()));
                getVGraph().update();
                update();
            } catch (NumberFormatException e1) {
            }
        } else if (e.getSource().equals(fMaxTime)) {
            try {
                Double.parseDouble(fMaxTime.getText());
                update();
            } catch (NumberFormatException e1) {
            }
        }

    }
}
