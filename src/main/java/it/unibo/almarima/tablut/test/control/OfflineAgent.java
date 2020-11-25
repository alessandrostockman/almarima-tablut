package it.unibo.almarima.tablut.test.control;

import it.unibo.almarima.tablut.test.exceptions.GameFinishedException;

public interface OfflineAgent {

    public void execute() throws GameFinishedException;

}
