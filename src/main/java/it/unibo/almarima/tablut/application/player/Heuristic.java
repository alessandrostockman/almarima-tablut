package it.unibo.almarima.tablut.application.player;

import it.unibo.almarima.tablut.application.game.BoardState;

public abstract class Heuristic {

	public Heuristic() {}

	public abstract double evaluate(BoardState state);
	
}
