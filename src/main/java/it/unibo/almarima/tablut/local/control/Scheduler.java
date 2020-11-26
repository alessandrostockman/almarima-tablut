package it.unibo.almarima.tablut.local.control;

import java.io.IOException;
import java.net.UnknownHostException;

import it.unibo.almarima.tablut.application.heuristics.*;
import it.unibo.almarima.tablut.external.State.Turn;

public class Scheduler {

    private Thread t1;
    private Thread t2;
    private Thread t3;

    public Scheduler() throws UnknownHostException, IOException {
        this(1, new WeightedHeuristic(), new WeightedHeuristic());
    }

    public Scheduler(int games, Heuristic hWhite, Heuristic hBlack) throws UnknownHostException, IOException {
        Shared whiteShared = new Shared();
        Shared blackShared = new Shared();
        OfflineAgent server = new OfflineServer(whiteShared, blackShared, 300, 0, 0, 0, 4, true);
        OfflineAgent whiteClient = new OfflineClient(whiteShared, Turn.WHITE, hWhite);
        OfflineAgent blackClient = new OfflineClient(blackShared, Turn.BLACK, hBlack);

        String two_heur_names= hWhite.toString()+"[W]_vs"+hBlack.toString()+"[B]";

        this.t1 = new OfflineThread(server, games,two_heur_names);
        this.t2 = new OfflineThread(whiteClient, games,two_heur_names);
        this.t3 = new OfflineThread(blackClient, games,two_heur_names);
    }

    public void start() {
        this.t1.start();
        this.t2.start();
        this.t3.start();
    }

}