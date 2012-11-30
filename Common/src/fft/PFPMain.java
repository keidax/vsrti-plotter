package fft;

public class PFPMain extends Main {

	static String title = "VSRTI Plotter - Fringe Pattern";
	
	public PFPMain() {
		link = "http://www1.union.edu/marrj/Ast240/Instructions_Plot_Fringe_Pattern.html";
        if(!link.equals(""))
            v.link=link;
        if(lambda!=-1.0)
            m.getVisibilityGraph().setLambda(lambda);
        v.go();
	}

}
