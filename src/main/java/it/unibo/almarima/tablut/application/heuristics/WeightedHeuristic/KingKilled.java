package it.unibo.almarima.tablut.application.heuristics;

import it.unibo.almarima.tablut.application.domain.BoardState;

public class KingKilled extends Heuristic {

	public double evaluate(BoardState state) {
		//TODO: count additional pawns needed to kill the king depending on where he is
		return 0;
	}

}