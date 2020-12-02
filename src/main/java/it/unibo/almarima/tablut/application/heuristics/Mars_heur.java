package it.unibo.almarima.tablut.application.heuristics;

import java.util.ArrayList;
import java.util.Arrays;

import it.unibo.almarima.tablut.application.domain.BoardState;
import it.unibo.almarima.tablut.application.domain.Coord;
import it.unibo.almarima.tablut.application.domain.Coordinates;
import it.unibo.almarima.tablut.external.State.Pawn;

public class Mars_heur extends Heuristic {
	
	private double pieceHWeight = 0.5;
	private double distToEscapeWeight = 0.2;    //TODO: forse 0,3 distEscape perchè spesso ha la possibilità di vincere e non lo fa
	private double dangerKingWeight = 0.15;
	private double escapeLineWeight = 0.15;
	
	private Coord kingPos;

	public double evaluate(BoardState state)  {
		if (state.getTurnPlayer() == BoardState.WHITE) {
			
			// if one wants to change weights only for WHITE
			pieceHWeight += 0;
			distToEscapeWeight += 0;
			dangerKingWeight += 0;
			escapeLineWeight += 0;

			this.kingPos=state.getKingPosition();
			
			// if there is a winner return the winner (corresponds to the max and min h values of 0 or 1)
			if (state.getWinner() == 0 || state.getWinner() == 1) {
				return state.getWinner();
			}
			
			// calculate a ratio of white to black pieces remaining on the board
			int whitePieceCount = state.getNumberPlayerPieces(BoardState.WHITE);
			int blackPieceCount = state.getNumberPlayerPieces(BoardState.BLACK);
			int totalPieceCount = whitePieceCount + blackPieceCount;
			double pieceH = (whitePieceCount*1.0/totalPieceCount);
			
			// calculate the distance of the king to the closest escape
			int kingDistToEscape = Coordinates.distanceToClosestEscape(kingPos);
			int maxDistToEscape = 6;
			double distToEscape= ((maxDistToEscape-kingDistToEscape)*1.0/maxDistToEscape); 

			double surrKing = this.scoreKing(state);		
			double escapeLine = this.checkKingEscapeLine(state);
			
			// weigh the two h values calculated above
			double h = (pieceH*pieceHWeight + distToEscape*distToEscapeWeight+surrKing*dangerKingWeight+escapeLine*escapeLineWeight)/(pieceHWeight+distToEscapeWeight+dangerKingWeight+escapeLineWeight); 
			return h;
		} else {

			// if one wants to change weights only for BLACK
			pieceHWeight += 0;
			distToEscapeWeight += 0;
			dangerKingWeight += 0;
			escapeLineWeight += 0;

			this.kingPos=state.getKingPosition();
			
			// if there is a winner return the winner (corresponds to the max and min h values of 0 or 1)
			if (state.getWinner() == 0 || state.getWinner() == 1) {
				return state.getWinner();
			}
			
			// calculate a ratio of white to black pieces remaining on the board
			int whitePieceCount = state.getNumberPlayerPieces(BoardState.WHITE);
			int blackPieceCount = state.getNumberPlayerPieces(BoardState.BLACK);
			int totalPieceCount = whitePieceCount + blackPieceCount;
			double pieceH = (whitePieceCount*1.0/totalPieceCount);
			
			// calculate the distance of the king to the closest escape
			int kingDistToEscape = Coordinates.distanceToClosestEscape(kingPos);
			int maxDistToEscape = 6;
			double distToEscape= ((maxDistToEscape-kingDistToEscape)*1.0/maxDistToEscape); 

			double surrKing = this.checkSurroundings(state, kingPos);
			
			double escapeLine = this.checkKingEscapeLine(state);
			
			// weigh the two h values calculated above
			double h = (pieceH*pieceHWeight + distToEscape*distToEscapeWeight+surrKing*dangerKingWeight+escapeLine*escapeLineWeight)/(pieceHWeight+distToEscapeWeight+dangerKingWeight+escapeLineWeight); 
			return h;	
	}	
	}

	private double scoreKing(BoardState b){
        double score = checkSurroundingsWhite(b, b.getKingPosition());
        if(Coordinates.isCenterOrNeighborCenter(b.getKingPosition()))
            return (4-score)/4;
        return (3-score)/4; 
    }


    /*Checks the numbers of black Pawns surrounding the king*/
    private double checkSurroundingsWhite(BoardState b, Coord kingPos){
        double counter = 0;
        ArrayList<Coord> neigh = (ArrayList<Coord>) Coordinates.getNeighbors(kingPos);

        for(Coord x: neigh){
            if(b.getPawnAt(x) == Pawn.BLACK || b.getPawnAt(x) == Pawn.THRONE || Coordinates.isCitadel(x)){
                counter++;
            }
        }
        return counter;
    }
	
	/*Checks the numbers of black Pawns surrounding the king*/
	private double checkSurroundings(BoardState b, Coord kingPos){
		int counter = 0;
		ArrayList<Coord> neigh = (ArrayList<Coord>) Coordinates.getNeighbors(kingPos);

		for(Coord x: neigh){
			if(b.getPawnAt(x) == Pawn.BLACK){
				counter++;
			}
		}
		return 1-(counter/4);
	}

	private double checkKingEscapeLine(BoardState bs){
		
		double score=0.0;

		for (int incr : Arrays.asList(-1, 1)) {
			ArrayList<Pawn> row=this.getRow(kingPos.x+incr,bs);
			if (!row.contains(Pawn.BLACK)){
				score+=0.05;
				if (!row.contains(Pawn.WHITE)){
					score+= 0.15;
					if(kingPos.x+incr==6 || kingPos.x+incr==7 || kingPos.x+incr==1 || kingPos.x+incr==2){
						score+= 0.05;
					}
				}
			}
			ArrayList<Pawn> col= this.getColumn(kingPos.y+incr,bs);
			if (!col.contains(Pawn.BLACK)){
				score+=0.05;
				if (!col.contains(Pawn.WHITE)){
					score+= 0.15;
					if(kingPos.y+incr==6 || kingPos.y+incr==7 || kingPos.y+incr==1 || kingPos.y+incr==2){
						score+= 0.05;
					}
				}
			}
		}

		return score;

	}

	//returns the i-th row[Pawn]
    public ArrayList<Pawn> getRow(int i,BoardState b){
        ArrayList<Pawn> ret = new ArrayList<>();
        for(int j=0; j<9; j++)
            ret.add(b.getPawnAt(Coordinates.get(i,j)));
        return ret;
    }

    //returns the i-th column [Pawn]
    public ArrayList<Pawn> getColumn(int i,BoardState b){
        ArrayList<Pawn> ret = new ArrayList<>();
        for(int j=0; j<9; j++)
            ret.add(b.getPawnAt(Coordinates.get(j,i)));
        return ret;
    }
	
	@Override
	public String printInfo() {
		return this.toString()+"\npieceHWeigt: "+ pieceHWeight + "\ndistToEscapeWeight: "+ distToEscapeWeight;
	}
}
