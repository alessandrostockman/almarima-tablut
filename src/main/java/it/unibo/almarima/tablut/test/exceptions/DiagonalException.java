package it.unibo.almarima.tablut.test.exceptions;

import it.unibo.almarima.tablut.external.Action;

/**
 * This exception represent an action that is moving a pawn diagonally
 * @author A.Piretti
 *
 */
public class DiagonalException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public DiagonalException(Action a)
	{
		super("Diagonal move is not allowed: "+a.toString());
	}

}
