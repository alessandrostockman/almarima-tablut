package it.unibo.almarima.tablut.application.heuristics;

import java.util.ArrayList;

import it.unibo.almarima.tablut.application.domain.*;
import it.unibo.almarima.tablut.external.State.Pawn;

public class heuristicFromInternet {
    public double evaluate(BoardState b){
        if (b.getWinner() == 0 || b.getWinner() == 1) {
			return b.getWinner();
		}
        int score = 0;
        if(Coordinates.isCenterOrNeighborCenter(b.getKingPosition())){
            score = score + 10*scoreKing(b,true);
        }
        else{
            score = score - 10*scoreKing(b, false);
        }
        if(b.getTurnPlayer() == 1) //white
            score += 16 - b.getNumberPlayerPieces(0);
        else //black
            score -= 9 - b.getNumberPlayerPieces(1);

        return (double) (score+19)/75;
    }

    /*returns the king's score based on the surroundings.*/
    private int scoreKing(BoardState b, boolean inThrone){
        int score = -checkSurroundings(b, b.getKingPosition());
        if(inThrone)
            score = score/4;
        return score;

    }


    /*Checks the numbers of black Pawns surrounding the king*/
    private int checkSurroundings(BoardState b, Coord kingPos){
        int counter = 0;
        ArrayList<Coord> neigh = (ArrayList<Coord>) Coordinates.getNeighbors(kingPos);

        for(Coord x: neigh){
            if(b.getPawnAt(x) == Pawn.BLACK){
                counter++;
            }
        }
        return counter;
    }


}
