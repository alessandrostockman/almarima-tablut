package it.unibo.almarima.tablut;

import java.io.IOException;

import it.unibo.almarima.tablut.application.client.TablutArtificialClient;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        if (args.length != 3) {
            System.out.println("Number of arguments not valid. Usage: AlMaRiMa.jar [black/white] [timeout] [ip]");
            System.exit(1);
        }

        String role = args[0].toLowerCase();
        if (!role.equals("black") && !role.equals("white")) {
            System.out.println("Player role not valid. Must be 'black' or 'white'.");
            System.exit(1);
        }

        TablutArtificialClient.main(args);
    }

    
}
