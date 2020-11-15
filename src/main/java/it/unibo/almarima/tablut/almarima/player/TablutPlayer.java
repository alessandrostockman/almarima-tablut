package it.unibo.almarima.tablut.almarima.player;

import it.unibo.almarima.tablut.almarima.game.BoardState;
import it.unibo.almarima.tablut.almarima.game.Move;
import it.unibo.almarima.tablut.unibo.State;

/*abstract class that all player extend*/
public abstract class TablutPlayer {
    
    public static final int ILLEGAL = -1;
    public static final int WHITE = 1;
    public static final int BLACK = 0;

    private int timeout;
    private State.Turn role;                //Black o White , è il ruolo del giocatore , qui tenuto dentro un oggetto Turn per rimanere coerenti con la loro implementazione
    public int playerId = ILLEGAL;          //0 is Black , 1 is White  per rendere più semplice la gestione del ruolo 

    private BoardState boardState;          //la board in un determinato turno reale di gioco

    public TablutPlayer(int timeout, State.Turn role ) {

        this.timeout=timeout;
        this.role=role;

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

    public State.Turn getRole() {
        return role;
    }

    public String getRoleString(){
        return this.role.toString();
    }

    public int getPlayerId(){
        return this.playerId;
    }

    public BoardState getBoardState() {
        return boardState;
    }

    

    

    
   

	


    

    
}
