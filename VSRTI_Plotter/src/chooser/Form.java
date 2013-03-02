package chooser;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * 
 * @author Karel Durkota
 * @author Adam Pere
 * @author Gabriel Holodak
 */

@SuppressWarnings("serial")
public class Form extends JFrame implements Runnable {
    
    private JButton beamButton, fringeButton, visButton;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Form("VRSTI Plotter"));
    }
    
    public Form(String str) {
        super(str);
        
        getContentPane().setLayout(new FlowLayout());
        
        beamButton = new JButton("Plot Beam");
        fringeButton = new JButton("Plot Fringe Pattern");
        visButton = new JButton("Plot Visibilities");
        
        getContentPane().add(beamButton);
        getContentPane().add(fringeButton);
        getContentPane().add(visButton);
        
        beamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                beam.Main.main(null);
            }
        });
        
        fringeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fringe.Main.main(null);
            }
        });
        
        visButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                visibilities.Main.main(null);
            }
        });
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    @Override
    public void run() {
        this.pack();
        this.setVisible(true);
    }
}