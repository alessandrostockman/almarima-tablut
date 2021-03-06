package it.unibo.almarima.tablut.external;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidParameterException;

import com.google.gson.Gson;

/**
 * Classe astratta di un client per il gioco Tablut
 * 
 * @author Andrea Piretti
 *
 */
public abstract class TablutClient implements Runnable {

	private State.Turn player;
	private String name;
	private Socket playerSocket;
	private DataInputStream in;
	private DataOutputStream out;
	private Gson gson;
	private State currentState;
	private int timeout;
	private String serverIp;

	public static int whitePort = 5800;
	public static int blackPort = 5801;

	public State.Turn getPlayer() {
		return player;
	}

	public void setPlayer(State.Turn player) {
		this.player = player;
	}

	public State getCurrentState() {
		return currentState;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}

	public int getTimeout(){
		return this.timeout;
	}

	/**
	 * Creates a new player initializing the sockets and the logger
	 * 
	 * @param player
	 *            The role of the player (black or white)
	 * @param name
	 *            The name of the player
	 * @param timeout
	 *            The timeout that will be taken into account (in seconds)
	 * @param ipAddress
	 *            The ipAddress of the server
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public TablutClient(String player, String name, int timeout, String ipAddress)
			throws UnknownHostException, IOException {
				
		int port;
		this.serverIp = ipAddress;
		this.timeout = timeout;
		this.gson = new Gson();
		if (player.toLowerCase().equals("white")) {
			this.player = State.Turn.WHITE;	
			port = TablutClient.whitePort;
		} 
		else if (player.toLowerCase().equals("black")) {
			this.player = State.Turn.BLACK;
			port = TablutClient.blackPort;
			
		} else {
			throw new InvalidParameterException("Player role must be BLACK or WHITE");
		}
		playerSocket = new Socket(serverIp, port);
		out = new DataOutputStream(playerSocket.getOutputStream());
		in = new DataInputStream(playerSocket.getInputStream());
		this.name = name;
	}

	/**
	 * Creates a new player initializing the sockets and the logger. The server
	 * is supposed to be communicating on the same machine of this player.
	 * 
	 * @param player
	 *            The role of the player (black or white)
	 * @param name
	 *            The name of the player
	 * @param timeout
	 *            The timeout that will be taken into account (in seconds)
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public TablutClient(String player, String name, int timeout) throws UnknownHostException, IOException {
		this(player, name, timeout, "localhost");
	}

	/**
	 * Creates a new player initializing the sockets and the logger. Timeout is
	 * set to be 60 seconds. The server is supposed to be communicating on the
	 * same machine of this player.
	 * 
	 * @param player
	 *            The role of the player (black or white)
	 * @param name
	 *            The name of the player
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public TablutClient(String player, String name) throws UnknownHostException, IOException {
		this(player, name, 60, "localhost");
	}

	/**
	 * Creates a new player initializing the sockets and the logger. Timeout is
	 * set to be 60 seconds.
	 * 
	 * @param player
	 *            The role of the player (black or white)
	 * @param name
	 *            The name of the player
	 * @param ipAddress
	 *            The ipAddress of the server
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public TablutClient(String player, String name, String ipAddress) throws UnknownHostException, IOException {
		this(player, name, 60, ipAddress);
	}

	/**
	 * Creates a new player initializing the sockets and the logger. Timeout is
	 * set to be 60 seconds.
	 * 
	 * @param player
	 *            The role of the player (black or white)
	 * @param name
	 *            The name of the player
	 * @param ipAddress
	 *            The ipAddress of the server
	 * @param port
	 *            The port of the server
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public TablutClient(String player, String name, String ipAddress, int port) throws UnknownHostException, IOException {
		this(player, name, 60, ipAddress);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Write an action to the server
	 */
	public void write(Action action) throws IOException, ClassNotFoundException {
		StreamUtils.writeString(out, this.gson.toJson(action));
	}

	/**
	 * Write the name to the server
	 */
	public void declareName() throws IOException, ClassNotFoundException {
		StreamUtils.writeString(out, this.gson.toJson(this.name));
	}

	/**
	 * Read the state from the server
	 */
	public void read() throws ClassNotFoundException, IOException {
		this.currentState = this.gson.fromJson(StreamUtils.readString(in), StateTablut.class);
	}
}
