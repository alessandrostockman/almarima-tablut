package it.unibo.almarima.tablut.application.heuristics;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class WeightBag {

	private boolean tuning;
	private boolean first = true;
	private Map<Parameter, Double> originalWeights;
	private Map<Parameter, Double> weights;
	private Map<Parameter, WeightTuner> tuners;
	private double sum;

	public WeightBag(boolean tuning) {
		this.tuning = tuning;
		this.weights = new HashMap<>();

		if (this.tuning) {
			this.originalWeights = new HashMap<>();
			this.tuners = new HashMap<>();
		}
	}

	public boolean retune() {
		if (this.first) {
			this.first = false;
			return true;
		}

		if (this.tuning) {
			for (Parameter p : this.tuners.keySet()) {
				WeightTuner t = this.tuners.get(p);
				if (!t.isFinished()) {
					this.weights.put(p, t.tune());
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}

	public void reset() {
		if (tuning) {
			this.weights = new HashMap<>(this.originalWeights);
			this.sum = 0;
			for (double d : this.weights.values()) {
				this.sum += d;
			}
		}
	}

	public void addWeight(Parameter p, double w) {
		this.weights.put(p, w);
		this.sum += w;

		if (this.tuning) {
			this.originalWeights.put(p, w);
		}
	}

	public void addWeight(Parameter p, double w, WeightTuner t) {
		this.tuners.put(p, t);
		this.addWeight(p, w);
	}

	public double getWeight(Parameter p) {
		return this.weights.get(p);
	}

	public double getSum() {
		return this.sum;
	}

	public Parameter[] getEnabledParameters() {
		Parameter[] ps = this.weights.keySet().toArray(new Parameter[this.weights.size()]);
		return ps;
	}

	public String toString() {
		return this.weights.values().stream()
			.map(String::valueOf)
			.collect(Collectors.joining(":"));
	}
	
}
