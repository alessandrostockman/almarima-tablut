package it.unibo.almarima.tablut.application.heuristics;

import java.util.HashSet;

import it.unibo.almarima.tablut.application.domain.BoardState;
import it.unibo.almarima.tablut.application.domain.Coord;
import it.unibo.almarima.tablut.application.domain.Coordinates;
import it.unibo.almarima.tablut.external.State.Pawn;
import it.unibo.almarima.tablut.external.State.Turn;

public class StockHeuristic extends WeightHeuristic {

	private WeightBag w;

	private int whitePieces;
	private int blackPieces;
	private Coord king;

	@Override
	public void initVariables(BoardState state) {
		this.whitePieces = state.getNumberPlayerPieces(BoardState.WHITE);
		this.blackPieces = state.getNumberPlayerPieces(BoardState.BLACK);
		this.king = state.getKingPosition();
	}

	@Override
	public WeightBag createWeightBag() {
		w = new WeightBag(true);
		w.addWeight(Parameter.KING_SAFETY, 0.7, new WeightTuner(0.7, 0.8, 0.1));
		w.addWeight(Parameter.KING_ESCAPE, 0.9);
		w.addWeight(Parameter.WHITE_ENDANGERED_PAWNS, 0.6);
		w.addWeight(Parameter.BLACK_ENDANGERED_PAWNS, 0.5);
		w.addWeight(Parameter.WHITE_PAWN_NUMBER, 0.3);
		w.addWeight(Parameter.BLACK_PAWN_NUMBER, 0.2);
		return w;
	}

	@Override
	public double computeParameterValue(Parameter p, BoardState state) {
		switch (p) {
			case KING_SAFETY:
				return this.getKingSafetyIndex(state);
			case KING_ESCAPE:
				return this.getKingEscaping(state);
			case WHITE_ENDANGERED_PAWNS:
				return this.getEndangeredPawns(state, Turn.WHITE);
			case BLACK_ENDANGERED_PAWNS:
				return this.getEndangeredPawns(state, Turn.WHITE);
			case WHITE_PAWN_NUMBER:
				return this.getWeightedPawnNumber();
			case BLACK_PAWN_NUMBER:
				return this.getWeightedPawnNumber();
			default:
				return 0.5;
		}
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
