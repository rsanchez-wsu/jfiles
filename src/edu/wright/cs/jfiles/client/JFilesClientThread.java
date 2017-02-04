/*
 * Copyright (C) 2016 - WSU CEG3120 Students
 *
 * Roberto C. Sánchez <roberto.sanchez@wright.edu>
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
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
	 *
	 * @param mclient
	 *            The client.
	 * @param msocket
	 *            The socket.
	 */
	public JFilesClientThread(JFilesClient mclient, Socket msocket) {
		client = mclient;
		socket = msocket;
		open();
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
	@Override
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

	/**
	 * Starts the thread.
	 */
	public void init() {
		this.start();
	}
}