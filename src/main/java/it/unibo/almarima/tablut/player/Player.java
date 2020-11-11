package it.unibo.almarima.tablut.player;

import java.io.IOException;
import java.util.List;

import it.unibo.almarima.tablut.domain.Action;
import it.unibo.almarima.tablut.domain.State;
public abstract class Player {
    
    private int timeout;
    private State.Turn color;

    public Player(int timeout, State.Turn color ) {

        this.timeout=timeout;
        this.color=color;

    }

    /*metodo che ritorna la migliore mossa valutata dal player implementato*/
    public abstract Action getMove(State currentState) throws IOException;
            

    public List<Action> getLegalMoves(State currentState){
        // if (currentState.getTurn().equals(StateTablut.Turn.BLACK)) { 
        //     int[] buf; 
        //     for (int i = 0; i < currentState.getBoard().length; i++) { 
        //         for (int j = 0; j < currentState.getBoard().length; j++) { 
        //             if (currentState.getPawn(i, j).equalsPawn(State.Pawn.BLACK.toString())) { 
        //                 buf = new int[2]; buf[0] = i; buf[1] = j; 
        //                 pawns.add(buf); 
        //             } 
                        
        //             else if (state.getPawn(i, j).equalsPawn(State.Pawn.EMPTY.toString())) { 
        //                 buf = new int[2]; buf[0] = i; buf[1] = j; 
        //                 empty.add(buf); 
        //             } 
        //         } 
        //     }
        // }

        return null;

    }


    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

	public State.Turn getColor() {
		return color;
	}

	public void setColor(State.Turn color) {
		this.color = color;
	}


    

    
}
