package it.unibo.almarima.tablut.local.control;

import it.unibo.almarima.tablut.local.exceptions.GameFinishedException;

public interface OfflineAgent {

    public void execute(String folder) throws GameFinishedException;

}
