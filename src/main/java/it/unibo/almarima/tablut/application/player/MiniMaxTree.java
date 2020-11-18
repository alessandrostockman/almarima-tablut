package it.unibo.almarima.tablut.application.player;

import java.util.List;

import it.unibo.almarima.tablut.application.game.BoardState;
import it.unibo.almarima.tablut.application.game.Move;


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
		Valuation alpha = new Valuation(0.0, depth);
        Valuation beta = new Valuation(1.0, depth);
        
        // if the player with the turn is a WHITE then maximize
		if ( headBoardState.getTurnPlayer() == BoardState.WHITE) {
			Move maxMove = null;
			List<Move> nextMoves = this.headBoardState.getAllLegalMoves();
			// iterate through all possible moves
			for (Move move : nextMoves) {
				// clone the bs and process the move to generate a new board
				BoardState childBS = (BoardState) this.headBoardState.clone();
				childBS.processMove(move);
				// recursively run minimax on the child board state
				Valuation childValuation = minimax(childBS, depth+1, alpha.clone(), beta.clone());
                
                //TODO: scegliere la mossa in base ai valori di alfa beta e h ( e anche la profondità se sono uguali)
                if (childValuation.gethVal()>alpha.gethVal() || (childValuation.gethVal()==alpha.gethVal() && childValuation.getDepthAttained() < alpha.getDepthAttained() ) ){
                    alpha = childValuation;
                    maxMove = move;
				}
			}
			return maxMove;
		}
		else {
			// if the player with the turn is a BLACK then minimize
			Move minMove = null;
			List<Move> nextMoves = this.headBoardState.getAllLegalMoves();
			// iterate through all possible moves
			for (Move move : nextMoves) {
				// clone the bs and process the move to generate a new board
				BoardState childBS = (BoardState) this.headBoardState.clone();
				childBS.processMove(move);
				// recursively run minimax on the child board state
                Valuation childValuation = minimax(childBS, depth+1, alpha.clone(), beta.clone());
                
                //TODO: scegliere la mossa in base ai valori di alfa beta e h ( e anche la profondità se sono uguali)
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
        
        if (System.currentTimeMillis() > endTime) {
			throw new TimeLimitException("");
		}
		// if node is a leaf, then evaluate with h function
		if (depth == this.maxDepth) {
			return new Valuation(evaluate(nodeBS), depth);
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
                
                //TODO: scegliere la mossa in base ai valori di alfa beta e h ( e anche la profondità se sono uguali)
                if (childValuation.gethVal()>alpha.gethVal() || (childValuation.gethVal()==alpha.gethVal() && childValuation.getDepthAttained() < alpha.getDepthAttained() ) ){
                    alpha = childValuation;

                }
                if (alpha.gethVal()>=beta.gethVal()){
                    break;
                }
			}
			return ;
        }

        if (nodeBS.getTurnPlayer()== BoardState.BLACK){

        }

       
        
        
        
        
        
        
        
        
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

}
