package it.unibo.almarima.tablut.application.heuristics;

import java.util.HashMap;
import java.util.Map;

public class WeightBag {

	private boolean tuning;
	private Map<Parameter, Double> weights;
	private double sum;

	public WeightBag(boolean tuning) {
		this.tuning = tuning;
		this.weights = new HashMap<>();
	}

	private boolean retune = true;
	public boolean retune() {
		if (retune) {
			retune = false;
			return true;
		}
		return false;
	}

	public void reset() {
		//this.weights.clear();
		//this.sum = 0;
	}

	public void addWeight(Parameter p, double w) {
		this.weights.put(p, w);
		this.sum += w;
	}

	public double getWeight(Parameter p) {
		return this.weights.get(p);
	}

	public double getSum() {
		return this.sum;
	}
	
}
