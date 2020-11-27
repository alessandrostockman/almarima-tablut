package it.unibo.almarima.tablut.local.control;

import it.unibo.almarima.tablut.application.heuristics.Heuristic;
import it.unibo.almarima.tablut.application.player.*;
import it.unibo.almarima.tablut.external.Action;
import it.unibo.almarima.tablut.external.StateTablut;
import it.unibo.almarima.tablut.external.TablutClient;
import it.unibo.almarima.tablut.external.State.Turn;
import it.unibo.almarima.tablut.local.exceptions.AgentStoppedException;
import it.unibo.almarima.tablut.local.game.Data;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


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

	public void execute(String folder) throws AgentStoppedException {


		String baseDirectory = "src/main/java/it/unibo/almarima/tablut/local/match_history";
		Path clientlogPath = Paths.get(baseDirectory + File.separator + folder);
		clientlogPath = clientlogPath.toAbsolutePath();

		Logger loggClient = Logger.getLogger("Client"+this.getPlayer()+"Log");

		try {
			File logDir = new File(clientlogPath.toString());
			if (!logDir.exists()) {
				logDir.mkdirs();
			}

			clientlogPath = Paths.get(clientlogPath.toString() + File.separator + (this.getPlayer().equals(Turn.WHITE) ? "WHITE":"BLACK") + "_" + new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString() + ".txt");
			
			File log = new File(clientlogPath.toString());
			if (!log.exists()) {
				log.createNewFile();
			}
			FileHandler fh = null;
			fh = new FileHandler(clientlogPath.toString(), true);
			loggClient.addHandler(fh);
			fh.setFormatter(new MyFormatter());
			loggClient.setLevel(Level.FINE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AgentStoppedException();
		}

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
			this.shared.setName(this.getName());
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
					Data d= p.computeMove();
					Action action = d.getMove().moveToAction(this.getPlayer());

					//TODO: loggare qualcosa 
					System.out.println(action);
					loggClient.fine("TurnNumber:  "+this.shared.getTurnNumber());
					loggClient.fine("Move:  " + d.getMove().toFormat());
					loggClient.fine("Action:  " + action.toFormat());
					loggClient.fine("Valuation:  " + (this.getPlayer().equals(Turn.WHITE)? "alpha":"beta")+"="+ d.getValuation().gethVal() + " at depth: " + d.getValuation().getDepthAttained());
					loggClient.fine("IterDepth reached:  " + Integer.toString(d.getDepth())+"\n\n");

					synchronized (this.shared) {
						this.shared.setTurnNumber(this.shared.getTurnNumber()+1);
						this.shared.setMove(action);
						this.shared.notify();
						System.out.println(this.getPlayer()+": Notify 3");
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
					System.out.println("Waiting for your opponent move... ");
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
