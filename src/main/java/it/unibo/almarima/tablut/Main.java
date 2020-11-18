package it.unibo.almarima.tablut;

import java.io.IOException;

import java.util.Scanner;

import it.unibo.almarima.tablut.application.client.TablutArtificialClient;
import it.unibo.almarima.tablut.external.TablutClient;

public class Main {

    // public static void main(String[] args) throws IOException, ClassNotFoundException {
    //     Scanner scan = new Scanner(System.in);
    //     System.out.println("Choose your role (BLACK or WHITE): ");
    //     String role = scan.nextLine();
    //     role=role.toLowerCase();


    //     System.out.println("Enter IP Address or 'localhost': ");
    //     String ip = scan.nextLine();
        
    
    //     System.out.println("Enter Port or 0 for default: ");
    //     String port = scan.nextLine();

    //     scan.close();
    //     if (role.equals("black") || role.equals("white")){
    //         String[] array = new String[]{role,ip,port};
    //         TablutArtificialClient.main(array);
    //     }
    //     else System.out.println("chosen role not valid");
    //     return ;
        
    // }
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
