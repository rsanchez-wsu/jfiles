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

package testconsoles;


import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A basic Dummy Server console for testing clients.
 * Exit by terminating.
 */
public class ConsoleServer {
	private static final int port = 9786;
	public ServerSocket server;
	/**
	 * Create a Console server and start it.
	 */

	public ConsoleServer() {
		Socket client;
		try {
			server = new ServerSocket(port);
			System.out.println("Dummy server started");
			client = server.accept();
			new ConsoleOut(new DataInputStream(new BufferedInputStream(client.getInputStream())));
			new ConsoleIn(new DataOutputStream(client.getOutputStream()));
			System.out.println("Client accepted.");
		} catch (IOException e) {
			System.out.println("Io error encountered. Exiting.");
		}
	}

	/**
	 * Start a server that links incoming utf tranmissions to the console.
	 * Runs until terminated.
	 * @param args
	 * Does nothing
	 */
	public static void main(String[] args) {
		new ConsoleServer();
	}
}