package it.unibo.almarima.tablut.main;

import java.io.IOException;
import java.net.UnknownHostException;

import it.unibo.almarima.tablut.application.heuristics.*;
import it.unibo.almarima.tablut.local.config.ClientConfig;
import it.unibo.almarima.tablut.local.config.ServerConfig;
import it.unibo.almarima.tablut.local.control.Scheduler;


public class LocalMain {

    public static void main(String[] args) throws UnknownHostException, IOException {
        /** Game Parameters */
        int gamesNumber = 1;
        Heuristic firstHeuristic = new PickleRicksheuristic();
        Heuristic secondHeuristic = new WeightedHeuristic();

        /** Server Configs */
        int serverTimeout = 300;
        int cache = 0;
        int errors = 0;
        int repeated = 0;
        boolean gui = false;

        /** Client Configs */
        int clientTimeout = 50;

        Scheduler s = new Scheduler(
            gamesNumber,
            firstHeuristic,
            secondHeuristic,
            new ServerConfig(serverTimeout, cache, errors, repeated, gui),
            new ClientConfig(clientTimeout)
        );
        s.start();
    }
}
