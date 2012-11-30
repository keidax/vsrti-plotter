package fft.Viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.SortedMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ImageFrame extends JFrame {

    private final double a, b;
    private final SortedMap<Double, Double> data;
    private JPanel p = new JPanel();

    public ImageFrame(String title, final double a, final double b, final SortedMap<Double, Double> sortedMap) {
        super(title);
        this.a = a;
        this.b = b;
        this.data = sortedMap;
        //this.getContentPane().add(p,BorderLayout.CENTER);
        this.getContentPane().add(new JPanel() {

            @Override
            public void paintComponent(Graphics g2) {
                Graphics2D g = (Graphics2D) g2;//p.getGraphics();//(Graphics2D)this.getContentPane().getGraphics();
                double width = this.getWidth() / sortedMap.lastKey();
                Double[] keys = sortedMap.keySet().toArray(new Double[0]);
                for (int i = 0; i < keys.length-1; i++) {
                    int c1 = (int) (a * sortedMap.get(keys[i]) + b);
                    int c2 = (int) (a * sortedMap.get(keys[i + 1]) + b);
                    System.out.println("Colros: " + c1 + "->" + c2);
                    g.setColor(Color.BLUE);
                    g.setPaint(new GradientPaint(
                            (int) (width * keys[i]),
                            0, new Color(c1, c1, c1),
                            (int) (width * keys[i + 1]),
                            0, new Color(c2, c2, c2)));
                    Rectangle r = new Rectangle((int) (width * keys[i]), 0, (int) (width * keys[i + 1]), this.getHeight());
                    g.fill(r);
                    g.setColor(new Color(0, 94, 0));

                }
                for (int i = 0; i < keys.length+1; i++) {
                    g.drawLine((int) (width * keys[i + 1]), this.getHeight() - 18, (int) (width * keys[i + 1]), this.getHeight() - 12);
                    g.drawString(Math.round(keys[i + 1] * 100) / 100.0 + "", (int) (width * keys[i + 1]), this.getHeight() - 1);
                }
                g.drawString(0.0 + "", 0, this.getHeight() - 1);
                g.drawLine(0, getHeight() - 15, this.getWidth(), getHeight() - 15);
            }
        }, BorderLayout.CENTER);
    }
//	public void paintComponent(Graphics g2){
//		g2.setColor(Color.BLACK);
//		g2.drawLine(0,0,100,100);
//		
//		/*
//		p.setSize(this.getWidth(), this.getHeight());
//		Graphics2D g = (Graphics2D)g2;//p.getGraphics();//(Graphics2D)this.getContentPane().getGraphics();
//		double width = this.getWidth()/data.lastKey();
//		Double[] keys = data.keySet().toArray(new Double[0]);
//		g.setColor(Color.BLACK);
//		g.drawLine(0,0,100,100);
//		for(int i=0;i<keys.length-1;i++){
//			int c1 = (int) (a*data.get(keys[i]) + b);
//			int c2 = (int) (a*data.get(keys[i+1]) + b);
//			g.setColor(Color.BLUE);
//			g.setPaint(new GradientPaint(
//					(int)(width*keys[i]),
//					0,new Color(c1,c1,c1),
//					(int)(width*keys[i+1]),
//					0,new Color(c2,c2,c2)));
//			Rectangle r = new Rectangle((int)(width*keys[i]),0,(int)(width*keys[i+1]),this.getHeight());
//			g.fill(r);
//		}*/
//	}
}
