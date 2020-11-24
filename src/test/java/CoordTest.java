import it.unibo.almarima.tablut.application.domain.BoardState;
import it.unibo.almarima.tablut.application.domain.Coord;
import it.unibo.almarima.tablut.application.domain.Coordinates;
import it.unibo.almarima.tablut.external.State;
import it.unibo.almarima.tablut.external.StateTablut;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CoordTest {
    
    private static State s;
    


    @BeforeAll
    public static void init(){
        s = new StateTablut();
        new BoardState(s);
    }

    @Test
    public void distanceTest(){
        Coord c1= Coordinates.get(4, 4);
        Coord c2= Coordinates.get(0,1);

        Coord c3= Coordinates.get(4, 6);
        Coord c4= Coordinates.get(2,0);

        assertEquals(c1.distance(c2), 7);
        assertEquals(c3.distance(c4), 8);
        
    }


    @Test
    public void getCoordsBetweenTest(){
        Coord c1= Coordinates.get(6, 4);
        Coord c2= Coordinates.get(0,4);
        List<Coord> actual1= c1.getCoordsBetween(c2);
        List<Coord> expected1 = new ArrayList<>();
        expected1.add(Coordinates.get(5,4));
        expected1.add(Coordinates.get(4,4));
        expected1.add(Coordinates.get(3,4));
        expected1.add(Coordinates.get(2,4));
        expected1.add(Coordinates.get(1,4));
        expected1.add(Coordinates.get(0,4));

        Coord c3= Coordinates.get(6, 2);
        Coord c4= Coordinates.get(0,4);
        List<Coord> actual2= c3.getCoordsBetween(c4);
        List<Coord> expected2 = new ArrayList<>();

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }


    @Test
    public void maxDifferenceTest(){
        Coord c1= Coordinates.get(6, 4);
        Coord c2= Coordinates.get(0,4);
        int diff1= c1.maxDifference(c2);
        Coord c3= Coordinates.get(5, 1);
        Coord c4= Coordinates.get(5,4);
        int diff2= c3.maxDifference(c4);

        assertEquals(diff1, 6);
        assertEquals(diff2, 3);

    }
}

    

