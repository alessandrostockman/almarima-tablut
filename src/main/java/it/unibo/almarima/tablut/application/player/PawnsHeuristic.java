package it.unibo.almarima.tablut.application.player;

import it.unibo.almarima.tablut.application.game.BoardState;

public class PawnsHeuristic extends Heuristic {

	public final int STARTING_WHITE_PAWNS = 8;
	public final int STARTING_BLACK_PAWNS = 12;

	public double evaluate(BoardState state) {
		int blackPieces = state.getNumberPlayerPieces(BoardState.BLACK);
		int whitePieces = state.getNumberPlayerPieces(BoardState.WHITE);
		return whitePieces / (whitePieces + blackPieces);
	}
	
}
