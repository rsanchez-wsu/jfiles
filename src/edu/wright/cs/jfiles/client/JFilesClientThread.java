/*
 * Copyright (C) 2016 - WSU students.
 * Copyright stuff.
 */

package edu.wright.cs.jfiles.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * THe main class.
 */
public class JFilesClientThread extends Thread {
	private Socket socket = null;
	private JFilesClient client = null;
	private DataInputStream streamIn = null;

	/**
	 * Constructor.
	 * @param mclient The client.
	 * @param msocket The socket.
	 */
	public JFilesClientThread(JFilesClient mclient, Socket msocket) {
		client = mclient;
		socket = msocket;
		open();
		start();
	}

	/**
	 * Open the stream.
	 */
	public void open() {
		try {
			streamIn = new DataInputStream(socket.getInputStream());
		} catch (IOException ioe) {
			System.out.println("Error getting input stream: " + ioe);
			client.stop();
		}
	}

	/**
	 * Close the stream.
	 */
	public void close() {
		try {
			if (streamIn != null) {
				streamIn.close();
			}
		} catch (IOException ioe) {
			System.out.println("Error closing input stream: " + ioe);
		}
	}

	/**
	 * Start everything Basically.
	 */
	public void run() {
		while (true) {
			try {
				client.handle(streamIn.readUTF());
			} catch (IOException ioe) {
				System.out.println("Listening error: " + ioe.getMessage());
				client.stop();
			}
		}
	}
}