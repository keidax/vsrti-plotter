package bw;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Main extends fft.Main {
	
	static String title="VSRTI Plotter - Plot Beam";
	
	public Main(String args[]){
        super();
        link = "http://www1.union.edu/marrj/Ast240/Instructions_Plot_Beam.html";
        readFromFile();
        if(!link.equals(""))
            v.link=link;
        if(lambda!=-1.0)
            m.getVisibilityGraph().setLambda(lambda);
        v.go();
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
