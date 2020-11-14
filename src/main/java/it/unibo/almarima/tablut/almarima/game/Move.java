package it.unibo.almarima.tablut.almarima.game;

import java.io.IOException;

import it.unibo.almarima.tablut.almarima.coordinates.Coord;
import it.unibo.almarima.tablut.almarima.coordinates.Coordinates;
import it.unibo.almarima.tablut.unibo.Action;
import it.unibo.almarima.tablut.unibo.State.Turn;

public class Move {

    private int playerId = -1;      //0 for black, 1 for white 
    private int xFrom = -1;
    private int yFrom = -1;
    private int xTo = -1;
    private int yTo = -1;

    /* Constructors */
    public Move(Coord start, Coord end, int id) {
        this(start.x, start.y, end.x, end.y,id);
    }

    public Move(int xFrom, int yFrom, int xTo, int yTo, int playerId) {
        this.playerId=playerId;
        this.xFrom = xFrom;
        this.yFrom = yFrom;
        this.xTo = xTo;
        this.yTo = yTo;
    }

    public Action moveToAction(Turn t) throws IOException {
        String from = String.valueOf((char)(this.getxFrom() + 65)) + (this.getyFrom() + 1);
        String to= String.valueOf((char)(this.getxTo() + 65)) + (this.getyTo() + 1);
        return new Action(from,to,t);
    }

    public Coord getFromPosition() {
        return Coordinates.get(this.xFrom, this.yFrom);
    }

    public Coord getToPosition() {
        return Coordinates.get(this.xTo, this.yTo);
    }

    public static String getPlayerName(int player) {
        if (player != 0 && player != 1)
            return "Illegal";
        return (player == 0) ? "Black" : "White";
    }

    public String toPrettyString() {
        return String.format("%s (p%d) move (%d, %d) to (%d, %d)", getPlayerName(playerId), playerId, xFrom, yFrom,
                xTo, yTo);
    }

	public int getxFrom() {
		return xFrom;
	}

	public void setxFrom(int xFrom) {
		this.xFrom = xFrom;
	}

	public int getyFrom() {
		return yFrom;
	}

	public void setyFrom(int yFrom) {
		this.yFrom = yFrom;
	}

	public int getxTo() {
		return xTo;
	}

	public void setxTo(int xTo) {
		this.xTo = xTo;
	}

	public int getyTo() {
		return yTo;
	}

	public void setyTo(int yTo) {
		this.yTo = yTo;
	}

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
    
}