package it.unibo.almarima.tablut.application.heuristics.WeightedHeuristic;

import it.unibo.almarima.tablut.application.domain.BoardState;
import it.unibo.almarima.tablut.application.heuristics.Heuristic;
import it.unibo.almarima.tablut.application.domain.Coordinates;

public class KingEscaped extends Heuristic {

	private final int MAX_KING_ESCAPE_DISTANCE = 6; 

	public double evaluate(BoardState state) {
		if (state.getWinner() == 0 || state.getWinner() == 1) {
			return state.getWinner();
		}
		return (this.MAX_KING_ESCAPE_DISTANCE - Coordinates.distanceToClosestEscape(state.getKingPosition()))/this.MAX_KING_ESCAPE_DISTANCE;
	}
	
}
