package it.unibo.almarima.tablut;

import it.unibo.almarima.tablut.client.TablutHumanClient;

import java.io.IOException;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        
        Scanner scan = new Scanner(System.in);
        System.out.println("choose your role (BLACK or WHITE): ");
        String role = scan.nextLine();
        System.out.println("enter ipAddress: ");
        String ip = scan.nextLine();
        scan.close();
        if (role.equals("BLACK") || role.equals("WHITE")){
            String[] array = new String[]{role,ip};
            TablutHumanClient.main(array);
        }
        else System.out.println("chosen role not valid");
        return ;
        
    }
}
