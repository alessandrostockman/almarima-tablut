import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import it.unibo.almarima.tablut.application.domain.BoardState;
import it.unibo.almarima.tablut.application.domain.Move;
import it.unibo.almarima.tablut.application.player.ImplPlayer;
import it.unibo.almarima.tablut.application.player.TablutPlayer;
import it.unibo.almarima.tablut.external.State;
import it.unibo.almarima.tablut.external.State.Turn;
import it.unibo.almarima.tablut.external.StateTablut;


public class TablutPlayerTest {

    @Test
    public void testCreation() {
        TablutPlayer whitePlayer = new ImplPlayer(60, Turn.WHITE);
        TablutPlayer blackPlayer = new ImplPlayer(60, Turn.BLACK);
        assertEquals(whitePlayer.getPlayerId(), BoardState.WHITE);
        assertEquals(blackPlayer.getPlayerId(), BoardState.BLACK);
    }

    @Test
    public void testComputeMove() {
        State s = new StateTablut();
        BoardState bs = new BoardState(s);
        TablutPlayer whitePlayer = new ImplPlayer(60, Turn.WHITE);
        whitePlayer.setBoardState(s);
        
        Move chosen = whitePlayer.computeMove();
        boolean found = false;
        for (Move m : bs.getAllLegalMoves()) {
            if (m.equals(chosen)) {
                found = true;
            }
        }
        assertTrue(found);


    }

}