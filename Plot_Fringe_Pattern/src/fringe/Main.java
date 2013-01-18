package fringe;

import fringe.Model.Adapter;
import fringe.Model.Model;
import fringe.View.View;

public class Main {
    public static double lambda = -1;
    
    public static void main(String[] args) {
        Model m = new Model();
        Adapter a = new Adapter(m);
        View v = new View(a, "VSRTI Plotter - Fringe Pattern");
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
