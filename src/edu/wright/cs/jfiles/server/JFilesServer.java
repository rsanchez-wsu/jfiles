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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
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
	private static int PORT;
	private static ServerSocket serverSocket;
	private static int MAXTHREADS;
	private static final String UTF_8 = "UTF-8";
	private Socket socket;

	static {
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			//e.printStackTrace();
			logger.error("Some error occured", e);
		}
	}

	/**
	 * Handles allocating resources needed for the server.
	 * 
	 * @throws IOException If there is a problem binding to the socket
	 */	
	private static void init() throws IOException {
		Properties prop = new Properties();
		FileInputStream fis = null;
		File config = null;	

		//Array of strings containing possible paths to check for config files
		String[] configPaths = {"$HOME/.jfiles/serverConfig.xml",
				"/usr/local/etc/jfiles/serverConfig.xml",
				"/opt/etc/jfiles/serverConfig.xml",
				"/etc/jfiles/serverConfig.xml",
				"%PROGRAMFILES%/jFiles/etc/serverConfig.xml",
				"%APPDATA%/jFiles/etc/serverConfig.xml"};

		//Checking location(s) for the config file);
		for (int i = 0; i < configPaths.length; i++) {
			if (new File(configPaths[i]).exists()) {
				config = new File(configPaths[i]);
				break;
			}
		}

		//Output location where the config file was found. Otherwise warn and use defaults.
		if (config == null) {		
			logger.info("No config file found. Using default values.");
		} else {
			logger.info("Config file found in " + config.getPath());
			//Read file
			try {
				//Reads xmlfile into prop object as key value pairs
				fis = new FileInputStream(config);
				prop.loadFromXML(fis);			
			} catch (IOException e) {
				logger.error("IOException occured when trying to access the server config", e);
			} finally {
				if (fis != null) {
					fis.close();
				}
			}
		}

		//Add setters here. First value is the key name and second is the default value.
		//Default values are require as they are used if the config file cannot be found OR if
		// the config file doesn't contain the key.
		PORT = Integer.parseInt(prop.getProperty("Port","9786"));
		logger.info("Config set to port " + PORT);

		MAXTHREADS = Integer.parseInt(prop.getProperty("maxThreads","10"));
		logger.info("Config set max threads to " + MAXTHREADS);		
	}

	/**
	 * This is a Javadoc comment to statisfy Checkstyle.
	 * @throws IOException When bad things happen
	 */
	public JFilesServer(Socket sock) throws IOException {
		serverSocket = new ServerSocket(PORT);
		//using sock so I can make a commit
		sock.getChannel();
	}

	/**
	 * Creates an XML file.
	 * @throws TransformerFactoryConfigurationError error in configuration
	 * @throws TransformerException error in configuration
	 */
	private void createXml() throws TransformerFactoryConfigurationError, 
		TransformerException {
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
			Node dir = createNode(doc,"directory");
			dir.appendChild(createNode(doc,"file"));
			root.appendChild(dir);

			//Output XML to console
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult console = new StreamResult(System.out);
			transformer.transform(source, console);

		} catch (ParserConfigurationException e) {
			logger.error("An error occurred while configuring the parser",e);
		} catch (TransformerConfigurationException e) {
			logger.error("An error occurred while configuring the transformer", e);
		} catch (TransformerFactoryConfigurationError e) {
			logger.error("An error occurred while configuring the transformer factory",e);
		}
	}

	/**
	 * Create an xml node.
	 * @param doc document to create node for
	 * @param name name of node that should be created
	 * @return returns a Node element
	 */
	private Node createNode(Document doc, String name) {
		Element node = doc.createElement(name);
		return node;
	}

	@Override
	public void run() {
		//String dir = System.getProperty("user.dir");
		//These were added to implement File command
		//------------------------------------------
		try {
			System.out.println("Received connection from" + socket.getRemoteSocketAddress());
			InputStreamReader isr = new InputStreamReader(socket.getInputStream(), UTF_8);
			BufferedReader in = new BufferedReader(isr);
			String cmd = in.readLine();

			if (cmd != null) {
				switch (cmd) {
				case "FILE":
					sendFile("AUTHORS",socket);
					socket.close();	
					break;

				case "LIST":
					break;

				default:
					System.out.println("Invalid Command.");
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	/**
	 * When FILE command is received from client, server calls this method
	 * to handle file transfer.
	 * 
	 * @param servsock the socket where the server connection resides
	 */
	public void sendFile(String file, Socket servsock) {


		try (BufferedReader br = new BufferedReader(new FileReader("AUTHORS"))) {
			OutputStreamWriter osw = new OutputStreamWriter(servsock.getOutputStream(), UTF_8);
			BufferedWriter out = new BufferedWriter(osw);
			String line;

			while ((line = br.readLine()) != null) {
				System.out.println(line);
				out.write(line + "\n") ;
			}
			out.flush();

		} catch (IOException e) {
			//e.printStackTrace();
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
			init();
			logger.info("Starting the server");
			//Counts how many Threads there are
			int numThrds = 1;
			//A while loop that currently runs forever that will constantly obtain
			//new connections and passing them to new threads.
			//Recycles variable names
			while (true) {
				System.out.println("Preparing thread " + numThrds);
				System.out.println("Waiting for connection...");
				//Obtain a Socket object
				Socket sock = serverSocket.accept();
				//Passes socket object to new server object
				JFilesServer jf = new JFilesServer(sock);
				try {
					jf.createXml();
				} catch (TransformerFactoryConfigurationError e) {
					e.printStackTrace();
				} catch (TransformerException e) {
					e.printStackTrace();
				}
				//Create and start a new Thread object with server object
				Thread thread = new Thread(jf);
				thread.start();
				//Sleep the main thread so that status messages remain organized
				//Thread.sleep(2);
				//Iterates the numThrds variable by 1
				numThrds++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

