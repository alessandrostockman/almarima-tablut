package it.unibo.almarima.tablut.application.player;

import it.unibo.almarima.tablut.application.game.BoardState;
import it.unibo.almarima.tablut.application.game.Move;
import it.unibo.almarima.tablut.external.State;

/*abstract class that all player extend*/
public abstract class TablutPlayer {
    
    public static final int ILLEGAL = -1;
    public static final int WHITE = 1;
    public static final int BLACK = 0;

    private int timeout;               
    protected int playerId = ILLEGAL;          //0 is Black , 1 is White  per rendere più semplice la gestione del ruolo 
    protected BoardState boardState;          //la board in un determinato turno reale di gioco

    public TablutPlayer(int timeout, State.Turn role ) {

        this.timeout=timeout;

        this.playerId = (role.equalsTurn("B") ? BLACK : WHITE);
    }

    
    /*metodo che ritorna la migliore mossa valutata dal player implementato*/
    public abstract Move computeMove();

            
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
