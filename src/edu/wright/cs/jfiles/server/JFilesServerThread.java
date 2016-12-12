package edu.wright.cs.jfiles.server;

import java.net.*;
import java.io.*;


public class JFilesServerThread extends Thread {
	private JFilesServer server = null;
	private Socket socket = null;
	private int id = -1;
	private DataInputStream streamIn = null;
	private DataOutputStream streamOut = null;

	/**
	 *  .
	 */
	public JFilesServerThread(JFilesServer parmServer, Socket parmSocket) {
		super();
		server = parmServer;
		socket = parmSocket;
		id = socket.getPort();
	}

	/**
	 *  .
	 */
	@SuppressWarnings("deprecation") // .interrupt();
	public void send(String msg) {
		try {
			streamOut.writeUTF(msg);
			streamOut.flush();
		} catch (IOException ioe) {
			System.out.println(id + " ERROR sending: " + ioe.getMessage());
			server.remove(id);
			stop();
		}
	}

	/**
	 * .
	 */
	public int getid() {
		return id;
	}

	/**
	 * .
	 */
	@SuppressWarnings("deprecation")
	public void run() {
		System.out.println("Server Thread " + id + " running.");
		while (true) {
			try {
				server.handle(id, streamIn.readUTF());
			} catch (IOException ioe) {
				System.out.println(id + " ERROR reading: " + ioe.getMessage());
				server.remove(id);
				stop();
			}
		}
	}

	/**
	 * .
	 */
	public void open() throws IOException {
		streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
	}

	/**
	 * .
	 */
	public void close() throws IOException {
		if (socket != null) {
			socket.close();
		}

		if (streamIn != null) {
			streamIn.close();
		}

		if (streamOut != null) {
			streamOut.close();
		}
	}
}