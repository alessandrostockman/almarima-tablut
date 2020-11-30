package it.unibo.almarima.tablut.application.heuristics;

import it.unibo.almarima.tablut.application.domain.BoardState;
import it.unibo.almarima.tablut.application.domain.Coordinates;

public class RicHeur extends WeightHeuristic {

	@Override
	public WeightBag createWeightBag() {
		w = new WeightBag(false);
		w.addWeight(Parameter.WHITE_PAWN_NUMBER, 0.8);
		w.addWeight(Parameter.KING_ESCAPE, 0.2);
		return w;
	}

	@Override
	public void initVariables(BoardState state) {

	}

	@Override
	public double computeParameterValue(Parameter p, BoardState state) {
		switch (p) {
			case KING_ESCAPE:
				return this.getDistToEscape(state);
			case WHITE_PAWN_NUMBER:
				return this.getWhiteBlackRatio(state);
			default:
				return 0.5;
		}
	}

	public double getDistToEscape(BoardState state) {
		// calculate the distance of the king to the closest escape
		int kingDistToEscape = Coordinates.distanceToClosestEscape(state.getKingPosition());
		int maxDistToEscape = 6;
		return (maxDistToEscape-kingDistToEscape)*1.0/maxDistToEscape;
	}

	public double getWhiteBlackRatio(BoardState state) {
		// calculate a ratio of white to black pieces remaining on the board
		int whitePieceCount = state.getNumberPlayerPieces(BoardState.WHITE);
		int blackPieceCount = state.getNumberPlayerPieces(BoardState.BLACK);
		int totalPieceCount = whitePieceCount + blackPieceCount;

		return whitePieceCount*1.0/totalPieceCount;
	}
}
