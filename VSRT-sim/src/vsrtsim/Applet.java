/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vsrtsim;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JApplet;

/**
 *
 * @author Administrator
 */
public class Applet extends JApplet implements MouseListener, MouseMotionListener{
    public ArrayList<Source> sources = new ArrayList<Source>();
    public static int widthRes=128;
    public static int width,height;
    public int mCanx,mCany;
    public Source active=null;
    public boolean move=false;


    public Applet(){
        super();
        this.setSize(512,512);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        sources.add(new Source());
        repaint();
    }

    @Override
    public void paint(Graphics g2){
        Graphics2D g = (Graphics2D)g2;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Complex[] back = doFFT(256);
        g.setColor(Color.white);
        g.fillRect(0,0,this.getWidth(),this.getHeight());
        g.setColor(Color.black);

        g.drawString("add", getWidth()-25,10);

        for(Source s : getSources()){
            s.drawSource(g, this.getWidth());
        }
        for(int i=0;i<back.length;i++){
           g.drawLine((i-1)*getWidth()/back.length,getHeight()-(int)back[Math.abs((i-1)-back.length/2)].abs()-30,i*getWidth()/back.length,getHeight()-(int)back[Math.abs(i-back.length/2)].abs()-30);
           g.drawLine((i-1)*3*getWidth()/back.length,getHeight()-(int)back[Math.abs((i-1)-back.length/2)].phase()*3-10,i*getWidth()/back.length*3,getHeight()-(int)back[Math.abs(i-back.length/2)].phase()*3-10);

        }
        float dash1[] = {10.0f};
        g.setStroke(new BasicStroke(1.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,10.0f, dash1, 0.0f));
        g.drawLine(getWidth()/2,0,getWidth()/2,getHeight());
    }

    public Complex[] doFFT(int n){
        //get data
        Complex[] data = new Complex[n];
        for(Source s : getSources()){
            System.out.println("From "+(int)(n*((s.getX()-s.radius)/this.getWidth()/2))+" to "+(int)(n*((s.getX()+s.radius)/this.getWidth()/2)));
            for(int i=(int)(n*((s.getX()-s.radius)/this.getWidth()/2));i<(int)(n*((s.getX()+s.radius)/this.getWidth()/2));i++){
                if(data.length<=i || i<0)
                    continue;
                data[i*2]= new Complex(s.color/10,0);
                System.out.println("data["+((int)(n*(s.getX()/this.getWidth()))+i)+"]="+s.color);
            }            
        }

        for(int i=0;i<data.length;i++)
        {
        	if(data[i] == null)
        		data[i] = new Complex(0,0);
            System.out.print("**" + data[i]);   
        }
        System.out.println("\n");
        //make fft
        
        Complex[] back = FFT.fft(data);
        return back;
    }

    @Override
	public void mouseMoved(MouseEvent evt) {
        mCanx = evt.getX();
        mCany = evt.getY();
        Source s = getSource(mCanx, mCany);
        if (s != null) {
            if(s.isRimmed(mCanx, mCany))
                setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            else
                setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        } else {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    public Source getSource(int x, int y){
        for(Source s : getSources()){
            if(s.isInside(x, y))
                return s;
        }
        return null;
    }

    @Override
	public void mouseClicked(MouseEvent e) {
        if(e.getX()>getWidth()-25 && e.getY()<10){
            System.out.println("adding new source");
            sources.add(new Source());
        }
        repaint();
    }

    @Override
	public void mousePressed(MouseEvent e) {
        active = getSource(e.getX(),e.getY());
        move=true;
        if(active!=null && active.isRimmed(e.getX(),e.getY()))
                move=false;
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

    public ArrayList<Source> getSources() {
        return sources;
    }

    public void setSources(ArrayList<Source> sources) {
        this.sources = sources;
    }

    @Override
	public void mouseDragged(MouseEvent e) {
        mCanx=e.getX();
        mCany=e.getY();
        if(active!=null){
            if(move){
                active.setLocation(e.getX(), 255-e.getY());
            }
            else
                active.setRadius(mCanx,mCany);
            repaint();
        }
    }
}

class Source extends Point2D.Double {

    public double dx=100,dy=30;
    public static int maxColor=255;
    public int color=50;
    public double radius=5;

    public void drawSource(Graphics2D g, int width) {
        if(g==null)
            return;
        double tx=getX();//(int)(this.getX()*Canvas.widthRes/width);
        g.setColor(new Color(color,color,color));
        g.fillArc( (int) (tx - radius), (int) (dy - radius),(int)(2*radius),(int) (2*radius), 0,360);
        g.setColor(Color.black);
        g.drawArc( (int) (tx - radius), (int) (dy - radius),(int)(2*radius),(int) (2*radius), 0,360);
        g.fillArc( (int) (tx-2), (int) (dy-2), 4, 4, 0,360);
    }

    public Source(){
        this.setLocation(dx, dy);
    }

    @Override
    public void setLocation(double x, double y){
        this.x=x;
        color=(int)Math.max(Math.min(255,y),0);
    }

    public boolean isInside(int x, int y){
        //System.out.println("Distance "+Math.sqrt(Math.pow(this.getX()-x,2)+Math.pow(this.getY()-y,2)));
        if(Math.sqrt(Math.pow(this.getX()-x,2)+Math.pow(dy-y,2))<radius+1)
            return true;
        return false;
    }

    public boolean isRimmed(int x, int y){
        if(Math.abs(Math.sqrt(Math.pow(this.getX()-x,2)+Math.pow(dy-y,2))-radius)<1)
            return true;
        return false;
    }

    void setRadius(int mCanx, int mCany) {
        radius=Math.sqrt(Math.pow(this.getX()-mCanx,2)+Math.pow(dy-mCany,2));
    }
}
