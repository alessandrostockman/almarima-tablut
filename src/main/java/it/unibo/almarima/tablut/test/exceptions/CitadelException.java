package it.unibo.almarima.tablut.test.exceptions;

import it.unibo.almarima.tablut.external.Action;

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
