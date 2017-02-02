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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;

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
public class JFilesServer implements Runnable {

	static final Logger logger = LogManager.getLogger(JFilesServer.class);
	private static int PORT = 9786;
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
		start();
	}

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
			try {
				createXml();
			} catch (TransformerFactoryConfigurationError e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
			setup();
		} catch (IOException ioe) {
			System.out.println("Can not bind to port " + PORT + ": " + ioe.getMessage());
		}
	}

	/**
	 * Creates an XML file.
	 *
	 * @throws TransformerFactoryConfigurationError
	 *             error in configuration
	 * @throws TransformerException
	 *             error in configuration
	 */
	private void createXml() throws TransformerFactoryConfigurationError, TransformerException {
		Document doc = null;
		try {
			// Create new XML document
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			doc = builder.newDocument();

			// Add elements to new document
			Element root = doc.createElement("fileSystem");
			doc.appendChild(root);
			Node dir = createNode(doc, "directory");
			dir.appendChild(createNode(doc, "file"));
			root.appendChild(dir);

			// Output XML to console
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult console = new StreamResult(System.out);
			transformer.transform(source, console);

		} catch (ParserConfigurationException e) {
			logger.error("An error occurred while configuring the parser", e);
		} catch (TransformerConfigurationException e) {
			logger.error("An error occurred while configuring the transformer", e);
		} catch (TransformerFactoryConfigurationError e) {
			logger.error("An error occurred while configuring the transformer factory", e);
		}
	}

	/**
	 * Create an xml node.
	 *
	 * @param doc
	 *            document to create node for
	 * @param name
	 *            name of node that should be created
	 * @return returns a Node element
	 */
	private static Node createNode(Document doc, String name) {
		Element node = doc.createElement(name);
		return node;
	}

	@Override
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
	@SuppressWarnings("deprecation")
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
	public synchronized void handle(int id, String input) {

		System.out.println("Got the input: " + input);

		logger.info("[Server] Recv command: " + input);

		String[] sinput = input.split(" ");

		Command cmd = Commands.getNewInstance(sinput[0],
					Arrays.copyOfRange(sinput, 1, sinput.length));

		clients[findClient(id)].send(cmd.execute());

		if (cmd instanceof Quit) {
			remove(id);
		}
	}

	/**
	 * This method handles removing a thread.
	 */
	@SuppressWarnings("deprecation")
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
	 */
	private void recursiveFindCmd(String dir, int id, String searchTerm) {
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(dir))) {
			for (Path path : directoryStream) {
				if (path.toFile().isDirectory()) {
					recursiveFindCmd(path.toString(), id, searchTerm);
				}
			}
		} catch (IOException e) {
			// e.printStackTrace();
			logger.error("Some error occured", e);
		}
		findCmd(dir, id, searchTerm);
	}

	/**
	 * The main entry point to the program.
	 */
	public static void main(String[] args) {
		new JFilesServer(PORT);
	}

	/**
	 * Sends path that contains displayed items to the GUI.
	 */
	public static String sendPath() {
		String dir = System.getProperty("user.dir");
		return dir;
	}
}
