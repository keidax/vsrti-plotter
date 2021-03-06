/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

package tift2;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import tift2.Model.Adapter;
import tift2.Model.Model;
import tift2.View.View;


/**
 * 
 * @author Administrator
 */
public class Main {
    
    /**
     * @param args
     *            the command line arguments
     */
    
    public static String link = "";
    public static double lambda = -1;
    
    public static void main(String[] args) {
        Model m = new Model();
        Adapter a = new Adapter(m);
        View v = new View(a, "Tool for Interactive Fourier Transform --- Cosine Transform");
        link = "http://www1.union.edu/marrj/radioastro/ToolforInteractiveFourierTransforms.html";
        if (!link.equals("")) {
            v.link = link;
        }
        if (lambda != -1.0) {
            m.visibilityGraph.setLambda(lambda);
        }
        v.go();
    }
    
    public static void readFromFile() {
        try {
            
            FileInputStream fstream = new FileInputStream("default.txt");
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            boolean vis = false;
            boolean im = false;
            while ((strLine = br.readLine()) != null) {
                System.out.println(strLine);
                if (strLine.trim().startsWith("//")) {
                    continue;
                }
                if (strLine.trim().startsWith("ft_link_cosine")) {
                    link = strLine.split(" ")[1];
                }
                if (strLine.trim().startsWith("lambda")) {
                    lambda = Double.parseDouble(strLine.split(" ")[1]);
                }
            }
            in.close();
        } catch (Exception e) {// Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }
}
