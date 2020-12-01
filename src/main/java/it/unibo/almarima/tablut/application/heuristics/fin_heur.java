package it.unibo.almarima.tablut.application.heuristics;

import java.util.ArrayList;

import it.unibo.almarima.tablut.application.domain.BoardState;
import it.unibo.almarima.tablut.application.domain.Coord;
import it.unibo.almarima.tablut.application.domain.Coordinates;
import it.unibo.almarima.tablut.application.domain.Coordinates.CoordinateDoesNotExistException;
import it.unibo.almarima.tablut.external.State.Pawn;

public class fin_heur extends Heuristic {

    @Override
    public double evaluate(BoardState state) {
        if (state.getWinner() == 0 || state.getWinner() == 1) {
            return state.getWinner();
        }

        return 0.5;
    }

    // compute the proportion between black and white pieces
    private double computeProportion(BoardState bs) {
        int black_num = bs.getNumberPlayerPieces(BoardState.BLACK);
        int white_num = bs.getNumberPlayerPieces(BoardState.WHITE) - 1;

        return (16 + black_num - white_num) / 24; // 16: max black , 24: max black + max white
    }

    /* Checks the numbers of black Pawns surrounding the king */
    private double evalKingDanger(BoardState bs) {
        int counter = 0;
        ArrayList<Coord> neigh = (ArrayList<Coord>) Coordinates.getNeighbors(bs.getKingPosition());

        for (Coord x : neigh) {
            Pawn p = bs.getPawnAt(x);
            if (p == Pawn.BLACK || Coordinates.isCitadel(x)) {
                counter++;
            }
        }
        return counter / 4;
    }

    private double protectKing(BoardState bs) throws CoordinateDoesNotExistException {
        int counter = 0;
        ArrayList<Coord> neigh = (ArrayList<Coord>) Coordinates.getNeighbors(bs.getKingPosition());

        for(Coord x: neigh){
            Pawn p = bs.getPawnAt(x);
            if(p == Pawn.WHITE){

                if(bs.getPawnAt(Coordinates.getSandwichCoord(x, bs.getKingPosition()))==Pawn.WHITE){

                }
            }
            if(p == Pawn.BLACK){

            }
        }
        return counter/4;
    }
    
    @Override
    public String printInfo() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
