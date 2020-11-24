package it.unibo.almarima.tablut.application.heuristics.WeightedHeuristic;

import it.unibo.almarima.tablut.application.domain.BoardState;
import it.unibo.almarima.tablut.application.heuristics.Heuristic;

public class WeightedHeuristic extends Heuristic {

	private int a;
	private int b;
	private int c;
	private int d;
	private Heuristic KE;
	private Heuristic KK;
	private Heuristic PD;
	private Heuristic EP;

	public WeightedHeuristic() {
		this.a = 1;
		this.b = 0;
		this.c = 1;
		this.d = 1;
		this.KE = new KingEscaped();
		this.KK = new KingKilled();
		this.PD = new PawnsDifference();
		this.EP = new EndangeredPawns();
	}

	public double evaluate(BoardState state) {
		if (state.getWinner() == 0 || state.getWinner() == 1) {
			return state.getWinner();
		}
		return (this.a*this.KE.evaluate(state)+this.b*this.KK.evaluate(state)+this.c*this.PD.evaluate(state)+this.d*this.EP.evaluate(state))/(this.a+this.b+this.c+this.d);
	}
	
}
