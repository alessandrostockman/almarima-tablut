package main.java.it.unibo.almarima.tablut.exceptions;

import main.java.it.unibo.almarima.tablut.domain.Action;

public class CitadelException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CitadelException(Action a)
	{
		super("Move into a citadel: "+a.toString());
	}

}
