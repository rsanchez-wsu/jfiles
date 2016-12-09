/*
 * Copyright (C) 2016 - WSU CEG3120 Students
 *
 * Roberto C. Sánchez <roberto.sanchez@wright.edu>
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 */

package edu.wright.cs.jfiles.server;

import edu.wright.cs.jfiles.core.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
	// private final ServerSocket serverSocket;
	private JFilesServerThread[] clients = new JFilesServerThread[50];
	private ServerSocket server = null;
	private Thread thread = null;
	private int clientCount = 0;
	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
	private Calendar theDate;

	/**
	 * Handles allocating resources needed for the server.
	 *
	 * @throws IOException
	 *             If there is a problem binding to the socket
	 */
	JFilesServer(int port) {
		try {
			System.out.println("Binding to port " + PORT + ", please wait  ...");
			server = new ServerSocket(PORT);
			System.out.println("Server started: " + server);
			start();
		} catch (IOException ioe) {
			System.out.println("Can not bind to port " + PORT + ": " + ioe.getMessage());
		}
	}

	/**
	 * .
	 */
	public void run() {
		while (true) {
			try {
				System.out.println("Waiting for a client ...");
				addThread(server.accept());
			} catch (IOException ioe) {
				System.out.println("Server accept error: " + ioe);
				stop();
			}
		}
	}

	/**
	 * .
	 */
	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	/**
	 * This method stops the thread.
	 */
	public void stop() {
		if (thread != null) {
			thread.stop();
			thread = null;
		}
	}

	/**
	 * This method searches for the client based on the id number.
	 */
	private int findClient(int id) {
		for (int i = 0; i < clientCount; i++) {
			if (clients[i].getid() == id) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * This method handles all the activities the thread will do.
	 */
	public synchronized void handle(int id, String input) throws IOException {

		// logger.info("Received connection from" +
		// server.getRemoteSocketAddress());
		String dir = System.getProperty("user.dir");
		File history = new File("Search History.txt");
		File cmdHistory = new File("Command History.txt");
		PrintWriter schHstWrt;
		PrintWriter cmdHstWrt;

		if (history.exists() && cmdHistory.exists()) { // determines if the word
														// need to be appended
			schHstWrt = new PrintWriter(new FileWriter(history, true));
			cmdHstWrt = new PrintWriter(new FileWriter(cmdHistory, true));
		} else {
			schHstWrt = new PrintWriter(history);
			cmdHstWrt = new PrintWriter(cmdHistory);
		}

		Locale.setDefault(new Locale("English"));

		String[] baseCommand = input.split(" ");
		theDate = Calendar.getInstance();
		cmdHstWrt.println(baseCommand[0] + "\t\t" + dateFormat.format(theDate.getTime()));
		switch (baseCommand[0].toUpperCase(Locale.ENGLISH)) {
		case "LIST":
			List cmd = new List(clients[findClient(id)]);
			cmd.executeCommand();

			break;
		case "FIND":
			theDate = Calendar.getInstance();
			schHstWrt.println(baseCommand[1] + "\t\t" + dateFormat.format(theDate.getTime()));
			if (isValid(baseCommand)) {
				findCmd(dir, id, baseCommand[1]);
			} else {

				clients[findClient(id)].send("Invaild Command\n");

			}

			break;
		case "FINDR":
			theDate = Calendar.getInstance();
			schHstWrt.println(baseCommand[1] + "\t\t" + dateFormat.format(theDate.getTime()));
			if (isValid(baseCommand)) {
				recursiveFindCmd(dir, id, baseCommand[1]);
			} else {
				clients[findClient(id)].send("Invaild Command\n");
			}

			break;
		case "FILE":
			break;
		case "EXIT":
			clients[findClient(id)].send(".exit");
			remove(id);
			break;
		default:
			logger.info("Hit default switch." + System.lineSeparator());
			break;
		}

		// out.flush();
		clients[findClient(id)].send(">");
		schHstWrt.flush();
		schHstWrt.close();
		cmdHstWrt.flush();
		cmdHstWrt.close();

	}

	/**
	 * This method handles removing a thread.
	 */
	public synchronized void remove(int id) {
		int pos = findClient(id);
		if (pos >= 0) {
			JFilesServerThread toTerminate = clients[pos];
			System.out.println("Removing client thread " + id + " at " + pos);
			if (pos < clientCount - 1) {
				for (int i = pos + 1; i < clientCount; i++) {
					clients[i - 1] = clients[i];
				}
				clientCount--;
			}

			try {
				toTerminate.close();
			} catch (IOException ioe) {
				System.out.println("Error closing thread: " + ioe);
			}
			toTerminate.stop();
		}
	}

	/**
	 * This method handles adding a new thread.
	 */
	private void addThread(Socket socket) {
		if (clientCount < clients.length) {
			System.out.println("Client accepted: " + socket);
			clients[clientCount] = new JFilesServerThread(this, socket);
			try {
				clients[clientCount].open();
				clients[clientCount].start();
				clientCount++;
			} catch (IOException ioe) {
				System.out.println("Error opening thread: " + ioe);
			}
		} else {
			System.out.println("Client refused: maximum " + clients.length + " reached.");
		}
	}

	/**
	 * Checks to make sure the command input is valid.
	 */
	boolean isValid(String[] command) {
		if (command.length <= 1) { // used for handling invalid error
			logger.error("Invalid Input, nothing to find");
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Find Command function. Method for the find command. Writes results found
	 * within current directory. Search supports glob patterns
	 * 
	 * @throws IOException
	 *             If there is a problem binding to the socket
	 */
	private void findCmd(String dir, int id, String searchTerm) {
		int findCount = 0;
		try (DirectoryStream<Path> directoryStream =
				Files.newDirectoryStream(Paths.get(dir), searchTerm)) {
			for (Path path : directoryStream) {
				// out.write(path.toString() + "\n");
				clients[findClient(id)].send(path.toString() + "\n");
				findCount++;
			}
			System.out.println("Found " + findCount + " file(s) in " + dir + " that contains \""
					+ searchTerm + "\"\n");
		} catch (IOException e) {
			logger.error("Some error occured", e);
		}
	}

	/**
	 * Recursive find Command function. Method for the recursive option of the
	 * find command. Calls itself if a child directory is found, otherwise calls
	 * findCmd to get results from current directory.
	 * 
	 * @throws IOException
	 *             If there is a problem binding to the socket
	 */
	private void recursiveFindCmd(String dir, int id, String searchTerm) {
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(dir))) {
			for (Path path : directoryStream) {
				if (path.toFile().isDirectory()) {
					recursiveFindCmd(path.toString(), id, searchTerm);
				}
			}
		} catch (IOException e) {

			logger.error("Some error occured", e);
		}
		findCmd(dir, id, searchTerm);
	}

	/**
	 * The main entry point to the program.
	 * 
	 * @throws IOException
	 *             If there is a problem binding to the socket
	 */
	public static void main(String[] args) {
		JFilesServer server = null;
		int porter = 5050;

		server = new JFilesServer(porter);
	}

}
