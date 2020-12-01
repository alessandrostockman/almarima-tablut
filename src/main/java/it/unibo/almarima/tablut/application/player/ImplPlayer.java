package it.unibo.almarima.tablut.application.player;

import it.unibo.almarima.tablut.application.domain.BoardState;
import it.unibo.almarima.tablut.application.domain.Valuation;
import it.unibo.almarima.tablut.application.heuristics.Heuristic;
import it.unibo.almarima.tablut.application.player.MiniMaxTree.TimeLimitException;
import it.unibo.almarima.tablut.external.State;
import it.unibo.almarima.tablut.local.logging.TablutLogger;
import it.unibo.almarima.tablut.local.game.Data;


public class ImplPlayer extends TablutPlayer{

	//TODO: scegliere l'offset da dargli
	long timeLimit = (this.getTimeout()-5)*1000;

	Heuristic h;
	

    public ImplPlayer(int timeout, State.Turn role,Heuristic heur) {
		super(timeout, role);
		this.h=heur;
    }
    
    /*return best move computed from player*/
    public  Data computeMove(){
        // keep track of time started and time limit
    	long startTime = System.currentTimeMillis();
		long endTime = startTime + timeLimit;
		long computeTime;
		TablutLogger.LogSpace s = this.getPlayerId() == BoardState.WHITE ? TablutLogger.LogSpace.WHITE : TablutLogger.LogSpace.BLACK;
		
		Data d;
		
		//TODO: decidere la depth iniziale
    	// start minimaxPruning algorithm with desired depth 
    	int iterDepth = 4;
		d=new Data(boardState.getRandomMove(),new Valuation(-1,-1),iterDepth, 0, 0);
		
		try {
			// if move is chosen without time limit reached, set it to chosenMove
			computeTime = System.currentTimeMillis();
			d = new MiniMaxTree(iterDepth, (BoardState) boardState.clone(), endTime,h).getBestMove();
			TablutLogger.log(s, "Computed move at depth " + d.getDepth() + " in " + (System.currentTimeMillis() - computeTime) + " ms by calculating " + d.getHeuristics() + " heuristics ("+d.getPrunings()+" prunings)");
        } catch (TimeLimitException e) {}
		
		//keep running minimax-pruning with higher depth as long as there is time left
    	while ((System.currentTimeMillis() - startTime) < timeLimit) {
			iterDepth++;
			try {
				// if move is chosen without time limit reached, set it to chosenMove
				computeTime = System.currentTimeMillis();
	    		Data newd = new MiniMaxTree(iterDepth, (BoardState) boardState.clone(), endTime,h).getBestMove();
				d = newd;
				TablutLogger.log(s, "Computed move at depth " + d.getDepth() + " in " + (System.currentTimeMillis() - computeTime) + " ms by calculating " + d.getHeuristics() + " heuristics ("+d.getPrunings()+" prunings)");
			} catch (TimeLimitException e) {}
		}

		
		return d;

    }

            
    
    

    

    
   

	


    

    
}
