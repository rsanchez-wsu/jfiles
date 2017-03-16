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

import edu.wright.cs.jfiles.commands.Mkdir;
import edu.wright.cs.jfiles.database.DatabaseController;
import edu.wright.cs.jfiles.database.DatabaseUtils;
import edu.wright.cs.jfiles.database.FailedInsertException;
import edu.wright.cs.jfiles.database.IdNotFoundException;
import edu.wright.cs.jfiles.database.User;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The main class of the JFiles server application.
 *
 * @author Roberto C. Sánchez &lt;roberto.sanchez@wright.edu&gt;
 *
 */
public class JFilesServer {

	static final Logger logger = LogManager.getLogger(JFilesServer.class);
	private ServerSocket server = null;
	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

	private boolean shouldRun = true;
	private ExecutorService executorService = Executors.newFixedThreadPool(10);
	private List<JFilesServerClient> clients;

	private static JFilesServer instance = new JFilesServer();

	private String defaultCwd;
	private String defaultUser;
	private int port;
	private int maxthreads;

	/**
	 * Returns the JFilesServer instance.
	 *
	 * @return Returns the JFilesServer instance.
	 */
	public static JFilesServer getInstance() {
		return instance;
	}

	/**
	 * Handles allocating resources needed for the server.
	 *
	 * @throws IOException
	 *             If there is a problem binding to the socket
	 */
	private void setup() {
		try (FileInputStream propIn = new FileInputStream(
				new File("src/edu/wright/cs/jfiles/server/server.properties"))) {
			Properties properties = new Properties();
			properties.load(propIn);

			port = Integer.parseInt(properties.getProperty("port", "9786"));
			maxthreads = Integer.parseInt(properties.getProperty("maxThreads", "10"));
			defaultUser = properties.getProperty("defaultUser", "default");
			defaultCwd = properties.getProperty("serverDirectory", "serverfiles/");
		} catch (FileNotFoundException e) {
			logger.error("server.properties file not found");
		} catch (IOException e) {
			logger.error("unable to load server.properties file");
		}

		ensureDatabase();

		// Ensure folder for user exists. If it doesn't, it'll error.
		File defaultUserDir = new File(defaultCwd + defaultUser);
		if (!defaultUserDir.exists()) {
			if (defaultUserDir.mkdirs()) {
				logger.info("Default user directory created successfully.");
			} else {
				logger.info("Could not create default user directory");
			}
		} else {
			logger.info("Default user directory already exists, doing nothing.");
		}
	}

	/**
	 * Gets the default user.
	 * @return Returns the default user.
	 */
	public User getDefaultUser() {
		return DatabaseController.getUser(defaultUser);
	}

	/**
	 * Handles allocating resources needed for the server.
	 *
	 * @throws IOException
	 *             If there is a problem binding to the socket
	 */
	private JFilesServer() {
		setup();
	}

	/**
	 * Starts the server
	 *
	 * @param port
	 *            The port to start on.
	 */
	public void start(int port) {
		shouldRun = true;
		clients = Collections.synchronizedList(new ArrayList<>());

		try {
			JFilesServer.print("Binding to port " + port + ", please wait  ...");
			server = new ServerSocket(port);
			JFilesServer.print("Server started: " + server);
		} catch (IOException ioe) {
			JFilesServer.print("Can not bind to port " + port + ": " + ioe.getMessage());
		}

		accept();
	}

	/**
	 * Constantly waits for new connection.
	 */
	public void accept() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (shouldRun) {
					try {
						System.out.println("Waiting for a client ...");

						/*
						 * Accept a new client
						 */
						JFilesServerClient client = new JFilesServerClient(server.accept());

						if (clients.size() < maxthreads) {
							// Add client to be tracked
							clients.add(client);
							JFilesServer.print(client + " connected");

							// Run the new client thread.
							executorService.execute(client);
						} else {
							JFilesServer.print("maxthread count reached, client not accepted");
							client.refuseConnection();
						}
					} catch (IOException ioe) {
						JFilesServer.print("Server accept error: " + ioe);
					}
				}
				stop();
			}
		}).start();
	}

	/**
	 * This method stops the thread.
	 */
	public void stop() {
		shouldRun = false;

		/*
		 * Go through each client connected and close the IO.
		 */
		for (JFilesServerClient client : clients) {
			client.close();
		}

		clients.clear();

		/*
		 * Shut down all client sockets.
		 */
		executorService.shutdownNow();

		/*
		 * Shut down the server.
		 */
		try {
			DatabaseController.shutdown();
			server.close();
			System.out.println("Server now closed!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * A thread-safe print.
	 *
	 * @param toPrint
	 *            A thread-safe print.
	 */
	public static void print(Object toPrint) {
		synchronized (System.out) {
			System.out.println(toPrint.toString());
		}
	}

	/**
	 * Adds a new client to be tracked.
	 *
	 * @param client
	 *            The client to be added.
	 */
	public synchronized void add(JFilesServerClient client) {
		clients.add(client);
	}

	/**
	 * Removes a client from being tracked.
	 *
	 * @param client
	 *            The client to be removed.
	 */
	public synchronized void remove(JFilesServerClient client) {
		clients.remove(client);
	}

	/**
	 * Returns the default Current Working Directory..
	 * @return The default CWD.
	 */
	public String getCwd() {
		return this.defaultCwd;
	}

	/**
	 * Ensures everything that needs to be created has been with the database.
	 */
	private void ensureDatabase() {
		if (!new File("JFilesDB/").exists()) {
			DatabaseController.createTables();
		}

		DatabaseController.dropTables();
		DatabaseController.createTables();

		User defUser = DatabaseController.getUser(defaultUser);

		if (defUser == null) {
			try {
				int roleId = DatabaseController.createRole("none");
				int userId = DatabaseController.createUser(defaultUser, "", roleId);
				String xml = DatabaseUtils.generateUserPermission(defaultCwd + defaultUser);
				int permId = DatabaseController.createPermission(xml);
				System.out.println(String.format("userId:%d, permId:%d", userId, permId));
				DatabaseController.addPermissionToUser(userId, permId);
			} catch (FailedInsertException | IdNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * The main entry point to the program.
	 */
	public static void main(String[] args) {
		JFilesServer.getInstance().start(9786);
	}
}