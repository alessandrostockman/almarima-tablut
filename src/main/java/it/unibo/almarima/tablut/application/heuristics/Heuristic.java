package it.unibo.almarima.tablut.application.heuristics;

import it.unibo.almarima.tablut.application.domain.BoardState;

public abstract class Heuristic {


	public Heuristic() {}

	public abstract double evaluate(BoardState state);

	public abstract String printInfo();

	
	public String toString(){
		return this.getClass().getSimpleName();  
	}


	
}
