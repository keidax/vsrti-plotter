package common.View;

public class ViewUtilities {
    public final static String TIFT_ABOUT_TEXT = "<html>"
            + "<p>TIFT - Tool for Interactive Fourier Transforms</p>"
            + "<p><table>"
            + "<tr><td>Authors:</td><td>Karel Durkota</td></tr>"
            + "<tr><td></td><td>Jonathan Marr</td></tr>"
            + "<tr><td></td><td>Adam Pere</td></tr>"
            + "<tr><td></td><td>Gabriel Holodak</td></tr>"
            // + "<tr><td>Funded by:</td><td>Valerie
            // B Barr</td></tr>
            + "</table></p>"
            + "<p></p>"
            + "<p>For more information, contact Valerie Barr, Prof. of Computer Science, barrv@union.edu or Jonathan Marr, Lecturer of Physics and Astronomy, marrj@union.edu</p><p></p>"
            + "<p>This research has been supported in part by a grant from the National Science Foundation, IIS CPATH Award #0722203</p><p></p>"
            + "<p>Software is written in Java and it is free open source</p>" + "</html>";
    public final static String VSRTI_ABOUT_TEXT = "<html>"
            + "<p>VRSTI Plotter version 1.0</p>"
            + "<p><table>"
            + "<tr><td>Authors:</td><td>Karel Durkota</td></tr>"
            + "<tr><td></td><td>Jonathan Marr</td></tr>"
            + "<tr><td></td><td>Adam Pere</td></tr>"
            + "<tr><td></td><td>Gabriel Holodak</td></tr>"
            // + "<tr><td>Funded by:</td><td>B Valerie Barr</td></tr>
            + "</table></p>"
            + "<p></p>"
            + "<p>For more information, contact Valerie Barr, Prof. of Computer Science, barrv@union.edu or Jonathan Marr, Lecturer of Physics and Astronomy, marrj@union.edu</p><p></p>"
            + "<p>This package was designed to be used with MIT Haystack Observatory VSRT interferometer, which was developed with funding from National Science Foundation.</p><p></p>"
            + "<p>This research has been supported in part by a grant from the National Science Foundation, IIS CPATH Award #0722203</p><p></p>"
            + "<p>Software is written in Java and it is free open source</p>" + "</html>";
    
    public final static String SRT_ABOUT_TEXT = "<html>"
            + "<p>SRT Plotter version 1.0</p>"
            + "<p><table>"
            + "<tr><td>Authors:</td><td>Adam Pere</td></tr>"
            + "<tr><td></td><td>Jonathan Marr</td></tr>"
            + "<tr><td></td><td>Karel Durkota</td></tr>"
            + "<tr><td></td><td>Gabriel Holodak</td></tr>"
            + "</table></p>"
            + "<p></p>"
            + "<p>For more information, contact Valerie Barr, Prof. of Computer Science, barrv@union.edu or Jonathan Marr, Lecturer of Physics and Astronomy, marrj@union.edu</p><p></p>"
            + "<p>This package was designed to be used with MIT Haystack Observatory SRT interferometer, which was developed with funding from National Science Foundation.</p><p></p>"
            + "<p>This research has been supported in part by a grant from the National Science Foundation, IIS CPATH Award #0722203</p><p></p>"
            + "<p>Software is written in Java and it is free open source</p>" + "</html>";
    
    /**
     * 
     * @param x
     * @return the sum of the first twenty elements of the first-order Bessel
     *         function
     */
    public static double besselJ(double x) {
        double fLast = x / 2.0;
        double fSum = fLast;
        for (int k = 1; k <= 20; k++) {
            double fK = -fLast * (Math.pow(x / 2, 2) / (k * (k + 1)));
            fSum += fK;
            fLast = fK;
        }
        
        return fSum;
    }
    
}
