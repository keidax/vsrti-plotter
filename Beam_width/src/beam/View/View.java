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
import java.util.Observable;
import java.util.Observer;
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

import common.View.BaseView;
import common.View.FileDrop;
import common.View.InputFile;
import common.View.ViewUtilities;

/**
 * Main GUI class for Plot_Beam.
 * 
 */

@SuppressWarnings("serial")
public class View extends BaseView implements Observer {
    
    private Canvas canvas;
    private TableModel tableModel;
    private FileTable jTable;
    public JTextField fD, fLambda, fSigma, fNoise, fHorizontalError, fPeakValue;
    private JButton updateButton;
    private JButton bSave, bOpen, bExit, bReset, bInstruction, bAbout, bHide, bDelete;
    private String link = "http://www1.union.edu/marrj/radioastro/Instructions_Plot_Beam.html";
    private Model model;
    private JLabel lblPeakValue;
    
    public View(Model m, String title) {
        super(title);
        model = m;
        model.addObserver(this);
        
        tableModel = new TableModel();
        jTable = new FileTable(tableModel, this);
        
        canvas = new Canvas(this, new TreeMap<Double, Double>());
        
        canvas.setSize(300, 100);
        
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
        fSigma = new JTextField(model.getDisplayFactor() + "");
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
        jScroll.setMaximumSize(new Dimension(200, 400));
        jScroll.setMinimumSize(new Dimension(200, 100));
        
        getContentPane().add(row1);
        row1.add(row1col1);
        row1.add(row1col2);
        row1col1.setPreferredSize(new Dimension(600, 500));
        row1col1.setMinimumSize(new Dimension(300, 300));
        row1col1.add(canvas);
        row1col2.setMaximumSize(new Dimension(200, 650));
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
        
        bExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                dispose();
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
                canvas.setGraphTitle("Beam (" + ViewUtilities.getShortFileName(files[0]) + ")");
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
        
        // this.setSize(1200, 800);
        this.pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
    }
    
    public void addUpdateParametersActionListener(ActionListener listener) {
        updateButton.addActionListener(listener);
        for (JTextField jt : new JTextField[] {fD, fLambda, fSigma, fNoise, fHorizontalError, fPeakValue}) {
            jt.addActionListener(listener);
        }
    }
    
    public void addOpenButtonActionListener(ActionListener listener) {
        bOpen.addActionListener(listener);
    }
    
    public void addSaveButtonActionListener(ActionListener listener) {
        bSave.addActionListener(listener);
    }
    
    public void addResetButtonActionListener(ActionListener listener) {
        bReset.addActionListener(listener);
    }
    
    public void addDeleteButtonActionListener(ActionListener listener) {
        bDelete.addActionListener(listener);
    }
    
    public void addHideButtonActionListener(ActionListener listener) {
        bHide.addActionListener(listener);
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
                    br.close();
                    return null;
                }
            }
            in.close();
            br.close();
        } catch (Exception e) {// Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
        TreeMap<Double, Double>[] back = (TreeMap<Double, Double>[]) new TreeMap[3];
        back[0] = ret;
        back[1] = rms;
        back[2] = retim;
        return back;
    }
    
    // @SuppressWarnings("unchecked")
    // public TreeMap<Double, Double>[] flatten(TreeMap<Double, ArrayList<Double>> data,
    // TreeMap<Double, ArrayList<Double>> rms) {
    // TreeMap<Double, Double> retData = new TreeMap<Double, Double>();
    // TreeMap<Double, Double> retRms = new TreeMap<Double, Double>();
    // Set<Double> dataKeys = data.keySet();
    // // Set<Double> rmsKeys = rms.keySet();
    // for (Double key : dataKeys) {
    // // x has 1 value, rms 0
    // if (data.get(key).size() == 1 && (rms.containsKey(key) ? rms.get(key).size() : 0) == 0) {
    // retData.put(key, data.get(key).get(0));
    // }
    // // x has 1 value, rms has 1 value
    // if (data.get(key).size() == 1 && (rms.containsKey(key) ? rms.get(key).size() : 0) == 1) {
    // retData.put(key, data.get(key).get(0));
    // retRms.put(key, rms.get(key).get(0));
    // }
    // // x has more then 1, but rms.size != x.size
    // if (data.get(key).size() != (rms.containsKey(key) ? rms.get(key).size() : 0)) {
    // retData.put(key, data.get(key).get(0));
    // }
    // // x.size == rms.size
    // else {
    // retData.put(key, countAverageValue(data.get(key), rms.containsKey(key) ? rms.get(key) : null));
    // retRms.put(key, countAverageRms(rms.containsKey(key) ? rms.get(key) : null));
    // }
    // System.out.println("Flatten: " + key + ", " + retData.get(key) + ", " + retRms.get(key));
    // }
    // TreeMap<Double, Double>[] back = new TreeMap[2];
    // back[0] = retData;
    // back[1] = retRms;
    // return back;
    // }
    
    // public double countAverageValue(ArrayList<Double> data, ArrayList<Double> rms) {
    // double num = 0;
    // double denom = 0;
    // for (int i = 0; i < data.size(); i++) {
    // num += data.get(i) / rms.get(i);
    // denom += 1 / rms.get(i);
    // }
    // return num / denom;
    // }
    //
    // public double countAverageRms(ArrayList<Double> rms) {
    // double sum = 0;
    // for (double r : rms) {
    // sum += r * r;
    // }
    // return Math.sqrt(sum / (rms.size() * (rms.size() - 1)));
    // }
    
    public void sendAdapterFiles() {
        ArrayList<InputFile> tempArray = ((TableModel) jTable.getModel()).getInputFiles();
        model.setRawPoints(tempArray);
    }
    
    public Model getModel() {
        return model;
    }
    
    // public void moveVisibilityPoint(double currentPoint, double toy) {
    // model.movePoint(currentPoint, toy);
    // }
    
    public void removeRmsPoint(double x) {
        model.removeRmsPoint(x);
    }
    
    /*
     * (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     * Main update method for the View class
     */
    @Override
    public void update(Observable o, Object arg) {
        bHide.setText(model.getBeamPatternVisible() ? "Hide Beam Pattern" : "Show Beam Pattern");
        fLambda.setText(model.getLambda() + "");
        fPeakValue.setText(model.getPeakValue() + "");
        fSigma.setText(model.getDisplayFactor() + "");
        fNoise.setText(model.getNoise() + "");
        fD.setText(model.getDiameter() + "");
        fHorizontalError.setText(model.getHorizontalError() + "");
        canvas.repaint();
        repaint();
    }
    
    public FileTable getFileTable() {
        return jTable;
    }
    
    public Canvas getCanvas() {
        return canvas;
    }
}
