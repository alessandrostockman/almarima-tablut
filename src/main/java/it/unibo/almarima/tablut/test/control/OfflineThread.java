package it.unibo.almarima.tablut.test.control;

import it.unibo.almarima.tablut.test.exceptions.GameFinishedException;

public class OfflineThread extends Thread {

    private OfflineAgent agent;
    private int maxGames;

    public OfflineThread(OfflineAgent agent, int games) {
        this.agent = agent;
        this.maxGames = games;
    }

    public void run(){
        int games = 0;
        while (games < this.maxGames) {
            try {
                this.agent.execute();
                return;
            } catch (GameFinishedException e) { 
                games++;
            }
        }
    }
}
