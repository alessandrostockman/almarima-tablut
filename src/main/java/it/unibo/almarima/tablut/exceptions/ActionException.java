package main.java.it.unibo.almarima.tablut.exceptions;

import main.java.it.unibo.almarima.tablut.domain.Action;

/**
 * This exception represent an action with the wrong format
 * @author A.Piretti
 *
 */
public class ActionException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public ActionException(Action a)
	{
		super("The format of the action is not correct: "+a.toString());
	}

}
