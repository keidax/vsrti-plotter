/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

package srt;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import srt.Model.Adapter;
import srt.Model.Model;
import srt.View.View;


/**
 * 
 * @author Administrator
 */
public class Main {
    
    public static String link = "http://www1.union.edu/marrj/radioastro/Instructions_SRT_Plotter.html";
    public static double lambda = -1;
    
    /**
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        Model m = new Model();
        Adapter a = new Adapter(m);
        View v = new View(a, "Small Radio Telescope (SRT) Plotter");
        link = "";
        readFromFile();
        /*
        if (!link.equals("")) {
            v.link = link;
        }*/
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
                if (strLine.trim().startsWith("//")) {
                    continue;
                }
                if (strLine.trim().startsWith("SRT_Plotter")) {
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
