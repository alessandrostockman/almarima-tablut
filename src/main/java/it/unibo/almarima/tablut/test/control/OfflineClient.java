package it.unibo.almarima.tablut.test.control;

import it.unibo.almarima.tablut.application.player.*;
import it.unibo.almarima.tablut.external.Action;
import it.unibo.almarima.tablut.external.StateTablut;
import it.unibo.almarima.tablut.external.TablutClient;
import it.unibo.almarima.tablut.external.State.Turn;

import java.io.IOException;
import java.net.UnknownHostException;

/*extends TablutClient : used to handle communication from and to the Server*/
public class OfflineClient extends TablutClient {

	private Shared shared;

	public OfflineClient(Shared shared, Turn role)
			throws UnknownHostException, IOException {
		super("AlMaRiMa", role);
		this.shared = shared;
	}

	@Override
	public void run() {
		synchronized (this.shared) {
			System.out.println(this.getPlayer()+": Wait 1");
			try {
				this.shared.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
			System.out.println(this.getPlayer()+": Wake 1");
		}
        TablutPlayer p = new ImplPlayer(this.getTimeout(),this.getPlayer()); 
		
		System.out.println("You are player " + this.getPlayer().toString() + "!");
		synchronized (this.shared) {
			System.out.println(this.getPlayer()+": Notify 2");
			this.shared.setName("AlMaRiMa");
			this.shared.notify();
		}

		while (true) {
			try {
				synchronized (this.shared) {
					System.out.println(this.getPlayer()+": Wait 2");
					this.shared.wait();
					this.setCurrentState(this.shared.getState());
					System.out.println(this.getPlayer()+": Wake 2");
				}

				if (this.isYourTurn()) {
					System.out.println("Player " + this.getPlayer().toString() + ", do your move: ");
					p.setBoardState(this.getCurrentState());
					Action action = p.computeMove().moveToAction(this.getPlayer());
					System.out.println(action);

					synchronized (this.shared) {
						this.shared.setMove(action);
						this.shared.notify();
						System.out.println(this.getPlayer()+": Notify 3");
					}
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

			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}

		
	}

	public boolean isYourTurn(){
		return this.getCurrentState().getTurn().equals(this.getPlayer());
	}

    
}
