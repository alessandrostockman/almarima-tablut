package it.unibo.almarima.tablut.local.control;

import it.unibo.almarima.tablut.local.exceptions.AgentStoppedException;

public class OfflineThread extends Thread {

    OfflineAgent client;

    public OfflineThread(OfflineAgent client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            this.client.execute();
        } catch (AgentStoppedException e) {
            e.printStackTrace();
        }
    }

}
