package it.unibo.almarima.tablut.application.heuristics;

import it.unibo.almarima.tablut.application.domain.BoardState;
import it.unibo.almarima.tablut.local.logging.TablutLogger;
import it.unibo.almarima.tablut.local.logging.TablutLogger.LogSpace;

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
		for (Parameter p : this.w.getEnabledParameters()) {
			score += this.computeParameterValue(p, state) * this.w.getWeight(p);
		}

		score = score / this.w.getSum();

		if (score == 0 || score == 1) {
			TablutLogger.log(LogSpace.SYSTEM, "Trovato score di " + score + " in " + this.toString());
		}
		return score;
	}

	public abstract WeightBag createWeightBag();

	public abstract void initVariables(BoardState state);

	public abstract double computeParameterValue(Parameter p, BoardState state);

	public WeightBag getWeightBag() {
		return this.w;
	}
	
}
