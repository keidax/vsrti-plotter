package chooser;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Displays a window with buttons to launch the VSRTI subprograms.
 * 
 * @author Karel Durkota
 * @author Adam Pere
 * @author Gabriel Holodak
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
        SwingUtilities.invokeLater(new Form("VRSTI Plotter"));
    }
    
    /**
     * Creates a new Form class, which appears as a window with buttons to open
     * the VSRTI subprograms.
     * 
     * @param str
     *            the title for the Form window
     */
    public Form(final String str) {
        super(str);
        
        getContentPane().setLayout(new FlowLayout());
        
        JButton beamButton = new JButton("Plot Beam");
//        JButton fringeButton = new JButton("Plot Fringe Pattern");
        JButton visButton = new JButton("Plot Visibilities");
        
        getContentPane().add(beamButton);
        // Hide for now
        //getContentPane().add(fringeButton);
        getContentPane().add(visButton);
        
        beamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                beam.Main.main(null);
            }
        });
        
        /*fringeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                fringe.Main.main(null);
            }
        });*/
        
        visButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                visibilities.Main.main(null);
            }
        });
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    @Override
    public final void run() {
        this.pack();
        this.setVisible(true);
    }
}
