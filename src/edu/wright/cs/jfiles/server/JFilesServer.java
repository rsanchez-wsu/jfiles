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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
<<<<<<< HEAD

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
=======
import java.util.Properties;
>>>>>>> refs/heads/team2-dev

/**
 * The main class of the JFiles server application.
 * 
 * @author Roberto C. Sánchez &lt;roberto.sanchez@wright.edu&gt;
 *
 */
public class JFilesServer implements Runnable {

	static final Logger logger = LogManager.getLogger(JFilesServer.class);
	private static int PORT;
	private final ServerSocket serverSocket;
	private static final String UTF_8 = "UTF-8";

	/**
	 * Handles allocating resources needed for the server.
	 * @throws FileNotFoundException 
	 * 
	 * @throws IOException
	 *             If there is a problem binding to the socket
	 */
	
	private static void init() throws IOException {
		Properties prop = new Properties();
		FileInputStream fis = null;
		File file = new File("serverConfig.xml");
		
		
		try {
			//Reads xmlfile into prop object as key value pairs
			fis = new FileInputStream(file);
			prop.loadFromXML(fis);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//Add setters here. First value is the key name and second is the default value.
		PORT = Integer.parseInt(prop.getProperty("Port","9786"));
		logger.info("Config set to port " + PORT);
	}
	
	/**
	 * This is a Javadoc comment to statisfy Checkstyle.
	 * @throws IOException When bad things happen
	 */
	public JFilesServer() throws IOException {
		serverSocket = new ServerSocket(PORT);
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
			logger.error("An error occurred while configuring the parser");
			logger.error(e.getStackTrace());
		} catch (TransformerConfigurationException e) {
			logger.error("An error occurred while configuring the transformer");
			logger.error(e.getStackTrace());
		} catch (TransformerFactoryConfigurationError e) {
			logger.error("An error occurred while configuring the transformer factory");
			logger.error(e.getStackTrace());
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
		String dir = System.getProperty("user.dir");
		try (Socket server = serverSocket.accept()) {
			logger.info("Received connection from" + server.getRemoteSocketAddress());
			InputStreamReader isr = new InputStreamReader(server.getInputStream(), UTF_8);
			BufferedReader in = new BufferedReader(isr);
			String cmd;
			while (null != (cmd = in.readLine())) {
				if ("".equals(cmd)) {
					break;
				}
				OutputStreamWriter osw = new OutputStreamWriter(server.getOutputStream(), UTF_8);

				BufferedWriter out = new BufferedWriter(osw);
				String[] baseCommand = cmd.split(" ");
				if ("LIST".equalsIgnoreCase(baseCommand[0])) {
					try (DirectoryStream<Path> directoryStream = Files
							.newDirectoryStream(Paths.get(dir))) {
						for (Path path : directoryStream) {
							out.write(path.toString() + "\n");
						}
					}

				}
				// start Search block
				if ("FIND".equalsIgnoreCase(baseCommand[0])) {

					try (DirectoryStream<Path> directoryStream = Files
							.newDirectoryStream(Paths.get(dir))) {
						for (Path path : directoryStream) {
							// out.write(path.toString() + "\n");
							if (path.toString().contains(baseCommand[1])) {
								out.write(path.toString() + "\n");
							}

						}
					}

				} else { // End search block
					logger.error("Unknown commad");
				}
				out.flush();
			}
		} catch (IOException e) {
			//TODO Auto-generated catch block
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
			JFilesServer jf = new JFilesServer();
			try {
				jf.createXml();
			} catch (TransformerFactoryConfigurationError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Thread thread = new Thread(jf);
			//thread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
