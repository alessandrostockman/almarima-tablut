package it.unibo.almarima.tablut.test.exceptions;

public class GameFinishedException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public GameFinishedException()
	{
		super("Game is over");
	}

}
