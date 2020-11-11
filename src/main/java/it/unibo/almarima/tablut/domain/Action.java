package it.unibo.almarima.tablut.domain;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidParameterException;

/**
 * this class represents an action of a player
 * 
 * @author A.Piretti
 * 
 */
public class Action implements Serializable {

	private static final long serialVersionUID = 1L;

	private Coordinate from;
	private Coordinate to;

	private State.Turn turn;

	public Action(String from, String to, StateTablut.Turn t) throws IOException {
		if (from.length() != 2 || to.length() != 2) {
			throw new InvalidParameterException("the FROM and the TO string must have length=2");
		} else {
			this.from = new Coordinate(from);
			this.to = new Coordinate(to);
			this.turn = t;
		}
	}

	public Action(Coordinate from, Coordinate to, StateTablut.Turn t) throws IOException {
		this.from = from;
		this.to = to;
		this.turn = t;
	}

	public String getFrom() {
		return this.from.toString();
	}

	public void setFrom(String from) {
		this.from = new Coordinate(from);
	}

	public String getTo() {
		return to.toString();
	}

	public void setTo(String to) {
		this.to = new Coordinate(to);
	}

	public StateTablut.Turn getTurn() {
		return turn;
	}

	public void setTurn(StateTablut.Turn turn) {
		this.turn = turn;
	}

	public String toString() {
		return "Turn: " + this.turn + " " + "Pawn from " + from + " to " + to;
	}

	/**
	 * @return means the index of the column where the pawn is moved from
	 */
	public int getColumnFrom() {
		return this.from.getX();
	}

	/**
	 * @return means the index of the column where the pawn is moved to
	 */
	public int getColumnTo() {
		return this.from.getY();
	}

	/**
	 * @return means the index of the row where the pawn is moved from
	 */
	public int getRowFrom() {
		return this.to.getX();
	}

	/**
	 * @return means the index of the row where the pawn is moved to
	 */
	public int getRowTo() {
		return this.to.getY();
	}

}
