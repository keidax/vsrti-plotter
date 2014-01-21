package tift;

import tift.Model.Adapter;
import tift.Model.Model;
import tift.View.View;


/**
 * @author Gabriel Holodak
 */
public class Main {
    public static String link = "http://www1.union.edu/marrj/radioastro/ToolforInteractiveFourierTransforms.html";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Model m = new Model();
        Adapter a = new Adapter(m);
        View v = new View(a, "Tool for Interactive Fourier Transform");
        v.link = link;
        v.go();
    }
}
