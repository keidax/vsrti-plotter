package fft;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import fft.Model.Controller;
import fft.Model.Model;
import fft.View.View;

public class Main {
	
    public static double lambda = -1;
    
    public static void main(String[] args) {
            Model m = new Model();
            View v = new View(m, "VSRTI Plotter - Plot Beam");
            Controller c = new Controller(m, v);
            m.addListener(v);
            v.setListener(c);
            
            //TODO is the read-from-file business really necessary?
            readFromFile();
            
            if(lambda!=-1.0)
                m.setLambda(lambda);
            
            
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
                /*if (strLine.trim().startsWith("beam_link")) {
                    link = strLine.split(" ")[1];
                }*/
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
