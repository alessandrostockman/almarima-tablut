package it.unibo.almarima.tablut.player;

import java.io.IOException;
import java.util.List;

import it.unibo.almarima.tablut.domain.Action;
import it.unibo.almarima.tablut.domain.State;
public abstract class Player {
    
    private int timeout;
    private State.Turn role;           //Black o White , è il ruolo del giocatore , qui tenuto dentro un oggetto Turn per rimanere coerenti con la loro implementazione

    public Player(int timeout, State.Turn role ) {

        this.timeout=timeout;
        this.role=role;

    }

    /*metodo che ritorna la migliore mossa valutata dal player implementato*/
    public abstract Action computeMove(State currentState) throws IOException;
            
    //TODO: implementare il metodo per ritornare tutte le mosse possibili a partire dallo stato attuale 
    public List<Action> getLegalMoves(State currentState){
        return null;
    }


    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public State.Turn getRole() {
        return role;
    }

    public String getRoleString(){
        return this.role.toString();
    }

    public void setRole(State.Turn role) {
        this.role = role;
    }

    
   

	


    

    
}
