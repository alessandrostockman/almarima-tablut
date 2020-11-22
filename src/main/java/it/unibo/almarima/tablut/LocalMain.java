package it.unibo.almarima.tablut;

import java.io.IOException;
import java.net.UnknownHostException;

import it.unibo.almarima.tablut.test.control.Scheduler;

public class LocalMain {

    public static void main(String[] args) throws UnknownHostException, IOException {
        Scheduler s = new Scheduler();
        s.start();
    }
}
