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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
	private void setup() throws IOException {
		Properties prop = new Properties();
		File config = null;

		// Array of strings containing possible paths to check for config files
		String[] configPaths = { "$HOME/.jfiles/serverConfig.xml",
				"/usr/local/etc/jfiles/serverConfig.xml", "/opt/etc/jfiles/serverConfig.xml",
				"/etc/jfiles/serverConfig.xml", "%PROGRAMFILES%/jFiles/etc/serverConfig.xml",
				"%APPDATA%/jFiles/etc/serverConfig.xml" };

		// Checking location(s) for the config file);
		for (int i = 0; i < configPaths.length; i++) {
			if (new File(configPaths[i]).exists()) {
				config = new File(configPaths[i]);
				break;
			}
		}

		// Output location where the config file was found. Otherwise warn and
		// use defaults.
		if (config == null) {
			logger.info("No config file found. Using default values.");
		} else {
			logger.info("Config file found in " + config.getPath());
			// Read file
			try (FileInputStream fis = new FileInputStream(config)) {
				// Reads xmlfile into prop object as key value pairs
				prop.loadFromXML(fis);
			} catch (IOException e) {
				logger.error("IOException occured when trying to access the server config", e);
			}
		}

		// Add setters here. First value is the key name and second is the
		// default value.
		// Default values are require as they are used if the config file cannot
		// be found OR if
		// the config file doesn't contain the key.
		// PORT = Integer.parseInt(prop.getProperty("Port", "9786"));
		// logger.info("Config set to port " + PORT);

		int maxThreads = Integer.parseInt(prop.getProperty("maxThreads", "10"));
		logger.info("Config set max threads to " + maxThreads);
	}

	/**
	 * Handles allocating resources needed for the server.
	 *
	 * @throws IOException
	 *             If there is a problem binding to the socket
	 */
	private JFilesServer() {
		try {
			setup();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

						// Add client to be tracked
						clients.add(client);
						JFilesServer.print(client + " connected");

						// Run the new client thread.
						executorService.execute(client);
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
	 * The main entry point to the program.
	 */
	public static void main(String[] args) {
		JFilesServer.getInstance().start(9786);
	}
}