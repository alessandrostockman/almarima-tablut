package it.unibo.almarima.tablut.local.exceptions;

import it.unibo.almarima.tablut.external.State.Turn;

public class GameFinishedException extends AgentStoppedException {

	private static final long serialVersionUID = 1L;

	private Turn winner;
	
	public GameFinishedException(Turn winner)
	{
		this.winner = winner;
	}

	public Turn getWinner() {
		return this.winner;
	}

}
