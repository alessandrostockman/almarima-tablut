import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import it.unibo.almarima.tablut.application.domain.BoardState;
import it.unibo.almarima.tablut.application.domain.Coord;
import it.unibo.almarima.tablut.application.domain.Coordinates;
import it.unibo.almarima.tablut.application.domain.Coordinates.CoordinateDoesNotExistException;
import it.unibo.almarima.tablut.external.State;
import it.unibo.almarima.tablut.external.StateTablut;

public class CoordinatesTest {

    private static State s;

    @BeforeAll
    public static void init() {
        s = new StateTablut();
        BoardState b = new BoardState(s);
    }

    @Test
    public void getCoordFromPosition() {

        assertEquals(Coordinates.get("F4"), Coordinates.get(3, 5));
        assertEquals(Coordinates.get("H2"), Coordinates.get(1, 7));


    }

    @Test
    public void isEscapeTest() {
        assertFalse(Coordinates.isEscape(Coordinates.get(4, 4)));

        assertTrue(Coordinates.isEscape(Coordinates.get(0, 1)));
    }

    @Test
    public void isCenterOrNeighborCenterTest() {
        assertTrue(Coordinates.isCenterOrNeighborCenter(Coordinates.get(4, 4)));
        assertTrue(Coordinates.isCenterOrNeighborCenter(Coordinates.get(3, 4)));
        assertFalse(Coordinates.isCenterOrNeighborCenter(Coordinates.get(2, 4)));
        assertFalse(Coordinates.isCenterOrNeighborCenter(Coordinates.get(1, 1)));

    }

    @Test
    public void isCitadelTest() {
        assertTrue(Coordinates.isCitadel(Coordinates.get(0, 4)));
        assertTrue(Coordinates.isCitadel(Coordinates.get(8, 5)));
        assertFalse(Coordinates.isCitadel(Coordinates.get(1, 0)));
        assertFalse(Coordinates.isCitadel(Coordinates.get(7, 6)));

    }

    @Test
    public void getNeighborsTest() {

        List<Coord> neigh1 = new ArrayList<>();
        List<Coord> neigh2 = new ArrayList<>();

        neigh1.add(Coordinates.get(1, 2));
        neigh1.add(Coordinates.get(2, 1));
        neigh1.add(Coordinates.get(3, 2));
        neigh1.add(Coordinates.get(2, 3));

        neigh2.add(Coordinates.get(3, 4));
        neigh2.add(Coordinates.get(4, 3));
        neigh2.add(Coordinates.get(5, 4));
        neigh2.add(Coordinates.get(4, 5));

        assertEquals(neigh1, Coordinates.getNeighbors(Coordinates.get(2, 2)));
        assertEquals(neigh2, Coordinates.getNeighbors(Coordinates.get(4, 4)));

    }

    @Test
    public void getSandwichCoordTest() throws CoordinateDoesNotExistException {
        assertEquals(Coordinates.get(3,3), Coordinates.getSandwichCoord(Coordinates.get(3,5), Coordinates.get(3,4)));
        assertThrows(CoordinateDoesNotExistException.class, ()-> Coordinates.getSandwichCoord(Coordinates.get(7,1), Coordinates.get(8,1)));
    }


    @Test
    public void distanceToClosestEscapeTest(){
        assertEquals(5, Coordinates.distanceToClosestEscape(Coordinates.get(4, 3)));
        assertEquals(6, Coordinates.distanceToClosestEscape(Coordinates.get(4, 4)));

    }

}