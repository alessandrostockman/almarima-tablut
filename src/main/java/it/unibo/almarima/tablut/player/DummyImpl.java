package it.unibo.almarima.tablut.player;

import java.io.IOException;

import it.unibo.almarima.tablut.domain.Action;
import it.unibo.almarima.tablut.domain.Coordinates;
import it.unibo.almarima.tablut.domain.State;

public class DummyImpl extends Player {

    public DummyImpl(int timeout, State.Turn role) {
        super(timeout, role);
    }

    @Override
    public Action computeMove(State currentState) throws IOException {
        
        for (int i = 0; i < currentState.getBoard().length; i++) { 
            for (int j = 0; j < currentState.getBoard().length; j++) { 
                if (currentState.getPawn(i, j).equalsPawn(this.getRoleString())) { 
                    for (int k = 0; k < 4; k++) { 
                        int x = i, y = j; 
                        switch (k) { 
                            case 0: y++; 
                            case 1: x++; 
                            case 2: y--; 
                            case 3: x--;
                        } 
                        if (x >= 0 && x < currentState.getBoard().length && y >= 0 && y < currentState.getBoard().length && currentState.getPawn(x, y).equals(State.Pawn.EMPTY)) { 
                            Coordinates from = new Coordinates(i,j);
                            Coordinates to = new Coordinates(x, y);
                            return new Action(from.toString(), to.toString(), this.getRole()); 
                        } 
                    } 
                } 
            } 
        }
        return null;
    }
    
    
}


