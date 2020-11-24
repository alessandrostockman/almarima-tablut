package it.unibo.almarima.tablut.application.heuristics.WeightedHeuristic;

import it.unibo.almarima.tablut.application.domain.BoardState;
import it.unibo.almarima.tablut.application.heuristics.Heuristic;
import it.unibo.almarima.tablut.application.domain.Coordinates;

public class KingEscaped extends Heuristic {

	private final int MAX_KING_ESCAPE_DISTANCE = 6; 

	public double evaluate(BoardState state) {
		return (this.MAX_KING_ESCAPE_DISTANCE - Coordinates.distanceToClosestEscape(state.getKingPosition()))/this.MAX_KING_ESCAPE_DISTANCE;
	}
	
}
