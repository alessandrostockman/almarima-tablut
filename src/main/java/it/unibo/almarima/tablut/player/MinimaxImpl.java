package it.unibo.almarima.tablut.player;

import java.io.IOException;
import java.util.ArrayList;

import it.unibo.almarima.tablut.domain.Action;
import it.unibo.almarima.tablut.domain.State;
import it.unibo.almarima.tablut.domain.State.Turn;

public class MinimaxImpl extends Player {
    Turn t;



    public MinimaxImpl(int timeout, Turn color) {
        super(timeout, color);
        // TODO Auto-generated constructor stub
    }


    public ArrayList<State> getLegalMoves(State board, Turn t) {
        // TODO : Algorithhm that returns a list of legal move for the player t

        return null;
    }

    public int max(int alpha,int val){
        //TODO : Algorithm that returns something based on heuristic, input arguments may need a change
        return 0;
    }

    public int min(int beta, int val){
        //TODO : Algorithm that returns something based on heuristic, input arguments may need a change
        return 0;
    }

    public int minimaxAlphaBeta(int depth, int alpha, int beta, Turn t, State currentState) throws IOException{
        int bestMove = 0;
        ArrayList<State> possibleMoves = getLegalMoves(currentState, t);

        if (depth == 0 || possibleMoves.isEmpty() || Turn.BLACKWIN == t || Turn.WHITEWIN == t){
            return bestMove;
        }
        if (t== Turn.BLACK){ //maximizing BLACK
            int maxValue = Integer.MIN_VALUE;
            for(State s : possibleMoves){
                int val = minimaxAlphaBeta(depth-1, alpha, beta, Turn.WHITE, s);
                maxValue = max(maxValue, val);
                alpha = max(alpha,maxValue);
                if(alpha >= beta)
                    break;
            }
            return maxValue;
        }
        else{ //minimizing WHITE
            int minValue = Integer.MAX_VALUE;
            for(State s: possibleMoves){
                int val = minimaxAlphaBeta(depth-1, alpha, beta, Turn.BLACK, s);
                minValue= min(minValue,val);
                beta = min(beta,val);
                if(alpha >= beta)
                    break;
            }
            return minValue;
        }
    }

    @Override
    public Action getMove(State currentState) throws IOException {
        return minimaxAlphaBeta(0, 0, 0, this.t, currentState);
    }
}
