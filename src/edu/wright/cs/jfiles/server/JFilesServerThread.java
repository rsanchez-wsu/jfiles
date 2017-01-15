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


package edu.wright.cs.jfiles.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Thread class for the server.
 *
 */
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