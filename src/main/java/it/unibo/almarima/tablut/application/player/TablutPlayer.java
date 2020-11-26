package it.unibo.almarima.tablut.application.player;

import it.unibo.almarima.tablut.application.domain.BoardState;
import it.unibo.almarima.tablut.external.State;
import it.unibo.almarima.tablut.local.game.Data;

/*abstract class that all player extend*/
public abstract class TablutPlayer {
    
    

    private int timeout;               
    protected int playerId = BoardState.ILLEGAL;           //0 is Black , 1 is White   
    protected BoardState boardState;                       //the board state in any real game turn 

    public TablutPlayer(int timeout, State.Turn role ) {

        this.timeout=timeout;

        this.playerId = (role.equalsTurn("B") ? BoardState.BLACK : BoardState.WHITE);
    }

    
    /*return the best move as retured from MinMax Algo*/
    public abstract Data computeMove();

            
    public void setBoardState(State st){
        this.boardState= new BoardState(st);
    }

    public int getTimeout() {
        return timeout;
    }
    
    public int getPlayerId(){
        return this.playerId;
    }

    public BoardState getBoardState() {
        return boardState;
    }

    

    

    
   

	


    

    
}
