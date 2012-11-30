package fft.Viewer;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ToolTipManager;

import fft.Model.Adapter;
import fft.Model.ModelListener;

public class Viewer extends JFrame implements ModelListener, ActionListener {

    private static final long serialVersionUID = 1L;
    public Adapter adapter;
    public VCanvas vGraph;
    public FFTCanvas iGraph;
    public JButton exit;
    //public Model model;
    public TableModel tableModel;
    public FileTable jTable;
    public JTextField fDelta, fLambda, fThetaMax, fSigma, fNumber;
    public JButton bSave, bOpen, bExit, bImage, bReset, bInstruction, bAbout, bDelete;
    public static Viewer viewer;
    final JPopupMenu menu = new JPopupMenu();
    public String link = "Instructions_Fourier_Transform.html";
    private  JFileChooser jfc;

    public Viewer(Adapter a, String title) {
        super(title);
        jfc = new JFileChooser();
        Viewer.viewer = this;
        //model = m;
        setAdapter(a);
        tableModel = new TableModel(this);
        jTable = new FileTable(tableModel);
        tableModel.supreme = jTable;
        vGraph = new VCanvas(this, this.getAdapter(), getAdapter().getVisiblityGraphPoints());
        iGraph = new FFTCanvas(this, this.getAdapter(), getAdapter().getImageGraphPoints());

        vGraph.setSize(300, 100);

        JPanel row1, row1col1, row2, row1col2, row1col2col2, labels, jDelta, jLambda, jExponent, jButtons, jButtons2, jBlank, jButtons3, jButtons4, jButtons5, jThetaMax, jSigma, jLabels, jFields;
        JLabel lDelta, lLambda, lExponent, lThetaMax, lSigma;
        //BUTTONS
        bOpen = new JButton("Open file");
        bSave = new JButton("Save file");
        bExit = new JButton("Exit");
        bImage = new JButton("Show grayscale image");
        bReset = new JButton("Reset");
        bDelete = new JButton("Delete Data");
        bInstruction = new JButton("Instructions");
        bAbout = new JButton("About");
        lDelta = new JLabel("<HTML><P>" + '\u0394' + "BaseLine: </P><P># of points:</P><P>" + '\u03BB' + ": </P><P>" + " </P><P>"+  '\u0398' + "<SUB><B>max</B></SUB>(radians): </P><P>" + "display factor of " + '\u03C3' + ": </P></HTML>");
        jTable.setToolTipText("<HTML><P WIDTH='100px'>Drag and Drop data files into this box. File names should contain baseline distance in single quotes. <B>Example:</B> file with baseline 23.9 can have this names: \"file_b'23.9'.rad\", \"jun3.12baseline'23.9'.rad\", etc.</P></HTML>");

        lDelta.setMaximumSize(new Dimension(120, 100));
        fDelta = new JTextField(this.adapter.getDeltaBaseline() + "");

        fNumber = new JTextField(this.adapter.getNumberOfPoints()+"");
        System.out.println("fNumber is "+fNumber.getText());
        fLambda = new JTextField(this.adapter.getLambda() + "");
        fThetaMax = new JTextField(adapter.getLambda()/this.adapter.getDeltaBaseline()+ "");
        fSigma = new JTextField(this.getVGraph().getSigma() + "");
        fDelta.setToolTipText("<HTML><P WIDTH='300px'>\u0394 Baseline = baseline distance between successive points. (Numbers only, do not include "
                + "units)<BR/>Step in "
                + " x = \u0394Baseline / \u03BB<BR/>baseline = distance between antennas "
                + "<P WIDTH='200px'>Example: if measured data are apart from each other 2 units, set \u0394Baseline = 2</P></HTML>");
        fLambda.setToolTipText("<HTML><P WIDTH='300px'>\u03BB is wavelength of radiation. "
                + "At the end, horizontal distances of points are calculated by formula \u0394Baseline / \u03BB.</P>"
                + "</P></HTML>");
        fNumber.setToolTipText("The number of points on screen.");
        fThetaMax.setToolTipText("<HTML><P WIDTH='300px'>\u0398 max = field of view which equals the X value of last point shown in the Image Graph.</P></HTML>");
        fSigma.setToolTipText("<HTML><P WIDTH='300px'>The displayed sizes of error bars = RMS * (display factor of \u03C3). "
                + "Error bars are not displayed where there is no RMS to calculate.</P></HTML>");
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

        fSigma.setMaximumSize(new Dimension(50, 250));
        //fSigma.setMinimumSize(new Dimension(50,20));
        fSigma.addActionListener(this);
        fDelta.setMaximumSize(new Dimension(50, 20));
        fDelta.addActionListener(this);
        fLambda.setMaximumSize(new Dimension(50, 20));
        fLambda.addActionListener(this);
        fThetaMax.setMaximumSize(new Dimension(50, 20));
        fThetaMax.addActionListener(this);
        fNumber.setMaximumSize(new Dimension(50, 20));
        fNumber.addActionListener(this);
        JScrollPane jScroll;
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        row1 = new JPanel();
        row1.setLayout(new BoxLayout(row1, BoxLayout.X_AXIS));
        row2 = new JPanel();
        row2.setLayout(new BoxLayout(row2, BoxLayout.X_AXIS));
        row1col1 = new JPanel();
        row1col1.setLayout(new BoxLayout(row1col1, BoxLayout.Y_AXIS));
        row1col2 = new JPanel();
        row1col2.setLayout(new BoxLayout(row1col2, BoxLayout.Y_AXIS));
        row1col2col2 = new JPanel();
        row1col2col2.setLayout(new BoxLayout(row1col2col2, BoxLayout.Y_AXIS));
        labels = new JPanel();
        labels.setLayout(new GridLayout());
        jScroll = new JScrollPane(jTable);
        jScroll.setMaximumSize(new Dimension(200, 80));

        getContentPane().add(row1);
        getContentPane().add(row2);
        row1.add(Box.createRigidArea(new Dimension(5, 5)));
        row1.add(row1col1);
//		row1.add(Box.createRigidArea(new Dimension(5,5)));
        row1.add(Box.createRigidArea(new Dimension(10, 10)));
        row1.add(row1col2);
        row1.add(Box.createRigidArea(new Dimension(5, 5)));
        row1col1.setPreferredSize(new Dimension(600, 500));
        row1col1.setMinimumSize(new Dimension(300, 300));
        row1col1.add(Box.createRigidArea(new Dimension(5, 5)));
        row1col1.add(vGraph);
        row1col1.add(Box.createRigidArea(new Dimension(5, 5)));
        row1col1.add(iGraph);
        row1col1.add(Box.createRigidArea(new Dimension(5, 5)));
        row1col2.setMaximumSize(new Dimension(100, 450));
        row1col2.add(jScroll);//fileTable.createVectors());
        row1col2.add(Box.createRigidArea(new Dimension(5, 20)));
        row1col2.add(row1col2col2);
        //row1col2col2.add(new JTable());
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
        jBlank = new JPanel();
      
        jThetaMax = new JPanel();
        jSigma = new JPanel();
        jLabels = new JPanel();
        jFields = new JPanel();
        labels.setSize(100, 200);
        labels.add(jLabels);
        labels.add(jFields);
        row1col2col2.add(jButtons);
        row1col2col2.add(jButtons3);
        row1col2col2.add(jButtons2);
        
        row1col2col2.add(jBlank);
        row1col2col2.add(jButtons4);
        row1.add(jButtons5);
        jDelta.setLayout(new BoxLayout(jDelta, BoxLayout.X_AXIS));
        jLambda.setLayout(new BoxLayout(jLambda, BoxLayout.X_AXIS));
        jExponent.setLayout(new BoxLayout(jExponent, BoxLayout.X_AXIS));
        jThetaMax.setLayout(new BoxLayout(jThetaMax, BoxLayout.X_AXIS));
        jButtons.setLayout(new BoxLayout(jButtons, BoxLayout.X_AXIS));
        jButtons2.setLayout(new BoxLayout(jButtons2, BoxLayout.X_AXIS));
        jLabels.setLayout(new GridLayout(5, 2, 1, 1));
        jFields.setLayout(new GridLayout(5, 2, 1, 1));
        
        jButtons3.add(bImage);
        jButtons.add(bOpen);
        jButtons.add(bSave);
        jButtons2.add(bReset);
        jButtons2.add(bDelete);
        jButtons4.add(bInstruction);
        jButtons5.add(bAbout);
        jButtons5.add(bExit);

      

        jLabels.add(new JLabel('\u0394' + "BaseLine:"));
        jLabels.add(new JLabel("# of points:"));
        jLabels.add(new JLabel('\u03BB' +""));
        jLabels.add(new JLabel("<HTML>"+'\u0398' + "<SUB><B>max</B></SUB>(radians):</HTML>"));
        jLabels.add(new JLabel("display factor of " + '\u03C3' ));
        
        jFields.add(fDelta);
        jFields.add(fNumber);
        jFields.add(fLambda);
        jFields.add(fThetaMax);
        jFields.add(fSigma);


        // BUTTONS FUNCTIONS
        bOpen.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                jfc.showOpenDialog(Viewer.viewer);
                File f = jfc.getSelectedFile();
                if (f == null || !f.canRead()) {
                    return;
                }
                TreeMap<Double, Double>[] tm = viewer.parseFile(f);
                if (tm != null) {
                	
                    viewer.adapter.importVisibilityGraphPoints(tm[0]);
                    viewer.adapter.importVisibilityGraphRms(tm[1]);//viewer.parseFile(f));
                    viewer.adapter.importImageGraphPoints(tm[2]);
                }
            }
        });

        bSave.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                jfc.showSaveDialog(Viewer.viewer);
                File f = jfc.getSelectedFile();
                if (f == null) {
                    return;
                }
                writeIntoFile(f, viewer.adapter.exportVisibilityGraphPoints());
            }

            private void writeIntoFile(File f,
                    String exportVisibilityGraphPoints) {

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
                System.exit(0);
            }
        });

        bImage.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                double a = 255 / (Viewer.viewer.getIGraph().getMaxY());
                double b = 0;
                ImageFrame frame;
                if (Viewer.viewer.getIGraph().getPoints().ceilingKey(Double.parseDouble(fThetaMax.getText())) == null) {
                    frame = new ImageFrame("Grayscale image", a, b, Viewer.viewer.getIGraph().getPoints().subMap(0.0, true, Viewer.viewer.getIGraph().getPoints().floorKey(Double.parseDouble(fThetaMax.getText())), true));
                } else {
                    frame = new ImageFrame("Grayscale image", a, b, Viewer.viewer.getIGraph().getPoints().subMap(0.0, true, Viewer.viewer.getIGraph().getPoints().ceilingKey(Double.parseDouble(fThetaMax.getText())), true));
                }
                //frame.displayData();
                frame.setVisible(true);
                //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(Viewer.viewer.getIGraph().getWidth(), Viewer.viewer.getIGraph().getHeight());
                //frame.pack();
            }
        });

        bReset.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                viewer.adapter.reset();
            }
        });
        
        bDelete.addActionListener(new ActionListener() {
        	////FIX ME TO DELETE THE DATA *********____________**************________________*************
            @Override
            public void actionPerformed(ActionEvent arg0) {
            	tableModel.removeAllInputFiles();
            	viewer.adapter.fullReset();
                
            }
        });

        bAbout.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFrame jf = new JFrame("About VRSTI Plotter");
                JLabel jl = new JLabel("<html>"
                        + "<p>VRSTI Plotter version 1.0</p>"
                        + "<p><table>"
                        + "<tr><td>Authors:</td><td>Karel Durkota</td></tr>"
                        + "<tr><td></td><td>Jonathan Marr</td></tr>"
                        + "<tr><td></td><td>Adam Pere</td></tr>"
                        //+ "<tr><td>Funded by:</td><td>Valerie B Barr</td></tr>
                        + "</table></p>"
                        + "<p></p>"
                        + "<p>For more information, contact Valerie Barr, Prof. of Computer Science, barrv@union.edu or Jonathan Marr, Visiting Prof. of Astronomy, marrj@union.edu</p><p></p>"
                        + "<p>This package was designed to be used with MIT Haystack Observatory VSRT interferometer, which was developed with funding from National Science Foundation.</p><p></p>"
                        + "<p>This research has been supported in part by a grant from the National Science Foundation, IIS CPATH Award #0722203</p><p></p>"
                        + "<p>Software is written in Java and it is free open source</p>"
                        + "</html>");
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
                    Logger.getLogger(Viewer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (URISyntaxException ex) {
                        Logger.getLogger(Viewer.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
        });

        FileDrop fileDrop = new FileDrop(jTable, new FileDrop.Listener() {

            @Override
			public void filesDropped(java.io.File[] files) {
                for (int i = 0; i < files.length; i++) {
                    if (InputFile.isFormatCorrect(files[i])) {
                        tableModel.addInputFile(new InputFile(files[i]));
                    } else {
                        JOptionPane.showMessageDialog(viewer,
                                "Incorrect data file format. Try to open file instead of drag-and-drop.",
                                "Incorrett format",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                viewer.sendAdapterFiles();
            }
        });

        this.getVGraph().addMouseListener(new MouseListener() {

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
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void paintComponent() {
        fLambda.setText(this.adapter.getLambda() + "");
        fDelta.setText(this.adapter.getDeltaBaseline() + "");
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
                    viewer.adapter.setLambda(Double.parseDouble(strLine.split(" ")[1]));
                    viewer.fLambda.setText(strLine.split(" ")[1]);
                } else if (strLine.trim().startsWith("*deltaBaseline")) {
                    viewer.adapter.setDeltaBaseline(Double.parseDouble(strLine.split(" ")[1]));
                    viewer.fDelta.setText(strLine.split(" ")[1]);
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
        } catch (Exception e) {//Catch exception if any
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
        fDelta.setText(this.adapter.getDeltaBaseline() + "");
        fLambda.setText(this.adapter.getLambda() + "");
        fNumber.setText(this.adapter.getNumberOfPoints()+"");
        //fThetaMax = new JTextField(adapter.getLambda()/this.adapter.getDeltaBaseline()+ "");
        fSigma.setText(this.getVGraph().getSigma() + "");

        repaint();
    }

    public void go() {
        this.getAdapter().getModel().getListeners().add(this);
    }

    public void sendAdapterFiles() {
        this.adapter.setRawPoints(((TableModel) this.jTable.getModel()).inputFiles);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(fDelta)) {
            try {
                Double.parseDouble(fDelta.getText());
                this.adapter.setDeltaBaseline(Double.parseDouble(fDelta.getText()));
            } catch (NumberFormatException e1) {}
        } else if (e.getSource().equals(fLambda)) {
            try {
                Double.parseDouble(fLambda.getText());
                this.adapter.setLambda(Double.parseDouble(fLambda.getText()));
            } catch (NumberFormatException e1) {}
        } else if (e.getSource().equals(fNumber)) {
            try {
                Integer.parseInt(fNumber.getText().trim());
                this.adapter.setNumberOfPoints(Integer.parseInt(fNumber.getText().trim()));
            } catch (NumberFormatException e1) {}
        } else if (e.getSource().equals(fThetaMax)) {
            try {
                Double.parseDouble(fThetaMax.getText());
                update();
            } catch (NumberFormatException e1) {}
        } else if (e.getSource().equals(fSigma)) {
            try {
                Integer.parseInt((fSigma.getText()));
                getVGraph().setSigma(Integer.parseInt((fSigma.getText())));
                getVGraph().update();
                update();
            } catch (NumberFormatException e1) {}
        }
    }
}
