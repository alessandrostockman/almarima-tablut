package it.unibo.almarima.tablut.almarima.player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import it.unibo.almarima.tablut.unibo.Action;
import it.unibo.almarima.tablut.almarima.domain.Coordinates;
import it.unibo.almarima.tablut.unibo.State;

public class DummyImpl extends TablutPlayer {

    List<Coordinates> citadels;

    public DummyImpl(int timeout, State.Turn role) {
        super(timeout, role);

        this.citadels = new ArrayList<Coordinates>();
        this.citadels.add(new Coordinates(3, 0));
        this.citadels.add(new Coordinates(4, 0));
        this.citadels.add(new Coordinates(4, 1));
        this.citadels.add(new Coordinates(5, 0));

        this.citadels.add(new Coordinates(0, 3));
        this.citadels.add(new Coordinates(0, 4));
        this.citadels.add(new Coordinates(1, 4));
        this.citadels.add(new Coordinates(0, 5));

        this.citadels.add(new Coordinates(3, 8));
        this.citadels.add(new Coordinates(4, 8));
        this.citadels.add(new Coordinates(4, 7));
        this.citadels.add(new Coordinates(5, 8));

        this.citadels.add(new Coordinates(8, 3));
        this.citadels.add(new Coordinates(8, 4));
        this.citadels.add(new Coordinates(7, 4));
        this.citadels.add(new Coordinates(8, 5));
    }

    @Override
    public Action computeMove(State currentState) throws IOException {
        
        for (int i = 0; i < currentState.getBoard().length; i++) { 
            for (int j = 0; j < currentState.getBoard().length; j++) { 
                if (currentState.getPawn(j, i).equalsPawn(this.getRoleString())) { 
                    Coordinates from = new Coordinates(i, j);
                    int direction = ThreadLocalRandom.current().nextInt(0, 5);
                    for (int k = 0; k < 4; k++, direction = (direction + 1) % 4) { 
                        int x = i, y = j; 
                        switch (direction) { 
                            case 0: 
                                y++;
                                break; 
                            case 1: 
                                x++;
                                break; 
                            case 2: 
                                y--;
                                break; 
                            case 3: 
                                x--;
                                break;
                        }
                        Coordinates to = new Coordinates(x, y);
                        if (x >= 0 && x < currentState.getBoard().length && y >= 0 && y < currentState.getBoard().length 
                                && !this.citadels.stream()
                                    .anyMatch(c -> c.equals(to))
                                && currentState.getPawn(to.getY(), to.getX()).equals(State.Pawn.EMPTY)) { 
                            return new Action(from.toString(), to.toString(), this.getRole()); 
                        } 
                    } 
                } 
            } 
        }
        throw new IOException("No valid move available");
    }
    
    
}


