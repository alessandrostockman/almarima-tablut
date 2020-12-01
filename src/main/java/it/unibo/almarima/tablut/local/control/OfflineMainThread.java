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

    private Map<Integer, Integer> runTurns;
    private int h1RandomMoves = 0;
    private int h2RandomMoves = 0;
    private int totalGames = 0;
    private int h1Wins = 0;
    private int h2Wins = 0;
    private int whiteWins = 0;
    private int whiteH1Wins = 0;
    private int whiteH2Wins = 0;
    private int blackWins = 0;
    private int blackH1Wins = 0;
    private int blackH2Wins = 0;
    private int draws = 0;

    private OfflineAgent agent;
    private int maxGames;

    protected Heuristic h1;
    protected Heuristic h2;

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

        this.runTurns = new HashMap<>();
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
                        this.endRunReport(e, hWhite, hBlack, games + (run * this.maxGames));
                        games++;
                    } catch (AgentStoppedException e) { 
                        games++;
                    }
                }
            // }
        }

        this.endGameReport();
    }
    
    public void endRunReport(GameFinishedException e, Heuristic hWhite, Heuristic hBlack, int run) {
        this.runTurns.put(run, e.getTurnNumber());
        this.totalGames++;

        if (hWhite.equals(this.h1)) {
            this.h1RandomMoves += e.getWhiteRandomMoves();
            this.h2RandomMoves += e.getBlackRandomMoves();
        } else {
            this.h1RandomMoves += e.getBlackRandomMoves();
            this.h2RandomMoves += e.getWhiteRandomMoves();
        }

        switch (e.getWinner()) {
            case WHITEWIN:
                if (this.h1.equals(hWhite)) {
                    this.h1Wins++;
                    this.whiteH1Wins++;
                } else {
                    this.h2Wins++;
                    this.whiteH2Wins++;
                }
                this.whiteWins++;
                break;
            case BLACKWIN:
                if (this.h1.equals(hBlack)) {
                    this.h1Wins++;
                    this.blackH1Wins++;
                } else {
                    this.h2Wins++;
                    this.blackH2Wins++;
                }
                this.blackWins++;
                break;
            case DRAW:
                this.draws++;
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

            loggReport.fine(this.h1.toString() + " Details: " + this.h1.printInfo() + "\n\n");
            loggReport.fine(this.h2.toString() + " Details: " + this.h2.printInfo() + "\n\n");

            int tot = 0;
            loggReport.fine("Number of Games: " + this.totalGames);
            for (int key : this.runTurns.keySet()) {
                tot += this.runTurns.get(key);
                loggReport.fine("Run " + key + " turns: " + this.runTurns.get(key));
            }
            loggReport.fine("Average Turns Number: " + tot / this.totalGames + "\n\n");

            
            loggReport.fine(this.h1.toString() + ": " + this.h1Wins);
            loggReport.fine(this.h1.toString() + " (as White): " + this.whiteH1Wins);
            loggReport.fine(this.h1.toString() + " (as Black): " + this.blackH1Wins);
            loggReport.fine(this.h2.toString() + ": " + this.h2Wins);
            loggReport.fine(this.h2.toString() + " (as White): " + this.whiteH2Wins);
            loggReport.fine(this.h2.toString() + " (as Black): " + this.blackH2Wins);
            loggReport.fine("Total White wins: " + this.whiteWins);
            loggReport.fine("Total Black wins: " + this.blackWins);
            loggReport.fine("Draws: " + this.draws + "\n\n");
            
            loggReport.fine("Random moves played (" + this.h1.toString() + "): " + this.h1RandomMoves);
            loggReport.fine("Random moves played (" + this.h2.toString() + "): " + this.h2RandomMoves);

        } catch (AgentStoppedException e) {
            e.printStackTrace();
        }
    }
}
