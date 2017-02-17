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
import java.net.Socket;
/**
 *Opens a server connection and links a UTF stream to the console.
 */

public class ConsoleClient {

	private static final int port = 9786;
	private static final String host = "localhost";
	/**
	 * Starts the program.
	 * @param args
	 * Does nothing.
	 */


	public static void main(String[] args) {
		try {
			@SuppressWarnings("resource")
			Socket socket = new Socket(host,port);
			ConsoleOut cout = new ConsoleOut(
					new DataInputStream(new BufferedInputStream(socket.getInputStream())));
			ConsoleIn cin = new ConsoleIn(new DataOutputStream(socket.getOutputStream()));
			cout.start();
			cin.start();
		} catch (IOException ex) {
			System.out.println("Error while connecting to server.");
		}
	}

}
