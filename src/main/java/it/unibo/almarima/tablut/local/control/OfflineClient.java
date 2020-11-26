package it.unibo.almarima.tablut.local.control;

import it.unibo.almarima.tablut.application.heuristics.Heuristic;
import it.unibo.almarima.tablut.application.player.*;
import it.unibo.almarima.tablut.external.Action;
import it.unibo.almarima.tablut.external.StateTablut;
import it.unibo.almarima.tablut.external.TablutClient;
import it.unibo.almarima.tablut.external.State.Turn;
import it.unibo.almarima.tablut.local.exceptions.GameFinishedException;

import java.io.IOException;
import java.net.UnknownHostException;

/*extends TablutClient : used to handle communication from and to the Server*/
public class OfflineClient extends TablutClient implements OfflineAgent {

	private Shared shared;
	private Heuristic heuristic;

	public OfflineClient(Shared shared, Turn role, Heuristic heuristic)
			throws UnknownHostException, IOException {
		super(role.equals(Turn.WHITE) ? "WHITE":"BLACK", role);
		this.shared = shared;
		this.heuristic = heuristic;
	}

	/**
	 * Not used
	 * Required by TablutClient
	 */
	public void run() { }

	public void execute(String folder) throws GameFinishedException {
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
        TablutPlayer p = new ImplPlayer(this.getTimeout(),this.getPlayer(), this.heuristic);
		
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
				} else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.WHITEWIN)) {
					System.out.println("WHITE WINS");
					throw new GameFinishedException();
				} else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.BLACKWIN)) {
					System.out.println("BLACK WINS");
					throw new GameFinishedException();
				} else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.DRAW)) {
					System.out.println("DRAW!");
					throw new GameFinishedException();
				} else {
					System.out.println("Waiting for your opponent move... ");
				}

			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
				throw new GameFinishedException();
			}
		}

		
	}

	public boolean isYourTurn(){
		return this.getCurrentState().getTurn().equals(this.getPlayer());
	}

    
}