package it.unibo.almarima.tablut.local.control;

import it.unibo.almarima.tablut.local.exceptions.AgentStoppedException;

public interface OfflineAgent {

    public void restart();

    public void execute() throws AgentStoppedException;

}
