import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.jupiter.api.Test;

import it.unibo.almarima.tablut.application.game.BoardState;
import it.unibo.almarima.tablut.external.State;
import it.unibo.almarima.tablut.external.StateTablut;
import it.unibo.almarima.tablut.external.State.Pawn;
import it.unibo.almarima.tablut.application.coordinates.Coord;

public class BasicTest {

    @Test
    public void test() throws UnknownHostException, IOException {
        State s = new StateTablut();
        BoardState b = new BoardState(s);
        assertEquals(b.getPawnAt(new Coord(4, 4)), Pawn.KING);
        assertNotEquals(b.getPawnAt(new Coord(0, 0)), Pawn.KING);
    }
}