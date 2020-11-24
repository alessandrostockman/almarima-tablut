import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import it.unibo.almarima.tablut.application.domain.BoardState;
import it.unibo.almarima.tablut.application.domain.Coord;
import it.unibo.almarima.tablut.application.domain.Move;
import it.unibo.almarima.tablut.application.domain.Valuation;
import it.unibo.almarima.tablut.application.heuristics.Heuristic;
import it.unibo.almarima.tablut.application.player.MiniMaxTree;
import it.unibo.almarima.tablut.application.player.MiniMaxTree.TimeLimitException;
import it.unibo.almarima.tablut.external.State.Pawn;
import it.unibo.almarima.tablut.external.StateTablut;

public class MinMaxTreeTest {

    @Test
    public void testHeuristicApplication() throws TimeLimitException {
        Coord target;
        BoardState s;
        MiniMaxTree m;
        Move chosen;

        /**
         * Checking that by creating an heuristic which assumes the presence of a white
         * pawn in tile (5, 5) and using a max depth of 1, the pawn is moved to that
         * tile in the chosen move
         */
        target = new Coord(5, 5);
        s = new BoardState(new StateTablut());
        m = new MiniMaxTree(1, s, System.currentTimeMillis() + 55000, new Heuristic() {

            @Override
            public double evaluate(BoardState state) {
                return state.getPawnAt(target).equals(Pawn.WHITE) ? 1 : 0;
            }

        });

        chosen = m.getBestMove();
        assertEquals(target.x, chosen.getxTo());
        assertEquals(target.y, chosen.getyTo());

        /**
         * Checking that by creating an heuristic which assumes the presence of the king
         * in tile (5, 5) and using a max depth of 6 (3 moves for white and 3 for black,
         * so that the position is actualy reachable), in the best move possible, a pawn
         * next to the king is going to be moved
         */
        s = new BoardState(new StateTablut());
        m = new MiniMaxTree(6, s, System.currentTimeMillis() + 55000, new Heuristic() {

            @Override
            public double evaluate(BoardState state) {
                return state.getPawnAt(target).equals(Pawn.KING) ? 1 : 0;
            }

        });

        chosen = m.getBestMove();
        assertTrue(chosen.getxFrom() == 4 && (chosen.getyFrom() == 3 || chosen.getyFrom() == 5)
                || chosen.getyFrom() == 4 && (chosen.getxFrom() == 3 || chosen.getxFrom() == 5));
    }

    @Test
    public void testTimeException() {
        /** 
         * Testing if the TimeLimitException is launched when inserting a sleep in the heuristic evaluation
         */
        Valuation alpha = new Valuation(0.0, 3);
        Valuation beta = new Valuation(1.0, 3);
        BoardState b1 = new BoardState(new StateTablut());
        MiniMaxTree m = new MiniMaxTree(3, b1, System.currentTimeMillis() + 100, new Heuristic() {
            @Override
            public double evaluate(BoardState state) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return 1;
            }
        });
        
        boolean exception = false;
        try {
            Valuation v = m.minimax(b1, 1, alpha, beta);
        } catch (TimeLimitException e) {
            exception = true;
        }

        assertTrue(exception);

    }

    @Test
    public void testMinimaxFunction() {
        int maxDepth = 3;
        Coord target = new Coord(5, 5);
        Valuation alpha = new Valuation(0.0, 2);
        Valuation beta = new Valuation(1.0, 2);
        BoardState b1 = new BoardState(new StateTablut());
        MiniMaxTree m = new MiniMaxTree(maxDepth, b1, System.currentTimeMillis() + 5000, new Heuristic() {
            @Override
            public double evaluate(BoardState state) {
                return state.getPawnAt(target).equals(Pawn.KING) ? 0.99 : 0.1;
            }
        });
        try {
            Valuation v = m.minimax(b1, 1, alpha, beta);
            assertEquals(maxDepth, v.getDepthAttained());
            assertEquals(0.1, v.gethVal());
        } catch (TimeLimitException e) {
            e.printStackTrace();
        }
    }

}