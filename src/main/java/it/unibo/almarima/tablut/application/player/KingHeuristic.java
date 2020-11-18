package it.unibo.almarima.tablut.application.player;

import it.unibo.almarima.tablut.application.coordinates.Coordinates;
import it.unibo.almarima.tablut.application.game.BoardState;

public class KingHeuristic extends Heuristic {

	private final int MAX_KING_ESCAPE_DISTANCE = 6; 

	public float evaluate(BoardState state) {
		return Coordinates.distanceToClosestEscape(state.getKingPosition()) / this.MAX_KING_ESCAPE_DISTANCE;
	}
	
}
