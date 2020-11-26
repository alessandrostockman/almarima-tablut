package it.unibo.almarima.tablut.local.exceptions;

public class AgentStoppedException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public AgentStoppedException()
	{
		super("Game is over");
	}

}
