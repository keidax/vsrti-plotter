package tiftchooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Displays a window with buttons to launch the TIFT subprograms.
 * 
 * @author Gabriel Holodak
 * 
 */
@SuppressWarnings("serial")
public class Form extends JFrame implements Runnable {
    
    /**
     * Main method for the Form class. Creates a new Form and displays it.
     * 
     * @param args
     *            no effect
     */
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Form("TIFT - Tool for Interactive Fourier Transform"));
    }
    
    /**
     * Creates a new Form class, which appears as a window with buttons to open
     * the TIFT subprograms.
     * 
     * @param str
     *            the title for the Form window
     */
    public Form(final String str) {
        super(str);
        getContentPane().setLayout(new FlowLayout());

        JButton fullButton = new JButton("Full Complex FT");
        JButton cosButton = new JButton("Cosine Transform");

        getContentPane().add(fullButton);
        getContentPane().add(cosButton);
        
        fullButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                tift.Main.main(null);
            }
        });
        
        cosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                tift2.Main.main(null);
            }
        });
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    @Override
    public final void run() {
        this.pack();
        this.setVisible(true);
    }
}
