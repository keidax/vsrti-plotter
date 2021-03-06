package fringe.View;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import common.View.BaseView;
import common.View.FileDrop;
import common.View.InputFile;

import common.View.ViewUtilities;
import fringe.Model.Adapter;
import fringe.Model.Model;
import fringe.Model.ModelListener;

public class View extends BaseView implements ModelListener {
    
    private static final long serialVersionUID = 1L;
    public Adapter adapter;
    public VCanvas vCanvas;
    // public FFTCanvas iGraph;
    public Model model;
    public TableModel tableModel;
    public FileTable jTable;
    public JTextField fD, fLambda, fSigma, fT1, fT2, fBaseline;
    public JLabel lDelta, lLambda, lSigma;
    public JButton bSave, bOpen, bExit, bReset, bInstruction, bAbout, bHide, bDelete;
    private JButton updateButton;
    
    public boolean showSinc = false, isDegrees = true;
    public String link = "http://www1.union.edu/marrj/radioastro/Instructions_Plot_Fringe_Pattern.html";
    
    public View(Adapter a, Model m, String title) {
        super(title);
        model = m;
        setAdapter(a);
        tableModel = new TableModel(this);
        jTable = new FileTable(tableModel, this);
        vCanvas = new VCanvas(this, getAdapter(), getAdapter().getVisiblityGraphPoints());
        // iGraph = new
        // FFTCanvas(this,this.getAdapter(),getAdapter().getImageGraphPoints());
        
        vCanvas.setSize(300, 100);
        
        JPanel row1, row1col1, row1col2, row1col2col2, labels, jDelta, jLambda, jExponent, jButtons, jButtons2, jButtons3, jButtons4, jButtons5, jThetaMax, jSigma, jLabels, jFields;
        
        // BUTTONS
        bOpen = new JButton("Open file");
        bSave = new JButton("Save file");
        bExit = new JButton("Exit");
        bReset = new JButton("Reset to defaults");
        bDelete = new JButton("Delete Data");
        bInstruction = new JButton("Instructions");
        bAbout = new JButton("About");
        bHide = new JButton("Show Fringe Pat.");
        
        updateButton = new JButton("Update");
        
        lDelta =
                new JLabel("<HTML><BODY><table border=\"0px\" style=\"position:relativ;top:-10px;\">"
                
                + "<tr height='10px'><td>" + "display factor of " + '\u03C3' + ": </td></tr>"
                        + "<tr height = '10px'><b>Model Parameters</b></tr>"
                        
                        + "<TR HEIGHT='10px'><td>T1: </td></tr>" + "<TR HEIGHT='10px'><td>T2: </td></tr>"
                        
                        + "<tr height='10px'><td>Baseline:</td></tr>"
                        
                        + "<tr height='10px'><td>D: </td></tr>" + "<tr height='10px'><td>" + '\u03BB' + ": </td></tr>"
                        + "</table></BODY></HTML>");
        jTable.setToolTipText("<HTML><P WIDTH='100px'>Drag and Drop data files into this box. File names should contain angle distance in single quotes. <B>Example:</B> file with angle 23.9 can have this names: \"file_a'23.9'.rad\", \"jun3.12angle'23.9'.rad\", etc.</P></HTML>");
        // lDelta.setMaximumSize(100, 22);
        lDelta.setMaximumSize(new Dimension(130, 200));
        lLambda = new JLabel('\u03BB' + ": ");
        lLambda.setSize(100, 20);
        lSigma = new JLabel('\u03C3' + " displaying factor: ");
        lSigma.setSize(100, 20);
        fD = new JTextField(model.getDiameter() + "");
        fD.setToolTipText("<HTML><P>D = diameter of detector</P></HTML>");
        fLambda = new JTextField(model.getLambda() + "");
        fLambda.setToolTipText("\u03BB = wavelength of the radiation detected");
        fSigma = new JTextField(model.getDisplayFactor() + "");
        fSigma.setMaximumSize(new Dimension(50, 20));
        fSigma.setToolTipText("<HTML><P WIDTH='300px'>The displayed sizes of error bars = RMS * (display factor of \u03C3)"
                + "Error bars are not displayed where there is no RMS to calculate.</P></HTML>");
        fT1 = new JTextField(model.getT1() + "");
        fT1.setToolTipText("T1 is the measured power with only the stationary CFL.");
        fT2 = new JTextField(model.getT2() + "");
        fT2.setToolTipText("T2 is the measured power with only the moving CFL when at the central position (theta = 0)");
        fBaseline = new JTextField(model.getBaseline() + "");
        fBaseline.setToolTipText("Baseline = distance between the feeds.");
        fT1.setMaximumSize(new Dimension(50, 20));
        fT2.setMaximumSize(new Dimension(50, 20));
        fBaseline.setMaximumSize(new Dimension(50, 20));
        fD.setMaximumSize(new Dimension(50, 20));
        fLambda.setMaximumSize(new Dimension(50, 225));
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
        jScroll = new JScrollPane(jTable);
        jScroll.setMaximumSize(new Dimension(200, 200));
        jScroll.setPreferredSize(new Dimension(200, 200));
        jScroll.setMinimumSize(new Dimension(150, 125));
        
        getContentPane().add(row1);
        row1.add(row1col1);
        row1.add(row1col2);
        row1col1.setPreferredSize(new Dimension(600, 500));
        row1col1.setMinimumSize(new Dimension(300, 300));
        row1col1.add(vCanvas);
        // row1col2.setMaximumSize(new Dimension(100, 450));
        row1col2.add(jScroll);// fileTable.createVectors());
        row1col2.add(Box.createRigidArea(new Dimension(5, 20)));
        row1col2.add(row1col2col2);
        // row1col2col2.add(new JTable());
        row1col2col2.add(labels);
        labels.setLayout(new BoxLayout(labels, BoxLayout.X_AXIS));
        
        jDelta = new JPanel();
        jLambda = new JPanel();
        jExponent = new JPanel();
        jButtons = new JPanel();
        jButtons2 = new JPanel();
        jButtons3 = new JPanel();
        jButtons4 = new JPanel();
        jButtons5 = new JPanel();
        jThetaMax = new JPanel();
        jSigma = new JPanel();
        jLabels = new JPanel();
        jFields = new JPanel();
        labels.setSize(100, 500);
        labels.add(jLabels);
        labels.add(jFields);
        // labels.add(jDelta);
        // labels.add(jLambda);
        // labels.add(jExponent);
        // labels.add(jThetaMax);
        // labels.add(jSigma);
        row1col2col2.add(jButtons);
        row1col2col2.add(jButtons3);
        row1col2col2.add(jButtons2);
        
        // row1col2col2.add(jBlank);
        row1col2col2.add(jButtons4);
        row1col2col2.add(jButtons5);
        // labels.add(jButtons);
        jDelta.setLayout(new BoxLayout(jDelta, BoxLayout.X_AXIS));
        jLambda.setLayout(new BoxLayout(jLambda, BoxLayout.X_AXIS));
        jExponent.setLayout(new BoxLayout(jExponent, BoxLayout.X_AXIS));
        jThetaMax.setLayout(new BoxLayout(jThetaMax, BoxLayout.X_AXIS));
        jButtons.setLayout(new BoxLayout(jButtons, BoxLayout.X_AXIS));
        jButtons2.setLayout(new BoxLayout(jButtons2, BoxLayout.X_AXIS));
        jLabels.setLayout(new GridLayout(8, 2, 1, 1));
        jFields.setLayout(new GridLayout(8, 2, 1, 1));
        
        // jDelta.add(lDelta);
        // jDelta.add(fDelta);
        // jLambda.add(lLambda);
        // jLambda.add(fLambda);
        // jExponent.add(lExponent);
        // jExponent.add(fExponent);
        // jThetaMax.add(lThetaMax);
        // jThetaMax.add(fThetaMax);
        // jSigma.add(lSigma);
        // jSigma.add(fSigma);
        jButtons3.add(bHide);
        jButtons.add(bOpen);
        jButtons.add(bSave);
        jButtons2.add(bReset);
        jButtons2.add(bDelete);
        jButtons4.add(bInstruction);
        jButtons5.add(bAbout);
        jButtons5.add(bExit);
        
        jLabels.add(new JLabel("display factor of " + '\u03C3' + ":"));
        jLabels.add(new JLabel("<HTML><b>Model Parameters</b></HTML>"));
        jLabels.add(new JLabel("T1:"));
        jLabels.add(new JLabel("T2:"));
        jLabels.add(new JLabel("Baseline:"));
        jLabels.add(new JLabel("D (ant. dia.):"));
        jLabels.add(new JLabel('\u03BB' + ":"));
        
        jFields.add(fSigma);
        jFields.add(Box.createRigidArea(new Dimension(5, 10)));
        jFields.add(fT1);
        jFields.add(fT2);
        jFields.add(fBaseline);
        jFields.add(fD);
        jFields.add(fLambda);
        jFields.add(updateButton);
        // jFields.add(fThetaMax);
        
        // BUTTONS FUNCTIONS
        bOpen.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser fileChooser = ViewUtilities.getFileChooser();
                fileChooser.showOpenDialog(View.this);
                File f = fileChooser.getSelectedFile();
                if (f == null || !f.canRead()) {
                    return;
                }
                TreeMap<Double, Double>[] tm = View.this.parseFile(f);
                View.this.adapter.importVisibilityGraphPoints(tm[0]);
                View.this.adapter.importVisibilityGraphRms(tm[1]);
                
                vCanvas.setGraphTitle("Fringe Function (" + ViewUtilities.getShortFileName(f) + ")");
                
            }
        });
        
        bSave.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser fileChooser = ViewUtilities.getFileChooser();
                fileChooser.showSaveDialog(View.this);
                File f = fileChooser.getSelectedFile();
                if (f == null) {
                    return;
                } else if (!f.getName().endsWith(".dat")) {
                    f = new File(f.getAbsolutePath() + ".dat");
                }
                writeIntoFile(f, View.this.adapter.exportVisibilityGraphPoints());
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
                View.this.adapter.reset();
            }
        });
        
        bDelete.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                tableModel.removeAllInputFiles();
                View.this.adapter.fullReset();
                vCanvas.setGraphTitle("Fringe Function");
            }
        });
        
        bHide.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showSinc) {
                    bHide.setText("Show Fringe Pat.");
                } else {
                    bHide.setText("Hide Fringe Pat.");
                }
                showSinc = !showSinc;
                update();
            }
            
        });
        
        /*
         * bDeg.addActionListener(new ActionListener() {
         * @Override
         * public void actionPerformed(ActionEvent e) {
         * if (isDegrees) {
         * bDeg.setText("In Degrees");
         * vCanvas.xAxisTitle = "angle [rad]";
         * } else {
         * bDeg.setText("In Radians");
         * vCanvas.xAxisTitle = "angle [\u00b0]";
         * }
         * isDegrees = !isDegrees;
         * // TODO make this actually do something
         * }
         * });
         */
        
        bAbout.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFrame jf = new JFrame("About VRSTI Plotter");
                JLabel jl = new JLabel(common.View.ViewUtilities.VSRTI_ABOUT_TEXT);
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
        FileDrop fileDrop = new FileDrop(jTable, new FileDrop.Listener() {
            
            @Override
            public void filesDropped(java.io.File[] files) {
                vCanvas.setGraphTitle("Fringe Function (" + ViewUtilities.getShortFileName(files[0]) + ")");
                for (int i = 0; i < files.length; i++) {
                    if (InputFile.isFormatCorrect(files[i])) {
                        tableModel.addInputFile(new InputFile(files[i]));
                    } else {
                        JOptionPane.showMessageDialog(View.this,
                                "Incorrect data file format. Try to open file instead of drag-and-drop.",
                                "Incorrect format", JOptionPane.ERROR_MESSAGE);
                    }
                }
                
                sendAdapterFiles();
                
            }
        });
        
        ActionListener updateListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateModelFromValues();
            }
        };
        
        updateButton.addActionListener(updateListener);
        
        for (JTextField jt : new JTextField[] {fD, fLambda, fSigma, fT1, fT2, fBaseline}) {
            jt.addActionListener(updateListener);
        }
        
        // this.setSize(800, 600);
        this.pack();
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
                    model.setLambda(Double.parseDouble(strLine.split(" ")[1]));
                    fLambda.setText(strLine.split(" ")[1]);
                } else if (strLine.trim().startsWith("*deltaBaseline")) {
                    model.setBaseline(Double.parseDouble(strLine.split(" ")[1]));
                } else if (strLine.trim().startsWith("*exponent")) {
                    model.setExponent(Integer.parseInt(strLine.split(" ")[1]));
                } else if (strLine.trim().startsWith("BASELINE_POWER_RMS")) {
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
                        // System.out.println(s.length + "*****");
                        ret.put(Double.parseDouble(s[0]), Double.parseDouble(s[1]));
                        if (s.length > 2 && !s[2].trim().equals("null")) {
                            rms.put(Double.parseDouble(s[0]), Double.parseDouble(s[2]));
                            // System.out.println("Adding rms for: " + Double.parseDouble(s[0]));
                        }
                    } catch (NumberFormatException e) {}
                } else if (im) {
                    if (strLine.trim().equals("")) {
                        continue;
                    }
                    try {
                        String[] s = strLine.split(" ");
                        retim.put(Double.parseDouble(s[0]), Double.parseDouble(s[1]));
                    } catch (NumberFormatException e) {}
                } else if (strLine.trim().length() == 0) {
                    continue;
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Incorrect file format. Try to drag-and-drop files into drag-and-drop table area.",
                            "Incorrect format", JOptionPane.ERROR_MESSAGE);
                    return null;
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
    
    public TreeMap<Double, Double>[] flatten(TreeMap<Double, ArrayList<Double>> data,
            TreeMap<Double, ArrayList<Double>> rms) {
        TreeMap<Double, Double> retData = new TreeMap<Double, Double>();
        TreeMap<Double, Double> retRms = new TreeMap<Double, Double>();
        Set<Double> dataKeys = data.keySet();
        // Set<Double> rmsKeys = rms.keySet();
        for (Double key : dataKeys) {
            // x has 1 value, rms 0
            if (data.get(key).size() == 1 && (rms.containsKey(key) ? rms.get(key).size() : 0) == 0) {
                retData.put(key, data.get(key).get(0));
            }
            // x has 1 value, rms has 1 value
            if (data.get(key).size() == 1 && (rms.containsKey(key) ? rms.get(key).size() : 0) == 1) {
                retData.put(key, data.get(key).get(0));
                retRms.put(key, rms.get(key).get(0));
            }
            // x has more then 1, but rms.size != x.size
            if (data.get(key).size() != (rms.containsKey(key) ? rms.get(key).size() : 0)) {
                retData.put(key, data.get(key).get(0));
            }
            // x.size == rms.size
            else {
                retData.put(key, countAverageValue(data.get(key), rms.containsKey(key) ? rms.get(key) : null));
                retRms.put(key, countAverageRms(rms.containsKey(key) ? rms.get(key) : null));
            }
            System.out.println("Flatten: " + key + ", " + retData.get(key) + ", " + retRms.get(key));
        }
        TreeMap<Double, Double>[] back = new TreeMap[2];
        back[0] = retData;
        back[1] = retRms;
        return back;
    }
    
    public double countAverageValue(ArrayList<Double> data, ArrayList<Double> rms) {
        double num = 0;
        double denom = 0;
        for (int i = 0; i < data.size(); i++) {
            num += data.get(i) / rms.get(i);
            denom += 1 / rms.get(i);
        }
        return num / denom;
    }
    
    public double countAverageRms(ArrayList<Double> rms) {
        double sum = 0;
        for (double r : rms) {
            sum += r * r;
        }
        return Math.sqrt(sum / (rms.size() * (rms.size() - 1)));
    }
    
    public Adapter getAdapter() {
        return adapter;
    }
    
    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
    }
    
    public VCanvas getVGraph() {
        return vCanvas;
    }
    
    public void setVGraph(VCanvas graph) {
        vCanvas = graph;
    }
    
    public Model getModel() {
        return model;
    }
    
    @Override
    public void update() {
        updateValuesFromModel();
        repaint();
        
        vCanvas.update();
        // this.getIGraph().update();
    }
    
    public void go() {
        getAdapter().getModel().getListeners().add(this);
    }
    
    public void sendAdapterFiles() {
        adapter.setRawPoints(((TableModel) jTable.getModel()).inputFiles);
    }
    
//    @Override
    public void updateValuesFromModel() {
        fBaseline.setText(model.getBaseline() + "");
        fSigma.setText(model.getDisplayFactor() + "");
        fLambda.setText(model.getLambda() + "");
        fD.setText(model.getDiameter() + "");
        fT1.setText(model.getT1() + "");
        fT2.setText(model.getT2() + "");
    }
    
//    @Override
    public void updateModelFromValues() {
        System.out.println("lambda text is " + fLambda.getText());
        try {
            double tempDiameter = Double.parseDouble(fD.getText());
            model.setDiameter(tempDiameter);
        } catch (NumberFormatException e1) {}
        try {
            double tempT1 = Double.parseDouble(fT1.getText());
            model.setT1(tempT1);
        } catch (NumberFormatException e1) {}
        try {
            double tempT2 = Double.parseDouble(fT2.getText());
            model.setT2(tempT2);
        } catch (NumberFormatException e1) {}
        try {
            double tempBaseline = Double.parseDouble(fBaseline.getText());
            model.setBaseline(tempBaseline);
        } catch (NumberFormatException e1) {}
        try {
            double tempLambda = Double.parseDouble(fLambda.getText());
            model.setLambda(tempLambda);
        } catch (NumberFormatException e1) {}
        try {
            double tempSigma = Double.parseDouble(fSigma.getText());
            model.setDisplayFactor(tempSigma);
        } catch (NumberFormatException e1) {}
        
        update();
    }
}
