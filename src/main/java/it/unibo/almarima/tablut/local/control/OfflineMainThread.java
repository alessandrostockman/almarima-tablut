package it.unibo.almarima.tablut.local.control;

import java.util.logging.Level;
import java.util.logging.Logger;

import it.unibo.almarima.tablut.local.exceptions.AgentStoppedException;
import it.unibo.almarima.tablut.local.exceptions.GameFinishedException;

public class OfflineMainThread extends OfflineThread {

    private int whiteWins = 0;
    private int blackWins = 0;
    private int draws = 0; 

    public OfflineMainThread(OfflineAgent agent, int games) {
        super(agent, games);
    }
    public void endRunReport(GameFinishedException e) {
        switch (e.getWinner()) {
            case WHITEWIN:
                whiteWins++;
                break;
            case BLACKWIN:
                blackWins++;
                break;
            case DRAW:
                draws++;
                break;
            default:
                break;
        }
    }

    public void endGameReport(int games) {
        Logger loggReport;
        try {
            loggReport = TablutLogger.get(TablutLogger.LogSpace.REPORT);
            loggReport.setLevel(Level.FINE);

            loggReport.fine("Games played:  " + games);
            loggReport.fine("White wins:    " + whiteWins);
            loggReport.fine("Black wins:    " + blackWins);
            loggReport.fine("Draws:         " + draws);
        } catch (AgentStoppedException e) {
            e.printStackTrace();
        }
    }
}
