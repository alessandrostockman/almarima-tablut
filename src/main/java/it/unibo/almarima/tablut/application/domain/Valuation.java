package it.unibo.almarima.tablut.application.domain;

/*this class is useful to model the value of the heuristic used by MinMax Algo , togheter with the depth at which it is found */
public class Valuation {

	private double hVal;
	private int depthAttained;
	
	public Valuation(double hVal, int depthAttained) {
		this.hVal = hVal;
		this.depthAttained = depthAttained;
	}
	
	public Valuation clone() {
		return new Valuation(hVal, depthAttained);
	}
	
	public String toString() {
		return ("(hVal: " + hVal + ", depthAttained: " + depthAttained + ")");
	}

	public double gethVal() {
		return hVal;
	}

	public int getDepthAttained() {
		return depthAttained;
	}

}
