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
import javax.swing.JTextField;
import javax.swing.ToolTipManager;

import fft.Model.Adapter;
import fft.Model.ModelListener;

/**
 * 
 * @author Karel Durktoa and Adam Pere
 *
 */
public class Viewer extends JFrame implements ModelListener, ActionListener {

    private static final long serialVersionUID = 1L;
    public Adapter adapter;
    public VCanvas vGraph, vGraph2; // real/magnitude graph and imaginary/phase graph in time domain
    public FFTCanvas iGraph, iGraph2; // real/magnitude graph and imaginary/phase graph in frequencydomain
    public JButton exit;
    public JTextField fDelta, fLambda, fThetaMax, fSigma, fNumber, fThetaMin, fMinTime,fMaxTime;
    public JButton bSave, bOpen, bExit, bImage, bReset, bAbout, bFullReset, bInstruction, bEquation, bRadio;
    public static Viewer viewer;
    final JPopupMenu menu = new JPopupMenu();
    public String link = "Instructions_Fourier_Transform.html";
    public String equation;
    public String iEquation;
    public JLabel lEquation;
    public boolean radio;
    public File f;
    private  JFileChooser jfc;
    
    public Viewer(Adapter a, String title) {
        super(title);
        Viewer.viewer = this;
        setAdapter(a);
        jfc = new JFileChooser();
        vGraph = new VCanvas(this, this.getAdapter(), getAdapter().getVisiblityGraphPoints(), "Magnitude","f(t) - Magnitude", true);
        vGraph2 = new VCanvas(this, this.getAdapter(), getAdapter().getVisiblityGraphPoints2(), "Phase", "f(t) - Phase",  false);
        iGraph = new FFTCanvas(this, this.getAdapter(), getAdapter().getImageGraphPoints(),"Magnitude", "F(ν) - Magnitude", true);
        iGraph2 = new FFTCanvas(this, this.getAdapter(), getAdapter().getImageGraphPoints2(),"Phase", "F(ν) - Phase", false);
        radio = true;
        vGraph.setSize(300, 100);
        vGraph2.setSize(300,100);
        f = null;

        JPanel row1, row1col1, row2, row1col2, row1col2col2, jblank, labels, labels2, jDelta, jLambda, jExponent, jButtons, jButtons2, jButtons3, jButtons4, jButton5, jThetaMax, jLabels, jFields;
        JLabel lDelta, lMaxF, lBlank ;
        equation = "";
        iEquation = "";
        
        //BUTTONS
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
        lDelta = new JLabel("<HTML><P><b>f(t) Input Paremeters:</b></P><P></P><P>" + '\u0394' + "t: </P><P></P><P># of points:</P></HTML>");
        lMaxF = new JLabel("<HTML><P><b>Display Options:</b></P><P></P><P>max time:</P><P></P><P>max frequency:</P></HTML>");
        lBlank = new JLabel("<HTML><P>  </P> <P> </P><P>  </P> <P> </P><P>  </P> </HTML>");
        lBlank.setMaximumSize(new Dimension(110, 100));
        lMaxF.setMaximumSize(new Dimension(110, 160));
        lDelta.setMaximumSize(new Dimension(110, 100));
        
        lEquation = new JLabel("Equation: ");
        
        //Text Boxes
        fDelta = new JTextField(this.adapter.getDeltaBaseline() + ""); // Delta t
        fNumber = new JTextField(this.adapter.getNumberOfPoints()+""); //Number of Points
        fLambda = new JTextField(this.adapter.getLambda() + ""); //Lambda
        fThetaMax = new JTextField(adapter.getLambda()/this.adapter.getDeltaBaseline()/2+ ""); //Max Frequency to be displayed
        fThetaMin = new JTextField("0.00"); //Min Frequency to be displayed
        fMinTime = new JTextField("0.00"); //Min Frequency to be displayed
        fMaxTime = new JTextField(adapter.getDeltaBaseline()*(double)adapter.getNumberOfPoints() +""); //Max Time to be displayed
        fSigma = new JTextField(this.getVGraph().getSigma() + ""); 
        fDelta.setToolTipText("<HTML><P WIDTH='300px'>\u0394t =  x-axis step-size. (Numbers only, do not include "
                + "units)<BR/>");
        fThetaMax.setToolTipText("<HTML><P WIDTH='300px'>max frequency = largest frequency displayed in frequency domain.</P></HTML>");
        fThetaMin.setToolTipText("<HTML><P WIDTH='300px'>min frequency = smallest frequency displayed in frequency domain.</P></HTML>");
        fMaxTime.setToolTipText("<HTML><P WIDTH='300px'>max time = largest time displayed in time domain.</P></HTML>");
        fMinTime.setToolTipText("<HTML><P WIDTH='300px'>min time = smallest time displayed in time domain.</P></HTML>");
        fNumber.setToolTipText("<HTML><P WIDTH = '300px'>number of points graphed.</P></HTML>");
        //Yellow Pop up box when mouse hovers over text box
        
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
        
        //Setting size of text boxes
        fSigma.setMaximumSize(new Dimension(50, 20));
        fSigma.addActionListener(this);
        fDelta.setMaximumSize(new Dimension(50, 20));
        fDelta.addActionListener(this);
        fLambda.setMaximumSize(new Dimension(50, 20));
        fLambda.addActionListener(this);
        fThetaMax.setMaximumSize(new Dimension(50, 20));
        fThetaMax.setMinimumSize(new Dimension(50, 20));
        fThetaMax.addActionListener(this);
        fThetaMin.setMaximumSize(new Dimension(50, 20));
        fThetaMin.setMinimumSize(new Dimension(50, 20));
        fThetaMin.addActionListener(this);
        fMaxTime.setMaximumSize(new Dimension(50, 20));
        fMaxTime.setMinimumSize(new Dimension(50, 20));
        fMaxTime.addActionListener(this);
        fMinTime.setMaximumSize(new Dimension(50, 20));
        fMinTime.setMinimumSize(new Dimension(50, 20));
        fMinTime.addActionListener(this);
        fNumber.setMaximumSize(new Dimension(50, 20));
        fNumber.setMinimumSize(new Dimension(50, 20));
        fNumber.addActionListener(this);
        
        //Setting up the GUI layout
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
        labels2 = new JPanel();
        labels2.setLayout(new GridLayout());
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
        row1col1.add(lEquation);
        row1col1.add(vGraph);
        row1col1.add(vGraph2);
        row1col1.add(Box.createRigidArea(new Dimension(5, 5)));
       
       	row1col1.add(iGraph);
       	row1col1.add(iGraph2);
        row1col1.add(Box.createRigidArea(new Dimension(5, 5)));
        row1col2.setMaximumSize(new Dimension(100, 500));
        
        row1col2.add(row1col2col2);
        
        //row1col2col2.add(new JTable());
       
        labels.setLayout(new BoxLayout(labels, BoxLayout.X_AXIS));
        labels2.setLayout(new BoxLayout(labels2, BoxLayout.X_AXIS));

        jDelta = new JPanel();
        jLambda = new JPanel();
        jExponent = new JPanel();
        jButtons = new JPanel();
        jButtons2 = new JPanel();
        jButtons3 = new JPanel();
        jButtons4 = new JPanel();
        jButton5 = new JPanel();
        jThetaMax = new JPanel();
        jblank = new JPanel();
        jLabels = new JPanel();
        jFields = new JPanel();
        JPanel JBBlank = new JPanel();
        labels.setSize(130, 350);
        
        labels.add(jLabels);
        labels.add(jFields);
        labels.add(Box.createRigidArea(new Dimension(5, 5)));
  
        labels2.add(lMaxF);
        labels2.add(JBBlank);
        JBBlank.setLayout(new BoxLayout(JBBlank, BoxLayout.Y_AXIS));
        
        JBBlank.add(Box.createRigidArea(new Dimension(5, 43)));
        JBBlank.add(fMaxTime);
        JBBlank.add(Box.createRigidArea(new Dimension(5, 10)));
        JBBlank.add(fThetaMax);
        JBBlank.add(Box.createRigidArea(new Dimension(5, 10)));
        
       
        
        row1col2.add(labels);
        row1col2.add(labels2);
        row1col2.add(jButtons);
        row1col2.add(jButtons2);
        row1col2.add(jblank);
        
        row1col2.add(jButtons3);
        
        row1col2.add(jButtons4);
        row1col2.add(jButton5);
        
        //row1col2col2.add(jBlank);
  
        
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
        //jButtons2.add(bImage);
        jButtons2.add(bEquation);
        jblank.add(bRadio);
        jButtons3.add(bReset);
        jButtons3.add(bFullReset);
        jButtons4.add(bInstruction);
        jButton5.add(bAbout);
       
        jButton5.add(bExit);
     
        
        jFields.add(Box.createRigidArea(new Dimension(5,60)));
        jLabels.add(lDelta);
        jFields.add(fDelta);
        jFields.add(Box.createRigidArea(new Dimension(5,5)));
        jFields.add(fNumber);
        jFields.add(Box.createRigidArea(new Dimension(5, 30)));
    


        // BUTTONS FUNCTIONS
        bOpen.addActionListener(new ActionListener() {
        	
            @Override
            public void actionPerformed(ActionEvent arg0) {
                jfc.showOpenDialog(Viewer.viewer);
                f = jfc.getSelectedFile();
                
                if (f == null || !f.canRead()) {
                    return;
                }
                Double tm[][] = viewer.parseFile(f);
                adapter.fullReset();
                if (tm != null) {;
                    viewer.adapter.importVisibilityGraphPoints(tm); 
                }
                viewer.lEquation.setText("Equation: ");
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
        
        // Pressing reset will set all points to the state of the last opened file or the last entered equation. Opened files take precedence over equations.
        bReset.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
            
              if( f != null && f.canRead())
	               {
	            	   Double tm[][] = viewer.parseFile(f);
		               adapter.fullReset();
		               if (tm != null) {;
		                   viewer.adapter.importVisibilityGraphPoints(tm); 
		               }
		               viewer.lEquation.setText("Equation: ");
	               }
               else
            	   {
            	   		if(!equation.equals(""))
            	   			{
            	   				adapter.evaluate(equation, iEquation);
            	   				lEquation.setText("Equation: " + equation + " " + iEquation + "i");
            	   			}
            	   		else
            	   			adapter.fullReset();
            	   		
            	   }
               	
                        }
        });
        bFullReset.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                viewer.adapter.fullReset();
                viewer.lEquation.setText("Equation: ");
            }
        });
        
        bRadio.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if(radio)
                	{
                		viewer.bRadio.setText("Show Polar");
                		vGraph.setYAxis("");
                		vGraph2.setYAxis("");
                		
                		vGraph.setTitle("f(t) - Real");
                		vGraph2.setTitle("f(t) - Imaginary");
                		
                		iGraph.setYAxis("");
                		iGraph2.setYAxis("");
                		
                		iGraph.setTitle("F(ν) - Real");
                		iGraph2.setTitle("F(ν) - Imaginary");
                	}
                else
                	{
                		viewer.bRadio.setText("Show Rectangular");
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
        
                if(!equation.equals(""))
                {
                	eqn = new JTextField(equation);
                	eqn2 = new JTextField(iEquation);
                	
                }
                else
                {
                	eqn = new JTextField("Enter Real Part Equation Here");
                	eqn2 = new JTextField("Enter Imaginary Part of Equation Here");
                }
//                eqn.setMaximumSize(new Dimension(700,100));
                JLabel jl = new JLabel("<html>Acceptable Inputs:<br><br>"
                        + "Use 'x' as the independent variable, e as Euler's Number, and pi as pi.<br>"
                        +"use _ to represent negative numbers.<br>"
                        +"*, ^, /, +, -, (, and ) are valid operators.<br>"
                        +"sin(_), cos(_), tan(_), log(_), ln(_), delta(_) and u(_) (unit step) are also valid.<br>"
                        +"Angles are in radians.<br><br>"
                        + "</html>");
                jf.getContentPane().setLayout(new FlowLayout());
                jf.add(row1);
                row1.add(jl);
                row1.add(eqn);
                row1.add(eqn2);
                row1.add(enter);
                
                jf.setMinimumSize(new Dimension(400,275));
                jf.pack();
                jf.setLocationRelativeTo(null);
                jf.setVisible(true);
                
                enter.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                       equation = eqn.getText().toLowerCase().replace(" ", "");
                       iEquation = eqn2.getText().toLowerCase().replace(" ", "");
                        try
                        {
                        	adapter.evaluate(equation, iEquation);
                            viewer.lEquation.setText("Equation: " + equation + " " + iEquation + "i");
                            jf.setVisible(false);
                        	
                        }
                        catch(Exception e)
                        {
                        	JOptionPane.showMessageDialog(jf, "Equation Error. Try adding parentheses.");
                        	System.out.println(e.getCause());
                        }
                    }
                    
                }); 
                
                eqn.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                       equation = eqn.getText().toLowerCase().replace(" ", "");
                       iEquation = eqn2.getText().toLowerCase().replace(" ", "");
                        try
                        {
                        	adapter.evaluate(equation, iEquation);
                            viewer.lEquation.setText("Equation: " + equation + " " + iEquation + "i");
                            jf.setVisible(false);
                        	
                        }
                        catch(Exception e)
                        {
                        	JOptionPane.showMessageDialog(jf, "Equation Error. Try adding parentheses.");
                        }
                    }
                    
                }); 
                eqn2.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                       equation = eqn.getText().toLowerCase().replace(" ", "");
                       iEquation = eqn2.getText().toLowerCase().replace(" ", "");
                        try
                        {
                        	adapter.evaluate(equation, iEquation);
                            viewer.lEquation.setText("Equation: " + equation + " " + iEquation + "i");
                            jf.setVisible(false);
                        	
                        }
                        catch(Exception e)
                        {
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
                JLabel jl = new JLabel("<html>"
                        + "<p>TIFT - Tool for Interactive Fourier Transforms</p>"
                        + "<p><table>"
                        + "<tr><td>Authors:</td><td>Karel Durkota</td></tr>"
                        + "<tr><td></td><td>Jonathan Marr</td></tr>"
                        +"<tr><td></td><td>Adam Pere</td></tr>"
                        //+ "<tr><td>Funded by:</td><td>Valerie B Barr</td></tr>
                        + "</table></p>"
                        + "<p></p>"
                        + "<p>For more information, contact Valerie Barr, Prof. of Computer Science, barrv@union.edu or Jonathan Marr, Visiting Prof. of Astronomy, marrj@union.edu</p><p></p>"
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
       
        this.getVGraph().addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

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
                    viewer.adapter.setLambda(Double.parseDouble(strLine.split(" ")[1]));
                    viewer.fLambda.setText(strLine.split(" ")[1]);
                } else if (strLine.trim().startsWith("*deltaBaseline")) {
                    viewer.adapter.setDeltaBaseline(Double.parseDouble(strLine.split(" ")[1]));
                    viewer.fDelta.setText(strLine.split(" ")[1]);
                } else if (strLine.trim().startsWith("*numberOfPoints")) {
                    viewer.adapter.setNumberOfPoints(Integer.parseInt(strLine.split(" ")[1]));
                    viewer.fNumber.setText(strLine.split(" ")[1]);
                } else if (strLine.trim().startsWith("X_Y_Real_Imaginary")) {
                    vis = true;
                }  else if (vis) {
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
                }  else  {
                    continue;
                }

            }
            in.close();
        } catch (Exception e) {//Catch exception if any
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
        fDelta.setText(this.adapter.getDeltaBaseline() + "");
        fLambda.setText(this.adapter.getLambda() + "");
        fNumber.setText(this.adapter.getNumberOfPoints()+"");
        //fMaxA.setText(this.adapter.getMaxAmp() + "");
        //fThetaMax.setText(adapter.getLambda()/this.adapter.getDeltaBaseline()+ "");
        fSigma.setText(this.getVGraph().getSigma() + "");
      
        repaint();
    }
    
    

    public void go() {
        this.getAdapter().getModel().getListeners().add(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(fDelta)) {
            try {
                Double.parseDouble(fDelta.getText());
                viewer.lEquation.setText("Equation: ");
                this.adapter.setDeltaBaseline(Double.parseDouble(fDelta.getText()));
                fMaxTime.setText(adapter.getDeltaBaseline()*(double)adapter.getNumberOfPoints() +"");
            } catch (NumberFormatException e1) {}
        } else if (e.getSource().equals(fLambda)) {
            try {
                Double.parseDouble(fLambda.getText());
                this.adapter.setLambda(Double.parseDouble(fLambda.getText()));
                fMaxTime.setText(adapter.getDeltaBaseline()*(double)adapter.getNumberOfPoints() +"");
            } catch (NumberFormatException e1) {}
        } else if (e.getSource().equals(fNumber)) {
            try {
            	viewer.lEquation.setText("Equation: ");
                Integer.parseInt(fNumber.getText().trim());
                int x = Integer.parseInt(fNumber.getText().trim());
                x = (int)((Math.log(x)/Math.log(2)) );
                this.adapter.setNumberOfPoints((int)Math.pow(2, x));
                fMaxTime.setText(adapter.getDeltaBaseline()*(double)adapter.getNumberOfPoints() +"");
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
        }else if (e.getSource().equals(fThetaMin)) {
            try {
                Double.parseDouble((fThetaMin.getText()));
                update();
            } catch (NumberFormatException e1) {}
        }else if (e.getSource().equals(fMaxTime)) {
            try {
                Double.parseDouble((fMaxTime.getText()));
                update();
            } catch (NumberFormatException e1) {}
        }else if (e.getSource().equals(fMinTime)) {
            try {
                Double.parseDouble((fMinTime.getText()));
                update();
            } catch (NumberFormatException e1) {}
        }
        
        
    }
}
