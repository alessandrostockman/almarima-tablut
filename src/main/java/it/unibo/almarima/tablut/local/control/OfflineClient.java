package it.unibo.almarima.tablut.local.control;

import it.unibo.almarima.tablut.application.heuristics.Heuristic;
import it.unibo.almarima.tablut.application.player.*;
import it.unibo.almarima.tablut.external.Action;
import it.unibo.almarima.tablut.external.StateTablut;
import it.unibo.almarima.tablut.external.TablutClient;
import it.unibo.almarima.tablut.external.State.Turn;
import it.unibo.almarima.tablut.local.exceptions.AgentStoppedException;
import it.unibo.almarima.tablut.local.game.Data;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Logger;


/*extends TablutClient : used to handle communication from and to the Server*/
public class OfflineClient extends TablutClient implements OfflineAgent {

	private Shared shared;
	private Heuristic heuristic;

	public OfflineClient(Shared shared, int timeout, Turn role, Heuristic heuristic)
			throws UnknownHostException, IOException {
		super(timeout, role.equals(Turn.WHITE) ? "WHITE":"BLACK", role);
		this.shared = shared;
		this.heuristic = heuristic;
	}

	/**
	 * Not used
	 * Required by TablutClient
	 */
	public void run() { }

	public void restart() {
		
	}

	public void execute() throws AgentStoppedException {
		Logger loggClient = TablutLogger.get(this.getPlayer().equals(Turn.WHITE) ? TablutLogger.LogSpace.WHITE : TablutLogger.LogSpace.BLACK);

		synchronized (this.shared) {
			while (!this.shared.getServerStarted()) {
				try {
					System.out.println(this.getPlayer()+": Wait 1 [Server started]");
					this.shared.wait();
					System.out.println(this.getPlayer()+": Wake 1 [Server started]");
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
			this.shared.setServerStarted(false);
		}
        TablutPlayer p = new ImplPlayer(this.getTimeout(), this.getPlayer(), this.heuristic);
		
		// System.out.println("You are player " + this.getPlayer().toString() + "!");
		synchronized (this.shared) {
			System.out.println(this.getPlayer()+": Notify 2 [Player name set]");
			this.shared.setName(this.getName());
			this.shared.notify();
		}

		while (true) {
			try {
				if (this.shared.getGameOver()) {
					throw new AgentStoppedException();
				}

				if (!this.shared.getMoveRequired()) {
					synchronized (this.shared) {
						System.out.println(this.getPlayer()+": Wait 3 [Move processed]");
						this.shared.wait();
						System.out.println(this.getPlayer()+": Wake 3 [Move processed]");
					}
				}
				this.setCurrentState(this.shared.getState());

				if (this.isYourTurn()) {
					// System.out.println("Player " + this.getPlayer().toString() + ", do your move: ");
					p.setBoardState(this.getCurrentState());
					Data d= p.computeMove();
					Action action = d.getMove().moveToAction(this.getPlayer());

					// System.out.println(action);
					loggClient.fine("TurnNumber:  "+this.shared.getTurnNumber());
					loggClient.fine("Move:  " + d.getMove().toFormat());
					loggClient.fine("Action:  " + action.toFormat());
					loggClient.fine("Valuation:  " + (this.getPlayer().equals(Turn.WHITE)? "alpha":"beta")+"="+ d.getValuation().gethVal() + " at depth: " + d.getValuation().getDepthAttained());
					loggClient.fine("IterDepth reached:  " + Integer.toString(d.getDepth())+"\n\n");

					synchronized (this.shared) {
						this.shared.setTurnNumber(this.shared.getTurnNumber()+1);
						this.shared.setMove(action);
						this.shared.setMoveRequired(false);
						this.shared.notify();
						System.out.println(this.getPlayer()+": Notify 4 [Player move]");
					}
				} else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.WHITEWIN)) {
					System.out.println("WHITE WINS");
					throw new AgentStoppedException();
				} else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.BLACKWIN)) {
					System.out.println("BLACK WINS");
					throw new AgentStoppedException();
				} else if (this.getCurrentState().getTurn().equals(StateTablut.Turn.DRAW)) {
					System.out.println("DRAW!");
					throw new AgentStoppedException();
				} else {
					// System.out.println("Waiting for your opponent move... ");
				}

			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
				throw new AgentStoppedException();
			}
		}

		
	}

	public boolean isYourTurn(){
		return this.getCurrentState().getTurn().equals(this.getPlayer());
	}

    
}
