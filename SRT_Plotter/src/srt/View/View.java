package srt.View;

import srt.Model.Adapter;
import srt.Model.ModelListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class View extends JFrame implements ModelListener, ActionListener {
    
    private static final long serialVersionUID = 1L;
    public Adapter adapter;
    public VCanvas canvas;
    public MyTableModel tableModel;
    public FileTable jTable;
    public JButton bSave, bOpen, bExit, bReset, bDelete, bSelectEnd, bInstruction, bAbout, bAvgFreq, bAvgBlocks, bAvgTAs,
            bSubtract, bUndo, bBeam;
    public JComboBox cbPlot;
    public String link = "http://www1.union.edu/marrj/radioastro/Instructions_SRT_Plotter.html";
    public boolean canDelete = true;
    public DataBlockList dataBlockList;
    public DataBlock currentDataBlock;
    public boolean choosingDelete;
    public JLabel lDelta;
    public double[] deleting;
    public JScrollPane jScroll;
    public int[] selectedRows, taPlotted;
    public boolean gotB = false;
    public double min = 0.0;
    public JTextField fD;
    private JFileChooser jfc;
    
    public View(Adapter a, String title) {
        super(title);
        setAdapter(a);
        choosingDelete = false;
        dataBlockList = new DataBlockList();
        currentDataBlock = null;
        tableModel = new MyTableModel(this, dataBlockList);
        jTable = new FileTable(tableModel);
        canvas = new VCanvas(this, getAdapter(), getAdapter().getVisiblityGraphPoints());
        setMinimumSize(new Dimension(1300, 500));
        canvas.setSize(300, 100);
        jfc = new JFileChooser();
        JPanel row1, row1col1, row1col2, row1col2col2, labels, jDelta, jLambda, jExponent, jButtons, jButtons2, jButtons3, jButtons4, jButtons6, jButtons7, jButtons8, jBlank, jThetaMax, jLabels, jFields;
        
        // BUTTONS
        bOpen = new JButton("Open file");
        bSave = new JButton("Save File");
        bAvgFreq = new JButton("Average Over Frequency");
        bExit = new JButton("Exit");
        bReset = new JButton("Undo Delete End Channels");
        bDelete = new JButton("Delete Channels");
        bSelectEnd = new JButton("Delete End Channels");
        bInstruction = new JButton("Instructions");
        bAbout = new JButton("About");
        bAvgBlocks = new JButton("Average Blocks of Data");
        bAvgTAs = new JButton("Plot Average TAs vs. Block Number");
        bSubtract = new JButton("Subtract Background");
        bUndo = new JButton("Undo Delete");
        bBeam = new JButton("Plot Beam");
        lDelta = new JLabel("");
        jTable.setToolTipText("<HTML><P WIDTH='100px'>Select a data block to plot.</HTML>");
        cbPlot = new JComboBox(new String[]{"Plot Channels", "Plot Frequencies", "Plot Velocities"});
        
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
        jScroll.setMinimumSize(new Dimension(100, 80));
        jScroll.setVisible(true);
        getContentPane().add(row1);
        row1.add(row1col1);
        row1.add(row1col2);
        row1col1.setPreferredSize(new Dimension(800, 500));
        row1col1.setMinimumSize(new Dimension(700, 300));
        row1col1.add(canvas);
        row1col2.setMaximumSize(new Dimension(500, 500));
        
        jDelta = new JPanel();
        jLambda = new JPanel();
        jExponent = new JPanel();
        jButtons = new JPanel();
        jButtons2 = new JPanel();
        jButtons3 = new JPanel();
        jButtons4 = new JPanel();
        
        jButtons6 = new JPanel();
        jButtons7 = new JPanel();
        jButtons8 = new JPanel();
        jBlank = new JPanel();
        jThetaMax = new JPanel();
        jLabels = new JPanel();
        jFields = new JPanel();
        labels.setSize(100, 200);
        labels.add(jLabels);
        
        row1col2.add(jButtons8);
        row1col2.add(Box.createRigidArea(new Dimension(5, 10)));
        row1col2.add(jScroll);
        row1col2.add(row1col2col2);
        
        row1col2col2.add(labels);
        labels.setLayout(new BoxLayout(labels, BoxLayout.X_AXIS));
        
        row1col2col2.add(Box.createRigidArea(new Dimension(5, 15)));
        row1col2col2.add(jButtons);
        row1col2col2.add(jButtons2);
        row1col2col2.add(jButtons6);
        row1col2col2.add(jButtons3);
        
        row1col2col2.add(jBlank);
        row1col2col2.add(jButtons4);
        
        row1col2col2.add(jButtons7);
        
        jDelta.setLayout(new BoxLayout(jDelta, BoxLayout.X_AXIS));
        jLambda.setLayout(new BoxLayout(jLambda, BoxLayout.X_AXIS));
        jExponent.setLayout(new BoxLayout(jExponent, BoxLayout.X_AXIS));
        jThetaMax.setLayout(new BoxLayout(jThetaMax, BoxLayout.X_AXIS));
        jButtons.setLayout(new BoxLayout(jButtons, BoxLayout.X_AXIS));
        jButtons2.setLayout(new BoxLayout(jButtons2, BoxLayout.X_AXIS));
        jLabels.setLayout(new BoxLayout(jLabels, BoxLayout.Y_AXIS));
        jFields.setLayout(new BoxLayout(jFields, BoxLayout.Y_AXIS));
        
        jButtons3.add(bAvgFreq);
        jButtons3.add(bBeam);
        jButtons.add(bOpen);
        jButtons4.add(cbPlot);
        
        jButtons.add(bSave);
        
        jButtons6.add(bSelectEnd);
        jButtons6.add(bReset);
        jButtons2.add(bDelete);
        jButtons2.add(bUndo);
        jButtons7.add(bAvgBlocks);
        jButtons7.add(bSubtract);
        jButtons8.add(bInstruction);
        jButtons8.add(bAbout);
        
        jButtons8.add(bExit);
        jButtons4.add(bAvgTAs);
        jLabels.add(lDelta);
        
        // BUTTONS FUNCTIONS
        bOpen.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                jfc.showOpenDialog(View.this);
                File f = jfc.getSelectedFile();
                if (f == null || !f.canRead()) {
                    return;
                }
                choosingDelete = false;
                bDelete.setText("Delete Channels");
                View.this.lDelta.setText("");
                View.this.tableModel.removeAllInputFiles();
                View.this.parseFile(f);
                jTable.changeSelection(0, 0, false, false);
                jTable.updateGraph();
                jScroll.updateUI();
            }
        });
        
        bSubtract.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                canDelete = false;
                final int[] a = new int[2];
                final JFrame jf = new JFrame("SRT Plotter - Subtract Background");
                jf.setMinimumSize(new Dimension(600, 400));
                jf.setPreferredSize(new Dimension(600, 400));
                JButton enter = new JButton("Cancel");
                JButton select = new JButton("Select");
                final FileTable table = new FileTable(tableModel);
                JScrollPane jSC = new JScrollPane(table);
                jSC.setMaximumSize(new Dimension(800, 300));
                
                JPanel row1 = new JPanel();
                JPanel row2 = new JPanel();
                row1.setLayout(new BoxLayout(row1, BoxLayout.Y_AXIS));
                row1.add(Box.createRigidArea(new Dimension(10, 5)));
                row2.setLayout(new BoxLayout(row2, BoxLayout.X_AXIS));
                final JLabel jl = new JLabel("Subtracting A - B. Please select data block A.");
                jf.getContentPane().setLayout(new FlowLayout());
                jf.getContentPane().add(row1);
                row1.add(jl);
                row1.add(Box.createRigidArea(new Dimension(20, 5)));
                row1.add(jSC);
                row1.add(Box.createRigidArea(new Dimension(30, 5)));
                row1.add(row2);
                row2.add(enter);
                row2.add(select);
                row2.add(Box.createRigidArea(new Dimension(10, 5)));
                
                jf.pack();
                jf.setLocationRelativeTo(null);
                jf.setVisible(true);
                
                enter.addActionListener(new ActionListener() {
                    
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        jTable.updateGraph();
                        jf.setVisible(false);
                        canDelete = true;
                        
                    }
                    
                });
                
                select.addActionListener(new ActionListener() {
                    
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        if (!gotB) {
                            a[0] = table.getSelectedRows()[0];
                            jl.setText("Subtracting A - B. Please select data block B.");
                            gotB = true;
                        } else {
                            a[1] = table.getSelectedRows()[0];
                            DataBlock A = dataBlockList.get(a[0]);
                            DataBlock B = dataBlockList.get(a[1]);
                            
                            if (A.data.length != B.data.length || A.fStart != B.fStart || A.fStep != B.fStep) {
                                JOptionPane
                                        .showMessageDialog(null,
                                                "Both data blocks must have the same data length, start frequency, and step frequency");
                            } else {
                                double[] data = new double[A.data.length];
                                double[] deleted = new double[A.data.length];
                                double[] endChannels = new double[A.data.length];
                                
                                for (int i = 0; i < A.data.length; i++) {
                                    if (A.data[i] != -1 && B.data[i] != -1) {
                                        data[i] = A.data[i] - B.data[i];
                                    } else if (A.data[i] == -1 && B.data[i] != -1) {
                                        data[i] = B.data[i];
                                    } else if (A.data[i] != -1 && B.data[i] == -1) {
                                        data[i] = A.data[i];
                                    } else {
                                        data[i] = -1;
                                    }
                                    
                                    if (A.deleted[i] != 0 && B.deleted[i] != 0) {
                                        deleted[i] = A.deleted[i] - B.deleted[i];
                                    } else if (A.deleted[i] == 0 && B.deleted[i] != 0) {
                                        deleted[i] = B.deleted[i];
                                    } else if (A.deleted[i] != 0 && B.deleted[i] == 0) {
                                        deleted[i] = A.deleted[i];
                                    } else {
                                        deleted[i] = 0;
                                    }
                                    
                                    if (A.endChannels[i] != 0 && B.endChannels[i] != 0) {
                                        endChannels[i] = A.endChannels[i] - B.endChannels[i];
                                    } else if (A.endChannels[i] == 0 && B.endChannels[i] != 0) {
                                        endChannels[i] = B.endChannels[i];
                                    } else if (A.endChannels[i] != 0 && B.endChannels[i] == 0) {
                                        endChannels[i] = A.endChannels[i];
                                    } else {
                                        endChannels[i] = 0;
                                    }
                                }
                                dataBlockList.add(new DataBlock("SUB: " + a[0] + " - " + a[1], data, A.fStart, A.fStep,
                                        deleted, endChannels));
                                gotB = false;
                                jTable.clearSelection();
                                jTable.changeSelection(dataBlockList.size() - 1, 0, false, false);
                                jTable.updateGraph();
                                jf.setVisible(false);
                                canDelete = true;
                            }
                        }
                        
                    }
                });
            }
        });
        
        cbPlot.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                String plotType = (String)cb.getSelectedItem();

                if(plotType.equals("Plot Channels")) {
                    View.this.canvas.setPlotMode(VCanvas.PlotMode.PLOT_CHANNELS);
                } else if(plotType.equals("Plot Frequencies")){
                    View.this.canvas.setPlotMode(VCanvas.PlotMode.PLOT_FREQUENCIES);
                } else if(plotType.equals("Plot Velocities")) {
                    View.this.canvas.setPlotMode(VCanvas.PlotMode.PLOT_VELOCITIES);
                }
                View.this.lDelta.setText("");
                View.this.jTable.updateGraph();
            }
        });
        
        bAvgTAs.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                canDelete = false;
                final JFrame jf = new JFrame("SRT Plotter - Plot TA vs. Order");
                jf.setMinimumSize(new Dimension(600, 400));
                jf.setPreferredSize(new Dimension(600, 400));
                JButton enter = new JButton("Cancel");
                JButton select = new JButton("Select");
                final FileTable table = new FileTable(tableModel);
                JScrollPane jSC = new JScrollPane(table);
                jSC.setMaximumSize(new Dimension(800, 300));
                
                JPanel row1 = new JPanel();
                JPanel row2 = new JPanel();
                row1.setLayout(new BoxLayout(row1, BoxLayout.Y_AXIS));
                row1.add(Box.createRigidArea(new Dimension(10, 5)));
                row2.setLayout(new BoxLayout(row2, BoxLayout.X_AXIS));
                JLabel jl = new JLabel("Select datablocks to plot.");
                jf.getContentPane().setLayout(new FlowLayout());
                jf.getContentPane().add(row1);
                row1.add(jl);
                row1.add(Box.createRigidArea(new Dimension(20, 5)));
                row1.add(jSC);
                row1.add(Box.createRigidArea(new Dimension(30, 5)));
                row1.add(row2);
                row2.add(enter);
                row2.add(select);
                
                jf.pack();
                jf.setLocationRelativeTo(null);
                jf.setVisible(true);
                
                enter.addActionListener(new ActionListener() {
                    
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        jTable.updateGraph();
                        jf.setVisible(false);
                        canDelete = true;
                    }
                    
                });
                
                select.addActionListener(new ActionListener() {
                    
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        taPlotted = table.getSelectedRows();
                        
                        if (taPlotted.length == 0) {
                            JOptionPane.showMessageDialog(null, "You must select one or more data blocks..");
                            return;
                        }
                        View.this.canvas.setPlotMode(VCanvas.PlotMode.PLOT_AVERAGE_TA);
                        TreeMap<Double, Double> points = new TreeMap<Double, Double>();
                        View.this.canvas.getPoints().clear();
                        
                        for (int i = 0; i < taPlotted.length; i++) {
                            points.put(i + 0.0, View.this.dataBlockList.get(taPlotted[i]).getAverageOverFrequency());
                        }
                        View.this.adapter.importVisibilityGraphPoints(points);
                        
                        jTable.clearSelection();
                        jTable.changeSelection(taPlotted[0], 0, true, false);
                        jf.setVisible(false);
                        canDelete = true;
                    }
                    
                });
            }
            
        });
        
        bBeam.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                
                final JFrame jf = new JFrame("SRT Plotter - Plot Beam");
                jf.setMinimumSize(new Dimension(600, 400));
                jf.setPreferredSize(new Dimension(600, 400));
                JButton enter = new JButton("Cancel");
                JButton select = new JButton("Select");
                MyTableModel t = new MyTableModel(View.this, dataBlockList, 3);
                final FileTable table = new FileTable(t);
                JScrollPane jSC = new JScrollPane(table);
                jSC.setMaximumSize(new Dimension(800, 300));
                
                Object[] o = {"Cancel", "Elevation", "Azimuth"};
                int opt =
                        JOptionPane.showOptionDialog(null, "Would you like to plot vs. azimuth or vs. elevation?",
                                "Plot Beam", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, o, o[2]);
                String[] temp;
                int count = 0;
                if (opt == 0) {
                    return;
                } else if (opt == 1) {
                    jf.setTitle("SRT Plotter - Plot Beam: Elevation");
                    min = 0.0;
                   for(DataBlock node : dataBlockList) {
                        if (node.title.contains("eloff")) {
                            temp = node.title.replace("   ", " ").replace("  ", " ").split(" ");
                            for (int i = 0; i < temp.length; i++) {
                                if (temp[i].equals("eloff")) {
                                    table.changeSelection(count, 0, true, false);
                                    node.angle = Double.parseDouble(temp[i + 1]);
                                    if (node.angle < min) {
                                        min = node.angle;
                                    }
                                    break;
                                }
                            }
                        }
                        count++;
                    }
                } else {
                    jf.setTitle("SRT Plotter - Plot Beam: Azimuth");
                    min = 0.0;
                    for (DataBlock node : dataBlockList) {
                        if (node.title.contains("azoff")) {
                            temp = node.title.replace("   ", " ").replace("  ", " ").split(" ");
                            for (int i = 0; i < temp.length; i++) {
                                if (temp[i].equals("azoff")) {
                                    table.changeSelection(count, 0, true, false);
                                    node.angle = Double.parseDouble(temp[i + 1]);
                                    // convert to true angle in sku
                                    node.angle *= Math.cos(node.elevation / 360 * 2 * Math.PI);
                                    if (node.angle < min) {
                                        min = node.angle;
                                    }
                                    break;
                                }
                            }
                        }
                        count++;
                    }
                }
                canDelete = false;
                
                JPanel row1 = new JPanel();
                JPanel row2 = new JPanel();
                row1.setLayout(new BoxLayout(row1, BoxLayout.Y_AXIS));
                row1.add(Box.createRigidArea(new Dimension(10, 5)));
                row2.setLayout(new BoxLayout(row2, BoxLayout.X_AXIS));
                JLabel jl = new JLabel("Select datablocks to plot.");
                jf.getContentPane().setLayout(new FlowLayout());
                jf.getContentPane().add(row1);
                row1.add(jl);
                row1.add(Box.createRigidArea(new Dimension(20, 5)));
                row1.add(jSC);
                row1.add(Box.createRigidArea(new Dimension(30, 5)));
                row1.add(row2);
                row2.add(enter);
                row2.add(select);
                
                jf.pack();
                jf.setLocationRelativeTo(null);
                jf.setVisible(true);
                
                enter.addActionListener(new ActionListener() {
                    
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        jTable.updateGraph();
                        jf.setVisible(false);
                        canDelete = true;
                    }
                    
                });
                
                select.addActionListener(new ActionListener() {
                    
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        
                        if (table.plotBeam()) {
                            View.this.lDelta.setText("");
                            View.this.canvas.setPlotMode(VCanvas.PlotMode.PLOT_BEAM_WIDTH);
                            jf.setVisible(false);
                            canDelete = true;

                        }
                    }
                    
                });
            }
            
        });
        
        bSave.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                
                int opt = 1;
                File f;
                if (jTable.getSelectedRows().length != 0) {
                    opt =
                            JOptionPane.showOptionDialog(null,
                                    "Save only the selected data block? (Choose no to save all)",
                                    "SRT Plotter - Save File", JOptionPane.YES_NO_CANCEL_OPTION,
                                    JOptionPane.PLAIN_MESSAGE, null, null, null);
                }
                if (opt == 0)// yes
                {
                    View.this.lDelta.setText("");
                    jfc.showSaveDialog(View.this);
                    f = jfc.getSelectedFile();
                    if (f == null) {
                        return;
                    }
                    writeIntoFile(f, currentDataBlock.toString());
                } else if (opt == 1) // no
                {
                    View.this.lDelta.setText("");
                    jfc.showSaveDialog(View.this);
                    f = jfc.getSelectedFile();
                    if (f == null) {
                        return;
                    }
                    writeIntoFile(f, dataBlockList.toString());
                    
                }
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
        
        bAvgBlocks.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                canDelete = false;
                final JFrame jf = new JFrame("SRT Plotter - Average Blocks of Data");
                jf.setMinimumSize(new Dimension(600, 400));
                jf.setPreferredSize(new Dimension(600, 400));
                JButton enter = new JButton("Cancel");
                JButton select = new JButton("Select");
                final FileTable table = new FileTable(tableModel);
                JScrollPane jSC = new JScrollPane(table);
                jSC.setMaximumSize(new Dimension(800, 300));
                
                JPanel row1 = new JPanel();
                JPanel row2 = new JPanel();
                row1.setLayout(new BoxLayout(row1, BoxLayout.Y_AXIS));
                row1.add(Box.createRigidArea(new Dimension(10, 5)));
                row2.setLayout(new BoxLayout(row2, BoxLayout.X_AXIS));
                JLabel jl = new JLabel("Select datablocks to average together.");
                jf.getContentPane().setLayout(new FlowLayout());
                jf.getContentPane().add(row1);
                row1.add(jl);
                row1.add(Box.createRigidArea(new Dimension(20, 5)));
                row1.add(jSC);
                row1.add(Box.createRigidArea(new Dimension(30, 5)));
                row1.add(row2);
                row2.add(enter);
                row2.add(select);
                
                jf.pack();
                jf.setLocationRelativeTo(null);
                jf.setVisible(true);
                
                enter.addActionListener(new ActionListener() {
                    
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        jTable.updateGraph();
                        jf.setVisible(false);
                        canDelete = true;
                    }
                    
                });
                select.addActionListener(new ActionListener() {
                    
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        
                        int[] selected = table.getSelectedRows();
                        
                        if (selected.length > 0) {
                            DataBlock[] nodes = new DataBlock[selected.length];
                            String title = "AVG: ";
                            
                            for (int i = 0; i < nodes.length; i++) {
                                nodes[i] = jTable.getModel().getValueAt(selected[i]);
                                title = title + selected[i] + " & ";
                            }
                            
                            double data[] = new double[nodes[0].data.length];
                            
                            for (int i = 0; i < data.length; i++) {
                                data[i] = nodes[0].data[i];
                                for (int j = 1; j < nodes.length; j++) {
                                    if (nodes[j - 1].data.length != nodes[j].data.length
                                            || nodes[j - 1].fStart != nodes[j].fStart
                                            || nodes[j - 1].fStep != nodes[j].fStep) {
                                        JOptionPane
                                                .showMessageDialog(null,
                                                        "All blocks of data must have the same length, start frequency, and step for Average Blocks of Data.");
                                        return;
                                    }
                                    data[i] = data[i] + nodes[j].data[i];
                                }
                                data[i] = data[i] / nodes.length;
                            }
                            
                            title = title.substring(0, title.length() - 2);
                            dataBlockList.add(new DataBlock(title, data, nodes[0].fStart, nodes[0].fStep));
                            View.this.jTable.addNotify();
                            jTable.clearSelection();
                            jTable.changeSelection(jTable.getRowCount() - 1, 0, true, false);
                            jTable.updateGraph();
                            jf.setVisible(false);
                            canDelete = true;
                        } else {
                            JOptionPane.showMessageDialog(null, "You must select one or more data blocks.");
                        }
                        
                    }
                    
                });
            }
            
        });
        
        bAvgFreq.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                DecimalFormat df = new DecimalFormat("#.####");
                if (currentDataBlock == null) {
                    JOptionPane.showMessageDialog(null,
                            "You must select a data block before you can find its average over frequency.");
                } else {
                    View.this.lDelta.setText("Average Over Frequency: "
                            + df.format(currentDataBlock.getAverageOverFrequency()));
                }
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
                View.this.lDelta.setText("");
                choosingDelete = false;
                bSelectEnd.setText("Delete End Channels");
                if (View.this.canvas.getPlotMode() == VCanvas.PlotMode.PLOT_FREQUENCIES
                        || View.this.canvas.getPlotMode() == VCanvas.PlotMode.PLOT_AVERAGE_TA) {
                    return;
                }
                choosingDelete = false;
                DataBlock node;
                if (selectedRows != null) {
                    for (int selectedRow : selectedRows) {
                        node = dataBlockList.get(selectedRow);
                        for (int i = 0; i < node.data.length; i++) {
                            if (node.endChannels[i] != 0) {
                                node.data[i] = node.endChannels[i];
                                node.endChannels[i] = 0;
                            }

                        }
                    }
                }
                jTable.updateGraph();
            }
        });
        
        bDelete.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                View.this.lDelta.setText("");
                if (View.this.canvas.getPlotMode() == VCanvas.PlotMode.PLOT_FREQUENCIES
                        || View.this.canvas.getPlotMode() == VCanvas.PlotMode.PLOT_AVERAGE_TA) {
                    return;
                }
                if (currentDataBlock == null) {
                    JOptionPane.showMessageDialog(null,
                            "You must first select a data block before you can delete data points.");
                    return;
                }
                if (!choosingDelete) {
                    deleting = new double[currentDataBlock.data.length];
                    choosingDelete = true;
                    bDelete.setText("Delete Selected Channels");
                } else {
                    choosingDelete = false;
                    bDelete.setText("Delete Channels");
                    for (int i = 0; i < deleting.length; i++) {
                        if (deleting[i] == -1) {
                            currentDataBlock.deleted[i] = currentDataBlock.data[i];
                            currentDataBlock.data[i] = deleting[i];
                        }
                    }
                    jTable.updateGraph();
                }
            }
        });
        
        bUndo.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (currentDataBlock == null
                        || View.this.canvas.getPlotMode() == VCanvas.PlotMode.PLOT_FREQUENCIES
                        || View.this.canvas.getPlotMode() == VCanvas.PlotMode.PLOT_AVERAGE_TA) {
                    return;
                }
                for (int i = 0; i < currentDataBlock.data.length; i++) {
                    if (currentDataBlock.deleted[i] != 0) {
                        currentDataBlock.data[i] = currentDataBlock.deleted[i];
                        currentDataBlock.deleted[i] = 0;
                    }
                }
                jTable.updateGraph();
            }
        });
        
        bSelectEnd.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                
                if (!choosingDelete) {
                    canDelete = false;
                    final JFrame jf = new JFrame("SRT Plotter - Deleting End Channels");
                    jf.setMinimumSize(new Dimension(600, 400));
                    jf.setPreferredSize(new Dimension(600, 400));
                    JButton enter = new JButton("Cancel");
                    JButton select = new JButton("Select");
                    JButton sAll = new JButton("Select All");
                    final FileTable table = new FileTable(tableModel);
                    JScrollPane jSC = new JScrollPane(table);
                    jSC.setMaximumSize(new Dimension(800, 300));
                    
                    JPanel row1 = new JPanel();
                    JPanel row2 = new JPanel();
                    row1.setLayout(new BoxLayout(row1, BoxLayout.Y_AXIS));
                    row1.add(Box.createRigidArea(new Dimension(10, 5)));
                    row2.setLayout(new BoxLayout(row2, BoxLayout.X_AXIS));
                    JLabel jl = new JLabel("Select which data blocks to remove end channels from.");
                    jf.getContentPane().setLayout(new FlowLayout());
                    jf.getContentPane().add(row1);
                    row1.add(jl);
                    row1.add(Box.createRigidArea(new Dimension(20, 5)));
                    row1.add(jSC);
                    row1.add(Box.createRigidArea(new Dimension(30, 5)));
                    row1.add(row2);
                    row2.add(enter);
                    row2.add(select);
                    row2.add(sAll);
                    
                    jf.pack();
                    jf.setLocationRelativeTo(null);
                    jf.setVisible(true);
                    
                    enter.addActionListener(new ActionListener() {
                        
                        @Override
                        public void actionPerformed(ActionEvent arg0) {
                            jTable.updateGraph();
                            jf.setVisible(false);
                            canDelete = true;
                        }
                        
                    });
                    select.addActionListener(new ActionListener() {
                        
                        @Override
                        public void actionPerformed(ActionEvent arg0) {
                            selectedRows = table.getSelectedRows();
                            if (selectedRows.length > 0) {
                                for (int i = 1; i < selectedRows.length; i++) {
                                    if (dataBlockList.get(selectedRows[i - 1]).data.length != dataBlockList.get(selectedRows[i]).data.length
                                            || dataBlockList.get(selectedRows[i - 1]).fStart != dataBlockList
                                                    .get(selectedRows[i]).fStart
                                            || dataBlockList.get(selectedRows[i - 1]).fStep != dataBlockList.get(selectedRows[i]).fStep) {
                                        JOptionPane
                                                .showMessageDialog(null,
                                                        "All blocks of data must have the same length, start frequency, and step for Average Blocks of Data.");
                                        return;
                                    }
                                }
                                currentDataBlock = dataBlockList.get(selectedRows[0]);
                                deleting = new double[currentDataBlock.data.length];
                                jTable.clearSelection();
                                jTable.changeSelection(selectedRows[0], 0, true, false);
                                choosingDelete = true;
                                bSelectEnd.setText("Delete Selected End Channels");
                                jf.setVisible(false);
                                canDelete = true;
                                JOptionPane
                                        .showMessageDialog(null,
                                                "Select the end channels to be deleted and then click the 'delete selected channels' button.");
                            } else {
                                JOptionPane.showMessageDialog(null, "You must select one or more data blocks.");
                            }
                            
                        }
                        
                    });
                    
                    sAll.addActionListener(new ActionListener() {
                        
                        @Override
                        public void actionPerformed(ActionEvent arg0) {
                            table.selectAll();
                            selectedRows = table.getSelectedRows();
                            if (selectedRows.length > 0) {
                                for (int i = 1; i < selectedRows.length; i++) {
                                    if (dataBlockList.get(selectedRows[i - 1]).data.length != dataBlockList.get(selectedRows[i]).data.length
                                            || dataBlockList.get(selectedRows[i - 1]).fStart != dataBlockList
                                                    .get(selectedRows[i]).fStart
                                            || dataBlockList.get(selectedRows[i - 1]).fStep != dataBlockList.get(selectedRows[i]).fStep) {
                                        JOptionPane
                                                .showMessageDialog(null,
                                                        "All blocks of data must have the same length, start frequency, and step for Average Blocks of Data.");
                                        return;
                                    }
                                }
                                currentDataBlock = dataBlockList.get(selectedRows[0]);
                                deleting = new double[currentDataBlock.data.length];
                                jTable.clearSelection();
                                jTable.changeSelection(selectedRows[0], 0, true, false);
                                choosingDelete = true;
                                bSelectEnd.setText("Delete Selected End Channels");
                                jf.setVisible(false);
                                canDelete = true;
                                JOptionPane
                                        .showMessageDialog(null,
                                                "Select the end channels to be deleted and then click the 'delete selected channels' button.");
                            } else {
                                JOptionPane.showMessageDialog(null, "You must select one or more data blocks.");
                            }
                            
                        }
                        
                    });
                    
                } else {
                    choosingDelete = false;
                    bSelectEnd.setText("Delete End Channels");
                    DataBlock node;
                    if (selectedRows.length > 0) {
                        for (int selectedRow : selectedRows) {
                            node = dataBlockList.get(selectedRow);
                            for (int i = 0; i < deleting.length; i++) {
                                if (deleting[i] == -1) {
                                    node.endChannels[i] = node.data[i];
                                    node.data[i] = deleting[i];
                                }
                            }

                        }
                        jTable.updateGraph();
                    }
                    
                }
            }
            
        });
        
        bAbout.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFrame jf = new JFrame("About SRT Plotter");
                
                JLabel jl = new JLabel(common.View.ViewUtilities.SRT_ABOUT_TEXT);
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
        
        this.setSize(800, 600);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
    }
    
    /**
     * Reads the inputed file.
     * 
     * @param f
     */
    public void parseFile(File f) {
        try {
            String title = "";
            double[] data = new double[156];
            double[] deleted = new double[156];
            double[] endChannels = new double[156];
            double freqStart = 0, freqStep = 0;
            double azumith = 0, elevation = 0;
            int numChannels = 156;
            
            FileInputStream fstream = new FileInputStream(f);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            int numRows = 0;
            int currentBlock = -1;
            String[] temp;
            Boolean first = false;
            
            while ((strLine = br.readLine()) != null) {
                
                if (strLine.trim().startsWith("*") || strLine.trim().startsWith("AVG")
                        || strLine.trim().startsWith("SUB")) {
                    
                    currentBlock++;
                    if (currentBlock != 0 && !first) {
                        for (int i = 0; i < numChannels; i++) {
                            data[i] = data[i] / numRows;
                        }
                        
                        DataBlock n = new DataBlock(title, data, freqStart, freqStep, deleted, endChannels);
                        n.azumith = azumith;
                        n.elevation = elevation;
                        dataBlockList.add(n);
                        
                    }
                    
                    if (first) {
                        title = title + " " + strLine;
                    } else {
                        title = strLine;
                        first = true;
                        numRows = 0;
                    }
                    
                } else {
                    strLine = strLine.replace("   ", " ");
                    strLine = strLine.replace("  ", " ");
                    
                    temp = strLine.split(" ");
                    if (first) {
                        azumith = Double.parseDouble(temp[1]);
                        elevation = Double.parseDouble(temp[2]);
                        freqStart = Double.parseDouble(temp[5]);
                        freqStep = Double.parseDouble(temp[6]);
                        numChannels = Integer.parseInt(temp[8]);
                        data = new double[numChannels];
                        deleted = new double[numChannels];
                        endChannels = new double[numChannels];
                        first = false;
                        
                    }
                    
                    if (temp[0].equals("Deleted")) {
                        
                        for (int i = 2; i < temp.length; i++) {
                            deleted[i - 2] = Double.parseDouble(temp[i]);
                        }
                    } else if (temp[0].equals("End")) {
                        for (int i = 2; i < temp.length; i++) {
                            endChannels[i - 2] = Double.parseDouble(temp[i]);
                        }
                    } else {
                        for (int i = 9; i < temp.length; i++) {
                            data[i - 9] += Double.parseDouble(temp[i]);
                        }
                        
                        numRows++;
                    }
                }
            }
            in.close();
            
            for (int i = 0; i < numChannels; i++) {
                data[i] = data[i] / numRows;
                
            }
            dataBlockList.add(new DataBlock(title, data, freqStart, freqStep, deleted, endChannels));
            
        } catch (Exception e) {// Catch exception if any
            JOptionPane.showMessageDialog(null, "The file is not formatted correctly.\n" + f);
        }
    }
    
    /**
     * 
     * @return Adapter
     */
    public Adapter getAdapter() {
        return adapter;
    }
    
    /**
     * 
     * @param adapter
     */
    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
    }
    
    /**
     * 
     * @return VGraph
     */
    public VCanvas getCanvas() {
        return canvas;
    }
    
    /**
     *
     * @param graph
     */
    public void setVGraph(VCanvas graph) {
        canvas = graph;
    }
    
    /**
     * Updates the graph
     */
    @Override
    public void update() {
        
        repaint();
    }
    
    public void go() {
        getAdapter().getModel().getListeners().add(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
    }
    
    public double getD() {
        if (fD != null) {
            return Double.parseDouble(fD.getText());
        } else {
            return 2.0;
        }
    }

    /**
     * Return the View object that the given container is a child of. May return
     * null if the top-level parent is not an instance of View.
     * @param c a child component of a View object (may be many levels deep)
     * @return a reference to a View object, or null
     */
    public static View getView(Container c){
        while(c.getParent() != null){
            c = c.getParent();
        }
        if(c instanceof View){
            return (View)c;
        }
        return null;
    }
}
