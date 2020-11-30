package it.unibo.almarima.tablut.local.control;

import java.io.IOException;
import java.net.UnknownHostException;

import it.unibo.almarima.tablut.application.heuristics.*;
import it.unibo.almarima.tablut.local.config.*;
import it.unibo.almarima.tablut.local.logging.TablutLogger;

public class Scheduler {

    private Thread thread;

    public Scheduler(int games, Heuristic h1, Heuristic h2, ServerConfig sConfig, ClientConfig cConfig) throws UnknownHostException, IOException {
        Shared whiteShared = new Shared();
        Shared blackShared = new Shared();
        OfflineAgent server = new OfflineServer(whiteShared, blackShared, sConfig);

        TablutLogger.init();

        this.thread = new OfflineMainThread(server, games, h1, h2, whiteShared, blackShared, cConfig);
    }

    public void start() {
        this.thread.start();
    }

}
