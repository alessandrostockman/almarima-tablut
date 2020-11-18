package it.unibo.almarima.tablut.application.heuristics;

import it.unibo.almarima.tablut.application.domain.Coordinates;
import it.unibo.almarima.tablut.application.domain.BoardState;

public class KingHeuristic extends Heuristic {

	private final int MAX_KING_ESCAPE_DISTANCE = 6; 

	public double evaluate(BoardState state) {
		if (state.getWinner() == 0 || state.getWinner() == 1) {
			return state.getWinner();
		}
		return Coordinates.distanceToClosestEscape(state.getKingPosition()) / this.MAX_KING_ESCAPE_DISTANCE;
	}
	
}
