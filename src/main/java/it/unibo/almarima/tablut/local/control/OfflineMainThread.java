package it.unibo.almarima.tablut.local.control;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.unibo.almarima.tablut.application.heuristics.Heuristic;
import it.unibo.almarima.tablut.external.State.Turn;
import it.unibo.almarima.tablut.local.config.ClientConfig;
import it.unibo.almarima.tablut.local.exceptions.AgentStoppedException;
import it.unibo.almarima.tablut.local.exceptions.GameFinishedException;
import it.unibo.almarima.tablut.local.logging.TablutLogger;

public class OfflineMainThread extends Thread {

    private OfflineAgent agent;
    private int maxGames;

    protected Heuristic h1;
    protected Heuristic h2;

    private Map<String, Integer> wins;
    private int draws = 0;

    private Thread whiteThread;
    private Thread blackThread;

    private Shared whiteShared;
    private Shared blackShared;

    private ClientConfig config;

    //TODO: Think better to object passed here
    public OfflineMainThread(OfflineAgent agent, int games, Heuristic h1, Heuristic h2, Shared whiteShared,
            Shared blackShared, ClientConfig config) {
        this.agent = agent;
        this.maxGames = games;
        this.h1 = h1;
        this.h2 = h2;
        this.whiteShared = whiteShared;
        this.blackShared = blackShared;
        this.config = config;

        this.wins = new HashMap<>();
    }

    public void run() {
        Heuristic hWhite, hBlack;
        for (int run = 0; run < 2; run++) { // loop throug h1 and h2
            if (run == 0) {
                hWhite = this.h1;
                hBlack = this.h2;
            } else {
                hWhite = this.h2;
                hBlack = this.h1;
            }

            hWhite.getWeightBag().reset();
            hBlack.getWeightBag().reset();

            int tuning = 1;
            //TODO: Add tuning loop
            // while (tuning <= 1) { // loop throug heuristic weights
                // tuning += 1;
                int games = 1;
                while (games <= this.maxGames) {
                    try {
                        this.whiteThread = new OfflineThread(new OfflineClient(whiteShared, Turn.WHITE, hWhite, this.config));
                        this.blackThread = new OfflineThread(new OfflineClient(blackShared, Turn.BLACK, hBlack, this.config));
                    } catch (IOException e) {
                        System.exit(1);
                    }

                    try {
                        this.setupLogs(hWhite, hBlack, tuning, games + (run * this.maxGames));

                        this.whiteThread.start();
                        this.blackThread.start();

                        //TODO: Substitute exception with returning object?
                        this.agent.execute();
                        return;
                    } catch (GameFinishedException e) {
                        this.endRunReport(e, hWhite, hBlack);
                        games++;
                    } catch (AgentStoppedException e) { 
                        games++;
                    }
                }
            // }
        }

        this.endGameReport();
    }
    
    public void endRunReport(GameFinishedException e, Heuristic hWhite, Heuristic hBlack) {
        String whiteKey = hWhite.toString();
        String blackKey = hBlack.toString();
        String drawKey = "Draws";

        if (!this.wins.containsKey(whiteKey)) {
            this.wins.put(whiteKey, 0);
        }

        if (!this.wins.containsKey(blackKey)) {
            this.wins.put(blackKey, 0);
        }

        if (!this.wins.containsKey(drawKey)) {
            this.wins.put(drawKey, 0);
        }

        switch (e.getWinner()) {
            case WHITEWIN:
                this.wins.put(whiteKey, this.wins.get(whiteKey) + 1);
                break;
            case BLACKWIN:
                this.wins.put(blackKey, this.wins.get(blackKey) + 1);
                break;
            case DRAW:
                this.wins.put(drawKey, this.wins.get(drawKey) + 1);
                break;
            default:
                break;
        }
    }

    public void setupLogs(Heuristic hWhite, Heuristic hBlack, int tuningNumber, int games) { 
        TablutLogger.setup(hWhite, hBlack, tuningNumber, games);
    }

    public void endGameReport() {
        Logger loggReport;
        try {
            loggReport = TablutLogger.get(TablutLogger.LogSpace.REPORT);
            loggReport.setLevel(Level.FINE);

            int total = 0;
            for (String key : this.wins.keySet()) {
                total += this.wins.get(key);
                loggReport.fine(key + ": " + this.wins.get(key));
            }

            loggReport.fine("Total games played: " + total);
        } catch (AgentStoppedException e) {
            e.printStackTrace();
        }
    }
}
