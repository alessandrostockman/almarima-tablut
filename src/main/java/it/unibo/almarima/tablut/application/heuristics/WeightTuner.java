package it.unibo.almarima.tablut.application.heuristics;


public class WeightTuner {

	private double min;
	private double max;
	private double step;
	private double last;

	public WeightTuner(double min, double max, double step) {
		this.min = min;
		this.max = max;
		this.step = step;
		this.last = min;
	}

	public boolean isFinished() {
		return this.last + this.step > this.max;
	}

	public double tune() {
		if (!this.isFinished()) {
			this.last += this.step;
		}
		return this.last;
	}

	public String toString() {
		return "Tuner [" + this.min + ":" + this.max  + ":" + this.step + "], Value: " + this.last;
	}

}
