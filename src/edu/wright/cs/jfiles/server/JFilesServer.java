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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

/**
 * The main class of the JFiles server application.
 * 
 * @author Roberto C. Sánchez &lt;roberto.sanchez@wright.edu&gt;
 *
 */
public class JFilesServer implements Runnable {

	static final Logger logger = LogManager.getLogger(JFilesServer.class);
	private static final int PORT = 9786;
	private final ServerSocket serverSocket;
	private static final String UTF_8 = "UTF-8";

	/**
	 * Handles allocating resources needed for the server.
	 * 
	 * @throws IOException
	 *             If there is a problem binding to the socket
	 */
	public JFilesServer() throws IOException {
		serverSocket = new ServerSocket(PORT);
	}

	@Override
	public void run() {
		String dir = System.getProperty("user.dir");
		Locale.setDefault(new Locale("English"));
		try (Socket server = serverSocket.accept()) {
			logger.info("Received connection from" + server.getRemoteSocketAddress());
			InputStreamReader isr = new InputStreamReader(server.getInputStream(), UTF_8);
			BufferedReader in = new BufferedReader(isr);
			String cmd;
			OutputStreamWriter osw = new OutputStreamWriter(server.getOutputStream(), UTF_8);

			BufferedWriter out = new BufferedWriter(osw);
			while (null != (cmd = in.readLine())) {
				if ("".equals(cmd)) {
					break;
				}

				String[] baseCommand = cmd.split(" ");

				switch (baseCommand[0].toUpperCase(Locale.ENGLISH)) {
				case "LIST":
					listCmd(dir, out);
					break;
				case "FIND":
					findCmd(dir, out, baseCommand[1].toLowerCase(Locale.ENGLISH));
					break;
				case "File":

					break;
				case "EXIT":
					try {
						out.close();
						in.close();
						serverSocket.close();
					} catch (IOException ex) {
						System.out.println("Error closing the socket and streams");
					}
					break;
				default:
					System.out.println("Invalid Command");
				}

				out.flush();
			}
		} catch (IOException e) {
			// TODO AUto-generated catch block
			// e.printStackTrace();
			logger.error("Some error occured", e);
		}
	}

	/**
	 * List Command function. Method for the list command.
	 * 
	 * @throws IOException
	 *             If there is a problem binding to the socket
	 */
	void listCmd(String dir, BufferedWriter out) {
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(dir))) {
			for (Path path : directoryStream) {
				out.write(path.toString() + "\n");
			}
		} catch (IOException e) {
			// TODO AUto-generated catch block
			// e.printStackTrace();
			logger.error("Some error occured", e);
		}
	}

	/**
	 * Find Command function. Method for the find command.
	 * 
	 * @throws IOException
	 *             If there is a problem binding to the socket
	 */
	void findCmd(String dir, BufferedWriter out, String searchTerm) {
		int findCount = 0;
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(dir))) {
			for (Path path : directoryStream) {
				if (path.toString().toLowerCase(Locale.ENGLISH).contains(searchTerm)) {
					out.write(path.toString() + "\n");
					findCount++;
				}
			}
			System.out.println("Found " + findCount + " file(s) that contains \"" 
					+ searchTerm + "\"\n");
		} catch (IOException e) {
			// TODO AUto-generated catch block
			// e.printStackTrace();
			logger.error("Some error occured", e);
		}
	}

	/**
	 * The main entry point to the program.
	 * 
	 * @throws IOException
	 *             If there is a problem binding to the socket
	 */
	public static void main(String[] args) {
		try {
			logger.info("Starting the server");
			JFilesServer jf = new JFilesServer();
			Thread thread = new Thread(jf);
			thread.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
