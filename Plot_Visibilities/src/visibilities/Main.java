package visibilities;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import visibilities.Model.Adapter;
import visibilities.Model.Model;
import visibilities.View.View;

public class Main {
    public static double lambda = -1;
    
    public static void main(String[] args) {
        Model m = new Model();
        Adapter a = new Adapter(m);
        View v = new View(a, "VSRTI Plotter - Plot Visibilities");
        if (lambda != -1.0) {
            m.getVisibilityGraph().setLambda(lambda);
        }
        v.go();
    }
    
    public static void readFromFile() {
        try {
            FileInputStream fstream = new FileInputStream("default.txt");
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            // boolean vis = false;
            // boolean im = false;
            while ((strLine = br.readLine()) != null) {
                // System.out.println(strLine);
                if (strLine.trim().startsWith("//")) {
                    continue;
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
