package it.unibo.almarima.tablut.application.heuristics;

import java.util.ArrayList;


import it.unibo.almarima.tablut.application.domain.BoardState;
import it.unibo.almarima.tablut.application.domain.Coord;
import it.unibo.almarima.tablut.application.domain.Coordinates;
import it.unibo.almarima.tablut.external.State.Pawn;

public class PickleRicksheuristic extends Heuristic{
    public final int STARTING_WHITE_PAWNS = 8;
    public final int STARTING_BLACK_PAWNS = 16;
    

    public double evaluate(BoardState state){
        if (state.getWinner() == 0 || state.getWinner() == 1) {
            //	System.out.println("HO TROVATO UNO 0/1");
                return state.getWinner();
            }
        
        double score =0;
        

        if(state.getTurnPlayer() == BoardState.WHITE){
            //For each black piece being near to king, -5 on score 
            score -= checkSurroundings(state, state.getKingPosition())*5;
            //value of white pieces + 50 (the king)
            score += state.getNumberPlayerPieces(BoardState.WHITE) * 50 + 50;
            //being black pieces more, i'll give them less value of the number
            score -= state.getNumberPlayerPieces(BoardState.WHITE) * 50;
            score += isKingOnEscapeLine(state, state.getKingPosition());

        }else{
            score-=checkSurroundings(state, state.getKingPosition())*5;
            score+=checkKingLines(state, state.getKingPosition())*3;
            //value of white pieces + 50 (the king)
            score += state.getNumberPlayerPieces(BoardState.WHITE) * 50 + 50;
            //being black pieces more, i'll give them less value of the number
            score -= state.getNumberPlayerPieces(BoardState.WHITE) * 50;
        }
        return (score+1000)/2000;
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
    //checks if on the above/under + left/right lines w.r.t. the king there are black pieces
    private int checkKingLines(BoardState state, Coord kingPos){
        int value=0;
        if(!state.getRow(kingPos.x+1).contains(Pawn.BLACK))
            value++;
        if(!state.getRow(kingPos.x-1).contains(Pawn.BLACK))
            value ++;
        if (!state.getColumn(kingPos.y+1).contains(Pawn.BLACK))
            value++;
        if (!state.getColumn(kingPos.y-1).contains(Pawn.BLACK))
            value++;
        return value;
    }
    private double isKingOnEscapeLine(BoardState state, Coord kingPos){
        double value = 0;
        //verifies the rows
        switch (kingPos.x) {
                case 6:
                    if(!state.getRow(6).contains(Pawn.BLACK))
                        value+=10;
                        if(state.getRow(6).contains(Pawn.WHITE))
                            value+= 500;
                    break;
                case 7:
                    if(!state.getRow(7).contains(Pawn.BLACK))
                            value+=10;
                            if(state.getRow(7).contains(Pawn.WHITE))
                                value+= 500;
                    break;
                case 1:
                    if(!state.getRow(1).contains(Pawn.BLACK))
                        value+=10;
                    if(state.getRow(1).contains(Pawn.WHITE))
                        value+= 500;
                    break;
                case 2:
                    if(!state.getRow(2).contains(Pawn.BLACK))
                            value+=10;
                        if(state.getRow(2).contains(Pawn.WHITE))
                            value+= 500;
                    break;
                default:
                    //the king is not on escape rows
                    value += 1;
        }

        //verifies the columns
        switch (kingPos.y) {
            case 6:
                if(!state.getColumn(6).contains(Pawn.BLACK))
                    value+=10;
                    if(state.getColumn(6).contains(Pawn.WHITE))
                        value+= 500;
                break;
            case 7:
                if(!state.getColumn(7).contains(Pawn.BLACK))
                        value+=10;
                        if(state.getColumn(7).contains(Pawn.WHITE))
                            value+= 500;
                break;
            case 1:
                if(!state.getColumn(1).contains(Pawn.BLACK))
                    value+=10;
                if(state.getColumn(1).contains(Pawn.WHITE))
                    value+= 500;
                break;
            case 2:
                if(!state.getColumn(2).contains(Pawn.BLACK))
                        value+=10;
                    if(state.getColumn(2).contains(Pawn.WHITE))
                        value+= 500;
                break;
            default:
                //the king is now on escape rows
                value+=1;
        }
        return value;
    }
    
}