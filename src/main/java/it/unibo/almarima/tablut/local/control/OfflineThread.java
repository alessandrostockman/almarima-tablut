package it.unibo.almarima.tablut.local.control;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.unibo.almarima.tablut.local.exceptions.AgentStoppedException;
import it.unibo.almarima.tablut.local.exceptions.GameFinishedException;

public class OfflineThread extends Thread {

    private OfflineAgent agent;
    private int maxGames;
    private List<String> logBuffer;

    public OfflineThread(OfflineAgent agent, int games) {
        this.agent = agent;
        this.maxGames = games;
        this.logBuffer = new ArrayList<>();
    }

    public void run(){
        int games = 0;
        while (games < this.maxGames) {
            try {
                TablutLogger.setup(games);
                this.agent.execute();
                return;
            } catch (AgentStoppedException e) { 
                if (e instanceof GameFinishedException) {
                    GameFinishedException gameReport = (GameFinishedException) e;
                    this.logBuffer.add("Winner " + gameReport.getWinner());
                }
                games++;
            }
        }

        if (logBuffer.size() > 0) {
            Logger loggReport;
            try {
                loggReport = TablutLogger.get(TablutLogger.LogSpace.REPORT);
                loggReport.setLevel(Level.FINE);
                for (String s : this.logBuffer) {
                    loggReport.fine(s);
                }
            } catch (AgentStoppedException e) {
                e.printStackTrace();
            }
        }
    }
}
