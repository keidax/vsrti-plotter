package fft;

public class PVMain extends Main{
	static String title = "VSRTI Plotter - Plot Visibilities";
	
	public PVMain(String args[]){
		super();
		link = "http://www1.union.edu/marrj/Ast240/Instructions_Plot_Visibilities.html";
        if(!link.equals(""))
            v.link=link;
        if(lambda!=-1.0)
            m.getVisibilityGraph().setLambda(lambda);
        v.go();
	}
}
