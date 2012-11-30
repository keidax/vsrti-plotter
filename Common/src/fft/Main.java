package fft;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import fft.Model.Adapter;
import fft.Model.Model;
import fft.Viewer.Viewer;

public abstract class Main {
	
	public static String link = "";
    public static double lambda = -1;
    static String title = "";

    public static void main(String[] args) {
            Model m = new Model();
            Adapter a = new Adapter(m);
            Viewer v = new Viewer(a, title);
    }
}
