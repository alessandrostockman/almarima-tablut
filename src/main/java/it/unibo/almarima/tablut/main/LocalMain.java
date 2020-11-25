package it.unibo.almarima.tablut.main;

import java.io.IOException;
import java.net.UnknownHostException;

import it.unibo.almarima.tablut.local.control.Scheduler;


public class LocalMain {

    public static void main(String[] args) throws UnknownHostException, IOException {
        Scheduler s = new Scheduler();
        s.start();
    }
}
