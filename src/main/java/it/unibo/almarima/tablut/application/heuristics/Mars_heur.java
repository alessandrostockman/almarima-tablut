package it.unibo.almarima.tablut.application.heuristics;

import it.unibo.almarima.tablut.application.domain.BoardState;
import it.unibo.almarima.tablut.application.domain.Coordinates;

public class Mars_heur extends Heuristic {

	private final static double pieceHWeight = 8;
	private final static double distToEscapeWeight = 2; 

	public double evaluate(BoardState state)  {
		
		// if there is a winner return the winner (corresponds to the max and min h values of 0 or 1)
		if (state.getWinner() == 0 || state.getWinner() == 1) {
			return state.getWinner();
		}
		
		// calculate a pieces score
		int whitePieceCount = state.getNumberPlayerPieces(BoardState.WHITE)-1;
		int blackPieceCount = state.getNumberPlayerPieces(BoardState.BLACK);
		double pieceH = (double) (16+2*whitePieceCount-blackPieceCount)/32;
		
		// calculate the distance of the king to the closest escape
		int kingDistToEscape = Coordinates.distanceToClosestEscape(state.getKingPosition());
		int maxDistToEscape = 6;
		double distToEscape= (double) (maxDistToEscape-kingDistToEscape)/maxDistToEscape;

		// weigh the two h values calculated above
		double h = (pieceH*pieceHWeight + distToEscape*distToEscapeWeight)/(pieceHWeight+distToEscapeWeight);

		return h;
	}

	@Override
	public String printInfo() {
		return "pieceHWeigt: "+ pieceHWeight + "\ndistToEscapeWeight: "+ distToEscapeWeight;
	}
}
