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
        this(1,new Ric_heur(),new WeightedHeuristic());
    }

    public Scheduler(int games, Heuristic hWhite, Heuristic hBlack) throws UnknownHostException, IOException {
        Shared whiteShared = new Shared();
        Shared blackShared = new Shared();
        OfflineAgent server = new OfflineServer(whiteShared, blackShared, 300, 0, 0, 0, 4, false);
        OfflineAgent whiteClient = new OfflineClient(whiteShared, 60, Turn.WHITE, hWhite);
        OfflineAgent blackClient = new OfflineClient(blackShared, 60, Turn.BLACK, hBlack);

        TablutLogger.init(hWhite, hBlack);

        this.t1 = new OfflineMainThread(server, games);
        this.t2 = new OfflineThread(whiteClient, games);
        this.t3 = new OfflineThread(blackClient, games);
    }

    public void start() {
        this.t1.start();
        this.t2.start();
        this.t3.start();
    }

}
