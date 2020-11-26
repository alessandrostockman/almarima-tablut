package it.unibo.almarima.tablut.local.control;

import it.unibo.almarima.tablut.local.exceptions.GameFinishedException;

public class OfflineThread extends Thread {

    private OfflineAgent agent;
    private int maxGames;
    private String path;

    public OfflineThread(OfflineAgent agent, int games,String twoHeurName) {
        this.agent = agent;
        this.maxGames = games;
        this.path=twoHeurName;
    }

    public void run(){
        int games = 0;
        String p;
        while (games < this.maxGames) {
            try {
                p=path.concat("_"+games);
                this.agent.execute(p);
                return;
            } catch (GameFinishedException e) { 
                games++;
            }
        }
    }
}
