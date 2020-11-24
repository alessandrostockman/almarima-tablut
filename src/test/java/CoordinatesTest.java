import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import it.unibo.almarima.tablut.application.domain.BoardState;
import it.unibo.almarima.tablut.application.domain.Coordinates;
import it.unibo.almarima.tablut.external.State;
import it.unibo.almarima.tablut.external.StateTablut;

public class CoordinatesTest {

    private static State s;
    
    @BeforeAll
    public static void init(){
        s = new StateTablut();
        BoardState b = new BoardState(s);
    }


    @Test
    public void getCoordFromPosition(){
        
        assertEquals(Coordinates.get("F4"),Coordinates.get(3,5));
        
    }


}