//This controls the size of everything that you see, the buttons, the buttons' labels, normal labels, opening and saving files, etc.
package fft.Viewer;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import fft.Model.Adapter;
import fft.Model.ModelListener;

public class Viewer extends JFrame implements ModelListener, ActionListener {

    private static final long serialVersionUID = 1L;
    public Adapter adapter;
    public VCanvas vGraph;
    //public FFTCanvas iGraph;
    public JButton exit;
    //public Model model;
    public TableModel tableModel;
    public FileTable jTable;
    private JTextField fLambda, fThetaMax, fSigma, fT1, fT2, fTheta, fPhi1, fPhi2;
    private JComboBox fShape1, fShape2;
    public JButton bSave, bOpen, bExit, bReset, bDelete,bInstruction, bAbout, bModel;
    public static Viewer viewer;
    public String link = "Instructions_Plot_Visibilities.html";
    public boolean showVis = false;
    public double T1 = 10, T2 = 10, theta = 0;
    private double d1=0, d2=0;
    private  JFileChooser jfc;
    
    public Viewer(Adapter a, String title) {
        super(title);
        Viewer.viewer = this;
        jfc = new JFileChooser();
        //model = m;
        setAdapter(a);
        tableModel = new TableModel(this);
        jTable = new FileTable(tableModel);
        tableModel.supreme = jTable;
        vGraph = new VCanvas(this, this.getAdapter(), getAdapter().getVisiblityGraphPoints());
        //iGraph = new FFTCanvas(this,this.getAdapter(),getAdapter().getImageGraphPoints());

        vGraph.setSize(300, 100);

        JPanel row1, row1col1, row2, row1col2, row1col2col2, labels, jDelta, jLambda, jExponent, jButtons, jButtons2, jButtons3, jButtons4, jButtons5, jThetaMax, jLabels, jFields;
        JLabel lDelta, lLambda, lSigma;
        //BUTTONS
        bOpen = new JButton("Open file");
        bSave = new JButton("Save file");
        bModel = new JButton("Display Model Visibility");
        bExit = new JButton("Exit");
        bReset = new JButton("Reset");
        bDelete = new JButton("Delete Data");
        bInstruction = new JButton("Instructions");
        bAbout = new JButton("About");
        lDelta = new JLabel("<HTML><P>" + '\u03BB' + ": </P>" +  "<P>" + "display factor of " + '\u03C3' + ": </P>" + ""+"<P></P><P><b>Model Parameters:</b></P><P></P><P>\u03A6:</P>" + "<P>" + "T1" + ": </P>" + "<P>" + "T2" + ": </P>" + "<P>" + "Position of Lamp 2, θ" + ": </P>"+"</HTML>");
        jTable.setToolTipText("<HTML><P WIDTH='100px'>Drag and Drop data files into this box. File names should contain baseline distance in single quotes. <B>Example:</B> file with baseline 23.9 can have these names: \"file_b'23.9'.rad\", \"jun3.12baseline'23.9'.rad\", etc.</P></HTML>");
        
        lDelta.setMaximumSize(new Dimension(140, 220));
        lLambda = new JLabel('\u03BB' + ": ");
        lLambda.setSize(100, 22);
        lSigma = new JLabel('\u03C3' + " displaying factor: ");
        lSigma.setSize(100, 20);
        fPhi1 = new JTextField(d1+"");
        fPhi1.setToolTipText("<HTML><P>\u03A6</P></HTML>");
        fPhi1.setMaximumSize(new Dimension(50, 20));
        fPhi1.addActionListener(this);
        fPhi2 = new JTextField(d2+"");
        fPhi2.setToolTipText("<HTML><P>\u03A6</P></HTML>");
        fPhi2.setMaximumSize(new Dimension(50, 20));
        fPhi2.addActionListener(this);
        fLambda = new JTextField(this.adapter.getLambda() + "");
        fLambda.setToolTipText("<HTML><P WIDTH='300px'>\u03BB is wavelenght of radiation. "
                + "At the end, horizontal distances of points are calculated by formula \u0394Baseline / \u03BB.</P>"
                + "</P></HTML>");
        fSigma = new JTextField(this.getVGraph().getSigma() + "");
        fSigma.setMaximumSize(new Dimension(50, 20));
        fSigma.addActionListener(this);
        fSigma.setToolTipText("<HTML><P WIDTH='300px'>The displayed sizes of error bars = RMS * (display factor of \u03C3). "
                + "Error bars are not displayed where there is no RMS to calculate.</P></HTML>");
        fT1 = new JTextField(this.T1 + "");
        fT1.setMaximumSize(new Dimension(50, 20));
        fT1.addActionListener(this);
        fT1.setToolTipText("<HTML><P WIDTH='300px'>The Temperature of Lamp 1.</P></HTML>");
        
        fT2 = new JTextField(this.T2 + "");
        fT2.setMaximumSize(new Dimension(50, 20));
        fT2.addActionListener(this);
        fT2.setToolTipText("<HTML><P WIDTH='300px'>The Temperature of Lamp 2.</P></HTML>");
        
        fTheta = new JTextField(this.theta + "");
        fTheta.setMaximumSize(new Dimension(50, 20));
        fTheta.addActionListener(this);
        fTheta.setToolTipText("<HTML><P WIDTH='300px'>The position of Lamp 2. Lamp 1 is at position 0.</P></HTML>");

        fLambda.setMaximumSize(new Dimension(50, 20));
        fLambda.addActionListener(this);
        
        fShape1=new JComboBox(new String[]{"rectangular", "uniform disk"});
        fShape2=new JComboBox(new String[]{"rectangular", "uniform disk"});
        
        fShape1.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				update();
			}
        });
        
        fShape2.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				update();
			}
        });
        
        
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
        //row1col1.add(iGraph);
        row1col1.add(Box.createRigidArea(new Dimension(5, 5)));
        //row1col2.setMaximumSize(new Dimension(500, 500));
        row1col2.add(jScroll);//fileTable.createVectors());
        jScroll.setMinimumSize(new Dimension(100, 150));
        row1col2.add(Box.createRigidArea(new Dimension(5, 10)));
        row1col2.setMaximumSize(new Dimension(300, 600));
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
        jThetaMax = new JPanel();
        jLabels = new JPanel();
        jFields = new JPanel();
        labels.setSize(100, 200);
        labels.add(jLabels);
        labels.add(jFields);
        
        
        row1col2col2.add(jButtons);
        row1col2col2.add(jButtons3);
        row1col2col2.add(jButtons2);
        
        //row1col2col2.add(new JPanel());
        row1col2col2.add(jButtons4);
        //row1col2col2.add(new JPanel());
        
        row1col2col2.add(jButtons5);
        
        jDelta.setLayout(new BoxLayout(jDelta, BoxLayout.X_AXIS));
        jLambda.setLayout(new BoxLayout(jLambda, BoxLayout.X_AXIS));
        jExponent.setLayout(new BoxLayout(jExponent, BoxLayout.X_AXIS));
        jThetaMax.setLayout(new BoxLayout(jThetaMax, BoxLayout.X_AXIS));
        jButtons.setLayout(new BoxLayout(jButtons, BoxLayout.X_AXIS));
        jButtons2.setLayout(new BoxLayout(jButtons2, BoxLayout.X_AXIS));
        jLabels.setLayout(new GridLayout(10, 2, 1, 1));
        jFields.setLayout(new GridLayout(10, 2, 1, 1));
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


        // BUTTONS FUNCTIONS
        bOpen.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                jfc.showOpenDialog(Viewer.viewer);
                File f = jfc.getSelectedFile();
                if (f == null || !f.canRead()) {
                    return;
                }
                TreeMap<Double, Double>[] tm = parseFile(f);
                viewer.adapter.importVisibilityGraphPoints(tm[0]);
                viewer.adapter.importVisibilityGraphRms(tm[1]);
                update();
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
        
        bModel.addActionListener(new ActionListener() {

            @Override
            	public void actionPerformed(ActionEvent e) {
                    if(showVis){
                        bModel.setText("Display Model Visibility");
                    }
                else{
                        bModel.setText("Hide Model Visibility");
                    }
                    showVis=!showVis;
                    update();
                }
        });
     

        bExit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                System.exit(0);
            }
        });

        bReset.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
//                if(!tableModel.inputFiles.isEmpty())
//                	viewer.adapter.resetF();
//                else
                	viewer.adapter.reset();
            }
        });
        
        bDelete.addActionListener(new ActionListener() {

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


        @SuppressWarnings("unused")
		FileDrop fileDrop = new FileDrop(jTable, new FileDrop.Listener() {

            @Override
			public void filesDropped(java.io.File[] files) {
                for (int i = 0; i < files.length; i++) {
                   
                        tableModel.addInputFile(new InputFile(files[i]));
                
            }
                
        }});

        this.setSize(800, 600);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
            boolean vis=false;
            boolean im=false;
            while ((strLine = br.readLine()) != null) {
                if (strLine.trim().startsWith(";"))
                    continue;
                if (strLine.trim().startsWith("*lambda")) {
                    fLambda.setText(strLine.split(" ")[1]);
                    adapter.setLambda(Double.parseDouble(fLambda.getText()));
                    System.out.println("lambda from file:\t"+Double.parseDouble(fLambda.getText()));
                } else if (strLine.trim().startsWith("*deltaBaseline")) {
                    viewer.adapter.setDeltaBaseline(Double.parseDouble(strLine.split(" ")[1]));
                } else if (strLine.trim().startsWith("X_Y_RMS")){
                    im=false;
                    vis=true;
                } else if (strLine.trim().startsWith("ANGLE_INTENSITY")){
                    vis=false;
                    im=true;
                } else if (vis){
                    if(strLine.trim().equals(""))
                        continue;
                    try {
                        String[] s = strLine.split(" ");
                        ret.put(Double.parseDouble(s[0]), Double.parseDouble(s[1]));
                        if (s.length > 2 && !s[2].trim().equals("null")) {
                            rms.put(Double.parseDouble(s[0]), Double.parseDouble(s[2]));
                        }
                    } catch (NumberFormatException e) {
                    }
                }else if (im){
                    if(strLine.trim().equals(""))
                        continue;
                    try {
                        String[] s = strLine.split(" ");
                        retim.put(Double.parseDouble(s[0]), Double.parseDouble(s[1]));
                    } catch (NumberFormatException e) {
                    }
                } else if (strLine.trim().length()==0)
                        continue;
                else {
                    JOptionPane.showMessageDialog(viewer,
    "Incorrect file format. Try to drag-and-drop files into drag-and-drop table area.",
    "Incorrect format",
    JOptionPane.ERROR_MESSAGE);
                    return null;
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
        update();
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

    public JButton getExit() {
        return exit;
    }

    public void setExit(JButton exit) {
        this.exit = exit;
    }

    @Override
    public void update() {
        fLambda.setText(this.adapter.getLambda() + "");
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
        if (e.getSource().equals(fLambda)) {
            try {
                Double.parseDouble(fLambda.getText());
                this.adapter.setLambda(Double.parseDouble(fLambda.getText()));
            } catch (NumberFormatException e1) {
            }
        } else if (e.getSource().equals(fThetaMax)) {
            try {
                Double.parseDouble(fThetaMax.getText());
                update();
            } catch (NumberFormatException e1) {
            }

        } else if (e.getSource().equals(fPhi1)) {
            try {
                setD1(Double.parseDouble(fPhi1.getText()));
                update();
            } catch (NumberFormatException e1) {
            }
            
        } else if (e.getSource().equals(fPhi2)) {
            try {
                setD2(Double.parseDouble(fPhi2.getText()));
                
                update();
            } catch (NumberFormatException e1) {
            }

        }else if (e.getSource().equals(fSigma)) {
            try {

                Integer.parseInt((fSigma.getText()));
                getVGraph().setSigma(Integer.parseInt((fSigma.getText())));
                getVGraph().update();
                update();
            } catch (NumberFormatException e1) {
            }

        }
        
        else if (e.getSource().equals(fT1)) {
            try {

                Double.parseDouble((fT1.getText()));
                this.T1 = Double.parseDouble((fT1.getText()));
                getVGraph().update();
                update();
            } catch (NumberFormatException e1) {
            }

        }
        
        else if (e.getSource().equals(fT2)) {
            try {

                Double.parseDouble((fT2.getText()));
                this.T2 = Double.parseDouble((fT2.getText()));
                getVGraph().update();
                update();
            } catch (NumberFormatException e1) {
            }

        }
        
        else if (e.getSource().equals(fTheta)) {
            try {

                Double.parseDouble((fTheta.getText()));
                this.theta = Double.parseDouble((fTheta.getText()));
                getVGraph().update();
                update();
            } catch (NumberFormatException e1) {
            }

        }
    }
    
    public double getD1() {
    	setD1(Double.parseDouble(fPhi1.getText()));
        return d1;
    }

    public void setD1(double d) {
    	/*
        if(d<=0)
            this.d1 =  20;
        else
        */
            this.d1=d;
    }
    
    public double getD2() {
    	setD2(Double.parseDouble(fPhi2.getText()));
        return d2;
    }

    public void setD2(double d) {
    	/*
        if(d<=0)
            this.d2 =  20;
        else
        */
            this.d2=d;
    }
    public boolean isShape1Rect(){
    	if(((String) fShape1.getSelectedItem()).equalsIgnoreCase("rectangular")){
    		return true;
    	}
    	
    	return false;
    }
    public boolean isShape2Rect(){
    	if(((String) fShape2.getSelectedItem()).equalsIgnoreCase("rectangular")){
    		return true;
    	}
    	
    	return false;
    }
}
