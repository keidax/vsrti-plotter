/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fft;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import fft.Model.Adapter;
import fft.Model.Model;
import fft.Viewer.Viewer;

public class Main {

    public static String link = "";
    public static double lambda = -1;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
            Model m = new Model();
            Viewer v = new Viewer("VSRTI Plotter - Plot Beam");
            Adapter a = new Adapter(m, v);
            m.addListener(v);
            v.addListener(a);
            link = "http://www1.union.edu/marrj/Ast240/Instructions_Plot_Beam.html";
            readFromFile();
            if(!link.equals(""))
                v.link=link;
            if(lambda!=-1.0)
                m.getVisibilityGraph().setLambda(lambda);
    }

    public static void readFromFile(){
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
                if (strLine.trim().startsWith("beam_link")) {
                    link = strLine.split(" ")[1];
                }
                if (strLine.trim().startsWith("lambda")) {
                    lambda = Double.parseDouble(strLine.split(" ")[1]);
                }
            }
            in.close();
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }
}
