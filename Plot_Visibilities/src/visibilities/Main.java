package visibilities;

import visibilities.Model.Adapter;
import visibilities.Model.Model;
import visibilities.View.View;

public class Main {
    public static void main(String[] args) {
        Model m = new Model();
        Adapter a = new Adapter(m);
        View v = new View(a, "VSRTI Plotter - Plot Visibilities");
        v.go();
    }
    
}
