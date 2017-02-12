/*
 * Copyright (C) 2017 - WSU CEG3120 Students
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

package edu.wright.cs.jfiles.core;

import edu.wright.cs.jfiles.commands.Command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Socket Client used by the GUI to communicate with server.
 *
 * @author Logan Rickert
 *
 */
public class SocketClient {

	private Socket socket = null;
	private static int port = 9786;
	private static String host = "localhost";
	private DataInputStream streamIn = null;
	private DataOutputStream streamOut = null;

	/**
	 * Create a new connection to the server.
	 */
	public SocketClient() {
		this(host, port);
	}

	/**
	 * Create a new connection to server.
	 * @param serverName hostname.
	 * @param serverPort port.
	 */
	public SocketClient(String serverName, int serverPort) {
		System.out.println("Establishing connection. Please wait ...");
		try {
			socket = new Socket(serverName, serverPort);
			System.out.println("Connected: " + socket);
			openStreams();
		} catch (UnknownHostException uhe) {
			System.out.println("Host unknown: " + uhe.getMessage());
		} catch (IOException ioe) {
			System.out.println("Unexpected exception: " + ioe.getMessage());
		}
	}

	/**
	 * Sends the given command to the server.
	 *
	 * @param cmd Command to send
	 */
	public void sendCommand(Command cmd) {
		send(cmd.toString());
	}

	/**
	 * Opens the socket streams.
	 *
	 * @throws IOException
	 *             can't open stream
	 */
	private void openStreams() throws IOException {
		streamIn = new DataInputStream(socket.getInputStream());
		streamOut = new DataOutputStream(socket.getOutputStream());
	}

	/**
	 * Reads in the data from the server.
	 *
	 * @return string data from server
	 */
	public String read() {
		String input = "";

		try {
			input = streamIn.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return input;
	}

	/**
	 * Sends data to the server
	 *
	 * @param output
	 *            string data to send to the server.
	 */
	public void send(String output) {
		try {
			streamOut.writeUTF(output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
