package it.unibo.almarima.tablut.application.heuristics;

import it.unibo.almarima.tablut.application.domain.BoardState;

public abstract class Heuristic {

	public Heuristic() {}

	public abstract double evaluate(BoardState state);

	public String toString(){
		return this.getClass().getSimpleName();  
	}
	
}
