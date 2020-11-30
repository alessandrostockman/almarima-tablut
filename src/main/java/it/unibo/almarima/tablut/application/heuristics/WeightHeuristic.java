package it.unibo.almarima.tablut.application.heuristics;

import it.unibo.almarima.tablut.application.domain.BoardState;

public abstract class WeightHeuristic extends Heuristic {

	protected WeightBag w;

	public WeightHeuristic() {
		this.w = this.createWeightBag();
	}

	@Override
	public final double evaluate(BoardState state) {
		if (state.getWinner() == 0 || state.getWinner() == 1) {
			return state.getWinner();
		}

		this.initVariables(state);

		double score = 0;
		for (Parameter p : this.getEnabledParameters()) {
			score += this.computeParameterValue(p, state) * this.w.getWeight(p);
		}
		return score / this.w.getSum();
	}

	public abstract WeightBag createWeightBag();

	public abstract void initVariables(BoardState state);

	public abstract Parameter[] getEnabledParameters();

	public abstract double computeParameterValue(Parameter p, BoardState state);

	public WeightBag getWeightBag() {
		return this.w;
	}
	
}
