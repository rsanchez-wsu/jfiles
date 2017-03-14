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

import edu.wright.cs.jfiles.commands.Command;
import edu.wright.cs.jfiles.commands.Commands;
import edu.wright.cs.jfiles.commands.Quit;
import edu.wright.cs.jfiles.commands.Stop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

/**
 * Thread class for the server.
 *
 */
public class JFilesServerClient implements Runnable {

	static final Logger logger = LogManager.getLogger(JFilesServer.class);
	private Socket socket = null;
	private DataInputStream streamIn = null;
	private DataOutputStream streamOut = null;
	private ClientProperties cp = new ClientProperties();

	/**
	 * Creates a new socket.
	 */
	public JFilesServerClient(Socket parmSocket) {
		socket = parmSocket;
		cp.setCwd(JFilesServer.getInstance().getCwd());
		cp.cachePermissionType();
	}

	/**
	 * Sends a message to the client.
	 */
	private void send(String msg) {
		try {
			streamOut.writeUTF(msg);
			streamOut.flush();
		} catch (IOException ioe) {
			JFilesServer.print("ERROR sending: " + ioe.getMessage());
			close();
		}
	}

	@Override
	public void run() {
		try {
			open();

			while (true) {
				handle(streamIn.readUTF());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			JFilesServer.print("Client socket terminated.");
		} finally {
			JFilesServer.print("Calling close....");
			close();
			remove();
		}
	}

	/**
	 * Handles new input.
	 * @param input The input given from client.
	 */
	private void handle(String input) {

		JFilesServer.print("Got the input: " + input);

		logger.info("[Server] Recv command: " + input);

		String[] sinput = input.split(" ");

		Command cmd =
				Commands.getNewInstance(sinput[0], Arrays.copyOfRange(sinput, 1, sinput.length));

		cmd.setClientProperties(cp);
		String cont = cmd.execute();

		JFilesServer.print("Sending back: " + cont);

		send(cont);

		if (cmd instanceof Quit) {
			close();
			remove();
		} else if (cmd instanceof Stop) {
			JFilesServer.getInstance().stop();
		}
	}

	/**
	 * Opens the streams.
	 */
	private void open() throws IOException {
		streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
	}

	/**
	 * Removes this instance from the Server.
	 */
	private void remove() {
		JFilesServer.getInstance().remove(this);
	}

	/**
	 * Closes the streams and socket.
	 */
	public void close() {
		if (streamIn != null) {
			try {
				streamIn.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (streamOut != null) {
			try {
				streamOut.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}