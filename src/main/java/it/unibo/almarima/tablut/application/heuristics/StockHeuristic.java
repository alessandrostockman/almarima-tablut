package it.unibo.almarima.tablut.application.heuristics;

import java.util.HashSet;

import it.unibo.almarima.tablut.application.domain.BoardState;
import it.unibo.almarima.tablut.application.domain.Coord;
import it.unibo.almarima.tablut.application.domain.Coordinates;
import it.unibo.almarima.tablut.external.State.Pawn;
import it.unibo.almarima.tablut.external.State.Turn;

public class StockHeuristic extends Heuristic {

	private int[] weights = {1, 1, 1, 1, 1};

	private int whitePieces;
	private int blackPieces;
	private Coord king;

	public StockHeuristic() {
		super();
	}

	enum PawnState {
		Safe(0), Empty(1), Reachable(2), Dangerous(3);

		private int val;
		
		PawnState(int val) {
			this.val = val;
		}
	
		public int getValue() {
			return val;
		}
	}

	public double evaluate(BoardState state) {
		if (state.getWinner() == 0 || state.getWinner() == 1) {
			return state.getWinner();
		}

		this.whitePieces = state.getNumberPlayerPieces(BoardState.WHITE);
		this.blackPieces = state.getNumberPlayerPieces(BoardState.BLACK);
		this.king = state.getKingPosition();

		double kingSafety = this.getKingSafetyIndex(state);
		double kingEscaping = this.getKingEscaping(state);
		double endangeredWhitePawns = this.getEndangeredPawns(state, Turn.WHITE);
		double endangeredBlackPawns = this.getEndangeredPawns(state, Turn.BLACK);
		double pawnNumber = this.getWeightedPawnNumber();

		double weightSum = 0;
		for (double w : this.weights) {
			weightSum += w;
		}
		return (kingSafety * weights[0] + kingEscaping * weights[1] + endangeredWhitePawns * weights[2] + endangeredBlackPawns * weights[3] + pawnNumber * weights[4]) / weightSum;
	}

	private double getKingSafetyIndex(BoardState bs) {
		return
			(this.getStateByCoord(bs, this.king, Turn.WHITE, false, false).getValue() +
			this.getStateByCoord(bs, this.king, Turn.WHITE, false, true).getValue() +
			this.getStateByCoord(bs, this.king, Turn.WHITE, true, false).getValue() +
			this.getStateByCoord(bs, this.king, Turn.WHITE, true, true).getValue())
		/ 16;
	}

	private double getKingEscaping(BoardState bs) {
		double points = 0;

		if (this.king.x < 3 || this.king.x > 4) {
			points += 2;
		}

		if (this.king.y < 3 || this.king.y > 4) {
			points += 2;
		}

		points += 6 - Coordinates.distanceToClosestEscape(this.king);

		if (false) {
			//TODO: Add points if street is clear
		}

		return points / 10;
	}

	private double getWeightedPawnNumber() {
		return this.whitePieces / 8 * (1 - (this.blackPieces / 16));
	}

	private double getEndangeredPawns(BoardState bs, Turn color) {
		HashSet<Coord> pawns;
		Turn player = bs.getTurnPlayer() == 1 ? Turn.WHITE : Turn.BLACK;
		if (player.equals(color)) {
			pawns = bs.getPlayerPieceCoordinates();
		} else {
			pawns = bs.getOpponentPieceCoordinates();
		}

		double danger = 0;
		for (Coord c : pawns) {
			danger += 
				this.getStateByCoord(bs, c, color, false, false).getValue() +
				this.getStateByCoord(bs, c, color, false, true).getValue() +
				this.getStateByCoord(bs, c, color, true, false).getValue() +
				this.getStateByCoord(bs, c, color, true, true).getValue();
		}

		return danger;
	}

	private PawnState getStateByCoord(BoardState bs, Coord c, Turn player, boolean xAxis, boolean predecessor) {
		int x;
		int y;
		if (xAxis) {
			x = c.x + (predecessor ? 1 : -1);
			y = c.y;
		} else {
			x = c.x;
			y = c.y + (predecessor ? 1 : -1);
		}

		if (x < 0 || x > 8 || y < 0 || y > 0) {
			return PawnState.Empty;
		}

		Pawn n = bs.getPawnAt(Coordinates.get(x, y));

		if (n.equals(Pawn.EMPTY)) {
			if (false) {
				return PawnState.Reachable;
			}
			return PawnState.Empty;
		} else if (bs.pieceBelongsTo(n, player.equals(Turn.WHITE) ? 1 : 0)) {
			return PawnState.Safe;
		} else {
			return PawnState.Dangerous;
		}
	} 
	
}
