/*
 * Copyright (C) 2017 - WSU CEG3120 Students
 *
 *
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

package edu.wright.cs.jfiles.junit;

import edu.wright.cs.jfiles.server.JFilesServer;
import edu.wright.cs.jfiles.server.JFilesServerClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Creates A server, runs it in its own thread, connects to it on
 *         a socket and creates a some handles to interact with it.
 */
public class ServerTestWidget implements Runnable {
	public JFilesServer server;
	public Socket socket;
	private Thread thread;
	public DataInputStream in;
	public DataOutputStream out;
	public JFilesServerClient client;

	/**
	 * Starts the server in a new thread and opens the client.
	 * @throws IOException
	 * This exception being thrown is a failure of the test.
	 */
	ServerTestWidget() throws IOException {
		server = JFilesServer.getInstance();
		thread = new Thread(this);
		thread.start();
		socket = new Socket("localhost", 9786);
		in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
	}
	/**
	 * Start the server listening thread.
	 */

	public void run() {
		server.start(9786);
	}
	/**
	 * Halt the server.
	 * thread.stop() can be safely used since everything that thread touches is
	 * no longer relevant
	 */

	@SuppressWarnings("deprecation")
	public void stop() throws IOException {
		socket.close();
		server.stop();
		thread.stop();
	}
	/**
	 * @return a line sent to the client.
	 */

	public String receive() throws IOException {
		return in.readUTF();
	}
	/**
	 * @param outline sent by the client.
	 */

	public void send(String outline) throws IOException {
		out.writeUTF(outline);
	}

}
