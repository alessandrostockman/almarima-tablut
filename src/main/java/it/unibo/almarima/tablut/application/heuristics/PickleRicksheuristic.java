package it.unibo.almarima.tablut.application.heuristics;

import java.util.ArrayList;
import java.util.List;

import it.unibo.almarima.tablut.application.domain.BoardState;
import it.unibo.almarima.tablut.application.domain.Coord;
import it.unibo.almarima.tablut.application.domain.Coordinates;
import it.unibo.almarima.tablut.external.State.Pawn;

public class PickleRicksheuristic extends Heuristic{
    public final int STARTING_WHITE_PAWNS = 8;
    public final int STARTING_BLACK_PAWNS = 16;
    

    public double evaluate(BoardState bs){
        if (bs.getWinner() == 0 || bs.getWinner() == 1) {
            //	System.out.println("HO TROVATO UNO 0/1");
                return bs.getWinner();
            }
        
        double score =0;
        

        if(bs.getTurnPlayer() == BoardState.WHITE){
            //For each black piece being near to king, -5 on score 
            score -= checkSurroundings(bs, bs.getKingPosition())*5;
            //value of white pieces + 50 (the king)
            score += bs.getNumberPlayerPieces(BoardState.WHITE) * 50 + 50;
            //being black pieces more, i'll give them less value of the number
            score -= bs.getNumberPlayerPieces(BoardState.BLACK) * 50;
            score += isKingOnEscapeLine(bs, bs.getKingPosition());

        }else{
            score-=checkSurroundings(bs, bs.getKingPosition())*5;
            score+=checkKingLines(bs, bs.getKingPosition())*3;
            //value of white pieces + 50 (the king)
            score += bs.getNumberPlayerPieces(BoardState.WHITE) * 50 + 50;
            //being black pieces more, i'll give them less value of the number
            score -= bs.getNumberPlayerPieces(BoardState.BLACK) * 50;
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
    private int checkKingLines(BoardState bs, Coord kingPos){
        int value=0;
        if(!this.getRow(2,bs).contains(Pawn.BLACK) && !this.getRow(2,bs).contains(Pawn.WHITE))
            value++;
        if(!this.getRow(3,bs).contains(Pawn.BLACK) && !this.getRow(3,bs).contains(Pawn.WHITE))
            value ++;
        if (!this.getColumn(2,bs).contains(Pawn.BLACK) && !this.getColumn(2,bs).contains(Pawn.WHITE))
            value++;
        if (!this.getColumn(3,bs).contains(Pawn.BLACK) && !this.getColumn(3,bs).contains(Pawn.WHITE))
            value++;
        if(!this.getRow(5,bs).contains(Pawn.BLACK) && !this.getRow(5,bs).contains(Pawn.WHITE))
            value++;
        if(!this.getRow(6,bs).contains(Pawn.BLACK) && !this.getRow(6,bs).contains(Pawn.WHITE))
            value ++;
        if (!this.getColumn(5,bs).contains(Pawn.BLACK) && !this.getColumn(5,bs).contains(Pawn.WHITE))
            value++;
        if (!this.getColumn(6,bs).contains(Pawn.BLACK) && !this.getColumn(6,bs).contains(Pawn.WHITE))
            value++;
        return value;
    }
    private double isKingOnEscapeLine(BoardState bs, Coord kingPos){
        double value = 0;
        //verifies the rows
        switch (kingPos.x) {
                case 6:
                    if(!this.getRow(6,bs).contains(Pawn.BLACK)){
                        value+=10;
                        if(!this.getRow(6,bs).contains(Pawn.WHITE))
                            value+= 500;
                    }
                    break;
                /*case 7:
                    if(!this.getRow(7,bs).contains(Pawn.BLACK)){
                            value+=10;
                            if(!this.getRow(7,bs).contains(Pawn.WHITE))
                                value+= 500;
                    }
                    break;*/
                /*case 1:
                    if(!this.getRow(1,bs).contains(Pawn.BLACK)){
                        value+=10;
                        if(!this.getRow(1,bs).contains(Pawn.WHITE))
                            value+= 500;
                    }
                    break;*/
                case 2:
                    if(!this.getRow(2,bs).contains(Pawn.BLACK)){
                        value+=10;
                        if(!this.getRow(2,bs).contains(Pawn.WHITE))
                            value+= 500;
                    }
                    break;
                default:
                    //the king is not on escape rows
                    value =0;
        }

        //verifies the columns
        switch (kingPos.y) {
            case 6:
                if(!this.getColumn(6,bs).contains(Pawn.BLACK)){
                    value+=10;
                    if(!this.getColumn(6,bs).contains(Pawn.WHITE))
                        value+= 500;
                }
                break;
            /*case 7:
                if(!this.getColumn(7,bs).contains(Pawn.BLACK)){
                        value+=10;
                        if(!this.getColumn(7,bs).contains(Pawn.WHITE))
                            value+= 500;
                }
                break;*/
            /*case 1:
                if(!this.getColumn(1,bs).contains(Pawn.BLACK)){
                    value+=10;
                    if(this.getColumn(1,bs).contains(Pawn.WHITE))
                        value+= 500;
                }
                break;*/
            case 2:
                if(!this.getColumn(2,bs).contains(Pawn.BLACK)){
                    value+=10;
                    if(!this.getColumn(2,bs).contains(Pawn.WHITE))
                        value+= 500;
                }
                break;
            default:
                //the king is now on escape rows
                value=0;
        }
        return value;
    }


    //returns the i-th row[Pawn]
    public List<Pawn> getRow(int i,BoardState b){
        ArrayList<Pawn> ret = new ArrayList<>();
        for(int j=0; j<9; j++)
            ret.add(b.getPawnAt(Coordinates.get(i,j)));
        return ret;
    }

    //returns the i-th column [Pawn]
    public List<Pawn> getColumn(int i,BoardState b){
        ArrayList<Pawn> ret = new ArrayList<>();
        for(int j=0; j<9; j++)
            ret.add(b.getPawnAt(Coordinates.get(j,i)));
        return ret;
    }

    @Override
    public String printInfo() {
        return this.toString() +"\nboooh";
    }
    
    
}