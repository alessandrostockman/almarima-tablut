package it.unibo.almarima.tablut;

import java.io.IOException;


import it.unibo.almarima.tablut.application.client.TablutArtificialClient;
import it.unibo.almarima.tablut.external.TablutClient;

public class MainTest {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
		TablutClient whiteClient = new TablutArtificialClient("white","tablut.ddns.net",5800);
		TablutClient blackClient = new TablutArtificialClient("black","tablut.ddns.net",5801);

        Thread t1 = new Thread(){
            public void run(){
                whiteClient.run();
            }
        };
        Thread t2 = new Thread(){
            public void run(){
                blackClient.run();
            }
        };

        t1.start();
        t2.start();
        return ;
        
    }

    
}
