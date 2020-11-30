package it.unibo.almarima.tablut.application.heuristics;

import it.unibo.almarima.tablut.application.domain.BoardState;
import it.unibo.almarima.tablut.application.domain.Coordinates;

public class Ric_heur extends Heuristic {

	public double evaluate(BoardState state)  {
		
		// if there is a winner return the winner (corresponds to the max and min h values of 0 or 1)
		if (state.getWinner() == 0 || state.getWinner() == 1) {
			return state.getWinner();
		}
		
		// calculate a ratio of white to black pieces remaining on the board
		int whitePieceCount = state.getNumberPlayerPieces(BoardState.WHITE);
		int blackPieceCount = state.getNumberPlayerPieces(BoardState.BLACK);
		int totalPieceCount = whitePieceCount + blackPieceCount;
		double pieceH = (whitePieceCount*1.0/totalPieceCount);
		
		// calculate the distance of the king to the closest escape
		int kingDistToEscape = Coordinates.distanceToClosestEscape(state.getKingPosition());
		int maxDistToEscape = 6;
		double distToEscape= ((maxDistToEscape-kingDistToEscape)*1.0/maxDistToEscape);
		
		double pieceHWeight = 0.8;
		double distToEscapeWeight = 0.2;

		// weigh the two h values calculated above
		double h = (pieceH*pieceHWeight + distToEscape*distToEscapeWeight)/(pieceHWeight+distToEscapeWeight);     //TODO: forse 0,3 distEscape perchè spesso ha la possibilità di vincere e non lo fa 

		return h;
	}
}
