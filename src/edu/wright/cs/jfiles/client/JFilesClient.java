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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 * The main class of the JFiles client application.
 *
 * @author Roberto C. Sánchez &lt;roberto.sanchez@wright.edu&gt;
 *
 */
public class JFilesClient implements Runnable {
	static final Logger logger = LogManager.getLogger(JFilesClient.class);
	private Socket socket = null;
	private Thread thread = null;
	private DataInputStream console = null;
	private DataOutputStream streamOut = null;
	private JFilesClientThread client = null;
	private static String host = "localhost";
	private static int port = 9786;

	/**
	 * The main Class.
	 *
	 * @param serverName
	 *            The name of the server.
	 * @param serverPort
	 *            The port number of the server.
	 */
	public JFilesClient(String serverName, int serverPort) {
		System.out.println("Establishing connection. Please wait ...");
		try {
			socket = new Socket(host, port);
			System.out.println("Connected: " + socket);
			start();
		} catch (UnknownHostException uhe) {
			System.out.println("Host unknown: " + uhe.getMessage());
		} catch (IOException ioe) {
			System.out.println("Unexpected exception: " + ioe.getMessage());
		}
	}

	/**
	 * Handles allocating resources needed for the server.
	 *
	 * @throws IOException
	 *             If there is a problem binding to the socket
	 */
	private static void setupConfig() throws IOException {
		Properties prop = new Properties();
		File config = null;

		// Array of strings containing possible paths to check for config files
		String[] configPaths = { "$HOME/.jfiles/clientConfig.xml",
				"/usr/local/etc/jfiles/clientConfig.xml", "/opt/etc/jfiles/clientConfig.xml",
				"/etc/jfiles/clientConfig.xml", "%PROGRAMFILES%/jFiles/etc/clientConfig.xml",
				"%APPDATA%/jFiles/etc/clientConfig.xml" };

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
		port = Integer.parseInt(prop.getProperty("port", "9786"));
		logger.info("Config set to port " + port);

		host = prop.getProperty("host", "localhost");
		logger.info("Config set max threads to " + host);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		System.out.print("> ");
		while (thread != null) {
			try {
				streamOut.writeUTF(console.readLine());
				streamOut.flush();
			} catch (IOException ioe) {
				System.out.println("Sending error: " + ioe.getMessage());
				stop();
			}
		}

	}

	/**
	 * This method handles the message.
	 *
	 * @param msg
	 *            The message to handle.
	 */
	public void handle(String msg) {
		if (msg.equals(".exit")) {
			System.out.println("Good bye. Press RETURN to exit ...");
			stop();
		} else {
			System.out.print(msg);
		}
	}

	/**
	 * Start the connection.
	 */
	public void start() throws IOException {
		console = new DataInputStream(System.in);
		streamOut = new DataOutputStream(socket.getOutputStream());
		if (thread == null) {
			client = new JFilesClientThread(this, socket);
			client.start();
			thread = new Thread(this);
			thread.start();
		}
	}

	/**
	 * Stops the thread.
	 */
	@SuppressWarnings("deprecation")
	public void stop() {
		if (thread != null) {
			thread.stop();
			thread = null;
		}
		try {
			if (console != null) {
				console.close();
			} else if (streamOut != null) {
				streamOut.close();
			} else if (socket != null) {
				socket.close();
			}
		} catch (IOException ioe) {
			System.out.println("Error closing ...");
		}
		client.close();
		client.stop();
	}

	/**
	 * The main method.
	 */
	public static void main(String[] args) {
		try {
			setupConfig();
		} catch (IOException e) {
			System.out.println("Error with IO");
			e.printStackTrace();
		}
		new JFilesClient(host, port);
	}
}
