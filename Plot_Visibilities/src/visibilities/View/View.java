// This controls the size of everything that you see, the buttons, the buttons'
// labels, normal labels, opening and saving files, etc.
package visibilities.View;

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
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import visibilities.Model.Adapter;
import visibilities.Model.ModelListener;

import common.View.BaseView;
import common.View.FileDrop;
import common.View.InputFile;

@SuppressWarnings("serial")
public class View extends BaseView implements ModelListener {
    public Adapter adapter;
    public VCanvas vCanvas;
    // public FFTCanvas iGraph;
    // public Model model;
    public TableModel tableModel;
    public FileTable jTable;
    private JTextField fLambda, fSigma, fT1, fT2, fTheta, fPhi1, fPhi2;
    private JComboBox fShape1, fShape2;
    public JButton bSave, bOpen, bExit, bReset, bDelete, bInstruction, bAbout, bModel;
    
    private JButton updateButton;
    
    public String link = "http://www1.union.edu/marrj/radioastro/Instructions_Plot_Visibilities.html";
    public boolean showVis = false;
    
    private final double defaultT1 = 10, defaultT2 = 10, defaultTheta = 0, defaultPhi1 = 0, defaultPhi2 = 0;
    public double T1 = defaultT1, T2 = defaultT2, theta = defaultTheta;
    private double phi1 = defaultPhi1, phi2 = defaultPhi2;
    
    public View(Adapter a, String title) {
        super(title);
        // model = m;
        setAdapter(a);
        tableModel = new TableModel(this);
        jTable = new FileTable(tableModel, this);
        vCanvas = new VCanvas(this, getAdapter(), getAdapter().getVisiblityGraphPoints());
        // iGraph = new
        // FFTCanvas(this,this.getAdapter(),getAdapter().getImageGraphPoints());
        
        vCanvas.setSize(300, 100);
        
        JPanel row1, row1col1, row1col2, row1col2col2, labels, jDelta, jLambda, jExponent, jButtons, jButtons2, jButtons3, jButtons4, jButtons5, jThetaMax, jLabels, jFields;
        JLabel lDelta, lLambda, lSigma;
        // BUTTONS
        bOpen = new JButton("Open file");
        bSave = new JButton("Save file");
        bModel = new JButton("Display Model Visibility");
        bExit = new JButton("Exit");
        bReset = new JButton("Reset");
        bDelete = new JButton("Delete Data");
        bInstruction = new JButton("Instructions");
        bAbout = new JButton("About");
        
        updateButton = new JButton("Update");
        
        lDelta =
                new JLabel("<HTML><P>" + '\u03BB' + ": </P>" + "<P>" + "display factor of " + '\u03C3' + ": </P>" + ""
                        + "<P></P><P><b>Model Parameters:</b></P><P></P><P>\u03A6:</P>" + "<P>" + "T1" + ": </P>"
                        + "<P>" + "T2" + ": </P>" + "<P>" + "Position of Lamp 2, θ" + ": </P>" + "</HTML>");
        jTable.setToolTipText("<HTML><P WIDTH='100px'>Drag and Drop data files into this box. File names should contain baseline distance in single quotes. <B>Example:</B> file with baseline 23.9 can have these names: \"file_b'23.9'.rad\", \"jun3.12baseline'23.9'.rad\", etc.</P></HTML>");
        
        lDelta.setMaximumSize(new Dimension(140, 220));
        lLambda = new JLabel('\u03BB' + ": ");
        lLambda.setSize(100, 22);
        lSigma = new JLabel('\u03C3' + " displaying factor: ");
        lSigma.setSize(100, 20);
        fPhi1 = new JTextField(phi1 + "");
        fPhi1.setToolTipText("<HTML><P>\u03A6</P></HTML>");
        fPhi1.setMaximumSize(new Dimension(50, 20));
        fPhi2 = new JTextField(phi2 + "");
        fPhi2.setToolTipText("<HTML><P>\u03A6</P></HTML>");
        fPhi2.setMaximumSize(new Dimension(50, 20));
        fLambda = new JTextField(adapter.getLambda() + "");
        fLambda.setToolTipText("<HTML><P WIDTH='300px'>\u03BB is wavelength of radiation. The x-axis values equal baseline divided by wavelength.</HTML>");
        fSigma = new JTextField(getVCanvas().getSigma() + "");
        fSigma.setMaximumSize(new Dimension(50, 20));
        fSigma.setToolTipText("<HTML><P WIDTH='300px'>The displayed sizes of error bars = RMS * (display factor of \u03C3). "
                + "Error bars are not displayed where there is no RMS to calculate.</P></HTML>");
        fT1 = new JTextField(T1 + "");
        fT1.setMaximumSize(new Dimension(50, 20));
        fT1.setToolTipText("<HTML><P WIDTH='300px'>The Temperature of Lamp 1.</P></HTML>");
        
        fT2 = new JTextField(T2 + "");
        fT2.setMaximumSize(new Dimension(50, 20));
        fT2.setToolTipText("<HTML><P WIDTH='300px'>The Temperature of Lamp 2.</P></HTML>");
        
        fTheta = new JTextField(theta + "");
        fTheta.setMaximumSize(new Dimension(50, 20));
        fTheta.setToolTipText("<HTML><P WIDTH='300px'>The position of Lamp 2. Lamp 1 is at position 0.</P></HTML>");
        
        fLambda.setMaximumSize(new Dimension(50, 20));
        
        fShape1 = new JComboBox(new String[] {"rectangular", "uniform disk"});
        fShape2 = new JComboBox(new String[] {"rectangular", "uniform disk"});
        
        /*
         * fShape1.addItemListener(new ItemListener() {
         * @Override
         * public void itemStateChanged(ItemEvent arg0) {
         * update();
         * }
         * });
         * fShape2.addItemListener(new ItemListener() {
         * @Override
         * public void itemStateChanged(ItemEvent arg0) {
         * update();
         * }
         * });
         */
        
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
        jScroll.setMaximumSize(new Dimension(200, 80));
        
        getContentPane().add(row1);
        row1.add(row1col1);
        row1.add(row1col2);
        row1col1.setPreferredSize(new Dimension(600, 500));
        row1col1.setMinimumSize(new Dimension(300, 300));
        row1col1.add(vCanvas);
        // row1col2.setMaximumSize(new Dimension(500, 500));
        row1col2.add(jScroll);// fileTable.createVectors());
        jScroll.setMinimumSize(new Dimension(100, 150));
        row1col2.add(Box.createRigidArea(new Dimension(5, 10)));
        row1col2.setMaximumSize(new Dimension(300, 800));
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
        jLabels = new JPanel();
        jFields = new JPanel();
        labels.setSize(100, 200);
        labels.add(jLabels);
        labels.add(jFields);
        
        row1col2col2.add(jButtons);
        row1col2col2.add(jButtons3);
        row1col2col2.add(jButtons2);
        
        // row1col2col2.add(new JPanel());
        row1col2col2.add(jButtons4);
        // row1col2col2.add(new JPanel());
        
        row1col2col2.add(jButtons5);
        
        jDelta.setLayout(new BoxLayout(jDelta, BoxLayout.X_AXIS));
        jLambda.setLayout(new BoxLayout(jLambda, BoxLayout.X_AXIS));
        jExponent.setLayout(new BoxLayout(jExponent, BoxLayout.X_AXIS));
        jThetaMax.setLayout(new BoxLayout(jThetaMax, BoxLayout.X_AXIS));
        jButtons.setLayout(new BoxLayout(jButtons, BoxLayout.X_AXIS));
        jButtons2.setLayout(new BoxLayout(jButtons2, BoxLayout.X_AXIS));
        jLabels.setLayout(new GridLayout(11, 2, 1, 1));
        jFields.setLayout(new GridLayout(11, 2, 1, 1));
        jButtons3.add(bModel);
        jButtons.add(bOpen);
        jButtons.add(bSave);
        jButtons2.add(bReset);
        jButtons2.add(bDelete);
        jButtons4.add(bInstruction);
        jButtons5.add(bAbout);
        jButtons5.add(bExit);
        
        jLabels.add(new JLabel('\u03BB' + ":"));
        jLabels.add(new JLabel("display factor of " + '\u03C3' + ":"));
        jLabels.add(new JLabel("<HTML><b>Model Parameters:</b></HTML>"));
        jLabels.add(new JLabel("\u03A6 1 (ang. dia. of lamp in radians):"));
        jLabels.add(new JLabel("\u03A6 2:"));
        jLabels.add(new JLabel("shape 1:"));
        jLabels.add(new JLabel("shape 2:"));
        jLabels.add(new JLabel("T1 (power of ant. 1):"));
        jLabels.add(new JLabel("T2 (power of ant. 1):"));
        jLabels.add(new JLabel("θ (Pos. of lamp 2 in radians):"));
        
        jFields.add(fLambda);
        jFields.add(fSigma);
        jFields.add(Box.createRigidArea(new Dimension(5, 20)));
        jFields.add(fPhi1);
        jFields.add(fPhi2);
        jFields.add(fShape1);
        jFields.add(fShape2);
        jFields.add(fT1);
        jFields.add(fT2);
        jFields.add(fTheta);
        jFields.add(updateButton);
        
        // BUTTONS FUNCTIONS
        bOpen.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                fileChooser.showOpenDialog(View.this);
                File f = fileChooser.getSelectedFile();
                if (f == null || !f.canRead()) {
                    return;
                }
                TreeMap<Double, Double>[] tm = parseFile(f);
                View.this.adapter.importVisibilityGraphPoints(tm[0]);
                View.this.adapter.importVisibilityGraphRms(tm[1]);
                update();
            }
        });
        
        bSave.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
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
        
        bModel.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showVis) {
                    bModel.setText("Display Model Visibility");
                } else {
                    bModel.setText("Hide Model Visibility");
                }
                showVis = !showVis;
                update();
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
                // reset local values
                // TODO these really should be in the model
                phi1 = defaultPhi1;
                phi2 = defaultPhi2;
                T1 = defaultT1;
                T2 = defaultT2;
                theta = defaultTheta;
                fShape1.setSelectedIndex(0);
                fShape2.setSelectedIndex(0);
                // reset model values
                getVCanvas().resetToDefaults();
                View.this.adapter.reset();
            }
        });
        
        bDelete.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                tableModel.removeAllInputFiles();
                jTable.repaint();
                View.this.adapter.fullReset();
            }
        });
        
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
        
        @SuppressWarnings("unused")
        FileDrop fileDrop = new FileDrop(jTable, new FileDrop.Listener() {
            
            @Override
            public void filesDropped(java.io.File[] files) {
                for (int i = 0; i < files.length; i++) {
                    
                    tableModel.addInputFile(new InputFile(files[i]));
                    
                }
                
                // TODO sendArrayFiles
                
            }
        });
        
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateModelFromValues();
            }
        });
        
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
                    fLambda.setText(strLine.split(" ")[1]);
                    adapter.setLambda(Double.parseDouble(fLambda.getText()));
                    System.out.println("lambda from file:\t" + Double.parseDouble(fLambda.getText()));
                } else if (strLine.trim().startsWith("*deltaBaseline")) {
                    adapter.setDeltaBaseline(Double.parseDouble(strLine.split(" ")[1]));
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
            
            br.close();
            
            in.close();
            fstream.close(); // TODO handle these resources better, maybe something with AutoClosable
            
        } catch (Exception e) {// Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
        TreeMap<Double, Double>[] back = new TreeMap[3]; // TODO figure this one out too
        back[0] = ret;
        back[1] = rms;
        back[2] = retim;
        update();
        return back;
    }
    
    public Adapter getAdapter() {
        return adapter;
    }
    
    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
    }
    
    public VCanvas getVCanvas() {
        return vCanvas;
    }
    
    public void setVCanvas(VCanvas canvas) {
        vCanvas = canvas;
    }
    
    @Override
    public void update() {
        updateValuesFromModel();
        
        fPhi1.setText(phi1 + "");
        fPhi2.setText(phi2 + "");
        fT1.setText(T1 + "");
        fT2.setText(T2 + "");
        fTheta.setText(theta + "");
        
        repaint();
    }
    
    public void go() {
        getAdapter().getModel().getListeners().add(this);
    }
    
    public void sendAdapterFiles() {
        adapter.setRawPoints(((TableModel) jTable.getModel()).inputFiles);
    }
    
    public double getPhi1() {
        setPhi1(Double.parseDouble(fPhi1.getText()));
        return phi1;
    }
    
    public void setPhi1(double phi) {
        /*
         * if(d<=0) this.d1 = 20; else
         */
        phi1 = phi;
    }
    
    public double getPhi2() {
        setPhi2(Double.parseDouble(fPhi2.getText()));
        return phi2;
    }
    
    public void setPhi2(double phi) {
        /*
         * if(d<=0) this.d2 = 20; else
         */
        phi2 = phi;
    }
    
    public boolean isShape1Rect() {
        if (((String) fShape1.getSelectedItem()).equalsIgnoreCase("rectangular")) {
            return true;
        }
        
        return false;
    }
    
    public boolean isShape2Rect() {
        if (((String) fShape2.getSelectedItem()).equalsIgnoreCase("rectangular")) {
            return true;
        }
        
        return false;
    }
    
    @Override
    public void updateValuesFromModel() {
        fLambda.setText(adapter.getLambda() + "");
        fSigma.setText(getVCanvas().getSigma() + "");
    }
    
    @Override
    public void updateModelFromValues() {
        try {
            double tempLambda = Double.parseDouble(fLambda.getText());
            adapter.setLambda(tempLambda);
        } catch (NumberFormatException e1) {}
        
        try {
            double tempSig = Double.parseDouble(fSigma.getText());
            getVCanvas().setSigma(tempSig);
        } catch (NumberFormatException e1) {}
        
        try {
            double tempPhi1 = Double.parseDouble(fPhi1.getText());
            setPhi1(tempPhi1);
        } catch (NumberFormatException e1) {}
        
        try {
            double tempPhi2 = Double.parseDouble(fPhi2.getText());
            setPhi2(tempPhi2);
        } catch (NumberFormatException e1) {}
        
        try {
            double tempT1 = Double.parseDouble(fT1.getText());
            T1 = tempT1;
        } catch (NumberFormatException e1) {}
        
        try {
            double tempT2 = Double.parseDouble(fT2.getText());
            T2 = tempT2;
        } catch (NumberFormatException e1) {}
        
        try {
            double tempTheta = Double.parseDouble(fTheta.getText());
            theta = tempTheta;
        } catch (NumberFormatException e1) {}
        
        getVCanvas().update();
        update();
        
    }
}
