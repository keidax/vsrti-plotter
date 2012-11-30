/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fftchooser;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
*
* @author Karel Durkota
* @author Adam Pere
* @author Gabriel Holodak
*/
public class Form extends JFrame{
    public int i=0;
    public final String[] filenames = {"Plot_Beam.jar","Plot_Fringe_Pattern.jar","Plot_Visibilities.jar"};
    public final String[] buttonnames = {"Plot Beam","Plot Fringe Pattern", "Plot Visibilities"};
    public JButton[] b = new JButton[3];

    public Form(String str){
        super(str);
        getContentPane().setLayout(new FlowLayout());
        for(i=0;i<b.length;i++){
            System.out.println(i+":"+filenames[i]);
            b[i]=new JButton(buttonnames[i]);
            getContentPane().add(b[i]);
        }
        b[0].addActionListener(new ActionListener(){
            @Override
			public void actionPerformed(ActionEvent e) {
                try {
                    String[] cmd = new String[3];
                   //cmd[0]="cmd.exe";
                    //cmd[1]="/C";
                    cmd[0]="java";
                    cmd[1]="-jar";
                    cmd[2]=filenames[0];
                    Runtime rt = Runtime.getRuntime();
                    for(int i=0;i<cmd.length;i++)
                        System.out.print(cmd[i]+" ");
                    Process proc = rt.exec(cmd);
                    System.out.println("next?");
                    // any error message?
                    StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");
                    // any output?
                    StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");
                    // kick them off
                    errorGobbler.start();
                    outputGobbler.start();
                    // any error???
                    //int exitVal = proc.waitFor();
                } catch (IOException ex) {
                    Logger.getLogger(Form.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });

        b[1].addActionListener(new ActionListener(){
            @Override
			public void actionPerformed(ActionEvent e) {
                    try {
                    String[] cmd = new String[3];
                    cmd[0]="java";
                    cmd[1]="-jar";
                    cmd[2]=filenames[1];
                    Runtime rt = Runtime.getRuntime();
                    System.out.println("Execing " + cmd[0] + " " + cmd[1] + " " + cmd[2]);
                    Process proc = rt.exec(cmd);
                    // any error message?
                    StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");
                    // any output?
                    StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");
                    // kick them off
                    errorGobbler.start();
                    outputGobbler.start();
                    // any error???
                    //int exitVal = proc.waitFor();
                } catch (IOException ex) {
                    Logger.getLogger(Form.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });

        b[2].addActionListener(new ActionListener(){
            @Override
			public void actionPerformed(ActionEvent e) {
               try {
                    String[] cmd = new String[3];
                    cmd[0]="java";
                    cmd[1]="-jar";
                    cmd[2]=filenames[2];
                    Runtime rt = Runtime.getRuntime();
                    //System.out.println("Execing " + cmd[0] + " " + cmd[1] + " " + cmd[2]);
                    Process proc = rt.exec(cmd);
                    // any error message?
                    StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");
                    // any output?
                    StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");
                    // kick them off
                    errorGobbler.start();
                    outputGobbler.start();
                    // any error???
                   // int exitVal = proc.waitFor();
                } catch (IOException ex) {
                    Logger.getLogger(Form.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });

     

        this.setVisible(true);
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public static void main(String[] args) {
        new Form("VRSTI Plotter");
    }


    class StreamGobbler extends Thread
{
    InputStream is;
    String type;

    StreamGobbler(InputStream is, String type)
    {
        this.is = is;
        this.type = type;
    }

        @Override
    public void run()
    {
            try {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                while ((line = br.readLine()) != null) {
                    //System.out.println(type + ">" + line);
                }
                //System.out.println(type + ">" + line);
            } catch (IOException ex) {
                Logger.getLogger(Form.class.getName()).log(Level.SEVERE, null, ex);
            }
    
}

}
}


