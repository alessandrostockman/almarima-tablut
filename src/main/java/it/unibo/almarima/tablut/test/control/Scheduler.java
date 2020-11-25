package it.unibo.almarima.tablut.test.control;

import java.io.IOException;
import java.net.UnknownHostException;

import it.unibo.almarima.tablut.application.heuristics.PawnsHeuristic;
import it.unibo.almarima.tablut.external.TablutClient;
import it.unibo.almarima.tablut.external.State.Turn;

public class Scheduler {

    private Thread t1;
    private Thread t2;
    private Thread t3;

    public Scheduler() throws UnknownHostException, IOException {
        Shared whiteShared = new Shared();
        Shared blackShared = new Shared();
        OfflineServer server = new OfflineServer(whiteShared, blackShared, 300, 0, 0, 0, 4, true);
        TablutClient whiteClient = new OfflineClient(whiteShared, Turn.WHITE, new PawnsHeuristic());
        TablutClient blackClient = new OfflineClient(blackShared, Turn.BLACK, new PawnsHeuristic());
        
        this.t1 = new Thread(){
            public void run(){
                server.run();
            }
        };
        this.t2 = new Thread(){
            public void run(){
                whiteClient.run();
            }
        };
        this.t3 = new Thread(){
            public void run(){
                blackClient.run();
            }
        };
    }

    public void start() {
        this.t1.start();
        this.t2.start();
        this.t3.start();
    }

}
