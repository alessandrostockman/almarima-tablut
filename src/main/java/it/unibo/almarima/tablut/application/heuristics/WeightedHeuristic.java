package it.unibo.almarima.tablut.application.heuristics;

import java.util.ArrayList;

import it.unibo.almarima.tablut.application.domain.BoardState;
import it.unibo.almarima.tablut.application.domain.Coord;
import it.unibo.almarima.tablut.application.domain.Coordinates;
import it.unibo.almarima.tablut.external.State.Pawn;

public class WeightedHeuristic extends Heuristic {
	public final int STARTING_WHITE_PAWNS = 8;
	public final int STARTING_BLACK_PAWNS = 16;
	private final int MAX_KING_ESCAPE_DISTANCE = 6; 

	private double a;
	private double b;
	private double c;
	private double d;
	private double KE;
	private double KK;
	private double PD;
	private double EP;

	private int scoreKing(BoardState b){
        int score = checkSurroundings(b, b.getKingPosition());
        if(Coordinates.isCenterOrNeighborCenter(b.getKingPosition()))
            return (4-score)/4;
        return (3-score)/4;
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

	public WeightedHeuristic() {
		a = 2;
		b = 4;
		c = 1;
		d = 1;
	}

	public double evaluate(BoardState state) {
		if (state.getWinner() == 0 || state.getWinner() == 1) {
		//	System.out.println("HO TROVATO UNO 0/1");
			return state.getWinner();
		}

		int blackPieces = state.getNumberPlayerPieces(BoardState.BLACK);
		int whitePieces = state.getNumberPlayerPieces(BoardState.WHITE)-1;

		int blackPiecesEndangered = state.numEndangeredPieces(BoardState.BLACK);
		int whitePiecesEndangered = state.numEndangeredPieces(BoardState.WHITE);
        

		KE = (double) (MAX_KING_ESCAPE_DISTANCE - Coordinates.distanceToClosestEscape(state.getKingPosition()))/MAX_KING_ESCAPE_DISTANCE;
		KK = (double) this.scoreKing(state);
		PD = (double) (STARTING_BLACK_PAWNS+2*whitePieces-blackPieces)/(2*STARTING_BLACK_PAWNS);
		EP = (double) (STARTING_BLACK_PAWNS-2*whitePiecesEndangered+blackPiecesEndangered)/(2*STARTING_BLACK_PAWNS);

		// System.out.println("KE = "+KE);
		// System.out.println("KK = "+KK);
		// System.out.println("PD = "+PD);
		// System.out.println("EP = "+EP);
		// System.out.println("score = "+((a*KE+b*KK+c*PD+d*EP)/(a+b+c+d)));
		if ((a*KE+b*KK+c*PD+d*EP)/(a+b+c+d) == 0 || (a*KE+b*KK+c*PD+d*EP)/(a+b+c+d) == 1){
			System.out.println("HO TROVATO UNO 0/1");
		}
		if ((a*KE+b*KK+c*PD+d*EP)/(a+b+c+d) > 1){
			System.out.println("HO TROVATO UNO > 1");
		}
		if ((a*KE+b*KK+c*PD+d*EP)/(a+b+c+d) < 0){
			System.out.println("HO TROVATO UNO < 0");
		}
		return (a*KE+b*KK+c*PD+d*EP)/(a+b+c+d);
	}
	
}
