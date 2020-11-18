package it.unibo.almarima.tablut.application.player;

import java.util.List;

import it.unibo.almarima.tablut.application.game.BoardState;

public class MeanCompositeHeuristic extends Heuristic {

	private List<Heuristic> heuristics;
	public MeanCompositeHeuristic(List<Heuristic> l) {
		this.heuristics = l;
	}

	public double evaluate(BoardState state) {
		float value = 0;
		for (Heuristic h : this.heuristics) {
			value += h.evaluate(state);
		}
		return value / this.heuristics.size();
	}
	
}
