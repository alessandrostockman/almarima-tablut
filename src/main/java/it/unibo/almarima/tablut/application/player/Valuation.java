package it.unibo.almarima.tablut.application.player;

public class Valuation {

	public double hVal;
	public int depthAttained;
	
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
}
