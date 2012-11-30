package fft.Model;
/**
 * This handles the Frequency Domain Graphs
 * @author Karel Durktoa and Adam Pere
 */
public class ImageGraph extends Graph {

    public Complex[] compl;
    public boolean polar;

    /**
     * Non-Default Constructor
     * @param m - model instance
     */
    public ImageGraph(Model m) {
        super(m);
        polar = true;
        
        
    }
    
    /**
     * Set the array of Complex Numbers to a new array of Complex Numbers.
     * @param numbers - new array of Complex Numbers
     */
    public void setComplex(Complex[] numbers){
    	compl = numbers;}
    
    /**
     * Set the instance variable polar to true or false. Polar determines whether or not the graph
     * is plotting magnitude/phase (polar form of complex numbers) or real and imaginary numbers (rectangular
     * form of complex numbers)
     * @param radio - Plot polar or rectangular form of complex numbers
     */
    public void setPolar(boolean radio){
    	polar = radio;
    	Object[] keys = this.getPoints().keySet().toArray();
    	
    	for(int i = 0; i< keys.length; i++)
    	{
    		if(polar)
	        {
	        	this.getPoints().put(Double.parseDouble(keys[i].toString()), compl[i].abs());
	        	this.getPoints2().put(Double.parseDouble(keys[i].toString()), compl[i].phase());
	        }
	        else
	        {
	        	
	        	this.getPoints().put(Double.parseDouble(keys[i].toString()), compl[i].re());
	        	this.getPoints2().put(Double.parseDouble(keys[i].toString()), compl[i].im());
	        }
    	}
    	model.updateListeners();
    	}
    
    /**
     * This handles the creating of the Visibility Graph (Time Domain Graphs). It does so by taking the ifft of the array of Complex Numbers
     * and setting the points in the Visibility Graph accordingly.
     */
    public void createVisibilityGraph() {
    	Complex[] res = FFT.fft(compl);
        model.visibilityGraph.setComplex(res);
        
        for (int i = 0; i < res.length; i++) {
            if(polar)
            	{
	            	model.visibilityGraph.getPoints().put(i * getDeltaBaseline() / model.getVisibilityGraph().getLambda(), res[i].abs());
	            	model.visibilityGraph.getPoints2().put(i * getDeltaBaseline() / model.getVisibilityGraph().getLambda(), res[i].phase());
            	}
            else
            	{   	
            		model.visibilityGraph.getPoints().put(i * getDeltaBaseline() / model.getVisibilityGraph().getLambda(), res[i].re());
                	model.visibilityGraph.getPoints2().put(i * getDeltaBaseline() / model.getVisibilityGraph().getLambda(), res[i].im());
            	}
        }
    }
    
    /**
     * Updates the model listeners
     */
    public void update() {
    	model.updateListeners();}
    
    /**
     * Changes the x-value of a point. This is called when a point is clicked or dragged on the graph. This handles
     * the real/magnitude graphs in the Frequency Domain.
     * @param currentPoint
     * @param toy - new x value.
     */
    public void movePoint(double currentPoint, double toy) {
    	int n = (int)(currentPoint*getDeltaBaseline()*getPoints().size()/model.getVisibilityGraph().getLambda());
     	if(polar)
        {
     	   if(toy != compl[n].abs())
     	   {
     		   	compl[n] =  new Complex(toy*Math.cos(compl[n].phase()),toy*Math.sin(compl[n].phase()));
     		   	this.getPoints2().put(currentPoint, compl[n].phase());
     		    this.getPoints().put(currentPoint, compl[n].abs());
     	   }
        }
     	else
     	{
     		if(toy != compl[n].re())
     		{
     			compl[n] =  new Complex(toy,compl[n].im());
     			this.getPoints2().put(currentPoint, compl[n].im());
 		       	this.getPoints().put(currentPoint, compl[n].re());
     		}
     	}
         this.createVisibilityGraph();
         this.update();
     }
    
    /**
     * Changes the x-value of a point. This is called when a point is clicked or dragged on the graph. This handles
     * the imaginary/phase graphs in the Frequency Domain.
     * @param currentPoint
     * @param toy - new x value.
     */
     public void movePoint2(double currentPoint, double toy) {
    	 int n = (int)(currentPoint*getDeltaBaseline()*getPoints().size()/model.getVisibilityGraph().getLambda());
      	if(polar)
         {
      	   if(toy != compl[n].phase())
      		   {
      		   		compl[n] =  new Complex(compl[n].abs()*Math.cos(toy),compl[n].abs()*Math.sin(toy));
 	     		   	this.getPoints2().put(currentPoint, compl[n].phase());
 			       	this.getPoints().put(currentPoint, compl[n].abs());
      		   }
      	   
         }
      		else
      		{
	      		if(toy != compl[n].re())
	       		   {
	      				compl[n] =  new Complex(compl[n].re(),toy);
	      				this.getPoints2().put(currentPoint, compl[n].im());
	     		       	this.getPoints().put(currentPoint, compl[n].re());
	       		   }
      		}
         this.createVisibilityGraph();
         this.update();
     }
}
