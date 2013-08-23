package beam.View;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import beam.Model.Model;

import common.Model.ModelListener;
import common.View.BaseView;
import common.View.FileDrop;
import common.View.InputFile;

/**
 * Main GUI class for Plot_Beam.
 * 
 */

@SuppressWarnings("serial")
public class View extends BaseView implements ModelListener {
    
    private ViewListener listener;
    private VCanvas vCanvas;
    private TableModel tableModel;
    private FileTable jTable;
    private JTextField fD, fLambda, fSigma, fNoise, fHorizontalError;
    private JButton updateButton;
    private JButton bSave, bOpen, bExit, bReset, bInstruction, bAbout, bHide, bDelete;
    private boolean showBeamPattern = false;
    private String link = "http://www1.union.edu/marrj/radioastro/Instructions_Plot_Beam.html";
    private Model model;
    private JTextField fPeakValue;
    private JLabel lblPeakValue;
    
    public View(Model m, String title) {
        super(title);
        model = m;
        
        tableModel = new TableModel();
        jTable = new FileTable(tableModel, this);
        
        vCanvas = new VCanvas(this, new TreeMap<Double, Double>());
        
        vCanvas.setSize(300, 100);
        
        JPanel row1, row1col1, row1col2, row1col2col2, labels, jDelta, jLambda, jExponent, jButtons, jButtons2, jButtons3, jButtons4, jButtons5, jRadioButtons, jThetaMax, jLabels, jFields;
        
        // BUTTONS
        bOpen = new JButton("Open file");
        bSave = new JButton("Save file");
        bExit = new JButton("Exit");
        bReset = new JButton("Reset to defaults");
        bInstruction = new JButton("Instructions");
        bAbout = new JButton("About");
        bHide = new JButton("Show Beam Pattern");
        bDelete = new JButton("Delete Data");
        
        updateButton = new JButton("Update");
        
        jTable.setToolTipText("<HTML><P WIDTH='100px'>Drag and Drop data files into this box. File names should contain angle distance in single quotes. <B>Example:</B> file with angle 23.9 can have these names: \"file_a'23.9'.rad\", \"jun3.12angle'23.9'.rad\", etc.</P></HTML>");
        fD = new JTextField(model.getDiameter() + "");
        fLambda = new JTextField(model.getLambda() + "");
        fLambda.setToolTipText("\u03BB = wavelength of the radiation detected");
        // fThetaMax.setToolTipText("<HTML><P WIDTH='300px'>\u0398 max = field of view which equals the X value of last point shown in the Image Graph.</P></HTML>");
        fSigma = new JTextField(vCanvas.getDisplayFactor() + "");
        fSigma.setMaximumSize(new Dimension(50, 20));
        // fSigma.setMinimumSize(new Dimension(50,20));
        fSigma.setToolTipText("<HTML><P WIDTH='300px'>The displayed sizes of error bars = RMS * (display factor of \u03C3.  )"
                + "Error bars are not displayed where there is no RMS to calculate.</P></HTML>");
        fNoise = new JTextField(model.getNoise() + "");
        fNoise.setMaximumSize(new Dimension(50, 20));
        fNoise.setToolTipText("<HTML><P WIDTH='300px'>signal due to noise in the system; set this to minimum y-value</P></HTML>");
        fD.setToolTipText("<HTML><P>D = diameter of detector</P></HTML>");
        fD.setMaximumSize(new Dimension(50, 20));
        fLambda.setMaximumSize(new Dimension(50, 20));
        fHorizontalError = new JTextField(model.getHorizontalError() + "");
        fHorizontalError.setToolTipText(null); // TODO what should tooltip text for fHorizontalError be?
        // fThetaMax.setMaximumSize(new Dimension(50, 20));
        // fThetaMax.addActionListener(this);
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
        row1col2.setMaximumSize(new Dimension(100, 450));
        row1col2.add(jScroll);
        row1col2.add(Box.createRigidArea(new Dimension(5, 20)));
        row1col2.add(row1col2col2);
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
        jRadioButtons = new JPanel();
        jThetaMax = new JPanel();
        // jSigma = new JPanel();
        jLabels = new JPanel();
        jFields = new JPanel();
        labels.setSize(100, 200);
        labels.add(jLabels);
        labels.add(jFields);
        
        row1col2col2.add(Box.createRigidArea(new Dimension(5, 20)));
        row1col2col2.add(jButtons);
        row1col2col2.add(jButtons3);
        row1col2col2.add(jButtons2);
        
        row1col2col2.add(jRadioButtons);
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
        
        jRadioButtons.setLayout(new BoxLayout(jRadioButtons, BoxLayout.Y_AXIS));
        
        jButtons3.add(bHide);
        jButtons.add(bOpen);
        jButtons.add(bSave);
        jButtons2.add(bReset);
        jButtons2.add(bDelete);
        jButtons4.add(bInstruction);
        jButtons5.add(bAbout);
        jButtons5.add(bExit);
        
        jLabels.add(new JLabel("display factor of σ:"));
        jLabels.add(new JLabel("horizontal uncertainty"));
        jLabels.add(new JLabel("Model Parameters:"));
        jLabels.add(new JLabel("D (ant. dia.):"));
        jLabels.add(new JLabel('\u03BB' + ":"));
        jLabels.add(new JLabel("Noise:"));
        
        lblPeakValue = new JLabel("Peak Value:");
        jLabels.add(lblPeakValue);
        
        jFields.add(fSigma);
        jFields.add(fHorizontalError);
        jFields.add(Box.createRigidArea(new Dimension(5, 20)));
        jFields.add(fD);
        jFields.add(fLambda);
        jFields.add(fNoise);
        
        fPeakValue = new JTextField(model.getPeakValue() + "");
        jFields.add(fPeakValue);
        fPeakValue.setColumns(10);
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
                TreeMap<Double, Double>[] tm = View.this.parseFile(f);
                if (tm != null) {
                    model.importPoints(tm[0]);
                    model.importRms(tm[1]);
                    
                    vCanvas.setGraphTitle("Beam (" + getShortFileName(f) + ")");
                }
                
                model.update();
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
                listener.writeSaveFile(f);
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
                listener.reset();
            }
        });
        
        bDelete.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                tableModel.removeAllInputFiles();
                listener.fullReset();
                vCanvas.setGraphTitle("Beam");
            }
        });
        
        bHide.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isBeamPatternVisible()) {
                    bHide.setText("Show Beam Pattern");
                } else {
                    bHide.setText("Hide Beam Pattern");
                }
                setBeamPatternVisible(!isBeamPatternVisible());
                vCanvas.repaint();
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
        
        FileDrop fileDrop = new FileDrop(jTable, new FileDrop.Listener() {
            
            @Override
            public void filesDropped(java.io.File[] files) {
                vCanvas.setGraphTitle("Beam (" + getShortFileName(files[0]) + ")");
                for (int i = 0; i < files.length; i++) {
                    if (InputFile.isFormatCorrect(files[i])) {
                        tableModel.addInputFile(new InputFile(files[i]));
                    } else {
                        JOptionPane.showMessageDialog(View.this, "Incorrect data file format", "Incorrect format",
                                JOptionPane.ERROR_MESSAGE);
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
        
        for (JTextField jt : new JTextField[] {fD, fLambda, fSigma, fNoise, fHorizontalError}) {
            jt.addActionListener(updateListener);
        }
        
        // this.setSize(1200, 800);
        this.pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
    }
    
    /*
     * public void paintComponent() { fLambda.setText(this.adapter.getLambda() +
     * ""); fD.setText(getD() + ""); }
     */
    
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
                    double tempLambda = Double.parseDouble(strLine.split(" ")[1]);
                    model.setLambda(tempLambda);
                } else if (strLine.trim().startsWith("*deltaBaseline")) {
                    double tempDeltaBaseline = Double.parseDouble(strLine.split(" ")[1]);
                    model.setDeltaBaseline(tempDeltaBaseline);
                    // viewer.adapter.setDeltaBaseline(Double.parseDouble(strLine.split(" ")[1]));
                } else if (strLine.trim().startsWith("*exponent")) {
                    int tempExponent = Integer.parseInt(strLine.split(" ")[1]);
                    model.setExponent(tempExponent);
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
                            System.out.println("********* " + s[2]);
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
                    in.close();
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
    
    @SuppressWarnings("unchecked")
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
    
    @Override
    public void updateView(TreeMap<Double, Double> points, TreeMap<Double, Double> rmsPoints) {
        System.out.println("called view.updateView(points, rmsPoints)");
        updateValuesFromModel();
        repaint();
        vCanvas.update(points, rmsPoints);
        // this.getIGraph().update();
        System.out.println("updating with " + points.size() + " points:");
        System.out.println(points);
    }
    
    public boolean isBeamPatternVisible() {
        return showBeamPattern;
    }
    
    public void setBeamPatternVisible(boolean showPattern) {
        showBeamPattern = showPattern;
    }
    
    public void sendAdapterFiles() {
        ArrayList<InputFile> tempArray = ((TableModel) jTable.getModel()).getInputFiles();
        model.setRawPoints(tempArray);
        
        model.update();
    }
    
    public Model getModel() {
        return model;
    }
    
    public void moveVisibilityPoint(double currentPoint, double toy) {
        model.movePoint(currentPoint, toy);
        model.update();
    }
    
    public void removeRmsPoint(double x) {
        model.removeRmsPoint(x);
        model.update();
    }
    
    public void setListener(ViewListener listener) {
        this.listener = listener;
    }
    
    @Override
    public void updateValuesFromModel() {
        fLambda.setText(model.getLambda() + "");
        fPeakValue.setText(model.getPeakValue() + "");
        fSigma.setText(model.getDisplayFactor() + "");
        fNoise.setText(model.getNoise() + "");
        fD.setText(model.getDiameter() + "");
        fHorizontalError.setText(model.getHorizontalError() + "");
        vCanvas.setDisplayFactor(model.getDisplayFactor());
    }
    
    @Override
    public void updateModelFromValues() {
        try {
            double tempNoise = Double.parseDouble(fNoise.getText());
            model.setNoise(tempNoise);
        } catch (NumberFormatException e) {}
        
        try {
            double tempDiameter = Double.parseDouble(fD.getText());
            model.setDiameter(tempDiameter);
        } catch (NumberFormatException e) {}
        
        try {
            double tempLambda = Double.parseDouble(fLambda.getText());
            model.setLambda(tempLambda);
        } catch (NumberFormatException e) {}
        
        try {
            double tempSigma = Double.parseDouble(fSigma.getText());
            model.setDisplayFactor(tempSigma);
        } catch (NumberFormatException e) {}
        
        try {
            double tempPeakValue = Double.parseDouble(fPeakValue.getText());
            model.setPeakValue(tempPeakValue);
        } catch (NumberFormatException e) {}
        
        try {
            double tempHorizontalError = Double.parseDouble(fHorizontalError.getText());
            model.setHorizontalError(tempHorizontalError);
        } catch (NumberFormatException e) {}
        
        model.update();
    }
}
