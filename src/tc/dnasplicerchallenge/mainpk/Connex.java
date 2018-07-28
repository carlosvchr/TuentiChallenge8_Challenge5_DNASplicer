package tc.dnasplicerchallenge.mainpk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connex {

	private final String HOST;
	private final int PORT;
	private Socket socket;
	private PrintWriter sender;
	private BufferedReader receiver;
	
	public Connex(String host, int port) {
		this.HOST = host;
		this.PORT = port;
	}
	
	/** Init socket and create all needed streams */
	public void open() {
		try {
			socket = new Socket(HOST, PORT);
			receiver = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			sender = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/** Close all streams and socket*/
	public void close() {
		try {
			receiver.close();
			sender.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Receive data from the server */
	public String receive() {
		try {
			return receiver.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/** Send data to the server */
	public void send(String s) {
		sender.println(s);
	}
	
	
}
