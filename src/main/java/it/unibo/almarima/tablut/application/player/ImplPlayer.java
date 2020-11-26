package it.unibo.almarima.tablut.application.player;

import it.unibo.almarima.tablut.application.domain.BoardState;
import it.unibo.almarima.tablut.application.domain.Move;
import it.unibo.almarima.tablut.application.domain.Valuation;
import it.unibo.almarima.tablut.application.heuristics.Heuristic;
import it.unibo.almarima.tablut.application.player.MiniMaxTree.TimeLimitException;
import it.unibo.almarima.tablut.external.State;
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
		
		Data d;
		
		//TODO: decidere la depth iniziale
    	// start minimaxPruning algorithm with desired depth 
    	int iterDepth = 4;
		d=new Data(boardState.getRandomMove(),new Valuation(-1,-1),iterDepth);
		
		try {
			// if move is chosen without time limit reached, set it to chosenMove
			d = new MiniMaxTree(iterDepth, (BoardState) boardState.clone(), endTime,h).getBestMove();
        } catch (TimeLimitException e) {}
		
		//keep running minimax-pruning with higher depth as long as there is time left
    	while ((System.currentTimeMillis() - startTime) < timeLimit) {
			iterDepth++;
			try {
				// if move is chosen without time limit reached, set it to chosenMove
	    		Data newd = new MiniMaxTree(iterDepth, (BoardState) boardState.clone(), endTime,h).getBestMove();
				d = newd;
			} catch (TimeLimitException e) {}
		}

		
		return d;

    }

            
    
    

    

    
   

	


    

    
}
