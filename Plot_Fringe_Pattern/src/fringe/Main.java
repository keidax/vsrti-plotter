package fringe;

import fringe.Model.Adapter;
import fringe.Model.Model;
import fringe.View.View;

public class Main {
    public static void main(String[] args) {
        Model m = new Model();
        Adapter a = new Adapter(m);
        View v = new View(a, m, "VSRTI Plotter - Fringe Pattern");
        v.go();
    }
}
