package it.unibo.almarima.tablut.application.heuristics.WeightedHeuristic;

import it.unibo.almarima.tablut.application.domain.BoardState;
import it.unibo.almarima.tablut.application.heuristics.Heuristic;

public class PawnsDifference extends Heuristic {

	public final int STARTING_WHITE_PAWNS = 8;
	public final int STARTING_BLACK_PAWNS = 16;

	public double evaluate(BoardState state) {
		int blackPieces = state.getNumberPlayerPieces(BoardState.BLACK);
		int whitePieces = state.getNumberPlayerPieces(BoardState.WHITE);
		return (STARTING_BLACK_PAWNS+2*whitePieces-blackPieces)/(2*STARTING_BLACK_PAWNS);
		//It basically is return (16+2*whitePieces-blackPieces)/32; but it was written this way in order to garantee more flexibility may the starting pieces numbers be changed
	}
	
}
