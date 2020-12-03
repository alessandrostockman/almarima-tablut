package it.unibo.almarima.tablut.application.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.unibo.almarima.tablut.application.domain.BoardState;
import it.unibo.almarima.tablut.application.domain.Coord;
import it.unibo.almarima.tablut.application.domain.Coordinates;
import it.unibo.almarima.tablut.application.domain.Move;
import it.unibo.almarima.tablut.application.domain.Valuation;
import it.unibo.almarima.tablut.external.State.Pawn;


public class MiniMaxTree {

    public class TimeLimitException extends Exception {

        public static final long serialVersionUID = 1L;

        public TimeLimitException(String errorMessage) {
            super(errorMessage);
        }
    }

    public int maxDepth;
    public BoardState headBoardState;
    public long endTime;

    public MiniMaxTree(int maxDepth, BoardState headBoardState, long endTime) {
        this.headBoardState = headBoardState;
        this.maxDepth = maxDepth;
        this.endTime = endTime;
    }

    // This function differs from minimax as it needs to keep track of not just max and min
	// but also the corresponding moves that led to those values.
    public Move getBestMove() throws TimeLimitException {
        
        int depth = 0;
		Valuation alpha = new Valuation(0.0, maxDepth+1);      
        Valuation beta = new Valuation(1.0, maxDepth+1);
        
        // if the player with the turn is a WHITE then maximize
		if (headBoardState.getTurnPlayer() == BoardState.WHITE) {
			
			Move maxMove = null;
			List<Move> nextMoves = this.headBoardState.getAllLegalMoves();

			// iterate through all possible moves
			for (Move move : nextMoves) {

				// clone the bs and process the move to generate a new board
				BoardState childBS = (BoardState) this.headBoardState.clone();
				childBS.processMove(move);

				// recursively run minimax on the child board state
				Valuation childValuation = minimax(childBS, depth+1, alpha.clone(), beta.clone());
                
                if (childValuation.gethVal()>alpha.gethVal() || (childValuation.gethVal()==alpha.gethVal() && childValuation.getDepthAttained() < alpha.getDepthAttained() ) ){
                    alpha = childValuation;
                    maxMove = move;
				}
			}
			return maxMove;
		}
		else {       // if the player with the turn is a BLACK then minimize
			
			Move minMove = null;
			List<Move> nextMoves = this.headBoardState.getAllLegalMoves();

			// iterate through all possible moves
			for (Move move : nextMoves) {

				// clone the bs and process the move to generate a new board
				BoardState childBS = (BoardState) this.headBoardState.clone();
				childBS.processMove(move);

				// recursively run minimax on the child board state
                Valuation childValuation = minimax(childBS, depth+1, alpha.clone(), beta.clone());
                
                if (childValuation.gethVal()<beta.gethVal() || (childValuation.gethVal()==beta.gethVal() && childValuation.getDepthAttained() < beta.getDepthAttained() ) ){
                    beta = childValuation;
                    minMove = move;
				}
			}
			return minMove;
		}
    }
    
    // Implementation of minmax alg with alpha beta pruning
	private Valuation minimax(BoardState nodeBS, int depth, Valuation alpha, Valuation beta) throws TimeLimitException {
		boolean updated = false;
		
        if (System.currentTimeMillis() > endTime) {
			throw new TimeLimitException("");
		}
		// if node is a leaf, then evaluate with h function
		if (depth == this.maxDepth) {
			return new Valuation(this.evaluate(nodeBS), depth);
		}
		// if there is a winner return the winner (corresponds to the max and min h values of 0 or 1)
		if (nodeBS.getWinner() == BoardState.BLACK || nodeBS.getWinner() == BoardState.WHITE) {
			return new Valuation(nodeBS.getWinner(), depth);
		}
        
        //maximizing heuristic 
        if (nodeBS.getTurnPlayer()== BoardState.WHITE){

			List<Move> nextMoves = nodeBS.getAllLegalMoves();

			// iterate through all possible moves
			for (Move move : nextMoves) {

				// clone the bs and process the move to generate a new board
				BoardState childBS = (BoardState) nodeBS.clone();
				childBS.processMove(move);
			
				// recursively run minimax on the child board state
				Valuation childValuation = minimax(childBS, depth+1, alpha.clone(), beta.clone());
                
                if (childValuation.gethVal()>alpha.gethVal() || (childValuation.gethVal()==alpha.gethVal() && childValuation.getDepthAttained() < alpha.getDepthAttained() ) ){
					alpha = childValuation;
					updated = true;

				}
				//if alpha>=beta prune the tree
                if (alpha.gethVal()>=beta.gethVal()){
                    break;
                }
			}
			//if i have never updated alpha value return the one that i got from the upper node with maxdepth to avoid chosing this move 
			if (!updated) {
				return new Valuation(alpha.gethVal(), maxDepth);
			}
			return alpha;
        }

        //minimizing heuristic 
        else {

			List<Move> nextMoves = nodeBS.getAllLegalMoves();

			// iterate through all possible moves
			for (Move move : nextMoves) {

				// clone the bs and process the move to generate a new board
				BoardState childBS = (BoardState) nodeBS.clone();
				childBS.processMove(move);

				// recursively run minimax on the child board state
				Valuation childValuation = minimax(childBS, depth+1, alpha.clone(), beta.clone());
                
                if (childValuation.gethVal()<beta.gethVal() || (childValuation.gethVal()==beta.gethVal() && childValuation.getDepthAttained() < beta.getDepthAttained() ) ){
					beta = childValuation;
					updated = true;

				}
				//if alpha>=beta prune the tree
                if (beta.gethVal()<=alpha.gethVal()){
                    break;
				}
			}
			//if i have never updated beta value return the one that i got from the upper node with maxdepth to avoid chosing this move 
			if (!updated) {
				return new Valuation(beta.gethVal(), maxDepth);
			}
			return beta;
        }
	}

	
	private Coord kingPos;

	private double pieceHWeight = 0.5;
	private double distToEscapeWeight = 0.2;
	private double dangerKingWeight = 0.15;
	private double escapeLineWeight = 0.15;
	
	private double evaluate(BoardState state) {
		if (state.getTurnPlayer() == BoardState.WHITE) {
			
			// if one wants to change weights only for WHITE
			pieceHWeight += 0;
			distToEscapeWeight += 0;
			dangerKingWeight += 0;
			escapeLineWeight += 0;

			this.kingPos = state.getKingPosition();
			
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

			this.kingPos = state.getKingPosition();
			
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

	
	private double scoreKing(BoardState b) {
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
	
}
