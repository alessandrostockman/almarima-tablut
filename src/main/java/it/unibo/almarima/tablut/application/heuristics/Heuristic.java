package it.unibo.almarima.tablut.application.heuristics;

import it.unibo.almarima.tablut.application.domain.BoardState;

public abstract class Heuristic {

	private WeightBag w;

	public Heuristic(WeightBag w) {
		this.w = w;
	}

	public Heuristic() { //TODO: To remove when WeighBag is ready to be used
		this.w = new WeightBag();
	}
	

	public abstract double evaluate(BoardState state);

	public WeightBag getWeightBag() {
		return this.w;
	}

	public String toString(){
		return this.getClass().getSimpleName();  
	}
	
}
