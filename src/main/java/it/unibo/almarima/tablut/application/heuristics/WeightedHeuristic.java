package it.unibo.almarima.tablut.application.heuristics;

import java.util.ArrayList;

import it.unibo.almarima.tablut.application.domain.BoardState;
import it.unibo.almarima.tablut.application.domain.Coord;
import it.unibo.almarima.tablut.application.domain.Coordinates;
import it.unibo.almarima.tablut.external.State.Pawn;

public class WeightedHeuristic extends WeightHeuristic {
	public final int STARTING_WHITE_PAWNS = 8;
	public final int STARTING_BLACK_PAWNS = 16;
	private final int MAX_KING_ESCAPE_DISTANCE = 6; 
	
	private int blackPieces;
	private int whitePieces;
	private int blackPiecesEndangered;
	private int whitePiecesEndangered;

	@Override
	public void initVariables(BoardState state) {
		this.blackPieces = state.getNumberPlayerPieces(BoardState.BLACK);
		this.whitePieces = state.getNumberPlayerPieces(BoardState.WHITE)-1;

		this.blackPiecesEndangered = state.numEndangeredPieces(BoardState.BLACK);
		this.whitePiecesEndangered = state.numEndangeredPieces(BoardState.WHITE);
	}

	@Override
	public WeightBag createWeightBag() {
		w = new WeightBag(false);
		w.addWeight(Parameter.KING_ESCAPE, 1);
		w.addWeight(Parameter.KING_SAFETY, 0);
		w.addWeight(Parameter.PAWN_NUMBER, 1);
		w.addWeight(Parameter.ENDANGERED_PAWNS, 0);
		return w;
	}

	@Override
	public double computeParameterValue(Parameter p, BoardState state) {
		switch (p) {
			case KING_ESCAPE:
				return this.getKE(state);
			case KING_SAFETY:
				return this.getKK(state);
			case PAWN_NUMBER:
				return this.getPD(state);
			case ENDANGERED_PAWNS:
				return this.getEP(state);
			default:
				return 0.5;
		}
	}

	private double getKE(BoardState state) {
		return (double) (MAX_KING_ESCAPE_DISTANCE - Coordinates.distanceToClosestEscape(state.getKingPosition()))/MAX_KING_ESCAPE_DISTANCE;
	}

	private double getKK(BoardState state) {
		return (double) this.scoreKing(state);
	}

	private double getPD(BoardState state) {
		return (double) (STARTING_BLACK_PAWNS+2*whitePieces-blackPieces)/(2*STARTING_BLACK_PAWNS);
	}

	private double getEP(BoardState state) {
		return (double) (STARTING_BLACK_PAWNS-2*whitePiecesEndangered+blackPiecesEndangered)/(2*STARTING_BLACK_PAWNS);
	}

    /*Checks the numbers of black Pawns surrounding the king*/
    private int checkSurroundings(BoardState b, Coord kingPos){
        int counter = 0;
        ArrayList<Coord> neigh = (ArrayList<Coord>) Coordinates.getNeighbors(kingPos);

        for(Coord x: neigh){
            if(b.getPawnAt(x) == Pawn.BLACK || b.getPawnAt(x) == Pawn.THRONE || Coordinates.isCitadel(x)){
                counter++;
            }
        }
        return counter;
    }

	private int scoreKing(BoardState b){
        int score = checkSurroundings(b, b.getKingPosition());
        if(Coordinates.isCenterOrNeighborCenter(b.getKingPosition()))
            return (4-score)/4;
        return (3-score)/4;
    }

}
