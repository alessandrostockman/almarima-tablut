package it.unibo.almarima.tablut.application.heuristics;

import java.util.List;

public class WeightBag {

	private List<Double> weights;

	public WeightBag() {}

	private boolean retune = true;
	public boolean retune() {
		if (retune) {
			retune = false;
			return true;
		}
		return false;
	}

	public void reset() {
		
	}

	public List<Double> getWeights() {
		return this.weights;
	}

	public void setWeights(List<Double> weights) {
		this.weights = weights;
	}
	
}
