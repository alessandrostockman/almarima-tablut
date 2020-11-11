package it.unibo.almarima.tablut;

import it.unibo.almarima.tablut.client.TablutArtificialClient;
import it.unibo.almarima.tablut.client.TablutClient;

import java.io.IOException;

import java.util.Scanner;
/*prova per marco*/
public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Scanner scan = new Scanner(System.in);
        System.out.println("Choose your role (BLACK or WHITE): ");
        String role = scan.nextLine();

        String defaultIp = "localhost";
        String ip;
        System.out.println("Enter IP Address [localhost]: ");
        ip = scan.nextLine();
        if (ip.equals("")) {
            ip = defaultIp;
        }
        
        String defaultPort = "";
        String port;
        if (role.equals("BLACK")){
            defaultPort = String.valueOf(TablutClient.blackPort);
        } else if (role.equals("WHITE")) {
            defaultPort = String.valueOf(TablutClient.whitePort);
        }
        System.out.println("Enter Port ["+defaultPort+"]: ");
        port = scan.nextLine();
        if (port.equals("")) {
            port = defaultPort;
        } 

        scan.close();
        if (role.equals("BLACK") || role.equals("WHITE")){
            String[] array = new String[]{role,ip,port};
            TablutArtificialClient.main(array);
        }
        else System.out.println("chosen role not valid");
        return ;
        
    }
}
