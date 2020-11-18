package it.unibo.almarima.tablut.application.client;

import it.unibo.almarima.tablut.application.player.*;
import it.unibo.almarima.tablut.external.Action;
import it.unibo.almarima.tablut.external.StateTablut;
import it.unibo.almarima.tablut.external.TablutClient;

import java.io.IOException;
import java.net.UnknownHostException;

/*extends TablutClient : used to handle communication from and to the Server*/
public class TablutArtificialClient extends TablutClient {

    public TablutArtificialClient(String role,String ipAddress, int port) throws UnknownHostException, IOException {
		super(role, "AlMaRiMa", ipAddress, port);
    }
    
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {

		if (args.length == 0) {
			System.out.println("You must specify which player you are (WHITE or BLACK)!");
			System.exit(-1);
		}
		System.out.println("Selected this: " + args[0]);

		TablutClient client = new TablutArtificialClient(args[0],args[1],Integer.parseInt(args[2]));

		client.run();

    }
    
    @Override
	public void run() {

		
        TablutPlayer p=new TablutPlayer(this.getTimeout(),this.getPlayer());               // create a new player who will play the game according to his algo
		
		try {
			this.declareName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("You are player " + this.getPlayer().toString() + "!");

		while (true) {
			try {
				this.read();

				System.out.println("Current state:");
				System.out.println(this.getCurrentState().toString());

				if (this.isYourTurn()) {
					System.out.println("Player " + this.getPlayer().toString() + ", do your move: ");
					p.setBoardState(this.getCurrentState());
					Action action=p.computeMove().moveToAction(this.getPlayer());
					this.write(action);
				} else if (!this.isYourTurn()) {
					System.out.println("Waiting for your opponent move... ");
				} else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.WHITEWIN)) {
					System.out.println("WHITE WINS");
					System.exit(0);
				} else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.BLACKWIN)) {
					System.out.println("BLACK WINS");
					System.exit(0);
				} else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.DRAW)) {
					System.out.println("DRAW!");
					System.exit(0);
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}

		
	}

	public boolean isYourTurn(){
		return this.getCurrentState().getTurn().equals(this.getPlayer());
	}

    
}
