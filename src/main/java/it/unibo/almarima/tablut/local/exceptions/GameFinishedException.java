package it.unibo.almarima.tablut.local.exceptions;

import it.unibo.almarima.tablut.external.State.Turn;

public class GameFinishedException extends AgentStoppedException {

	private static final long serialVersionUID = 1L;

	private Turn winner;
	private int whiteRandomMoves;
	private int blackRandomMoves;
	private int turns;
	
	public GameFinishedException(Turn winner, int whiteRandomMoves, int blackRandomMoves, int turns)
	{
		this.winner = winner;
		this.whiteRandomMoves = whiteRandomMoves;
		this.blackRandomMoves = blackRandomMoves;
		this.turns = turns;
	}

	public Turn getWinner() {
		return this.winner;
	}

	public int getWhiteRandomMoves() {
		return this.whiteRandomMoves;
	}

	public int getBlackRandomMoves() {
		return this.blackRandomMoves;
	}

	public int getTurnNumber() {
		return this.turns;
	}

}
