package it.unibo.almarima.tablut.application.heuristics;

import it.unibo.almarima.tablut.application.domain.BoardState;
import it.unibo.almarima.tablut.application.domain.Coordinates;

public class WHeuristic extends Heuristic {

    public static final double w1=1.0;
    public static final double w2=0.0;
    public static final double w3=1.0;
    public static final double w4=0.0;

    private final double MAX_KING_ESCAPE_DISTANCE = 6.0;


    @Override
    public double evaluate(BoardState state) {

        if (state.getWinner() == 0 || state.getWinner() == 1) {
			return state.getWinner();
		}
        
        double kingEscape= (MAX_KING_ESCAPE_DISTANCE - Coordinates.distanceToClosestEscape(state.getKingPosition()))/MAX_KING_ESCAPE_DISTANCE;

        double kingKilled = state.


    }
    
}
