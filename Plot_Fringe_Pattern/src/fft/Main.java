/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

package fft;

import fft.Model.Adapter;
import fft.Model.Model;
import fft.View.View;

/**
 * 
 * @author Administrator
 */
public class Main {
    
    public static String link = "";
    public static double lambda = -1;
    
    /**
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        Model m = new Model();
        Adapter a = new Adapter(m);
        View v = new View(a, "VSRTI Plotter - Fringe Pattern");
        link = "http://www1.union.edu/marrj/radioastro/Instructions_Plot_Fringe_Pattern.html";
        if (!link.equals("")) {
            v.link = link;
        }
        if (lambda != -1.0) {
            m.getVisibilityGraph().setLambda(lambda);
        }
        v.go();
    }
    
    /*
     * public static void readFromFile(){ try { FileInputStream fstream = new
     * FileInputStream("default.txt"); DataInputStream in = new
     * DataInputStream(fstream); BufferedReader br = new BufferedReader(new
     * InputStreamReader(in)); String strLine; boolean vis = false; boolean im =
     * false; while ((strLine = br.readLine()) != null) {
     * System.out.println(strLine); if (strLine.trim().startsWith("//")) {
     * continue; } if (strLine.trim().startsWith("fringe_link")) { link =
     * strLine.split(" ")[1]; } if (strLine.trim().startsWith("lambda")) {
     * lambda = Double.parseDouble(strLine.split(" ")[1]); } } in.close(); }
     * catch (Exception e) {//Catch exception if any
     * System.err.println("Error: " + e.getMessage()); } }
     */
}
