package it.unibo.almarima.tablut.local.control;

import it.unibo.almarima.tablut.external.Action;
import it.unibo.almarima.tablut.external.State;

public class Shared {

    private Action move;
    private State state;
    private int turnNumber;

    private boolean serverStarted = false;
    private boolean moveRequired = false;
    private boolean gameOver = false;
    private String name = "";

    public Shared() {
    }

    public Action getMove() {
        return this.move;
    }

    public void setMove(Action move) {
        this.move = move;
    }

    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean getServerStarted() {
        return this.serverStarted;
    }

    public void setServerStarted(boolean serverStarted) {
        this.serverStarted = serverStarted;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public void incrementTurnNumber() {
        this.turnNumber++;
    }

    public void resetTurnNumber() {
        this.turnNumber = 1;
    }

    public boolean getMoveRequired() {
        return this.moveRequired;
    }

    public void setMoveRequired(boolean required) {
        this.moveRequired = required;
    }

    public boolean getGameOver() {
        return this.gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    
}
