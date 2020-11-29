package it.unibo.almarima.tablut.application.heuristics;

import it.unibo.almarima.tablut.application.domain.BoardState;

public class InverseHeuristic extends Heuristic {

	private Heuristic other;

	public InverseHeuristic(Heuristic other) {
		super();
		this.other = other;
	}

	public double evaluate(BoardState state) {
		return 1 - this.other.evaluate(state);
	}

}
