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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * The main class of the JFiles client application.
 * 
 * @author Roberto C. Sánchez &lt;roberto.sanchez@wright.edu&gt;
 *
 */
public class JFilesClient implements Runnable {

	private String host = "localhost";
	private int port = 9786;
	private static final String UTF_8 = "UTF-8";

	/**
	 * Handles allocating resources needed for the client.
	 * 
	 * @throws IOException
	 *             If there is a problem binding to the socket
	 */
	public JFilesClient() {
	}

	@Override
	public void run() {
		try (Socket socket = new Socket(host, port)) {
			OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream(), UTF_8);
			BufferedWriter out = new BufferedWriter(osw);
			out.write("LIST\n");
			out.flush();
			InputStreamReader isr = new InputStreamReader(socket.getInputStream(), UTF_8);
			BufferedReader in = new BufferedReader(isr);
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * The main entry point to the program.
	 * 
	 * @param args
	 *            The command-line arguments
	 */
	public static void main(String[] args) {
		System.out.println("Starting the server");
		JFilesClient jf = new JFilesClient();
		Thread thread = new Thread(jf);
		thread.start();
	}

}
