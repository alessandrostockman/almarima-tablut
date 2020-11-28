package it.unibo.almarima.tablut.local.control;

import it.unibo.almarima.tablut.local.exceptions.AgentStoppedException;
import it.unibo.almarima.tablut.local.exceptions.GameFinishedException;

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
                TablutLogger.setup(games);
                this.agent.execute();
                return;
            } catch (GameFinishedException e) {
                this.endRunReport(e);
                games++;
            } catch (AgentStoppedException e) { 
                games++;
            }
        }

        this.endGameReport(games);
    }

    public void endRunReport(GameFinishedException e) {}

    public void endGameReport(int games) {}
}
