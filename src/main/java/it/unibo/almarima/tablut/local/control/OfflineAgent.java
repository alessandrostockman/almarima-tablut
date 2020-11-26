package it.unibo.almarima.tablut.local.control;

import it.unibo.almarima.tablut.local.exceptions.AgentStoppedException;

public interface OfflineAgent {

    public void execute(String folder) throws AgentStoppedException;

}
