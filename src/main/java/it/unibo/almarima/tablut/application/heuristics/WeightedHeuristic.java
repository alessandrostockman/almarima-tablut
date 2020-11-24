package it.unibo.almarima.tablut.application.heuristics;

import java.util.List;

import it.unibo.almarima.tablut.application.domain.BoardState;
import it.unibo.almarima.tablut.application.domain.Coord;
import it.unibo.almarima.tablut.application.domain.Coordinates;
import it.unibo.almarima.tablut.application.heuristics.Heuristic;
import it.unibo.almarima.tablut.external.State.Pawn;

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
		a = 4;
		b = 0;
		c = 1;
		d = 1;
	}

	public double evaluate(BoardState state) {
		if (state.getWinner() == 0 || state.getWinner() == 1) {
			return state.getWinner();
		}

		int blackPieces = state.getNumberPlayerPieces(BoardState.BLACK);
		int whitePieces = state.getNumberPlayerPieces(BoardState.WHITE);

		int blackPiecesEndangered = state.numEndangeredPieces(BoardState.BLACK);
		int whitePiecesEndangered = state.numEndangeredPieces(BoardState.WHITE);

		/*
		(int num=0;
        Coord c = state.getKingPosition();
        List<Coord> neigh = Coordinates.getNeighbors(c);
        if (Coordinates.isCenterOrNeighborCenter(c) == true){
            
        } else {
            for (Coord n: neigh){
                try {
                    Coord s= Coordinates.getSandwichCoord(n,c);
                        if (state.canCaptureWithCoord(s, state.BLACK) && state.getPawnAt(n)==Pawn.EMPTY) {
                            num+=1;
                            break;
                        }
                }catch(Exception e) {}
            }
        }
        */
        

		KE = (MAX_KING_ESCAPE_DISTANCE - Coordinates.distanceToClosestEscape(state.getKingPosition()))/MAX_KING_ESCAPE_DISTANCE;
		KK = 0;
		PD = (STARTING_BLACK_PAWNS+2*whitePieces-blackPieces)/(2*STARTING_BLACK_PAWNS);
		EP = (STARTING_BLACK_PAWNS-2*whitePiecesEndangered+blackPiecesEndangered)/(2*STARTING_BLACK_PAWNS);


		return (a*KE+b*KK+c*PD+d*EP)/(a+b+c+d);
	}
	
}
