package it.unibo.almarima.tablut.application.heuristics.WeightedHeuristic;

import it.unibo.almarima.tablut.application.domain.BoardState;
import it.unibo.almarima.tablut.application.domain.Coordinates;
import it.unibo.almarima.tablut.application.heuristics.Heuristic;

public class WeightedHeuristic extends Heuristic {
	public final int STARTING_WHITE_PAWNS = 8;
	public final int STARTING_BLACK_PAWNS = 16;
	private final int MAX_KING_ESCAPE_DISTANCE = 6; 

	private int a;
	private int b;
	private int c;
	private int d;
	private double KE;
	private double KK;
	private double PD;
	private double EP;

	public WeightedHeuristic() {
		this.a = 4;
		this.b = 0;
		this.c = 1;
		this.d = 1;
	}

	public double evaluate(BoardState state) {
		if (state.getWinner() == 0 || state.getWinner() == 1) {
			return state.getWinner();
		}

		int blackPieces = state.getNumberPlayerPieces(BoardState.BLACK);
		int whitePieces = state.getNumberPlayerPieces(BoardState.WHITE);

		int blackPiecesEndangered = state.numEndangeredPieces(BoardState.BLACK);
		int whitePiecesEndangered = state.numEndangeredPieces(BoardState.WHITE);


		KE = (MAX_KING_ESCAPE_DISTANCE - Coordinates.distanceToClosestEscape(state.getKingPosition()))/MAX_KING_ESCAPE_DISTANCE;
		KK = 0;
		PD = (STARTING_BLACK_PAWNS+2*whitePieces-blackPieces)/(2*STARTING_BLACK_PAWNS);
		EP = (STARTING_BLACK_PAWNS-2*whitePiecesEndangered+blackPiecesEndangered)/(2*STARTING_BLACK_PAWNS);


		return (a*KE+b*KK+c*PD+d*EP)/(a+b+c+d);
	}
	
}
