package it.unibo.almarima.tablut.test.control;

import it.unibo.almarima.tablut.external.Action;
import it.unibo.almarima.tablut.external.State;

public class Shared {

    private Action move;
    private State state;

    private boolean ping = false;
    private boolean serverStarted = false;
    private String name = "";

    public Shared() {
    }

    synchronized public Action getMove() {
        return this.move;
    }

    synchronized public void setMove(Action move) {
        this.move = move;
    }

    synchronized public State getState() {
        return this.state;
    }

    synchronized public void setState(State state) {
        this.state = state;
    }

    synchronized public boolean getPing() {
        return this.ping;
    }

    synchronized public void setPing(boolean ping) {
        this.ping = ping;
    }

    synchronized public boolean getServerStarted() {
        return this.serverStarted;
    }

    synchronized public void setServerStarted(boolean serverStarted) {
        this.serverStarted = serverStarted;
    }

    synchronized public String getName() {
        return this.name;
    }

    synchronized public void setName(String name) {
        this.name = name;
    }
    
}
