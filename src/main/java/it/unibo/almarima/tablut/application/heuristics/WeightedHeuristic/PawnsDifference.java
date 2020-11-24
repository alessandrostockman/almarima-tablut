package it.unibo.almarima.tablut.application.heuristics;

import it.unibo.almarima.tablut.application.domain.BoardState;

public class PawnsDifference extends Heuristic {

	public final int STARTING_WHITE_PAWNS = 8;
	public final int STARTING_BLACK_PAWNS = 16;

	public double evaluate(BoardState state) {
		int blackPieces = state.getNumberPlayerPieces(BoardState.BLACK);
		int whitePieces = state.getNumberPlayerPieces(BoardState.WHITE);
		return (STARTING_BLACK_PAWNS-(STARTING_BLACK_PAWNS/STARTING_WHITE_PAWNS)*whitePieces+blackPieces)/(STARTING_BLACK_PAWNS+(STARTING_BLACK_PAWNS/STARTING_WHITE_PAWNS));
		//It basically is return (16+2*whitePieces-blackPieces)/32; but it was written this way in order to garantee more flexibility may the starting pieces numbers be changed
	}
	
}
