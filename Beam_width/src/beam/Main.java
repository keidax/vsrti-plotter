package beam;

import beam.Model.Controller;
import beam.Model.Model;
import beam.View.View;

public class Main {
    public static void main(String[] args) {
        Model m = new Model();
        View v = new View(m, "VSRTI Plotter - Plot Beam");
        Controller c = new Controller(m, v);
    }
}
