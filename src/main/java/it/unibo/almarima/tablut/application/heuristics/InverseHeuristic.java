package it.unibo.almarima.tablut.application.heuristics;

import it.unibo.almarima.tablut.application.domain.BoardState;

public class InverseHeuristic extends WeightHeuristic {

	private WeightHeuristic other;

	public InverseHeuristic(WeightHeuristic other) {
		this.other = other;
		this.w = other.getWeightBag();
	}

	@Override
	public WeightBag createWeightBag() {
		if (other == null) {
			return null;
		}
		return this.other.getWeightBag();
	}

	@Override
	public void initVariables(BoardState state) { 
		this.other.initVariables(state);
	}

	@Override
	public Parameter[] getEnabledParameters() {
		return this.other.getEnabledParameters();
	}

	@Override
	public double computeParameterValue(Parameter p, BoardState state) {
		return 1 - this.other.computeParameterValue(p, state);
	}

}
