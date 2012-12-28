package fft.View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.SortedMap;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

public class ImageFrame extends JFrame implements MouseListener {
    
    private final double a, b;
    private final SortedMap<Double, Double> data;
    private JPanel p = new JPanel();
    final JPopupMenu menu = new JPopupMenu();
    
    public ImageFrame(String title, final double a, final double b,
            final SortedMap<Double, Double> sortedMap) {
        super(title);
        this.a = a;
        this.b = b;
        data = sortedMap;
        // p.addMouseListener(this);
        addMouseListener(this);
        // this.getContentPane().add(p,BorderLayout.CENTER);
        
        // Create and add a menu item
        JMenuItem item = new JMenuItem("Save as JPEG");
        item.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser
                        .setFileFilter(new javax.swing.filechooser.FileFilter() {
                            
                            @Override
                            public boolean accept(File f) {
                                if (f.isDirectory()) {
                                    return true;
                                }
                                String s = f.getName();
                                int i = s.lastIndexOf('.');
                                
                                if (i > 0 && i < s.length() - 1) {
                                    String extension =
                                            s.substring(i + 1).toLowerCase();
                                    if ("jpeg".equals(extension)
                                            || "jpg".equals(extension)) {
                                        return true;
                                    } else {
                                        return false;
                                    }
                                }
                                return false;
                            }
                            
                            // The description of this filter
                            
                            @Override
                            public String getDescription() {
                                return "Only JPEG";
                            }
                        });
                // fileChooser.addChoosableFileFilter(new jpgSaveFilter());
                if (fileChooser.showSaveDialog(p) == JFileChooser.APPROVE_OPTION) {
                    ObjectOutputStream out;
                    try {
                        
                        File f = fileChooser.getSelectedFile();
                        if (!(f.getName().trim().endsWith(".jpg") || f
                                .getName().trim().endsWith(".jpeg"))) {
                            f = new File(f.getAbsolutePath() + ".jpeg");
                        }
                        out = new ObjectOutputStream(new FileOutputStream(f));
                        BufferedImage image =
                                new BufferedImage(p.getWidth(), p.getHeight(),
                                        BufferedImage.TYPE_INT_RGB);
                        p.paint(image.createGraphics());
                        ImageIO.write(image, "jpeg", f);
                        out.close();
                    } catch (IOException ex) {
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "cannot save image",
                            "save error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        // item.addActionListener(actionListener);
        menu.add(item);
        
        p = new JPanel() {
            
            @Override
            public void paintComponent(Graphics g2) {
                Graphics2D g = (Graphics2D) g2;// p.getGraphics();//(Graphics2D)this.getContentPane().getGraphics();
                double width = getWidth() / sortedMap.lastKey();
                Double[] keys = sortedMap.keySet().toArray(new Double[0]);
                for (int i = 0; i < keys.length - 1; i++) {
                    int c1 = (int) Math.max(0, a * sortedMap.get(keys[i]) + b);
                    int c2 =
                            (int) Math.max(0, a * sortedMap.get(keys[i + 1])
                                    + b);
                    // System.out.println("Colros: " + c1 + "->" + c2);
                    g.setColor(Color.BLUE);
                    g.setPaint(new GradientPaint((int) (width * keys[i]), 0,
                            new Color(c1, c1, c1), (int) (width * keys[i + 1]),
                            0, new Color(c2, c2, c2)));
                    Rectangle r =
                            new Rectangle((int) (width * keys[i]), 0,
                                    (int) (width * keys[i + 1]), getHeight());
                    g.fill(r);
                    g.setColor(new Color(0, 94, 0));
                    
                }
                for (int i = 0; i < keys.length - 1; i++) {
                    g.drawLine((int) (width * keys[i + 1]), getHeight() - 18,
                            (int) (width * keys[i + 1]), getHeight() - 12);
                    g.drawString(Math.round(keys[i + 1] * 100) / 100.0 + "",
                            (int) (width * keys[i + 1]), getHeight() - 1);
                }
                g.drawString(0.0 + "", 0, getHeight() - 1);
                g.drawLine(0, getHeight() - 15, getWidth(), getHeight() - 15);
            }
        };
        getContentPane().add(p, BorderLayout.CENTER);
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        
        // System.out.println("CLICKED");
        if (e.getButton() == MouseEvent.BUTTON3) {
            menu.show(this, e.getX(), e.getY());
        }
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        // menu.show(this, e.getX(), e.getY());
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
    // public void paintComponent(Graphics g2){
    // g2.setColor(Color.BLACK);
    // g2.drawLine(0,0,100,100);
    //
    // /*
    // p.setSize(this.getWidth(), this.getHeight());
    // Graphics2D g =
    // (Graphics2D)g2;//p.getGraphics();//(Graphics2D)this.getContentPane().getGraphics();
    // double width = this.getWidth()/data.lastKey();
    // Double[] keys = data.keySet().toArray(new Double[0]);
    // g.setColor(Color.BLACK);
    // g.drawLine(0,0,100,100);
    // for(int i=0;i<keys.length-1;i++){
    // int c1 = (int) (a*data.get(keys[i]) + b);
    // int c2 = (int) (a*data.get(keys[i+1]) + b);
    // g.setColor(Color.BLUE);
    // g.setPaint(new GradientPaint(
    // (int)(width*keys[i]),
    // 0,new Color(c1,c1,c1),
    // (int)(width*keys[i+1]),
    // 0,new Color(c2,c2,c2)));
    // Rectangle r = new
    // Rectangle((int)(width*keys[i]),0,(int)(width*keys[i+1]),this.getHeight());
    // g.fill(r);
    // }*/
    // }
}
