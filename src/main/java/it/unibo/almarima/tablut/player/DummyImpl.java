package it.unibo.almarima.tablut.player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import it.unibo.almarima.tablut.domain.Action;
import it.unibo.almarima.tablut.domain.Coordinate;
import it.unibo.almarima.tablut.domain.State;
import it.unibo.almarima.tablut.domain.State.Turn;

public class DummyImpl extends Player {

    List<Coordinate> citadels;

    public DummyImpl(int timeout, Turn color) {
        super(timeout, color);

        this.citadels = new ArrayList<Coordinate>();
        this.citadels.add(new Coordinate(3, 0));
        this.citadels.add(new Coordinate(4, 0));
        this.citadels.add(new Coordinate(4, 1));
        this.citadels.add(new Coordinate(5, 0));

        this.citadels.add(new Coordinate(0, 3));
        this.citadels.add(new Coordinate(0, 4));
        this.citadels.add(new Coordinate(1, 4));
        this.citadels.add(new Coordinate(0, 5));

        this.citadels.add(new Coordinate(3, 8));
        this.citadels.add(new Coordinate(4, 8));
        this.citadels.add(new Coordinate(4, 7));
        this.citadels.add(new Coordinate(5, 8));

        this.citadels.add(new Coordinate(8, 3));
        this.citadels.add(new Coordinate(8, 4));
        this.citadels.add(new Coordinate(7, 4));
        this.citadels.add(new Coordinate(8, 5));
    }

    @Override
    public Action getMove(State currentState) throws IOException {
        
        for (int i = 0; i < currentState.getBoard().length; i++) { 
            for (int j = 0; j < currentState.getBoard().length; j++) { 
                if (currentState.getPawn(j, i).equalsPawn(this.getColor().toString())) { 
                    Coordinate from = new Coordinate(i, j);
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
                        Coordinate to = new Coordinate(x, y);
                        if (x >= 0 && x < currentState.getBoard().length && y >= 0 && y < currentState.getBoard().length 
                                && !this.citadels.stream()
                                    .anyMatch(c -> c.equals(to))
                                && currentState.getPawn(to.getY(), to.getX()).equals(State.Pawn.EMPTY)) { 
                            return new Action(from.toString(), to.toString(), this.getColor()); 
                        } 
                    } 
                } 
            } 
        }
        throw new IOException("No valid move available");
    }
    
    
}


