package tiftchooser;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class Form extends JFrame implements Runnable {
    
    private JButton fullButton, cosButton;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Form("TIFT - Tool for Interactive Fourier Transform"));
    }
    
    public Form(String str) {
        super(str);
        getContentPane().setLayout(new FlowLayout());
        
        fullButton = new JButton("Full Complex TIFT");
        cosButton = new JButton("Cosine TIFT");
        
        getContentPane().add(fullButton);
        getContentPane().add(cosButton);
        
        fullButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tift.Main.main(null);
            }
        });
        
        cosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tift2.Main.main(null);
            }
        });
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    @Override
    public void run() {
        this.pack();
        this.setVisible(true);
    }
}
